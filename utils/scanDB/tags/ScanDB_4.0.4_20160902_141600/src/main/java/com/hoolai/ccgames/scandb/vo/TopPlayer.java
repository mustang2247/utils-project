package com.hoolai.ccgames.scandb.vo;

public class TopPlayer implements Comparable<TopPlayer> {
	public  long    userId;          // 用户ID
	public  long    gold;             // 用户财富
	public  int     level;            // 用户等级
	
	public  TopPlayer() {}
	
	public TopPlayer( long id, int lv, long gold ) {
		this.userId = id;
		this.gold = gold;
		this.level = lv;
	}
	
	@Override
	public int compareTo(TopPlayer o) {
		if( this.gold < o.gold ) return -1;
		else if( this.gold > o.gold ) return 1;
		else if( this.level < o.level ) return -1;
		else if( this.level > o.level ) return 1;
		return 0;
	}
}
