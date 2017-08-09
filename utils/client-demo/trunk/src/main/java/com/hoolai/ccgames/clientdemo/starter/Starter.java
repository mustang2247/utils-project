/**
 * Author: guanxin
 * Date: 2015-07-24
 */

package com.hoolai.ccgames.clientdemo.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.gamedemo.protocol.CommandList;
import com.hoolai.ccgames.gamedemo.protocol.Login;
import com.hoolai.ccgames.gamedemo.protocol.MessageLogin;
import com.hoolai.ccgames.skeleton.arch.GameClient;
import com.hoolai.ccgames.skeleton.dispatch.NetMessage;
import com.hoolai.ccgames.skeleton.utils.PropUtil;


public class Starter {
	
	private static final Logger logger = LoggerFactory.getLogger( Starter.class );

	public static void main(String[] args) {
		
		String envDir = PropUtil.getProp( "/env.properties", "envdir" );
		
		GameClient c = new GameClient( "ClientDemo" );
		c.setInitializer( new DemoInitializer() )
			.setChannelInitalizer( new DemoChannelInitializer() )
			.initClient( "/" + envDir + "client.properties" );
		c.startClient();
		
		while( !c.isActive() ) {
			logger.debug( "client not connected" );
			try {
				Thread.sleep( 100 );
			}
			catch( Exception e ) {
				logger.error( e.getMessage(), e );
			}
		}
		
		logger.debug( "client connected" );
//		MessageLogin msg = new MessageLogin();
//		msg.platform = "MIXI";
//		msg.appId = "12345";
//		msg.openId = "ABCDEF123456";
//		c.getChannel().pipeline().writeAndFlush( new NetMessage( CommandList.LOGIN, msg ) );
		
		Login.MessageLogin req = Login.MessageLogin.newBuilder()
				.setPlatform( "MIXI" )
				.setAppId( "12345" )
				.setOpenId( "ABCDEF123456" ).build();
		c.getChannel().pipeline().writeAndFlush( new NetMessage( CommandList.LOGIN2, req ) );
	}
}
