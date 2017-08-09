/**
 * Author: guanxin@hoolai.com
 * Data: 2012-02-16 10:30
 */

package com.hoolai.texaspoker.datatypes;


import java.util.LinkedList;

/**
 * 用户的道具列表
 */
public final class ItemList {

	public  volatile  ItemDesc[]      item_packs;    // 用户拥有的道具  
	public  volatile  ItemDesc[]      item_used;     // 用户使用过的道具统计
	// TODO 已删除保险箱功能。保险箱的get操作没有做CAS操作
	public  volatile  SafeBox         safeBox;       //用户保险箱
	public  volatile  LinkedList<ItemSendRecord> sendRecord; //赠送记录
	
	/** 上次更新获得雪球的时间 */
	public long lastUpdateSnowballTime; 
	
	public  ItemList() {
		item_packs = new ItemDesc[0];
		item_used  = new ItemDesc[0];
		sendRecord  = new LinkedList<ItemSendRecord>();
	}
	
}