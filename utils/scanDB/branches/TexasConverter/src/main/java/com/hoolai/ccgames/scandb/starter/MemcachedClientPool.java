package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 一个简单的连接池
 * 负责分配连接
 * 如果当前连接总数已经达到最大，则阻塞等待
 */

public class MemcachedClientPool {
	
	private static final Logger logger = LoggerFactory.getLogger( MemcachedClientPool.class );
	
	private MemcachedCfg config;
	
	private int currentCount;
	
	private BlockingQueue< MemcachedClient > freeClients;
	
	public MemcachedClientPool( MemcachedCfg cfg ) {
		this.config = cfg;
		currentCount = 0;
		freeClients = new LinkedBlockingQueue<>();
		for( int i = 0; i < config.MAX_CONN; ++i ) {
			MemcachedClient mc = getClient();
			releaseClient( mc );
		}
	}
	
	public MemcachedClient getClient() {
		MemcachedClient c = null;

		// 没有用原子操作，即使超出一些连接也无所谓
		if( currentCount >= config.MAX_CONN ) {
			try {
    			c = freeClients.take();
    		}
    		catch( InterruptedException e ) {
    			logger.error( e.getMessage(), e );
    		}
		}
		else {
			c = DaoFactory.produceMC( config );
			if( c != null ) {
				++currentCount;
			}
		}
		// logger.debug( "return client " + System.currentTimeMillis() );
		return c;
	}
	
	public void releaseClient( MemcachedClient client ) {
		if( client != null )
			freeClients.add( client );
	}
	
	public void shutdown() {
		while( !freeClients.isEmpty() ) {
			MemcachedClient c;
			try {
				c = freeClients.take();
				c.shutdown();
			}
			catch( InterruptedException e ) {
				logger.error( e.getMessage(), e );
			}
		}
	}
}
