package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.center.vo.BasicInfo;
import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.ItemUnit;
import com.hoolai.ccgames.center.vo.TopPlayers;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.vo.TopPlayerBuf;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.hoolai.centersdk.sdk.CenterGateSdk.GAMEID_LANDLORDS;

public class LandlordsScanner extends Scanner {

    public static final String PLATFORM = "TENCENT";

    public static final String LANDLORDS_APPID = "1104791642";

    public static final int TOPN = 20;

    public CenterRepo centerRepo;
    public Map< String, TopPlayerBuf > appid2tpb = new HashMap<>();

    public AtomicLong uuid = new AtomicLong( 0 );
    public long uuidEnd = 0;

    public LandlordsScanner( String name ) {
        super( name );
    }

    @Override
    protected void init() {
        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );
            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
            InfoProto.RangeUUIDResp range = centerRepo.getUUIDRange( PLATFORM, LANDLORDS_APPID );
            if( range == null ) {
                throw new RuntimeException( "Can't get landlords uuid range" );
            }
            else {
                logger.info( "range {} {}", range.getBegin(), range.getEnd() );
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

        // 斗地主区间
        while( true ) {
            long uid = uuid.getAndIncrement();
            if( uid > uuidEnd ) break;

            try {
                String appId = centerRepo.getAppId( uid );
                long gold = centerRepo.getGold( uid );
                int lv = centerRepo.getLevel( uid );
                ItemList items = centerRepo.getAllItems( uid, GAMEID_LANDLORDS );
                String str = "";
                for( ItemUnit item : items.items ) {
                    str += item.itemID + "," + item.itemCount + ";";
                }
                logger.debug( "User:{} Gold:{} Level:{} Items:{}", uid, gold, lv, str );

                if( appId != null ) {
                    TopPlayerBuf tpb = appid2tpb.get( appId );
                    if( tpb == null ) {
                        tpb = new TopPlayerBuf( TOPN );
                        appid2tpb.put( appId, tpb );
                    }
                    tpb.add( new TopPlayers.TopPlayer( uid, gold, lv, "", "" ) );
                }
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
        }
    }

    @Override
    protected void shutdown() {
        try {
            appid2tpb.forEach( ( k, v ) -> {
                TopPlayers topPlayers = new TopPlayers();
                v.heap.sort_heap( v.count );
                for( int i = 0; i < TOPN; ++i ) {
                    TopPlayers.TopPlayer player = v.topPlayers[i];
                    long userId = v.topPlayers[i].userId;
                    if( userId > 0 ) {
                        BasicInfo basicInfo = centerRepo.getBasicInfo( userId );
                        player.name = basicInfo == null ? "" : basicInfo.name;
                        player.headImgUrl = basicInfo == null ? "" : basicInfo.headImgUrl;
                    }
                    topPlayers.add( player );
                }
                topPlayers.timestamp = System.currentTimeMillis();
                logger.info( "ENDING APPID:{} TOP:{}", k, JsonUtil.toString( topPlayers ) );
                centerRepo.setAppTopPlayers( k, topPlayers );
            } );

            if( centerRepo != null ) centerRepo.shutdown();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

}
