package com.hoolai.ccgames.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;

public class Main {
	
	private static Logger logger = LoggerFactory.getLogger( Main.class );
	private static MemcachedClient mc = null;

	public static void main( String[] args ) {
		
		if( args.length != 2 ) {
			System.err.println( "Usage: java -jar uuid-range-manager.jar DB_ADDR FILE" );
			System.exit( -1 );
		}
		
		String addr = args[0];
		String file = args[1];
		
		try {
			mc = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( addr ) );
		}
		catch( IOException e ) {
			logger.error( e.getMessage(), e );
			System.exit( -1 );
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream( new File( file ) ) ) );
			String line = null;

			while( ( line = reader.readLine() ) != null ) {
				if( !line.trim().equals( "" ) ) {
					String[] param = line.split( " " );
					if( param.length != 3 ) {
						logger.error( "can't parse {}", line );
						continue;
					}
					process( param[0].trim(), Long.parseLong( param[1] ), Long.parseLong( param[2] ) );
				}
			}
		}
		catch( IOException e ) {
			logger.error( e.getMessage(), e );
		}
		finally {
			if( reader != null ) {
				try {
					reader.close();
				}
				catch( IOException e1 ) {
					logger.error( e1.getMessage(), e1 );
				}
			}
		}
		
		mc.shutdown();
		System.exit( 0 );
	}
	
	public static void process( String rangeId, long beg, long end ) {
		String begKey = "UUID:" + rangeId + ":L";
		String endKey = "UUID:" + rangeId + ":R";
		
		try {
			mc.add( begKey, 0, "" + beg ).get();
			mc.add( endKey, 0, "" + end ).get();
			String begVal = (String) mc.get( begKey );
			String endVal = (String) mc.get( endKey );
			
			logger.info( "Now for {} beg is {} end is {}", rangeId, begVal, endVal );
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}
}
