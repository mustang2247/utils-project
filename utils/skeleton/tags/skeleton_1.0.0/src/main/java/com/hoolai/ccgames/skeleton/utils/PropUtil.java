package com.hoolai.ccgames.skeleton.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtil {

    private static final Logger logger = LoggerFactory
            .getLogger( PropUtil.class );

    public static InputStream getInputStream( String filePath ) {
        InputStream in = Object.class.getResourceAsStream( filePath );
        if( in == null )
            in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream( filePath );
        if( in == null )
            in = ClassLoader.getSystemResourceAsStream( filePath );
        return in;
    }

    /**
     * 为了简化读取配置代码 但是要反复打开文件，比较慢，如果配置文件内容不多时可以使用
     */
    public static String getProp( String filePath, String key ) {

        Properties prop = new Properties();
        InputStream in = getInputStream( filePath );
        try {
            prop.load( in );
            return prop.getProperty( key ).trim();
        } catch( IOException e ) {
            logger.error( e.getMessage(), e );
        }

        return null;
    }

    public static Properties getProps( String filePath ) {
        Properties prop = new Properties();
        InputStream in = getInputStream( filePath );
        try {
            prop.load( in );
            return prop;
        } catch( IOException e ) {
            logger.error( e.getMessage(), e );
        }

        return null;
    }

    public static Map< String, Properties > getSectionProps( String filePath ) throws IOException {
        Map< String, Properties > rv = new HashMap< String, Properties >();
        BufferedReader reader = new BufferedReader( new InputStreamReader(
                getInputStream( filePath ) ) );
        String line = null;
        String section = null;
        Properties properties = null;
        while( ( line = reader.readLine() ) != null ) {
            line = line.trim();
            if( line.length() == 0 || line.startsWith( "#" ) ) continue;
            else if( line.matches( "\\[.*\\]" ) ) {
                section = line.replaceFirst( "\\[(.*)\\]", "$1" );
                properties = new Properties();
                rv.put( section, properties );
            }
            else if( line.matches( ".*=.*" ) ) {
                if( properties != null ) {
                    int i = line.indexOf( '=' );
                    String name = line.substring( 0, i ).trim();
                    String value = line.substring( i + 1 ).trim();
                    properties.setProperty( name, value );
                }
            }
        }

        return rv;
    }

}
