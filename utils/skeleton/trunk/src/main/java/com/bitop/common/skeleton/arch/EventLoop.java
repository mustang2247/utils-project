package com.bitop.common.skeleton.arch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.bitop.common.skeleton.base.TableBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLoop implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( EventLoop.class );

	private ConcurrentHashMap<TableBase, Boolean> tables;
	private ReentrantLock lock;
	
	public EventLoop() {
		tables = new ConcurrentHashMap< TableBase, Boolean >();
		lock = new ReentrantLock();
	}
	
	public void register( TableBase table ) {
		tables.put( table, Boolean.TRUE );
	}
	
	public void unregister( TableBase table ) {
		tables.remove( table );
	}
	
	@Override
	public void run() {
		// 执行太慢，掉帧了
		if( !lock.tryLock() ) {
			logger.warn( "Slow frame, just ignore" );
			return;
		}
		try {
			for( Map.Entry< TableBase, Boolean > entry : tables.entrySet() ) {
				TableBase table = entry.getKey();
				table.update();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public static void main( String[] args ) {
		ConcurrentHashMap< Integer, Boolean > m = new ConcurrentHashMap< Integer, Boolean >();
		
		for( Integer i = 0; i < 10000; ++i ) {
			m.put( i, Boolean.TRUE );
		}
		
		int tot = 0;
		for( Map.Entry< Integer, Boolean > entry : m.entrySet() ) {
			Integer i = entry.getKey();
			m.remove( i );
			m.put( i - 1, Boolean.TRUE );
			System.out.println( i );
			++tot;
		}
		
		System.out.println( "m size = " + m.size() );
		System.out.println( "tot = " + tot );
	}
}
