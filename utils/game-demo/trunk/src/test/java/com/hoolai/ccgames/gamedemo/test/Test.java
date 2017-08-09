package com.hoolai.ccgames.gamedemo.test;

import java.util.Properties;

import com.hoolai.ccgames.skeleton.utils.PropUtil;

public class Test {

	public static void main( String[] args ) {
		Properties props = PropUtil.getProps( "/testserver/server.properties" );
		System.out.println( props.get( "listen_port" ) );
	}
	
}
