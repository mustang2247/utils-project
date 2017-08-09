package com.hoolai.texaspoker.datatype;

/**
 * 保存用户经验等级
 * 用cas保证数据正确性，类内不加同步处理
 */

public class ExpInfo {
	// 经验级别
	public volatile short level; // 用户等级
	public volatile int cur_exp; // 用户在本级中获得的经验
	public volatile int max_exp; // 升级需要的经验，超过这个值用户升级

}
