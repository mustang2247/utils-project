package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.ItemUnit;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClient;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClientImpl;
import com.hoolai.ccgames.scandb.vo.TopPlayer;
import com.hoolai.ccgames.scandb.vo.TopPlayerList;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.concurrent.atomic.AtomicLong;

import static com.hoolai.centersdk.sdk.CenterGateSdk.GAMEID_MANGJIANG;

public class MahjongScanner extends Scanner {

    public static final String PLATFORM = "TENCENT";

    public static final String MAHJONG_APPID = "1104754063";
    public static final String ERREN_MAHJONG_APPID = "1104791638";
    public static final int TOPN = 20;
    public static final String TEXAS_TOP_KEY = "MAHJONG_TOP_PLAYER";
    public CenterRepo centerRepo;
    public MemcachedCfg mahjongCfg = new MemcachedCfg();
    public TopPlayerList mahjongTpl = new TopPlayerList( TOPN );

    public AtomicLong oldUuid = new AtomicLong( 7000000L );
    public long oldUuidEnd = 10502214L;

    public AtomicLong uuid = new AtomicLong( 0 );
    public long uuidEnd = 0;

    public AtomicLong errenUuid = new AtomicLong( 0 );
    public long errenUuidEnd = 0;

    public boolean scanOld = false;

    public MahjongScanner( String name, boolean scanOld ) {
        super( name );
        this.scanOld = scanOld;
    }

    @Override
    protected void init() {

        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );

            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
            mahjongCfg.init( PropUtil.getSectionProps( "/" + envDir + "memcached.properties" ).get( "Mahjong" ) );

            InfoProto.RangeUUIDResp range = centerRepo.getUUIDRange( PLATFORM, MAHJONG_APPID );
            if( range == null ) {
                throw new RuntimeException( "Can't get mahjong uuid range" );
            }
            uuid.set( range.getBegin() );
            uuidEnd = range.getEnd();

            range = centerRepo.getUUIDRange( PLATFORM, ERREN_MAHJONG_APPID );
            if( range == null ) {
                throw new RuntimeException( "Can't get erren_mahjong uuid range" );
            }
            errenUuid.set( range.getBegin() );
            errenUuidEnd = range.getEnd();

        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }

    }

    @Override
    protected void doWork() {
        // 读取老德州中的用户
        while( scanOld ) {
            long uid = oldUuid.getAndIncrement();
            if( uid > oldUuidEnd ) break;

            try {
                String appId = centerRepo.getAppId( uid );
                if( !MAHJONG_APPID.equals( appId ) ) continue;

                long gold = centerRepo.getGold( uid );
                int lv = centerRepo.getLevel( uid );
                ItemList items = centerRepo.getAllItems( uid, GAMEID_MANGJIANG );
                String str = "";
                for( ItemUnit item : items.items ) {
                    str += item.itemID + "," + item.itemCount + ";";
                }
                mahjongTpl.add( new TopPlayer( uid, lv, gold ) );
                logger.debug( "User:{} Gold:{} Items:{}", uid, gold, str );
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
        }

        // 读取麻将达人区间
        while( true ) {
            long uid = uuid.getAndIncrement();
            if( uid > uuidEnd ) break;

            try {
                long gold = centerRepo.getGold( uid );
                int lv = centerRepo.getLevel( uid );
                ItemList items = centerRepo.getAllItems( uid, GAMEID_MANGJIANG );
                String str = "";
                for( ItemUnit item : items.items ) {
                    str += item.itemID + "," + item.itemCount + ";";
                }
                mahjongTpl.add( new TopPlayer( uid, lv, gold ) );
                logger.debug( "User:{} Gold:{} Level:{} Items:{}", uid, gold, lv, str );
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
        }

        // 读取二麻区间
        while( true ) {
            long uid = errenUuid.getAndIncrement();
            if( uid > errenUuidEnd ) break;

            try {
                long gold = centerRepo.getGold( uid );
                int lv = centerRepo.getLevel( uid );
                ItemList items = centerRepo.getAllItems( uid, GAMEID_MANGJIANG );
                String str = "";
                for( ItemUnit item : items.items ) {
                    str += item.itemID + "," + item.itemCount + ";";
                }
                mahjongTpl.add( new TopPlayer( uid, lv, gold ) );
                logger.debug( "User:{} Gold:{} Level:{} Items:{}", uid, gold, lv, str );
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
        }
    }

    @Override
    protected void shutdown() {
        try {
            ExtendedMemcachedClient mahjongDB = new ExtendedMemcachedClientImpl( "MahjongDB", mahjongCfg.ADDRS );
            mahjongDB.start();

            mahjongDB.set( TEXAS_TOP_KEY, mahjongTpl.toString() );

            if( centerRepo != null ) centerRepo.shutdown();
            if( mahjongDB != null ) mahjongDB = null;
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

}
