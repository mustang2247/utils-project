/**
 * Author: guanxin
 * Date: 2015-07-21
 */

package com.hoolai.ccgames.center.repo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;

import com.hoolai.texaspoker.config.DaoConfig;

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

	private static DaoPool pool;
	static {
		getDaoPool();
	}
	
	public static MemcachedClient produceMC() {
		
		MemcachedClient mc = null;
		
		try {
			mc = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( DaoConfig.ADDRS ) );
		}
		catch( IOException e ) {
			logger.error( e.getMessage(), e );
		}
		
		return mc;
	}

	public static DaoPool getDaoPool() {
		if( pool == null ) pool = new DaoPool();
		return pool;
	}
}
