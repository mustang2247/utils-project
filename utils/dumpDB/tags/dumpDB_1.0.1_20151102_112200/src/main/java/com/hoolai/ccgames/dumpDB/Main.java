package com.hoolai.ccgames.dumpDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.config.DaoConfig;
import com.hoolai.ccgames.center.datatypes.ItemGiveRecord;
import com.hoolai.ccgames.center.repo.DaoFactory;
import com.hoolai.ccgames.center.repo.GlobalRepo;
import com.hoolai.ccgames.center.repo.UserRepo;
import com.hoolai.ccgames.center.utils.JsonUtil;
import com.hoolai.ccgames.center.utils.PropUtil;
import com.hoolai.ccgames.texaspoker.repo.ExtendedMemcachedClientImpl;
import com.hoolai.texaspoker.datatypes.BasicInfo;
import com.hoolai.texaspoker.datatypes.ExpInfo;
import com.hoolai.texaspoker.datatypes.GoldExpInfo;
import com.hoolai.texaspoker.datatypes.ItemDesc;
import com.hoolai.texaspoker.datatypes.ItemList;
import com.hoolai.texaspoker.datatypes.ItemSendRecord;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;

/**
 * 从旧数据库中将金币和道具信息导入新的数据库 因为旧数据库是json格式存储的，所以改动数据结构时，
 * 只需要删除相关的field即可，不影响解析
 */

public class Main {
	
	private  static final Logger logger = LoggerFactory.getLogger( Main.class );

	public static AtomicLong uuid = new AtomicLong();
	public static long uuid_end;
	
	public static UserRepo userRepo;
	public static GlobalRepo globalRepo;
	public static ExtendedMemcachedClientImpl mcOld;
	public static CountDownLatch latch;
	
	public static void main( String[] args ) {

		if( args.length != 3 ) {
			System.err.printf( "Usage: java -jar dumpDB nthread begin end\n" );
			return;
		}
		int nthread = Integer.parseInt( args[0] );
		
		init();
		
		logger.info( "Create {} threads", nthread );
		
		// Main.test( nthread, Long.parseLong( args[1] ), Long.parseLong( args[2] ) );
		
		Main.dumpAll( nthread );
		
		DaoFactory.getDaoPool().destroy();
	}
	
	public static void dumpAll( int nthread ) {
		
		uuid.set( 10000 );
		
		Main.dumpUUID();
		
		latch = new CountDownLatch( nthread );
		
		long beg = System.nanoTime();
		
		for( int i = 0; i < nthread; ++i ) {
			new Thread( new Runnable() {
				@Override
				public void run() {
					Main.dumpParalell();
				}
			} ).start();
		}
		try {
			latch.await();
			logger.info( "total dump: {} ~ {}", 10000, uuid_end );
			logger.info( "finished cost: {}", System.nanoTime() - beg );
			
		}
		catch( InterruptedException e ) {
			logger.error( e.getMessage(), e );
		}
	}
	
	public static void test( int nthread, long start, long fini ) {
		
		uuid.set( start );
		
		Main.dumpUUID();
		
		uuid_end = fini;
		
		latch = new CountDownLatch( nthread );
		
		long beg = System.nanoTime();
		
		for( int i = 0; i < nthread; ++i ) {
			new Thread( new Runnable() {
				@Override
				public void run() {
					Main.dumpParalell();
				}
			} ).start();
		}
		try {
			latch.await();
			logger.info( "finished cost: {}", System.nanoTime() - beg );
			
		}
		catch( InterruptedException e ) {
			logger.error( e.getMessage(), e );
		}
	}
	
	
	public static void init() {
		String  envDir = PropUtil.getProp( "/env.properties", "envdir" );
		DaoConfig.init( "/" + envDir + "dao.properties" );
		DaoFactory.getDaoPool();
		userRepo = UserRepo.getInstance();
		globalRepo = GlobalRepo.getInstance();
		mcOld = new ExtendedMemcachedClientImpl( "memPool", DaoConfig.OLD_ADDRS );
	}
	
