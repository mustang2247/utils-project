package com.hoolai.texaspoker.vo;

import com.hoolai.centersdk.utils.JsonUtil;

public class Player {
	
	public int id;				//自增长ID
	
	public String playerId;		//电玩吧用户Id
	
	public String playerAppId;	//用户APPID
	
	public String playerOpenId;	//openId
	
	public double haveMoney;	//筹码
	
	public transient String props;		//道具json数据

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerAppId() {
		return playerAppId;
	}

	public void setPlayerAppId(String playerAppId) {
		this.playerAppId = playerAppId;
	}

	public String getPlayerOpenId() {
		return playerOpenId;
	}

	public void setPlayerOpenId(String playerOpenId) {
		this.playerOpenId = playerOpenId;
	}

	public double getHaveMoney() {
		return haveMoney;
	}

	public void setHaveMoney(double haveMoney) {
		this.haveMoney = haveMoney;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}

	@Override
	public String toString() {
		return JsonUtil.toJson(this);
	}
}