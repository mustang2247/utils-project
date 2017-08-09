package com.hoolai.ccgames.dumpDB;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.datatypes.ItemGiveRecord;
import com.hoolai.ccgames.center.repo.ExtendedMemcachedClientImpl;
import com.hoolai.ccgames.center.repo.GlobalRepo;
import com.hoolai.ccgames.center.repo.UserRepo;
import com.hoolai.ccgames.center.utils.JsonUtil;
import com.hoolai.ccgames.center.utils.PropUtil;
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

	public static void main( String[] args ) {
		// Main.test0();
		// Main.test1();
		// Main.test();
		// Main.dumpTest();
		Main.dumpAll();
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
	
	public static void test() {
		// 读入指定的test账户
		ArrayList< Integer >  lst = new ArrayList< Integer >();
		
		// 5543827，10006，10004
		lst.add( 10004 );
		lst.add( 10005 );
		lst.add( 10006 );
		lst.add( 8740464 );
		lst.add( 5543827 );
		lst.add( 229484 );
		lst.add( 4702085 );
		
		testUserinfo( lst );
		dumpTest( lst );
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
			logger.error( e.getMessage(), e.getCause() );
		}
    	
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
			
			dumpTest( lst );
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e.getCause() );
		}
	}
	
	public static void dumpTest( ArrayList< Integer >  lst ) {
		String  envDir = PropUtil.getProp( "/env.properties", "envdir" );
		String  daoFilePath = "/" + envDir + "dao.properties";
		Properties props = PropUtil.getProps( daoFilePath );

		String oldDatabaseAddr = props.getProperty( "oldDatabaseAddr" ).trim();
		String newDatabaseAddr = props.getProperty( "newDatabaseAddr" ).trim();
		String platformName = props.getProperty( "platformName" ).trim();
		String applicationID = props.getProperty( "applicationID" ).trim();
		String uuidRange = props.getProperty( "uuidRange" ).trim();
		String uuidRangeLeft = uuidRange.split( "," )[0];
		String uuidRangeRight = uuidRange.split( "," )[1];

		try {
			ExtendedMemcachedClientImpl mcOld = new ExtendedMemcachedClientImpl(
					"memPool", oldDatabaseAddr );

			MemcachedClient mcNew = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( newDatabaseAddr ) );

			UserRepo.getInstance().setMC( mcNew );
			GlobalRepo.getInstance().setMC( mcNew );

			long uuid = Long.parseLong( (String) mcOld.get( "UUID" ) );
			int tot = 0;

			mcNew.add( GlobalRepo.getInstance()
					.getUUIDBeginKey( platformName, applicationID ), 0, uuidRangeLeft ).get();
			mcNew.add( GlobalRepo.getInstance()
					.getUUIDEndKey( platformName, applicationID ), 0, uuidRangeRight ).get();
			mcNew.replace( GlobalRepo.getInstance()
					.getUUIDEndKey( platformName, applicationID ), 0, String
					.valueOf( uuid ) ).get();

			long beg = System.nanoTime();

			// 读入指定的test账户
//			ArrayList< Integer >  lst = new ArrayList< Integer >();
//			lst.add( 10005 );
//			lst.add( 8740464 );
			
			for( int uid : lst ) {
				try {
					if( uid % 1000 == 0 ) {
						logger.info( "tot: {}  time: {}", tot, System.nanoTime() );
					}

					String basicKey = String.format( "U%x-6", uid );
					String basicValue = (String) mcOld.get( basicKey );
					String goldKey = String.format( "U%x-1", uid );
					String goldValue = (String) mcOld.get( goldKey );
					String itemKey = String.format( "U%x-C", uid );
					String itemValue = (String) mcOld.get( itemKey );
					String expKey = String.format( "U%x-0", uid );

					if( basicValue != null && goldValue != null
							&& itemValue != null ) {
						++tot;

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
								.valueOf( ( basicInfo.appId == 0L ? applicationID
										: basicInfo.appId ) );
						
						// 线上服务器还有ROBOT-FUTURE#1367120596这样的platID
						// 目前机器人已经废弃，跳过
						if( "ROBOT".equals( platName ) ) {
							logger.info( "Robot will not save {}", uid );
							continue;
						}

						GlobalRepo
								.getInstance()
								.newUserID( platName, appID, platID, (long) uid );

						UserRepo.getInstance().openAccount( uid );

						long gameGold = goldExpInfo.game_gold;
						long safeboxChip = 0;
						if( itemList.safeBox != null )
							safeboxChip = itemList.safeBox.chip;
						if( safeboxChip < 0 )
							safeboxChip = 0;
						gameGold += safeboxChip;

						UserRepo.getInstance().replaceGold( uid, gameGold );
						UserRepo.getInstance()
								.replaceHoolaiGold( uid, goldExpInfo.hoolai_gold );
						UserRepo.getInstance().replaceMasterPoint( uid,
								goldExpInfo.master_point < 0 ? 0
										: goldExpInfo.master_point );
						// help used 就当0计算了

						UserRepo.getInstance()
								.replaceItems( uid, toNewItemList( itemList ) );
						UserRepo.getInstance()
								.replaceItemUsed( uid, toNewItemUsed( itemList ) );
						UserRepo.getInstance()
								.replaceItemGive( uid, toNewItemGive( itemList ) );

					}

					else {
						// System.out.printf( "User %d not exist\n", uid );
					}
				}
				catch( Exception e ) {
					logger.error( e.getMessage(), e.getCause() );
				}
			}

			long end = System.nanoTime();

			logger.info( "cost nanos: " + ( end - beg ) );

			logger.info( "UUID:" + uuid );
			logger.info( "tot:" + tot );

			mcNew.shutdown();
		}
		catch( IOException e ) {
			logger.error( e.getMessage(), e.getCause() );
		}
		catch( InterruptedException e1 ) {
			e1.printStackTrace();
		}
		catch( ExecutionException e1 ) {
			e1.printStackTrace();
		}
	}
	
	public static void dumpAll() {
		String  envDir = PropUtil.getProp( "/env.properties", "envdir" );
		String  daoFilePath = "/" + envDir + "dao.properties";
		Properties props = PropUtil.getProps( daoFilePath );

		String oldDatabaseAddr = props.getProperty( "oldDatabaseAddr" ).trim();
		String newDatabaseAddr = props.getProperty( "newDatabaseAddr" ).trim();
		String platformName = props.getProperty( "platformName" ).trim();
		String applicationID = props.getProperty( "applicationID" ).trim();
		String uuidRange = props.getProperty( "uuidRange" ).trim();
		String uuidRangeLeft = uuidRange.split( "," )[0];
		String uuidRangeRight = uuidRange.split( "," )[1];

		try {
			ExtendedMemcachedClientImpl mcOld = new ExtendedMemcachedClientImpl(
					"memPool", oldDatabaseAddr );

			MemcachedClient mcNew = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( newDatabaseAddr ) );

			UserRepo.getInstance().setMC( mcNew );
			GlobalRepo.getInstance().setMC( mcNew );

			long uuid = Long.parseLong( (String) mcOld.get( "UUID" ) );
			int tot = 0;

			mcNew.add( GlobalRepo.getInstance().getUUIDBeginKey( platformName, applicationID ), 0, uuidRangeLeft );
			mcNew.add( GlobalRepo.getInstance().getUUIDEndKey( platformName, applicationID ), 0, uuidRangeRight );
			mcNew.replace( GlobalRepo.getInstance().getUUIDEndKey( platformName, applicationID ), 0, String.valueOf( uuid ) );

			long beg = System.nanoTime();

			for( int uid = 1; uid <= uuid; ++uid ) {
				try {
					if( uid % 1000 == 0 ) {
						logger.info( "tot: {}  time: {}", tot, System.nanoTime() );
					}

					String basicKey = String.format( "U%x-6", uid );
					String basicValue = (String) mcOld.get( basicKey );
					String goldKey = String.format( "U%x-1", uid );
					String goldValue = (String) mcOld.get( goldKey );
					String itemKey = String.format( "U%x-C", uid );
					String itemValue = (String) mcOld.get( itemKey );
					String expKey = String.format( "U%x-0", uid );

					if( basicValue != null && goldValue != null&& itemValue != null ) {
						++tot;

						BasicInfo basicInfo = JsonUtil.fromJson( basicValue, BasicInfo.class );
						if( basicInfo == null || basicInfo.platform_id == null ) {
							logger.info( "basicInfo not good {}", uid );
							continue;
						}

						GoldExpInfo goldExpInfo = JsonUtil.fromJson( goldValue, GoldExpInfo.class );
						
						ExpInfo expInfo = new ExpInfo();
						expInfo.setLevelExp( goldExpInfo.level, goldExpInfo.cur_exp );
						mcOld.set( expKey, JsonUtil.toJson( expInfo ) );
						
						ItemList itemList = JsonUtil.fromJson( itemValue, ItemList.class );

						String[] plat = basicInfo.platform_id.split( "\\-" );
						String platName = plat[0];
						String platID = plat[1];
						String appID = String.valueOf( ( basicInfo.appId == 0L ? applicationID: basicInfo.appId ) );
						
						// 线上服务器还有ROBOT-FUTURE#1367120596这样的platID
						// 目前机器人已经废弃，跳过
						if( "ROBOT".equals( platName ) ) {
							logger.info( "Robot will not save {}", uid );
							continue;
						}

						GlobalRepo.getInstance().newUserID( platName, appID, platID, (long) uid );
						UserRepo.getInstance().openAccount( uid );

						long gameGold = goldExpInfo.game_gold;

						UserRepo.getInstance().replaceGold( uid, gameGold );
						UserRepo.getInstance().replaceHoolaiGold( uid, goldExpInfo.hoolai_gold );
						UserRepo.getInstance().replaceMasterPoint( uid,goldExpInfo.master_point < 0 ? 0: goldExpInfo.master_point );
						// help used 就当0计算了

						UserRepo.getInstance().replaceItems( uid, toNewItemList( itemList ) );
						UserRepo.getInstance().replaceItemUsed( uid, toNewItemUsed( itemList ) );
						UserRepo.getInstance().replaceItemGive( uid, toNewItemGive( itemList ) );
					}
					else {
						// System.out.printf( "User %d not exist\n", uid );
					}
				}
				catch( Exception e ) {
					logger.error( e.getMessage(), e.getCause() );
				}
			}

			long end = System.nanoTime();

			logger.info( "cost nanos: " + ( end - beg ) );

			logger.info( "UUID:" + uuid );
			logger.info( "tot:" + tot );

			mcNew.shutdown();
		}
		catch( IOException e ) {
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
			catch( NumberFormatException e ) {
				logger.error( e.getMessage(), e.getCause() );
			}
			catch( ParseException e ) {
				logger.error( e.getMessage(), e.getCause() );
			}
		}
		return newList;
	}

}
