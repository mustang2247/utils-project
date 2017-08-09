/**
 * Author: guanxin
 * Date: 2015-07-27
 */

package com.hoolai.ccgames.utils;

import java.util.TimeZone;

public final class TimeUtil {

	private static TimeZone zone = TimeZone.getDefault();

	public static int getDays( long millis ) {
		return (int) ( ( millis + zone.getOffset( millis ) ) / 86400000L );
	}

	public static boolean isSameDay( long m1, long m2 ) {
		return getDays( m1 ) == getDays( m2 );
	}

}
