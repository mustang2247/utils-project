package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.protocol.InfoProto;
import com.hoolai.ccgames.center.vo.ItemGiveList;
import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.ItemUnit;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.skeleton.utils.PropUtil;
import io.netty.util.internal.ConcurrentSet;

import java.util.concurrent.atomic.AtomicLong;

public class TexasScanner extends Scanner {

    public static final String PLATFORM = "TENCENT";

    public static final String TEXAS_APPID = "100632434";

    public CenterRepo centerRepo;

    public AtomicLong uuid = new AtomicLong( 0 );
    public long uuidEnd;

    public ConcurrentSet< Integer > packItemIds = new ConcurrentSet<>();
    public ConcurrentSet< Integer > usedItemIds = new ConcurrentSet<>();
    public ConcurrentSet< Integer > giveItemIds = new ConcurrentSet<>();

    public TexasScanner( String name ) {
        super( name );
    }

    @Override
    protected void init() {

        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );

            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );

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
                // step 1
                // 扫描老德州用户背包，使用道具记录，赠送记录
                ItemList list = centerRepo.queryItems( uid );
                if( list != null ) {
                    for( ItemUnit item : list.items ) {
                        packItemIds.add( item.itemID );
                    }
                }

                list = centerRepo.queryItemUsed( uid );
                if( list != null ) {
                    for( ItemUnit item : list.items ) {
                        packItemIds.add( item.itemID );
                    }
                }

                ItemGiveList list2 = centerRepo.queryItemGive( uid );
                if( list2 != null ) {
                    list2.records.forEach( record -> giveItemIds.add( record.itemID ) );
                }

                // step 2
                // 如果是老德州用户，首先检测等级，如果是1级，直接跳过
                // 如果大于2级，进行以下操作：
                // 等级转换，并且补偿筹码或道具
                // 道具转换，卖掉不用道具，剩下道具一部分存入私有，一部分转换为公用道具
//				String appId = centerRepo.getAppId( uid );
//				if( !TEXAS_APPID.equals( appId ) ) continue;
//
//				long gold = centerRepo.getGold( uid );
//				int lv = centerRepo.getLevel( uid );
//				logger.debug( "User:{} Level:{} Gold:{}", uid, lv, gold );
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
        }
    }

    @Override
    protected void shutdown() {
        try {
            packItemIds.forEach( id -> logger.info( "pack {} ", id ) );
            usedItemIds.forEach( id -> logger.info( "used {} ", id ) );
            giveItemIds.forEach( id -> logger.info( "give {} ", id ) );

            if( centerRepo != null ) centerRepo.shutdown();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

}
