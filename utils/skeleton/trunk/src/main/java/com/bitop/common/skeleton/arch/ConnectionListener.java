package com.bitop.common.skeleton.arch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionListener implements ChannelFutureListener {

	private static final Logger logger = LoggerFactory.getLogger( ConnectionListener.class );
	
	private GameClient client;
	private boolean retry;

	public ConnectionListener( GameClient client, boolean retry ) {
		this.client = client;
		this.retry = retry;
	}

	@Override
	public void operationComplete( ChannelFuture channelFuture )
			throws Exception {
		if( channelFuture.isSuccess() ) {
			logger.info( "GameClient-{} connect to server OK", client.getName() );
		}
		else {
			logger.error( "GameClient-{} connect to server FAIL", client.getName() );
			if( retry ) {
				logger.info( "GameClient-{} retry connect to server", client.getName() );
				Thread.sleep( 1000L );
				client.reconnect();
			}
		}
	}
}