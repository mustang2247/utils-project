/**
 * Author: guanxin
 * Date: 2015-07-21
 */

package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//@formatter:off

/**
 * DAO工厂，实际是一个静态类
 * 仔细想了想，没有必要抽出DaoInterface这个接口，因为不同的数据库实现原理可能不一样
 * 例如memcached提供的CAS，redis内部自带的各种数据结构
 * 盲目的通用，只能导致提供几种简单的操作，无法最大化利用该数据库提供的优势
 * 如果在应用层进行各种加锁，一方面增加了应用层的复杂度，另一方面无法多台机器操作同一个数据库
 */
//@formatter:on

public class DaoFactory {

    private final static Logger logger = LoggerFactory
            .getLogger( DaoFactory.class );

    public static MemcachedClient produceMC( MemcachedCfg cfg ) {

        MemcachedClient mc = null;

        try {
            mc = new MemcachedClient(
                    new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
                            .setOpTimeout( 1500 )
                            .setTimeoutExceptionThreshold( 5 )
                            .build(),
                    AddrUtil.getAddresses( cfg.ADDRS ) );
        }
        catch( IOException e ) {
            logger.error( e.getMessage(), e );
        }

        return mc;
    }
}
