package com.hoolai.texaspoker.datatypes;


/**
 * 保存用户经验等级
 */

public class ExpInfo {
	// 经验级别
	private volatile short level; // 用户等级
	private volatile int cur_exp; // 用户在本级中获得的经验
	private volatile int max_exp; // 升级需要的经验，超过这个值用户升级

	
	public ExpInfo() {
		this.level = 0;
		this.cur_exp = 0;
		this.max_exp = calcMaxExp( level );
	}

	public short getLevel() {
		return level;
	}

	public void setLevel( short level ) {
		this.level = level;
	}

	public int getCur_exp() {
		return cur_exp;
	}

	public void setCur_exp( int cur_exp ) {
		this.cur_exp = cur_exp;
	}

	public int getMax_exp() {
		return max_exp;
	}

	public void setMax_exp( int max_exp ) {
		this.max_exp = max_exp;
	}

	public int calcMaxExp( short lv ) {
		return (int) Math.round( ( Math.pow( ( lv - 1 ) / 2.0, 3.4 )
				+ Math.pow( lv - 1.0, 1.5 ) + 15.0 * lv + 35.0 ) );
	}

	public boolean addExp( int exp ) {
		boolean ret = false;
		if( level < 50 ) {
			cur_exp += exp;
			while( cur_exp >= max_exp ) {
				cur_exp -= max_exp;
				max_exp = calcMaxExp( ++level );
				ret = true;
			}
		}
		else {
			ret = false;
		}
		return ret;
	}

	public boolean setLevelExp( short level, int exp ) {
		int max_exp0 = calcMaxExp( level );
		if( exp >= max_exp0 )
			return false;
		this.level = level;
		this.max_exp = max_exp0;
		this.cur_exp = exp;
		return true;
	}
}
