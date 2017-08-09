package com.bitop.common.skeleton.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassEncrypter {

    private static final Logger logger = LoggerFactory.getLogger( PassEncrypter.class );

    private static String prefixDefault = "";
    private static String suffixDefault = "";

    public static void init( String prefix, String suffix ) {
        prefixDefault = prefix;
        suffixDefault = suffix;
    }

    public static String encrypt( String prefix, String val, String suffix ) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance( "MD5" );
            md5.update( ( prefix + val + suffix ).getBytes() );
            byte[] buf = md5.digest();
            char[] digit = "0123456789ABCDEF".toCharArray();
            StringBuilder sb = new StringBuilder();
            for( byte b : buf )
                sb.append( digit[( b & 0xF0 ) >> 4] ).append( digit[b & 0xF] );
            return sb.toString();
        }
        catch( NoSuchAlgorithmException e ) {
            logger.error( e.getMessage(), e );
        }
        return "";
    }

    public static String encrypt( String val ) {
        return encrypt( prefixDefault, val, suffixDefault );
    }
}