	public static void dumpUUID() {
		
		try {

			MemcachedClient mcNew = DaoFactory.produceMC();
			
			uuid_end = Long.parseLong( (String) mcOld.get( "UUID" ) );

			mcNew.add( globalRepo.getUUIDBeginKey( DaoConfig.platformName, DaoConfig.applicationID ),
					0, DaoConfig.uuidRangeLeft ).get();
			mcNew.add( globalRepo.getUUIDEndKey( DaoConfig.platformName, DaoConfig.applicationID ),
					0, DaoConfig.uuidRangeRight ).get();
			mcNew.replace( globalRepo.getUUIDEndKey( DaoConfig.platformName, DaoConfig.applicationID ),
					0, String.valueOf( uuid_end ) ).get();
			
			logger.info( "from old database UUID:" + uuid_end );
			mcNew.shutdown();
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}
	

	
	public static void dumpParalell() {
		
		while( true ) {
			int uid = (int) uuid.getAndIncrement();
			if( uid > uuid_end ) break;
			if( uid % 1000 == 0 ) {
				logger.info( "process {} {}", uid, System.nanoTime() );
			}
			
			try {
    			String basicKey = String.format( "U%x-6", uid );
				String basicValue = (String) mcOld.get( basicKey );
				String goldKey = String.format( "U%x-1", uid );
				String goldValue = (String) mcOld.get( goldKey );
				String itemKey = String.format( "U%x-C", uid );
				String itemValue = (String) mcOld.get( itemKey );
				String expKey = String.format( "U%x-0", uid );

				if( basicValue != null && goldValue != null
						&& itemValue != null ) {

					BasicInfo basicInfo = JsonUtil
							.fromJson( basicValue, BasicInfo.class );
					if( basicInfo == null || basicInfo.platform_id == null ) {
						logger.info( "basicInfo not good {}", uid );
						continue;
					}

					GoldExpInfo goldExpInfo = JsonUtil
							.fromJson( goldValue, GoldExpInfo.class );
					
					ExpInfo expInfo = new ExpInfo();
					expInfo.setLevelExp( goldExpInfo.level, goldExpInfo.cur_exp );
					mcOld.set( expKey, JsonUtil.toJson( expInfo ) );
					
					ItemList itemList = JsonUtil
							.fromJson( itemValue, ItemList.class );

					String[] plat = basicInfo.platform_id.split( "\\-" );
					String platName = plat[0];
					String platID = plat[1];
					String appID = String
							.valueOf( ( basicInfo.appId == 0L ? DaoConfig.applicationID
									: basicInfo.appId ) );
					
					// 线上服务器还有ROBOT-FUTURE#1367120596这样的platID
					// 目前机器人已经废弃，跳过
					if( "ROBOT".equals( platName ) ) {
						logger.info( "Robot will not save {}", uid );
						continue;
					}

					globalRepo.newUserID( platName, appID, platID, (long) uid );

					userRepo.openAccount( uid );

					long gameGold = goldExpInfo.game_gold;

					userRepo.replaceGold( uid, gameGold );
					userRepo.replaceHoolaiGold( uid, goldExpInfo.hoolai_gold );
					userRepo.replaceMasterPoint( uid,
							goldExpInfo.master_point < 0 ? 0
									: goldExpInfo.master_point );
					// help used 就当0计算了

					userRepo.replaceItems( uid, toNewItemList( itemList ) );
					userRepo.replaceItemUsed( uid, toNewItemUsed( itemList ) );
					userRepo.replaceItemGive( uid, toNewItemGive( itemList ) );

				}
				else {
					logger.error( "User {} not exist", uid );
				}
			}
			catch( Exception e ) {
				logger.error( e.getMessage(), e );
			}
		} // while
		latch.countDown();
	}
	

	
	public static void testUserinfo( ArrayList< Integer >  lst ) {
		
		try {
			ExtendedMemcachedClientImpl mcOld = new ExtendedMemcachedClientImpl(
					"memPool", "1.2.51.161:10004" );
			
			ExtendedMemcachedClientImpl mcNew = new ExtendedMemcachedClientImpl(
					"memPool2", "10.204.197.221:11211" );
			
			
			
			for( int uid : lst ) {
				for( int suff = 1; suff < 10; ++suff ) {
					String key = String.format( "U%x-%d", uid, suff );
					Object val = mcOld.get( key );
					if( val != null ) {
						mcNew.set( key, val );
					}
					
					if( suff == 1 ) {
						String expKey = String.format( "U%x-0", uid );
						mcNew.set( expKey, val );
					}
				}
				for( char suff = 'A'; suff <= 'Z'; ++suff ) {
					String key = String.format( "U%x-%c", uid, suff );
					Object val = mcOld.get( key );
					if( val != null ) {
						mcNew.set( key, val );
					}
				}
			}
			
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e.getCause() );
		}
	}
	

	


