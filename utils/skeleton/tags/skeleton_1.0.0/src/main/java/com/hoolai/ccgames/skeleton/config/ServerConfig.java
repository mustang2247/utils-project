/**
 * Author: guanxin
 * Date: 2015-07-24
 */

package com.hoolai.ccgames.skeleton.config;

import com.hoolai.ccgames.skeleton.utils.PropUtil;

import java.util.Properties;

public class ServerConfig {

    public int LISTEN_PORT;

    public int MAX_CONN_NUM;

    public int ACCEPT_BACKLOG_NUM;

    public int ACCEPT_THREAD_NUM;

    public int IO_THREAD_NUM;

    public boolean init( String filePath ) {
        return init( PropUtil.getProps( filePath ) );
    }

    public boolean init( Properties props ) {
        LISTEN_PORT = Integer.parseInt( props.getProperty( "listen_port" ).trim() );
        MAX_CONN_NUM = Integer.parseInt( props.getProperty( "max_conn_num" ).trim() );
        ACCEPT_BACKLOG_NUM = Integer.parseInt( props.getProperty( "accept_backlog_num" ).trim() );
        ACCEPT_THREAD_NUM = Integer.parseInt( props.getProperty( "accept_thread_num" ).trim() );
        IO_THREAD_NUM = Integer.parseInt( props.getProperty( "io_thread_num" ).trim() );
        return extraInit( props );
    }

    public boolean extraInit( Properties props ) {
        return true;
    }
}
