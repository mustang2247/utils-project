package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.scandb.config.MemcachedCfg;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClient;
import com.hoolai.ccgames.scandb.utils.ExtendedMemcachedClientImpl;

public class Starter {

    public static void main( String[] args ) {

        if( args.length == 0 ) {
            System.err.println( "USAGE: java -jar Setter.jar -addr ADDR -key KEY -val VAL" );
            return;
        }

        MemcachedCfg memcachedCfg = new MemcachedCfg();
        String key = null;
        String val = null;

        for( int i = 0; i < args.length; ++i ) {
            if( "-addr".equals( args[i] ) ) {
                memcachedCfg.ADDRS = args[++i];
            }
            else if( "-key".equals( args[i] ) ) {
                key = args[++i];
            }
            else if( "-val".equals( args[i] ) ) {
                val = args[++i];
            }
        }

        ExtendedMemcachedClient db = new ExtendedMemcachedClientImpl( "TexasDB", memcachedCfg.ADDRS );
        db.start();
        db.set( key, val );
        db = null;

        System.exit( 0 );
    }
}
