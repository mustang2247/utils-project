/**
 * 
 */
package com.hoolai.texaspoker.datatype;

import com.hoolai.texaspoker.scandb.Period;

/**
 * @author Cedric(TaoShuang)
 * @create 2013-4-9
 */
public final class SuperVIP {
	/**
	 * VIP持续的时间
	 */
	public Period period;
	
	/**
	 * 存这个用户游戏的中级场的总局数
	 */
	public volatile int round_mid;
	
	/**
	 * 存这个用户游戏的专家场的总局数
	 */
	public volatile int round_pro;
	
	/**
	 * 存这个用户游戏的大师场的总局数
	 */
	public volatile int round_master;
	
	/**
	 * VIP类型
	 * 
	 * DIAMOND -> 大师
	 * GOLD -> 专家
	 * SILVER -> 中级
	 */
	public int type;
	
	public SuperVIP() {
		this.period = Period.NULL;
		this.round_master = 0;
		this.round_pro = 0;
		this.round_mid = 0;
		this.type = 0;
	}
	
	
}