package com.bitop.common.skeleton.config;

import com.bitop.common.skeleton.utils.PropUtil;

import java.util.Properties;

public class ClientConfig {

    public String SERVER_ADDR;

    public int SERVER_PORT;

    public boolean init( String filePath ) {
        return init( PropUtil.getProps( filePath ) );
    }

    public boolean init( Properties props ) {

        SERVER_ADDR = props.getProperty( "server_addr" ).trim();
        SERVER_PORT = Integer.parseInt( props.getProperty( "server_port" ).trim() );

        return extraInit( props );
    }

    public boolean extraInit( Properties props ) {
        return true;
    }
}
