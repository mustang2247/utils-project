package com.hoolai.texaspoker.scandb;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.PropertyConfigurator;

import com.hoolai.ccgames.center.repo.DaoFactory;
import com.hoolai.ccgames.center.repo.DaoPool;
import com.hoolai.ccgames.center.repo.GlobalRepo;
import com.hoolai.ccgames.center.repo.UserRepo;
import com.hoolai.texaspoker.bi.BICollector;
import com.hoolai.texaspoker.bi.BIManager;
import com.hoolai.texaspoker.config.DaoConfig;
import com.hoolai.texaspoker.config.GlobalConfig;
import com.hoolai.texaspoker.datatype.BasicInfo;
import com.hoolai.texaspoker.datatype.Clan;
import com.hoolai.texaspoker.datatype.ClanMember;
import com.hoolai.texaspoker.datatype.ExpInfo;
import com.hoolai.texaspoker.datatype.LimitsAndConsts;
import com.hoolai.texaspoker.datatype.TopPlayer;
import com.hoolai.texaspoker.datatype.TopPlayerList;
import com.hoolai.texaspoker.datatype.VIPInfo;

public class App 
{
	
	public static AtomicLong uuid = new AtomicLong();
	public static long uuidEnd;
	public static CountDownLatch latch;
	public static DaoPool pool;
	public static GlobalRepo globalRepo;
	public static UserRepo userRepo;
	public static DaoInterface client;
	
	// 统计全服前TOPN选手
	public static TopPlayerList  topPlayerList;
	public static int TOPN = 20;
	public static MinHeap<TopPlayer>  heap;
	
    public static void main( String[] args )
    {
    	if( args.length == 0 ) {
    		System.err.println( "USAGE: java -jar scanDB.jar nthread" );
    		return;
    	}
    	
    	int nthread = Integer.parseInt( args[0] );
    	
    	init();
    	
    	scanAll( nthread );
    	
    	pool.destroy();
    	
    	System.exit( 0 );
    	
    }
    
    public static void scanAll( int nthread ) {
    	
    	App.readUUID();
    	
    	long uuid_beg = uuid.get();
    	
    	latch = new CountDownLatch( nthread );
		
		long beg = System.nanoTime();
		
		for( int i = 0; i < nthread; ++i ) {
			new Thread( new Runnable() {
				@Override
				public void run() {
					App.scanParalell();
				}
			} ).start();
		}
		try {
			latch.await();
			
			heap.sort_heap( TOPN );
    		client.save( "TOP_PLAYERS", JsonUtil.toJson( topPlayerList ) );
			
			Log.i( "total dump: " + uuid_beg + " ~ " + uuidEnd );
			Log.i( "finished cost: " + ( System.nanoTime() - beg ) );
			
		}
		catch( Exception e ) {
			Log.e( e.getMessage(), e );
		}
    	
    }
    
    public static void readUUID() {
    	
    	MemcachedClient c = pool.getClient();
    	
    	try {
    		String beg = (String) c.get( globalRepo.getUUIDBeginKey( DaoConfig.PLATFORM_NAME, DaoConfig.APP_ID ) );
    		uuid.set( Long.parseLong( beg ) );
    		String end = (String) c.get( globalRepo.getUUIDEndKey( DaoConfig.PLATFORM_NAME, DaoConfig.APP_ID ) );
    		uuidEnd = Long.parseLong( end );
    	}
    	finally {
    		pool.releaseClient( c );
    	}
    }
    
