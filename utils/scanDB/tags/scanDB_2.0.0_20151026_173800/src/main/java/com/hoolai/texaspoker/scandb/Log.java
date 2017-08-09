/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-2-27
 */
public abstract class Log {
	private static final Logger log = Logger.getLogger("global");

	public static void e(Object message, Throwable t) {
		if (log.isEnabledFor(Level.ERROR))
		log.error(message, t);
	}

	public static void i(Object message, Throwable t) {
		if (log.isInfoEnabled())
		log.info(message, t);
	}

	public static void d(Object message, Throwable t) {
		if (log.isDebugEnabled())
		log.debug(message, t);
	}

	public static void e(Object message) {
		if (log.isEnabledFor(Level.ERROR))
		log.error(message);
	}

	public static void i(Object message) {
		if (log.isInfoEnabled())
		log.info(message);
	}

	public static void d(Object message) {
		if (log.isDebugEnabled())
		log.debug(message);
	}
	
	public static void set(Level level) {
		log.setLevel(level);
	}
	
	public static boolean isDebug() {
		return log.isDebugEnabled();
	}
	
	public static boolean isInfo() {
		return log.isInfoEnabled();
	}
	
	public static boolean isError() {
		return log.isEnabledFor(Level.ERROR);
	}
}
