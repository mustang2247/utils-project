/**
 * 
 */
package com.hoolai.texaspoker.scandb;


/**
 * 平台ID制造者。
 * 
 * @author Cedric(TaoShuang)
 * @create 2012-3-6
 */
public abstract class PlatformIDBuilder {
	public static String build(String platformType, String platformAccountID) {
		StringBuilder builder = new StringBuilder();
		builder.append(platformType);
		builder.append(RepoConstances.SPLITTER);
		builder.append(platformAccountID);
		return builder.toString();
	}
	
	public static String getAccountID(String platformID) {
		//TODO 这里少一个与客户端交互的错误码，如果客户端输入的platformID不是严格的pType:pAccountID，则返回错误吗
		String[] results = platformID.split(RepoConstances.SPLITTER);
		if (results.length != 2) {
			Log.e("Bad platformID " + platformID);
			return null;
		}
		return results[1];
	}
	
	public static String getPlatformType(String platformID) {
		String[] results = platformID.split(RepoConstances.SPLITTER);
		if (results.length != 2) {
			Log.e("Bad platformID " + platformID);
			return null;
		}
		return results[0];
	}
}
