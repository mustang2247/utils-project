/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-3-1
 */
public abstract class PropertiesReader {
	public static Properties createAbsolute(String url) {
		InputStream is = null;
		Properties prop = null;
		try {
			Log.i(url);
			is = new FileInputStream(url);
			prop = new Properties();
			prop.load(is);
		} catch (IOException e) {
			Log.e(e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				Log.e(e.getMessage(), e);
			}
		}
		return prop;
	}
	
	public static Properties create(String url) {
		InputStream is = null;
		Properties prop = null;
		try {
			Log.i(url);
			is = UrlUtil.find(url);
			prop = new Properties();
			prop.load(is);
		} catch (IOException e) {
			Log.e(e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				Log.e(e.getMessage(), e);
			}
		}
		return prop;
	}
}
