package com.hoolai.texaspoker.vo;

import com.hoolai.centersdk.utils.JsonUtil;

public class Item {

	public int id; // 自增长ID

	public int nowNum; // 电玩吧用户Id

	@Override
	public String toString() {
		return JsonUtil.toJson(this);
	}
}