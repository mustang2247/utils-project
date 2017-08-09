package com.hoolai.ccgames.gamedemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

import com.hoolai.ccgames.gamedemo.protocol.CommandList;
import com.hoolai.ccgames.gamedemo.protocol.Login;
import com.hoolai.ccgames.gamedemo.protocol.MessageLogin;
import com.hoolai.ccgames.gamedemo.protocol.MessageUserId;
import com.hoolai.ccgames.gamedemo.protocol.User;
import com.hoolai.ccgames.gamedemo.repo.RepoManager;
import com.hoolai.ccgames.protocol.common.Constants;
import com.hoolai.ccgames.skeleton.dispatch.NetMessage;

public class LoginService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger( LoginService.class );
	
	public void login( Integer msgId, MessageLogin msg, ChannelHandlerContext ctx ) {
		long uid = RepoManager.centerRepo.getUserId( msg.platform, msg.appId, msg.openId );
		if( uid == Constants.INVALID_UID ) {
			uid = RepoManager.centerRepo.newUser( msg.platform, msg.appId, msg.openId );
			if( uid == Constants.INVALID_UID ) {
				logger.error( "Can't new user {} {} {}", msg.platform, msg.appId, msg.openId );
				return;
			}
		}
		ctx.attr( userIdKey ).set( uid );
		
		MessageUserId resp = new MessageUserId();
		resp.userId = uid;
		ctx.writeAndFlush( new NetMessage( CommandList.LOGIN, resp ) );
	}
	
	public void login2( Integer msgId, Login.MessageLogin msg, ChannelHandlerContext ctx ) {
		long uid = RepoManager.centerRepo.getUserId( msg.getPlatform(), msg.getAppId(), msg.getOpenId() );
		if( uid == Constants.INVALID_UID ) {
			uid = RepoManager.centerRepo.newUser( msg.getPlatform(), msg.getAppId(), msg.getOpenId() );
			if( uid == Constants.INVALID_UID ) {
				logger.error( "Can't new user {} {} {}", msg.getPlatform(), msg.getAppId(), msg.getOpenId() );
				return;
			}
		}
		ctx.attr( userIdKey ).set( uid );
		
		User.MessageUserId resp = User.MessageUserId.newBuilder().setUserId( uid ).build();
		ctx.writeAndFlush( new NetMessage( CommandList.LOGIN2, resp ) );
	}
}
