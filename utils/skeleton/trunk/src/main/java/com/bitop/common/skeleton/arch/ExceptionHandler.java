package com.bitop.common.skeleton.arch;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

public class ExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger( ExceptionHandler.class );
	
	public static void handle( ChannelHandlerContext ctx, Throwable cause ) {
		
		if( cause instanceof ClosedChannelException ) {
			logger.info( "remote peer {} closed", ctx.channel().remoteAddress() );
			ctx.close();
			return;
		}
		else if( cause instanceof IOException ) {
			logger.error( cause.getMessage() );
			ctx.close();
			return;
		}
		logger.error( cause.getMessage(), cause );
	}

}
