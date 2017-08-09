/**
 * Author: guanxin
 * Date: 2015-07-24
 */

package com.hoolai.ccgames.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.utils.PropUtil;

public class ServerConfig {
	
	private final static Logger logger = LoggerFactory.getLogger( ServerConfig.class );

	public static String gateIP = null;
	
	public static int gatePort; // connect port
	
	public static String loginName;
	
	public static String loginPass;
	
	public static int messageVersion = 1;

	public static boolean init() {

		String  envDir = PropUtil.getProp( "/env.properties", "envdir" );
		
		String  gateRelativePath = "/" + envDir + "cgate.properties";
		
		Properties props = PropUtil.getProps( gateRelativePath );
		
		gateIP = props.getProperty( "gateIP" ).trim();
		gatePort = Integer.parseInt( props.getProperty( "gatePort" ) );
		loginName = props.getProperty( "loginName" ).trim();
		loginPass = props.getProperty( "loginPass" ).trim();
		messageVersion = Integer.parseInt( props.getProperty( "messageVersion" ) );

		return true;
	}
}
