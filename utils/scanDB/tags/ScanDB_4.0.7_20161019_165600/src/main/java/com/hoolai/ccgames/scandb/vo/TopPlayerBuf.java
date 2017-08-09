package com.hoolai.ccgames.scandb.vo;

import com.hoolai.ccgames.center.vo.TopPlayers;
import com.hoolai.ccgames.scandb.utils.MinHeap;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;

/**
 * Created by hoolai on 2016/10/18.
 */
public class TopPlayerBuf {

    public int count = 0;
    public TopPlayers.TopPlayer[] topPlayers;
    public MinHeap< TopPlayers.TopPlayer > heap = new MinHeap<>();

    public TopPlayerBuf( int n ) {
        count = n;
        topPlayers = new TopPlayers.TopPlayer[n+1];
        for( int i = 0; i <= n; ++i ) topPlayers[i] = new TopPlayers.TopPlayer();
        heap.elements = topPlayers;
    }

    public void add( TopPlayers.TopPlayer player ) {
        topPlayers[count] = player;
        heap.push_heap( count );
    }

    public static void main( String[] args ) {
        TopPlayerBuf a = new TopPlayerBuf( 10 );
        a.add( new TopPlayers.TopPlayer( 123, 123, 123, "", "" ) );
        a.heap.sort_heap( a.count );
        System.out.println( JsonUtil.toString( a ) );
    }
}
