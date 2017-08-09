/**
 * Author: guanxin
 * Date: 2015-07-27
 */

package com.hoolai.ccgames.protocol.common;

public class CodecUtil {

	public static int getVer( int props ) {
		return props & 0xFF;
	}
	
	public static int setVer( int props, int ver ) {
		return (props & 0xFFFFFF00) | (ver & 0xFF);
	}
}
