package com.hoolai.ccgames.gamedemo.starter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.centergate.client.CgateConfig;
import com.hoolai.ccgames.gamedemo.repo.RepoManager;
import com.hoolai.ccgames.skeleton.arch.GameThreadPool;
import com.hoolai.ccgames.skeleton.base.Initializer;
import com.hoolai.ccgames.skeleton.base.SynEnvTask;
import com.hoolai.ccgames.skeleton.dispatch.CommandRegistry;
import com.hoolai.ccgames.skeleton.net.BusyBlockingQueue;
import com.hoolai.ccgames.skeleton.utils.PropUtil;

public class DemoInitializer implements Initializer {

	private static Logger logger = LoggerFactory.getLogger( DemoInitializer.class );
	
	@Override
	public boolean init() {
		// 初始化玩法特定的东东
		logger.info( "Start init demo" );
		
		String envDir = PropUtil.getProp( "/env.properties", "envdir" );
		
		logger.info( "CgateConfig " + "/" + envDir + "cgate.properties" );
		CgateConfig.init( "/" + envDir + "cgate.properties" );
		
		RepoManager.init();
		
		GameThreadPool.init( 4 );
		GameThreadPool.set( "SYNC", new ThreadPoolExecutor( 1, 4, 10L, TimeUnit.SECONDS, new BusyBlockingQueue< SynEnvTask >( Integer.MAX_VALUE ) ) );
		GameThreadPool.set( "SLOW", new ThreadPoolExecutor( 1, 4, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue< Runnable >( Integer.MAX_VALUE ) ) );
		
		try {
			CommandRegistry.getInstance().init( "/commands.xml" );
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
			throw new RuntimeException( "DemoInitializer init fail" );
		}
		
		logger.info( "DemoInitializer init OK" );
		
		return true;
	}

}
