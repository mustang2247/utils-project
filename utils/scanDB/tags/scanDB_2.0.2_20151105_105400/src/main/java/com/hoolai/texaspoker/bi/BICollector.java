/**
 * 
 */
package com.hoolai.texaspoker.bi;

import java.math.BigInteger;
import java.util.Date;

import com.dw.metrics.GameInfo;
import com.dw.services.TrackServices;
import com.hoolai.texaspoker.datatype.BasicInfo;
import com.hoolai.texaspoker.scandb.Log;


public abstract class BICollector {
	
	public  static  final  int  TENCENT_OPENID_RADIX = 16;  
	public  static  final  int  BI_OPENID_RADIX = 10;
	public static void onSendGoldInfo( String platID, BasicInfo  info, String chips, long coin, String level, String userID,int code,long chip) {
		try {
			GameInfo  gameInfo = new GameInfo();
			gameInfo.setUserId( toBIOpenID(platID) );
			gameInfo.setGameinfo( "chips_left" );
			gameInfo.setKingdom( info.user_name );
			gameInfo.setPhylum( info.team_name );
			gameInfo.setClassfield( info.team_id.toString() );
			gameInfo.setFamily(info.club_no.toString());
			gameInfo.setUser_level( level );
			gameInfo.setValue( chips );
			gameInfo.setGenus( userID );
			// 是领队的话就是1，不是领队的话就是0,%d需要填一个整形进去
			gameInfo.setExtra( String.format("hoolai_coin:%d,is_lingdui:%d,baoxianxiang:%d", coin, code,chip));
			Date date = new Date();
			gameInfo.setGameinfo_date( BIManager.DATE_FORMAT.format(date) );
			gameInfo.setGameinfo_time( BIManager.TIME_FORMAT.format(date) );
			TrackServices.add( gameInfo );
		} catch( Exception e ) {
			Log.e( "[ScanDB] err format " + platID );
		}
	}
	public static void onSendItemInfo( String platID, String itemID, String count ) {
		try {
			GameInfo  gameInfo = new GameInfo();
			gameInfo.setUserId( toBIOpenID(platID) );
			gameInfo.setGameinfo( "item_status" );
			gameInfo.setKingdom( String.valueOf(itemID) );
			gameInfo.setValue( String.valueOf(count) );
			Date date = new Date();
			gameInfo.setGameinfo_date( BIManager.DATE_FORMAT.format(date) );
			gameInfo.setGameinfo_time( BIManager.TIME_FORMAT.format(date) );
			TrackServices.add( gameInfo );
		} catch( Exception e ) {
			Log.e( "[ScanDB] err format " + platID );
		}
	}
	
	/**
	 * 正式环境不可能有这样的情况，openID(16进制)总是可以转换成10进制的
	 * 
	 * @param openID
	 * @return
	 * @author Cedric(TaoShuang)
	 */
	public static String toBIOpenID(String openID) {
		try {
			if (openID.equals("")) {
				return openID;
			}
			if (BIManager.needTransfer) {
				return new BigInteger(openID, TENCENT_OPENID_RADIX)
				.toString(BI_OPENID_RADIX);
			} else {
				return openID;
			}
			
		} catch (Exception e) {
			Log.e("toBIOpenID(String[" + openID + "]) failed, just return openID instead");
			return openID;
		}
	}
}
