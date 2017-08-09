package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.scandb.vo.TopPlayerBuf;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FullScanner extends Scanner {

    public static final int TOPN = 20;

    public CenterRepo centerRepo;
    public TopPlayerBuf topPlayerBuf = new TopPlayerBuf( TOPN, null );
    public List< Integer > rangeIds;

    public FullScanner( String name, String rangeIdStr ) {
        super( name );
        rangeIds = Arrays.stream( rangeIdStr.split( "," ) ).map( Integer::parseInt ).collect( Collectors.toList() );
    }

    @Override
    protected void init() {

		try {

		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}

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
