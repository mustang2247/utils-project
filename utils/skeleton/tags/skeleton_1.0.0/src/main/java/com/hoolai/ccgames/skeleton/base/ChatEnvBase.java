package com.hoolai.ccgames.skeleton.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatEnvBase {

	// 用户ID在当前对话环境的第几个频道
	// 如果是第0个频道，接受来自所有频道的。

	public ConcurrentHashMap< Long, Integer > chatters;
	public ChatCallbackBase callback;

	public ChatEnvBase( ChatCallbackBase cb ) {
		chatters = new ConcurrentHashMap<>( 8, 0.75f, 2 );
		callback = cb;
	}

	public boolean add( long userID, int channel ) {
		return chatters.putIfAbsent( userID, channel ) == null;
	}

	public boolean remove( long userID ) {
		return chatters.remove( userID ) != null;
	}

	public void broadcast( String sentence, int channel ) {
		List< Long > receivers = new ArrayList< Long >();
		for( Map.Entry< Long, Integer > entry : chatters.entrySet() ) {
			if( entry.getValue() == 0 || entry.getValue() == channel ) {
				receivers.add( entry.getKey() );
			}
		}
		callback.call( receivers );
	}

	public void broadcast( String sentence ) {
		broadcast( sentence, 0 );
	}

	public static void main( String[] args ) {

		ChatEnvBase ceb = new ChatEnvBase( new ChatCallbackBase(){
			@Override
			public void call( List< Long > receivers ) {
				
			}
		});

	}
}
