/**
 * Author: guanxin
 * Date: 2015-07-24
 */

package com.hoolai.ccgames.gamedemo.starter;

import com.hoolai.ccgames.skeleton.arch.GameServer;
import com.hoolai.ccgames.skeleton.utils.PropUtil;


public class Starter {

	public static void main(String[] args) {
		
		String envDir = PropUtil.getProp( "/env.properties", "envdir" );
		
		Global.gServer = new GameServer( "GameDemo" );
		Global.gServer.setInitializer( new DemoInitializer() )
			.setChannelInitalizer( new DemoChannelInitializer() )
			.initServer( "/" + envDir + "server.properties" );
		
		Global.gServer.startServer();
	}
}
