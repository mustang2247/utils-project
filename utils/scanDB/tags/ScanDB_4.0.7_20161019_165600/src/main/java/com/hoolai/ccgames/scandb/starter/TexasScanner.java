package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClient;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClientImpl;
import com.hoolai.ccgames.scandb.vo.TopPlayer;
import com.hoolai.ccgames.scandb.vo.TopPlayerList;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.concurrent.atomic.AtomicLong;

public class TexasScanner extends Scanner {

	public static final String PLATFORM = "TENCENT";

	public static final String TEXAS_APPID = "100632434";

	public static final int TOPN = 20;
	public static final String TEXAS_TOP_KEY = "TEXAS_TOP_PLAYER";
	public TopPlayerList texasTpl = new TopPlayerList( TOPN );

	public CenterRepo centerRepo;
	public MemcachedCfg texasCfg = new MemcachedCfg();

	public AtomicLong uuid = new AtomicLong( 0 );
	public long uuidEnd;

	public TexasScanner( String name ) {
		super( name );
	}

	@Override
	protected void init() {
		
		try {
			String envDir = PropUtil.getProp( "/env.properties", "envdir" );

			centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
			texasCfg.init( PropUtil.getSectionProps( "/" + envDir + "memcached.properties" ).get( "Texaspoker" ) );

			InfoProto.RangeUUIDResp range = centerRepo.getUUIDRange( PLATFORM, TEXAS_APPID );
			if( range == null ) {
				logger.error( "Can't get texaspoker uuid range" );
				throw new RuntimeException( "Can't get texaspoker uuid range" );
			}
			uuid.set( range.getBegin() );
			uuidEnd = range.getEnd();
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}
	
	@Override
	protected void doWork() {
		while( true ) {
    		long uid = uuid.getAndIncrement();
    		if( uid > uuidEnd ) break;
    		
    		try {
				String appId = centerRepo.getAppId( uid );
				if( !TEXAS_APPID.equals( appId ) ) continue;

				long gold = centerRepo.getGold( uid );
				int lv = centerRepo.getLevel( uid );
				texasTpl.add( new TopPlayer( uid, lv, gold ) );
				logger.debug( "User:{} Level:{} Gold:{}", uid, lv, gold );
    		}
    		catch( Exception e ) {
    			logger.error( "Scan UserId : {} ERR", uid );
    		}
		}
	}

	@Override
	protected void shutdown() {
		try {
			ExtendedMemcachedClient texasDB = new ExtendedMemcachedClientImpl( "TexasDB", texasCfg.ADDRS );
			texasDB.start();

			texasDB.set( TEXAS_TOP_KEY, texasTpl.toString() );

			if( centerRepo != null ) centerRepo.shutdown();
			if( texasDB != null ) texasDB = null;
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}

}
