package com.hoolai.ccgames.clientdemo.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.skeleton.arch.ExceptionHandler;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;
import com.hoolai.ccgames.skeleton.dispatch.MessageDispatcher;
import com.hoolai.ccgames.skeleton.dispatch.NetMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class MessageHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger( MessageHandler.class );
	
	@Override
	public void channelRead( ChannelHandlerContext ctx, Object msg ) {
		try {
			logger.debug( JsonUtil.toString( msg ) );
			if( msg instanceof NetMessage ) {
				NetMessage msg0 = (NetMessage) msg;
				MessageDispatcher.dispatch( msg0.getKindId(), msg0.getMsgId(), msg0.getMessage(), ctx );
			}
		}
		finally {
			ReferenceCountUtil.release( msg );
		}
	}
	
	@Override
	public void channelInactive( ChannelHandlerContext ctx ) {
		logger.info( "Disconnect {}", ctx.channel().remoteAddress() );
	}

	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) {
		ExceptionHandler.handle( ctx, cause );
	}
}
