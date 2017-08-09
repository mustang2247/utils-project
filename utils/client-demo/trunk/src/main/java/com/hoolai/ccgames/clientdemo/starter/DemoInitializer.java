package com.hoolai.ccgames.clientdemo.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.skeleton.arch.GameThreadPool;
import com.hoolai.ccgames.skeleton.base.Initializer;
import com.hoolai.ccgames.skeleton.dispatch.CommandRegistry;

public class DemoInitializer implements Initializer {

	private static final Logger logger = LoggerFactory.getLogger( DemoInitializer.class );
	
	@Override
	public boolean init() {
		// 初始化玩法特定的东东
		logger.info( "Start init demo" );
		
		GameThreadPool.init( 2 );
		
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
