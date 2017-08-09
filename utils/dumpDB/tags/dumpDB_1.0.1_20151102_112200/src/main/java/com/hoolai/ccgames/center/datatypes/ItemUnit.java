/**
 * Author: guanxin
 * Date: 2015-07-22
 */

package com.hoolai.ccgames.center.datatypes;

public class ItemUnit {

	public int itemID;

	public int itemCount;
	
	public long expireTime;
	
	public static int getMemBytes() {
		return 16;
	}
	
	public ItemUnit() {
	}

	public ItemUnit( int id, int cnt, long expire ) {
		this.itemID = id;
		this.itemCount = cnt;
		this.expireTime = expire;
	}
}
