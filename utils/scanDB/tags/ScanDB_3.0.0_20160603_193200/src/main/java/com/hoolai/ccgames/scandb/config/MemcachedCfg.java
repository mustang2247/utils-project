package com.hoolai.ccgames.scandb.config;

import java.util.Properties;

import com.hoolai.ccgames.skeleton.utils.PropUtil;

public class MemcachedCfg {
	
	public String TEXAS_ADDRS;
	public String MAHJONG_ADDRS;
	
	public boolean init( String filePath ) {
		
		Properties props = PropUtil.getProps( filePath );
		
		TEXAS_ADDRS = props.getProperty( "texas_addrs" ).trim();
		MAHJONG_ADDRS = props.getProperty( "mahjong_addrs" ).trim();
		
		return true;
	}

}
