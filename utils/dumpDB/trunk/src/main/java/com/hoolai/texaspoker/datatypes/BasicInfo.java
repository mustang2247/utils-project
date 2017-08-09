package com.hoolai.texaspoker.datatypes;

public final class BasicInfo {
	public  volatile  String   user_name;      // 用户昵称
	public  volatile  long     appId;          // 应用ID
	public  volatile  String   platform_id;    // 平台类型及ID
	public  volatile  String   head_img_url;   // 用户头像URL
	public  volatile  Gender   gender;         // 性别
	public  volatile  Integer  team_id;        // 战队ID
	public  volatile  String   team_name;      // 战队名称（这个信息虽然冗余，但是由于经常需要玩家名字带战队名，所以就冗余吧）
	public	volatile  Integer  club_no = -1;   // 战队小队编号
	public  volatile  String   country;        // 国家
	public  volatile  String   province;       // 省份
	public  volatile  String   city;		   // 城市
	public  volatile  String   email;          // 邮件地址
	public  volatile  boolean  is_vip;         // 是否是黄钻
	public  volatile  int      vipLevel;       // 黄钻等级
	public  volatile  long     register_time;  // 注册时间
	
	public BasicInfo() {
		club_no = -1;
	}
}