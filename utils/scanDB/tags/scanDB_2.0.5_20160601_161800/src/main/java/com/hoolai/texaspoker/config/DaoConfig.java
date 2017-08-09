package com.hoolai.texaspoker.config;

import java.util.Properties;

import com.hoolai.ccgames.center.utils.PropUtil;

public class DaoConfig {
	
	public static String ADDRS;
	
	public static String PLATFORM_NAME;
	
	public static String APP_ID;
	
	public static int INIT_CONN;
	
	public static int MAX_CONN;
	
	public static int TIMEOUT;
	
	public static boolean init( String filePath ) {
		
		Properties props = PropUtil.getProps( filePath );
		
		ADDRS = props.getProperty( "addrs" ).trim();
		PLATFORM_NAME = props.getProperty( "platform_name" ).trim();
		APP_ID = props.getProperty( "app_id" ).trim();
		
		INIT_CONN = Integer.parseInt( props.getProperty( "init_conn" ) );
		MAX_CONN = Integer.parseInt( props.getProperty( "max_conn" ) );
		TIMEOUT = Integer.parseInt( props.getProperty( "timeout" ) );

		return true;
	}
}
