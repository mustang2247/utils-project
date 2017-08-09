package com.hoolai.ccgames.scandb.vo;

import com.hoolai.ccgames.scandb.utils.MinHeap;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;


public class TopPlayerList {
	public  int          count;    // 人数
	public  TopPlayer[]  players;  // 信息
	
	public transient MinHeap< TopPlayer > heap;
	
	public  TopPlayerList() {
		count = 0;
	}
	
	public  TopPlayerList( int n ) {
		count = n;
		players = new TopPlayer[n+1];  // add 1 for buffer
		for( int i = 0; i <= n; ++i ) players[i] = new TopPlayer( -1L, -1, -1L );
		heap = new MinHeap< TopPlayer >( players );
	}
	
	public void add( TopPlayer p ) {
		players[count] = p;
		heap.push_heap( count );
	}
	
	public String toString() {
		heap.sort_heap( count );
		if( count < players.length ) {
			TopPlayer[] tmp = new TopPlayer[count];
			System.arraycopy( players, 0, tmp, 0, count );
			players = tmp;
		}
		return JsonUtil.toString( this );
	}
}
