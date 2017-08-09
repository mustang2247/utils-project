/**
 * Author: guanxin@hoolai.com
 * Data: 2012-02-16 10:30
 */

package com.hoolai.texaspoker.datatype;

/**
 * 用户的道具列表
 */
public final class ItemList {

	public  volatile  ItemDesc[]      item_packs;    // 用户拥有的道具  
	public  volatile  ItemDesc[]      item_used;     // 用户使用过的道具统计
	public  volatile  SafeBox         safeBox;       //用户保险箱
	
	public  ItemList() {
		item_packs = new ItemDesc[0];
		item_used  = new ItemDesc[0];
	}
	
}