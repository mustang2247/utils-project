package com.hoolai.texaspoker.datatype;

public class TopPlayerList {
	public  int          count;    // 人数
	public  TopPlayer[]  players;  // 信息
	
	public  TopPlayerList() {
		count = 0;
	}
	
	public  TopPlayerList( int n ) {
		count = n;
		players = new TopPlayer[n+1];  // add 1 for buffer
		for( int i = 0; i <= n; ++i ) players[i] = new TopPlayer( 0, null, null, -1, -1, -1 );
	}
}
