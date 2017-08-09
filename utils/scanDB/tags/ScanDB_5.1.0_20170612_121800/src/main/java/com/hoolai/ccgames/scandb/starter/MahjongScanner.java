package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.center.vo.BasicInfo;
import com.hoolai.ccgames.center.vo.TopPlayers;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.vo.TopPlayerBuf;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.hoolai.centersdk.sdk.CenterGateSdk.GAMEID_MANGJIANG;

public class MahjongScanner extends Scanner {

    public static final int TOPN = 20;

    public CenterRepo centerRepo;
    public Map< Integer, TopPlayerBuf > type2tpb = new HashMap<>();

    public List< Integer > rangeIds;
    public List< AtomicLong > uuidBegins = new ArrayList<>();
    public List< Long > uuidEnds = new ArrayList<>();

    public MahjongScanner( String name, List< Integer > rangeIds, boolean scanOld ) {
        super( name );
        this.rangeIds = rangeIds;
        // 这是之前麻将混在老德州中的用户ID区间，大约有18w的用户
        if( scanOld ) {
            uuidBegins.add( new AtomicLong( 8596214L ) );
            uuidEnds.add( 10502214L );
        }
    }

    @Override
    protected void init() {

        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );

            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
            rangeIds.forEach( rangeId -> {
                InfoProto.RangeUUIDResp range = centerRepo.getUUIDRange( rangeId );
                if( range == null ) {
                    logger.error( "Can't get uuid range {}", rangeId );
                    return;
                }
                logger.info( "uuid range {} [{},{}]", rangeId, range.getBegin(), range.getEnd() );
                uuidBegins.add( new AtomicLong( range.getBegin() ) );
                uuidEnds.add( range.getEnd() );
            } );
            type2tpb.put( 1, new TopPlayerBuf( TOPN, ( a, b ) -> {
                if( a.gold < b.gold ) return -1;
                if( a.gold > b.gold ) return 1;
                return 0;
            } ) );
//            type2tpb.put( 2, new TopPlayerBuf( TOPN, ( a, b ) -> {
//                if( a.diamond < b.diamond ) return -1;
//                if( a.diamond > b.diamond ) return 1;
//                return 0;
//            } ) );
//            type2tpb.put( 3, new TopPlayerBuf( TOPN, ( a, b ) -> {
//                if( a.level < b.level ) return -1;
//                if( a.level > b.level ) return 1;
//                return 0;
//            } ) );
//            type2tpb.put( 4, new TopPlayerBuf( TOPN, ( a, b ) -> {
//                if( a.vipLevel < b.vipLevel ) return -1;
//                if( a.vipLevel > b.vipLevel ) return 1;
//                return 0;
//            } ) );

        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }

    }

    @Override
    protected void doWork() {

        for( int i = 0; i < uuidBegins.size(); ++i ) {
            AtomicLong uuidBegin = uuidBegins.get( i );
            long uuidEnd = uuidEnds.get( i );
            while( true ) {
                long uid = uuidBegin.getAndIncrement();
                if( uid > uuidEnd ) break;

                try {
                    long gold = centerRepo.getGold( uid );
//                    long diamond = centerRepo.getDiamond( uid );
//                    int lv = centerRepo.getLevel( uid );
//                    long exp = centerRepo.getNewExp( uid );
//                    int vipLv = centerRepo.getVipLevel( uid );
//                    long vipExp = centerRepo.getVipExp( uid );

                    logger.debug( "User:{} Gold:{} Diamond:{} Level:{} VipLevel:{}", uid, gold, 0, 0, 0 );
                    type2tpb.get( 1 ).add( new TopPlayers.TopPlayer( uid, gold, 0, 0, 0, "", "" ) );
//                    type2tpb.get( 2 ).add( new TopPlayers.TopPlayer( uid, gold, diamond, lv, vipLv, "", "" ) );
//                    type2tpb.get( 3 ).add( new TopPlayers.TopPlayer( uid, gold, diamond, lv, vipLv, "", "" ) );
//                    type2tpb.get( 4 ).add( new TopPlayers.TopPlayer( uid, gold, diamond, lv, vipLv, "", "" ) );
                }
                catch( Exception e ) {
                    logger.error( "Scan UserId : {} ERR", uid );
                }
            }
        }
    }

    @Override
    protected void shutdown() {
        try {
            type2tpb.forEach( ( type, tpb ) -> {
                TopPlayers topPlayers = new TopPlayers();
                topPlayers.timestamp = System.currentTimeMillis();
                tpb.heap.sort_heap( tpb.count );
                for( int i = 0; i < TOPN; ++i ) {
                    TopPlayers.TopPlayer player = tpb.topPlayers[i];
                    long userId = tpb.topPlayers[i].userId;
                    if( userId > 0 ) {
                        BasicInfo basicInfo = centerRepo.getBasicInfo( userId );
                        player.name = basicInfo == null ? "" : basicInfo.name;
                        player.headImgUrl = basicInfo == null ? "" : basicInfo.headImgUrl;
                        topPlayers.add( player );
                    }
                }

                logger.info( "Result GameId:{} Type:{} TopData:{}", GAMEID_MANGJIANG, type, JsonUtil.toString( topPlayers ) );
                centerRepo.setGameTopPlayers( GAMEID_MANGJIANG, type, topPlayers );
            } );

            if( centerRepo != null ) centerRepo.shutdown();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

}
