package com.hoolai.ccgames.skeleton.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SynEnvTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger( SynEnvTask.class );

	private final SynEnvBase synEnv;

	public SynEnvTask( SynEnvBase synEnv ) {
		this.synEnv = synEnv;
	}

	public boolean isSynEnvProcessing() {
		return getSynEnv().isProcessing();
	}

	public void setSynEnvProcessing( boolean isProcessing ) {
		getSynEnv().setProcessing( isProcessing );
	}
	
	public SynEnvBase getSynEnv() {
		return synEnv;
	}

	@Override
	public void run() {
		try {
			processTask();
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
		finally {
			getSynEnv().setProcessing( false );
		}
	}

	public abstract void processTask();

}
