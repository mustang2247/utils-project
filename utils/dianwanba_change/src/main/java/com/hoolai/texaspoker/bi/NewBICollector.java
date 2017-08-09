package com.hoolai.texaspoker.bi;



/**
 */

public class NewBICollector extends com.hoolai.centersdk.bi.NewBICollector {

	private static final String INSTALL = "updateInstall";

	/**
	 * 安装
	 */
	public static void onInstall(String appid, long userId,String openId,String platform) {
		biSvc.send( INSTALL,
				String.valueOf( System.currentTimeMillis() ),
				safeString( appid ),
				String.valueOf(userId),
				safeString(openId),
				safeString( null ),
				safeString( platform ),
				safeString( null ),
				safeString( null ),
				booleanString(false));
	}
}
