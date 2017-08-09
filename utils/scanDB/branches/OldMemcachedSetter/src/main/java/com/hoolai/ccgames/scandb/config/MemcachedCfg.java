package com.hoolai.ccgames.scandb.config;

import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.Properties;

public class MemcachedCfg {
	
	// 这些配置是连接memcached的，存储用户数据
	public int INIT_CONN; 
	
	public int MAX_CONN;
	
	public int TIMEOUT;
	
	public String ADDRS;
	
	public boolean init( String filePath ) {
		return init( PropUtil.getProps( filePath ) );
	}

	public boolean init( Properties props ) {
		INIT_CONN = Integer.parseInt( props.getProperty( "init_conn" ) );
		MAX_CONN = Integer.parseInt( props.getProperty( "max_conn" ) );
		TIMEOUT = Integer.parseInt( props.getProperty( "timeout" ) );
		ADDRS = props.getProperty( "addr" );

		return true;
	}
}