    public static void scanParalell() {
    	
    	while( true ) {
    		int uid = (int) uuid.getAndIncrement();
    		if( uid > uuidEnd ) break;
    		
    		try {
        		String basicKey = String.format( "U%x-6", uid );
    			String basicValue = (String) client.load( basicKey );
    			String expKey = String.format( "U%x-0", uid );
    			String expValue = (String) client.load( expKey );
    			String vipKey = String.format( "U%x-7", uid );
    			String vipValue = (String) client.load( vipKey );
    			
    			Log.i( "[UserID:" + uid + "] " + basicValue );
    			Log.i( "[UserID:" + uid + "] " + expValue );
    			
    			if( basicValue != null && expValue != null ) {
    				BasicInfo  basicInfo = JsonUtil.fromJson( basicValue, BasicInfo.class );
    				if( basicInfo == null || basicInfo.platform_id == null ) {
    					continue;
    				}
    				Clan clan = null;
    				if (basicInfo.team_id != null && basicInfo.team_id != LimitsAndConsts.UNKNOWN_ID) {
    					String clanKey = String.format("clan-%d", basicInfo.team_id);
    					String clanValue = (String) client.load(clanKey);
    					clan = JsonUtil.fromJson(clanValue, Clan.class);
    					Log.i( "[UserID:" + uid + "] " + clanValue );
    				}
    				
    				String[] plat = basicInfo.platform_id.split( "\\-" );
    				if( !plat[0].equals( BIManager.platform ) ) continue;
    				String  platID = plat[1];
    				
					long gold = UserRepo.getInstance().getGold( uid );
					long hoolaiGold = UserRepo.getInstance().getHoolaiGold( uid );
    					
    				Log.i( "[UserID:" + uid + "] gold:" + gold + "  hoolai:" + hoolaiGold );
    					
    				ExpInfo expInfo = JsonUtil.fromJson( expValue, ExpInfo.class );
    					
    				VIPInfo vipInfo = JsonUtil.fromJson( vipValue, VIPInfo.class );
    					
					int code = 0;
					if (clan != null) {
						ClanMember member = clan.members.get( uid );
						if (member != null) {
							code = member.No != LimitsAndConsts.UNKNOWN_INT ? 1 : 0; // 说明是领队
						}
					}
    					

					BICollector.onSendGoldInfo( platID, basicInfo,
							String.valueOf( gold ),
							hoolaiGold,
							String.valueOf( expInfo.level ),String.valueOf( uid ),code,0);
    				
					TopPlayer player = new TopPlayer( uid,
							basicInfo.user_name,
							basicInfo.head_img_url,
							gold,
							(int)expInfo.level,
							(int)vipInfo.vip_now );
					
					updateTopPlayer( player );
    			}

    			else {
    				Log.e("[ScanDB] can't find userinfo " + uid );
    			}
			} catch (Exception e) {
				Log.e( "[ScanDB] Exception " + uid, e );
			}
    	}
    	
    	latch.countDown();
    }
    
    public static synchronized void updateTopPlayer( TopPlayer tp ) {
    	topPlayerList.players[TOPN] = tp;
    	heap.push_heap( TOPN );
    }
    
    private static void init() {
		Log.i("Initializing begin...");
		
		Properties properties = PropertiesReader.create("log4j_common.properties");
    	PropertyConfigurator.configure(properties);
    	
    	ContextUtil.init();
		
		
		Properties db = PropertiesReader.create(GlobalConfig.DAO_CONFIG_URL);
		String db_type = db.getProperty("type");
		if (db_type.equals("memcached")) {
			DaoService.setInstance(new DaoMemcached((ExtendedMemcachedClientImpl) ContextUtil.getBean("memPool")));
			Object orderObj = DaoService.getInstance().load("PHONEORDER");
			if (orderObj == null || (Long.parseLong((String) orderObj)) < 10000) {
				DaoService.getInstance().save("PHONEORDER", "9999");
			}
		} else {
			DaoService.setInstance(new DaoRedis((JedisPool)ContextUtil.getBean("jedisPool")));
			Object orderObj = DaoService.getInstance().load("PHONEORDER");
			if (orderObj == null || (Long.parseLong((String) orderObj)) < 10000) {
				DaoService.getInstance().save("PHONEORDER", "9999");
			}
		}
		
		
		DaoConfig.init( "/tencent/dao.properties" );
		pool = DaoFactory.getDaoPool();
		globalRepo = GlobalRepo.getInstance();
		userRepo = UserRepo.getInstance();
		
		client = DaoService.getInstance();
		BIManager.getInstance().init();
		
		topPlayerList = new TopPlayerList( TOPN );
		heap = new MinHeap<TopPlayer>( topPlayerList.players );
		
		Log.i("Initializing complete!");
	}
}
