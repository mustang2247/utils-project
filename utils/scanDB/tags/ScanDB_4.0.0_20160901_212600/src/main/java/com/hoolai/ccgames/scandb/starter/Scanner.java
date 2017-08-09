package com.hoolai.ccgames.scandb.starter;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Scanner {

	protected static final Logger logger = LoggerFactory.getLogger( Scanner.class );
	
	protected String name;
	protected CountDownLatch latch;
	
	public Scanner( String name ) {
		this.name = name;
	}
	
	public void run( int concurrentLevel ) {
		latch = new CountDownLatch( concurrentLevel );
		
		try {
			init();
			
			final Scanner scanner = this;
			for( int i = 0; i < concurrentLevel; ++i ) {
				new Thread( new Runnable() {
					@Override
					public void run() {
						scanner.runParalell();
					}
				} ).start();
			}
			
			latch.await();
			
			shutdown();
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
		logger.info( "{} run finish", name );
	}
	
	protected void runParalell() {
		try {
			doWork();
		}
		finally {
			latch.countDown();
		}
	}
	
	protected abstract void init();
	
	protected abstract void doWork();
	
	protected abstract void shutdown();
}
