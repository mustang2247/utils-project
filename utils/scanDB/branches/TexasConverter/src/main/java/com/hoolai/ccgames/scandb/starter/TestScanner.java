package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.vo.ItemGiveList;
import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.LevelExp;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.database.trans.ItemGiveTranscoder;
import com.hoolai.ccgames.scandb.database.trans.ItemTranscoder;
import com.hoolai.ccgames.scandb.database.trans.LevelExpTranscoder;
import com.hoolai.ccgames.skeleton.utils.PropUtil;
import net.spy.memcached.MemcachedClient;

import java.util.concurrent.atomic.AtomicLong;

public class TestScanner extends Scanner {

    public static final String PLATFORM = "TENCENT";

    public static final String TEXAS_APPID = "100632434";
    public static final String HLQP_APPID = "1104737759";
    public static final String DDZHJ_APPID = "1104830871";
    protected static ItemTranscoder ITEM_TC = new ItemTranscoder();
    protected static ItemGiveTranscoder ITEM_GIVE_TC = new ItemGiveTranscoder();
    protected static LevelExpTranscoder LEVEL_EXP_TC = new LevelExpTranscoder();
    public CenterRepo centerRepo;
    public MemcachedClientPool mcMainPool;
    public AtomicLong uuid = new AtomicLong( 0 );
    public long uuidEnd;
    public long userId;

//    public ConcurrentSet< Integer > packItemIds = new ConcurrentSet<>();
//    public ConcurrentSet< Integer > usedItemIds = new ConcurrentSet<>();
//    public ConcurrentSet< Integer > giveItemIds = new ConcurrentSet<>();

    public TestScanner( String name, long userId ) {
        super( name );
        this.userId = userId;
    }

    @Override
    protected void init() {

        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );

            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
            Converter.centerRepo = centerRepo;

            MemcachedCfg mainCfg = new MemcachedCfg();
            mainCfg.init( PropUtil.getSectionProps( "/" + envDir + "memcached.properties" ).get( "Main" ) );
            mcMainPool = new MemcachedClientPool( mainCfg );
            Thread.sleep( 1000L );

            uuid.set( userId );
            uuidEnd = userId;
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
            MemcachedClient dbClient = mcMainPool.getClient();

            try {
                // step 1
                // 扫描老德州用户背包，使用道具记录，赠送记录
//                ItemList list = centerRepo.queryItems( uid );
//                if( list != null ) {
//                    for( ItemUnit item : list.items ) {
//                        packItemIds.add( item.itemID );
//                    }
//                }
//
//                list = centerRepo.queryItemUsed( uid );
//                if( list != null ) {
//                    for( ItemUnit item : list.items ) {
//                        packItemIds.add( item.itemID );
//                    }
//                }
//
//                ItemGiveList list2 = centerRepo.queryItemGive( uid );
//                if( list2 != null ) {
//                    list2.records.forEach( record -> giveItemIds.add( record.itemID ) );
//                }

                // step 2
                // 如果是老德州用户，进行以下操作：
                // 等级转换，并且补偿筹码或道具
                // 道具转换，卖掉不用道具，剩下道具一部分存入私有，一部分转换为公用道具
                // 道具使用记录转换
                // 道具赠送记录转换
                String key = new StringBuilder().append( "OldProcess" ).append( ':' ).append( uid )
                        .toString();
                String appid = centerRepo.getAppId( uid );
                logger.debug( "==== try to convert user {} ====", uid );
                if( uid > 0 && dbClient.incr( key, 1, 1, 0 ) == 1  // 第一次处理
                        && ( TEXAS_APPID.equals( appid ) || HLQP_APPID.equals( appid )
                        || DDZHJ_APPID.equals( appid ) ) ) {
                    logger.debug( "==== start to convert user {} ====", uid );
                    key = new StringBuilder().append( "Exp" ).append( ':' )
                            .append( uid )
                            .toString();
                    LevelExp levelExp = dbClient.get( key, LEVEL_EXP_TC );
                    if( levelExp != null ) {
                        Converter.convertLevel( uid, levelExp.level );
                    }

                    Converter.convertMP( uid );

                    key = new StringBuilder().append( "Item" ).append( ':' ).append( uid )
                            .toString();
                    ItemList itemPack = dbClient.get( key, ITEM_TC );
                    if( itemPack != null ) {
                        Converter.convertPacks( uid, itemPack );
                    }

                    key = new StringBuilder().append( "ItemUsed" ).append( ':' ).append( uid )
                            .toString();
                    ItemList itemUsed = dbClient.get( key, ITEM_TC );
                    if( itemUsed != null ) {
                        Converter.convertUsed( uid, itemUsed );
                    }

                    key = new StringBuilder().append( "ItemGive" ).append( ':' ).append( uid )
                            .toString();
                    ItemGiveList itemGive = dbClient.get( key, ITEM_GIVE_TC );
                    if( itemGive != null ) {
                        Converter.convertGive( uid, itemGive );
                    }
                }
            }
            catch( Exception e ) {
                logger.error( "Scan UserId : {} ERR", uid );
            }
            finally {
                mcMainPool.releaseClient( dbClient );
            }
        }
    }

    @Override
    protected void shutdown() {
        try {
//            packItemIds.forEach( id -> logger.info( "pack {} ", id ) );
//            usedItemIds.forEach( id -> logger.info( "used {} ", id ) );
//            giveItemIds.forEach( id -> logger.info( "give {} ", id ) );

            if( centerRepo != null ) centerRepo.shutdown();
            if( mcMainPool != null ) mcMainPool.shutdown();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

}
