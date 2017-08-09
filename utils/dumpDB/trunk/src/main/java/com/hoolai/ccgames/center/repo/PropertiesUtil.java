/**
 * 
 */
package com.hoolai.ccgames.center.repo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 鎿嶄綔properties鏂囦欢鐨勫伐鍏风被
 * @author luzj
 *
 */
public class PropertiesUtil {

	private Properties properties;
	
	private boolean available = true;
	
	/**
	 * 鏄惁鏄湁鏁堢殑properties鏂囦欢
	 * @return
	 */
	public boolean available(){
		return available;
	}
	
	public PropertiesUtil(Properties p) {
		this.properties = p;
	}

	/**
	 * 鐩稿浜巆lasspath鐨勮矾寰�
	 * @param filePath
	 */
	public PropertiesUtil(String filePath) {
		properties = new Properties();
		
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
		if(in == null){
			in = ClassLoader.getSystemResourceAsStream(filePath);
		}
		
		if(in == null){
			available = false;
		} else {
			try {
				properties.load(in);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String getProperty(String key) {
		String property = properties.getProperty(key);
		if(property != null){
		    return property.trim();
		}
		return null;
	}
	
	public int getPropertyAsInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public boolean getPropertyAsBoolean(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	public byte getPropertyAsByte(String key) {
		return Byte.parseByte(getProperty(key));
	}

	public long getPropertyAsLong(String key) {
		return Long.parseLong(getProperty(key));
	}

	public short getPropertyAsShort(String key) {
		return Short.parseShort(getProperty(key));
	}

	public float getPropertyAsFloat(String key) {
		return Float.parseFloat(getProperty(key));
	}

	public double getPropertyAsDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}
}