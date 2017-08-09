package com.hoolai.ccgames.gamedemo.service;

import io.netty.util.AttributeKey;

public class BaseService {

	protected AttributeKey< Long > userIdKey = AttributeKey.valueOf( "UserID" );
}
