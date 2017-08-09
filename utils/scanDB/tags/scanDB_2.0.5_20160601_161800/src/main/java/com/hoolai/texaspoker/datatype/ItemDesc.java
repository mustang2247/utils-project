/**
 * Author: guanxin@hoolai.com
 * Date: 2012-04-18 18:16
 */

package com.hoolai.texaspoker.datatype;

public final class ItemDesc {
	public  volatile  int     item_id;                 // 道具ID
	public  volatile  int     count;                   // 道具数量
	
	public  ItemDesc( int id, int count ) {
		this.item_id = id;
		this.count = count;
	}
}