	public static com.hoolai.ccgames.center.datatypes.ItemList toNewItemList(
			ItemList list ) {
		com.hoolai.ccgames.center.datatypes.ItemList newList = new
				com.hoolai.ccgames.center.datatypes.ItemList();
		for( ItemDesc item : list.item_packs ) {
			newList.change( item.item_id, item.count, 0L );
		}
		return newList;
	}

	public static com.hoolai.ccgames.center.datatypes.ItemList toNewItemUsed(
			ItemList list ) {
		com.hoolai.ccgames.center.datatypes.ItemList newList = new
				com.hoolai.ccgames.center.datatypes.ItemList();
		for( ItemDesc item : list.item_used ) {
			newList.change( item.item_id, item.count, 0L );
		}
		return newList;
	}

	private static SimpleDateFormat ymdhmsFormat = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss" );

	public static com.hoolai.ccgames.center.datatypes.ItemGiveList toNewItemGive(
			ItemList list ) {
		com.hoolai.ccgames.center.datatypes.ItemGiveList newList = new
				com.hoolai.ccgames.center.datatypes.ItemGiveList();
		for( ItemSendRecord r : list.sendRecord ) {
			long time = 0;
			try {
				String timeStr = r.getDate();
				if( timeStr.length() == "20151021105730".length() ) {
					timeStr = timeStr.substring( 0, 4 ) + "-" +
							timeStr.substring( 4, 6 ) + "-" +
							timeStr.substring( 6, 8 ) + " " +
							timeStr.substring( 8, 10 ) + ":" +
							timeStr.substring( 10, 12 ) + ":" +
							timeStr.substring( 12, 14 );
				}
				time = ymdhmsFormat.parse( timeStr ).getTime();
				newList.addRecord( new ItemGiveRecord( Long.parseLong( r
						.getFormId() ),
						Long.parseLong( r.getTargetId() ),
						time,
						Integer.parseInt( r.getItemId() ),
						Integer.parseInt( r.getCount() ),
						Integer.parseInt( r.getStatus() ) ) );
			}
			catch( Exception e ) {
				logger.error( e.getMessage(), e );
			}
		}
		return newList;
	}
	
	
	public static void test1() {
    	MemcachedClient mcNew;
		try {
			mcNew = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( "1.2.51.161:1026" ) );
			mcNew.replace( "UUID:TENCENT:100632434:L", 0, "54321" ).get();
			mcNew.shutdown();
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
    	
	}
	
	public static void test0() {
		for( char suff = 'A'; suff <= 'Z'; ++suff ) {
			System.out.println( String.format( "abc%c", suff ) );
		}
		
		String timeStr = "20151021105730";
		if( timeStr.length() == "20151021105730".length() ) {
			timeStr = timeStr.substring( 0, 4 ) + "-" +
					timeStr.substring( 4, 6 ) + "-" +
					timeStr.substring( 6, 8 ) + " " +
					timeStr.substring( 8, 10 ) + ":" +
					timeStr.substring( 10, 12 ) + ":" +
					timeStr.substring( 12, 14 );
		}
		System.out.println( timeStr );
	}

}
