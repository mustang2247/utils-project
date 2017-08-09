package com.hoolai.texaspoker.datatypes;

public final class GoldExpInfo {
	// 经验级别
	public volatile short level; // 用户等级
	public volatile int cur_exp; // 用户在本级中获得的经验
	public volatile int max_exp; // 升级需要的经验，超过这个值用户升级

	// 金钱道具
	public volatile long hoolai_gold; // 胡来币，可用来兑换胡来币
	public volatile long game_gold; // 游戏币，可用来兑换筹码
	public volatile long military_successes; // 战绩，可以买东西
	public volatile int help_used; // 本日领取救济次数
	public volatile long master_point; // 大师分儿
}
