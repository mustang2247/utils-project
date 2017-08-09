package com.hoolai.ccgames.scandb.starter;

import java.util.concurrent.atomic.AtomicLong;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.center.vo.LevelExp;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClient;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClientImpl;
import com.hoolai.ccgames.scandb.vo.TopPlayer;
import com.hoolai.ccgames.scandb.vo.TopPlayerList;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

public class FullScanner extends Scanner {

	public static final String PLATFORM = "TENCENT";
	
	public static final String TEXAS_APPID = "100632434";
	public static final String MAHJONG_APPID = "1104754063";

	public MemcachedCfg memcachedCfg;
	
	public ExtendedMemcachedClient texasDB;
	public ExtendedMemcachedClient mahjongDB;
	public CenterRepo centerRepo;
	
	public AtomicLong uuid = new AtomicLong( 0 );
	public long uuidEnd;
	
	public static final int TOPN = 20;
	public static final String TEXAS_TOP_KEY = "TEXAS_TOP_PLAYER";
	public static final String MAHJONG_TOP_KEY = "MAHJONG_TOP_PLAYER";
	public TopPlayerList texasTpl = new TopPlayerList( TOPN );
	public TopPlayerList mahjongTpl = new TopPlayerList( TOPN );
	
	public FullScanner( String name ) {
		super( name );
	}

	@Override
	protected void init() {

//		try {
//			String envDir = PropUtil.getProp( "/env.properties", "envdir" );
//			memcachedCfg = new MemcachedCfg();
//			memcachedCfg.init( "/" + envDir + "memcached.properties" );
//
//			centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
//
//			InfoProto.RangeUUIDResp range = centerRepo.getUUIDRange( PLATFORM, TEXAS_APPID );
//			if( range == null ) {
//				throw new RuntimeException( "Can't get texas uuid range" );
//			}
//			uuid.set( range.getBegin() );
//			uuidEnd = range.getEnd();
//
//		}
//		catch( Exception e ) {
//			logger.error( e.getMessage(), e );
//		}
		
	}
	
	@Override
	protected void doWork() {
//		while( true ) {
//    		long uid = uuid.getAndIncrement();
//    		if( uid > uuidEnd ) break;
//
//    		try {
//    			long gold = centerRepo.getGold( uid );
//    			int lv = centerRepo.getLevel( uid );
//    			logger.debug( "User:{} Level:{} Gold:{}", uid, lv, gold );
//    			String appid = centerRepo.getAppId( uid );
//    			if( TEXAS_APPID.equals( appid ) ) {
//    				texasTpl.add( new TopPlayer( uid, lv, gold ) );
//    			}
//    			else if( MAHJONG_APPID.equals( appid ) ) {
//    				mahjongTpl.add( new TopPlayer( uid, lv, gold ) );
//    			}
//    		}
//    		catch( Exception e ) {
//    			logger.error( "Scan UserId : {} ERR", uid );
//    		}
//		}
	}

	@Override
	protected void shutdown() {
//		try {
//			texasDB = new ExtendedMemcachedClientImpl( "TexasDB", memcachedCfg.TEXAS_ADDRS );
//			texasDB.start();
//			mahjongDB = new ExtendedMemcachedClientImpl( "MahjongDB", memcachedCfg.MAHJONG_ADDRS );
//			mahjongDB.start();
//
//			texasDB.set( TEXAS_TOP_KEY, texasTpl.toString() );
//			mahjongDB.set( MAHJONG_TOP_KEY, mahjongTpl.toString() );
//
//			if( centerRepo != null ) centerRepo.shutdown();
//			if( texasDB != null ) texasDB = null;
//			if( mahjongDB != null ) mahjongDB = null;
//    	}
//    	catch( Exception e ) {
//    		logger.error( e.getMessage(), e );
//    	}
	}

}
