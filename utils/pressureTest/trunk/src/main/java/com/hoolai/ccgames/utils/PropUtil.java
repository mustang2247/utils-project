/**
 * Author: guanxin
 * Date: 2015-07-27
 */

package com.hoolai.ccgames.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtil {

	/**
	 * 为了简化读取配置代码 但是要反复打开文件，比较慢，如果配置文件内容不多时可以使用
	 */
	public static String getProp( String filePath, String key ) {

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream( filePath );
		try {
			prop.load( in );
			return prop.getProperty( key ).trim();
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		finally {
			if( in != null ) {
				try {
					in.close();
				}
				catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static Properties getProps( String filePath ) {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream( filePath );
		try {
			prop.load( in );
			return prop;
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		finally {
			if( in != null ) {
				try {
					in.close();
				}
				catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
