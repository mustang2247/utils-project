package com.hoolai.ccgames.center.config;

import java.util.Properties;

import com.hoolai.ccgames.center.utils.PropUtil;

public class DaoConfig {
	
	public static int INIT_CONN;
	
	public static int MAX_CONN;
	
	public static int TIMEOUT;
	
	public static String OLD_ADDRS;
	
	public static String NEW_ADDRS;
	
	public static String platformName;
	
	public static String applicationID;
	
	public static String uuidRangeLeft;
	
	public static String uuidRangeRight;
	
	public static boolean init( String filePath ) {
		
		Properties props = PropUtil.getProps( filePath );
		
		OLD_ADDRS = props.getProperty( "oldDatabaseAddr" );
		NEW_ADDRS = props.getProperty( "newDatabaseAddr" );
		platformName = props.getProperty( "platformName" );
		applicationID = props.getProperty( "applicationID" );
		String uuidRange = props.getProperty( "uuidRange" );
		uuidRangeLeft = uuidRange.split( "," )[0];
		uuidRangeRight = uuidRange.split( "," )[1];
		
		INIT_CONN = Integer.parseInt( props.getProperty( "init_conn" ) );
		MAX_CONN = Integer.parseInt( props.getProperty( "max_conn" ) );
		TIMEOUT = Integer.parseInt( props.getProperty( "timeout" ) );

		
		return true;
	}
}
