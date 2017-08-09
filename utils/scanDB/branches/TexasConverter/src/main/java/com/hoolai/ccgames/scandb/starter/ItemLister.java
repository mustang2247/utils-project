package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;
import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.database.trans.ItemTranscoder;
import com.hoolai.ccgames.skeleton.utils.PropUtil;
import net.spy.memcached.MemcachedClient;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hoolai on 2016/9/24.
 */
public class ItemLister extends Scanner {

    public CenterRepo centerRepo;
    public MemcachedClientPool mcMainPool;
    protected static ItemTranscoder ITEM_TC = new ItemTranscoder();

    public long userId;

    public ItemLister( String name, long userId ) {
        super( name );
        this.userId = userId;
    }

    @Override
    protected void init() {

        try {
            String envDir = PropUtil.getProp( "/env.properties", "envdir" );

            centerRepo = new CenterRepoImpl( "/" + envDir + "cgate.properties" );
            MemcachedCfg mainCfg = new MemcachedCfg();
            mainCfg.init( PropUtil.getSectionProps( "/" + envDir + "memcached.properties" ).get( "Main" ) );
            mcMainPool = new MemcachedClientPool( mainCfg );
            Thread.sleep( 1000L );
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

    @Override
    protected void doWork() {
        MemcachedClient dbClient = mcMainPool.getClient();
        try {
            String key = new StringBuilder().append( "Item" ).append( ':' ).append( userId )
                    .toString();
            ItemList itemPack = dbClient.get( key, ITEM_TC );
            itemPack.print();
            ItemList commonPack = centerRepo.getItems( userId, "-1" );
            commonPack.print();
            ItemList texasPack = centerRepo.getItems( userId, "1010" );
            texasPack.print();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
        finally {
            mcMainPool.releaseClient( dbClient );
        }
    }

    @Override
    protected void shutdown() {
        try {
            if( centerRepo != null ) centerRepo.shutdown();
            if( mcMainPool != null ) mcMainPool.shutdown();
        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }
}
