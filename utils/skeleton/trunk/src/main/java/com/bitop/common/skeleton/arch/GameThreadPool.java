package com.bitop.common.skeleton.arch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class GameThreadPool {

	public static DefaultEventExecutorGroup DEFAULT_POOL;
	
	private static Map< String, ExecutorService > POOLS;
	
	private static boolean active = false;
	
	public static boolean init( int nthreads ) {
		DEFAULT_POOL = new DefaultEventExecutorGroup( nthreads );
		POOLS = new HashMap< String, ExecutorService >();
		set( "DEFAULT", DEFAULT_POOL );
		active = true;
		return true;
	}
	
	public static boolean init() {
		POOLS = new HashMap< String, ExecutorService >();
		active = true;
		return true;
	}
	
	public static void shutdown() {
		if( active ) {
			for( ExecutorService es : POOLS.values() ) {
				es.shutdown();
			}
		}
	}
	
	public static ExecutorService get( String poolName ) {
		return POOLS.get( poolName );
	}
	
	public static ExecutorService set( String poolName, ExecutorService ses ) {
		return POOLS.put( poolName, ses );
	}
}
