package com.hoolai.ccgames.clientdemo.service;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.gamedemo.protocol.MessageUserId;
import com.hoolai.ccgames.gamedemo.protocol.User;

public class LoginService {

	private static Logger logger = LoggerFactory.getLogger( LoginService.class );
	
	public void login( Integer msgId, MessageUserId msg, ChannelHandlerContext ctx ) {
		// TODO
		logger.debug( "==== do client work" );
	}
	
	public void login2( Integer msgId, User.MessageUserId msg, ChannelHandlerContext ctx ) {
		// TODO
		logger.debug( "==== do client work 2" );
	}
}
