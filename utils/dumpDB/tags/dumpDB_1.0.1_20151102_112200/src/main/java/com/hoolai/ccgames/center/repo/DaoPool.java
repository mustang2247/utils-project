package com.hoolai.ccgames.center.repo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.config.DaoConfig;

/**
 * 一个简单的连接池
 * 负责分配连接
 * 如果当前连接总数已经达到最大，则阻塞等待
 */

public class DaoPool {
	
	private static final Logger logger = LoggerFactory.getLogger( DaoPool.class );
	
	private int currentCount;
	
	private BlockingQueue< MemcachedClient > freeClients;
	
	public DaoPool() {
		currentCount = 0;
		freeClients = new LinkedBlockingQueue< MemcachedClient >();
		for( int i = 0; i < DaoConfig.INIT_CONN; ++i ) {
			MemcachedClient mc = getClient();
			releaseClient( mc );
		}
	}
	
	public MemcachedClient getClient() {
		MemcachedClient c = null;
		
		// 没有用原子操作，即使超出一些连接也无所谓
		if( currentCount >= DaoConfig.MAX_CONN ) {
			try {
    			c = freeClients.take();
    		}
    		catch( InterruptedException e ) {
    			logger.error( e.getMessage(), e );
    		}
		}
		else {
			c = DaoFactory.produceMC();
			if( c != null ) {
				++currentCount;
			}
		}
		logger.debug( "return client " + System.currentTimeMillis() );
		return c;
	}
	
	public void releaseClient( MemcachedClient client ) {
		if( client != null )
			freeClients.add( client );
	}
	
	public void destroy() {
		
		logger.info( "DaoPool destroy" );
		
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
