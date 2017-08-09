/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import java.io.InputStream;

/**
 * @author Cedric(Tao Shuang)
 * @date 2012-8-1
 */
public final class UrlUtil {
	private UrlUtil() {
		
	}
	public static InputStream find(String url) {
		InputStream in = UrlUtil.class.getClassLoader().getResourceAsStream(url);
		if(in == null){
			in = ClassLoader.getSystemResourceAsStream(url);
		}
		if (in == null) {
			throw new RuntimeException();
		}
		return in;
	}
}
