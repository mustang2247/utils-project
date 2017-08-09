package com.hoolai.texaspoker.datatype;

public class TopPlayer implements Comparable<TopPlayer> {
	public  int     userID;          // 用户ID
	public  String  userName;        // 用户昵称
	public  String  headImgUrl;     // 用户头像
	public  long    gold;             // 用户财富
	public  int     level;            // 用户等级
	public  int     vipLevel;        // 用户vip类型
	
	public  TopPlayer() {}
	public  TopPlayer( int id, String name, String head, long gold, int lv, int vlv ) {
		this.userID = id;
		this.userName = name;
		this.headImgUrl = head;
		this.gold = gold;
		this.level = lv;
		this.vipLevel = vlv;
	}
	
	public  void  setValue( int id, String name, String head, long gold, int lv, int vlv ) {
		this.userID = id;
		this.userName = name;
		this.headImgUrl = head;
		this.gold = gold;
		this.level = lv;
		this.vipLevel = vlv;
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
