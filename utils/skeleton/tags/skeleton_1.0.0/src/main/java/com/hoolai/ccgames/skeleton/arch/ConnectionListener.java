package com.hoolai.ccgames.skeleton.arch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionListener implements ChannelFutureListener {

	private static final Logger logger = LoggerFactory.getLogger( ConnectionListener.class );
	
	private GameClient client;

	public ConnectionListener( GameClient client ) {
		this.client = client;
	}

	@Override
	public void operationComplete( ChannelFuture channelFuture )
			throws Exception {
		if( channelFuture.isSuccess() ) {
			logger.info( "GameClient-{} connect to server OK", client.getName() );
			client.setActive( true );
		}
		else {
			logger.info( "GameClient-{} connect to server FAIL, retry...", client.getName() );
			Thread.sleep( 1000L );
			client.reconnect();
		}
	}
}