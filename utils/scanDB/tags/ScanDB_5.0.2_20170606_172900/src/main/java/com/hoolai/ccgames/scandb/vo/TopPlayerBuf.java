package com.hoolai.ccgames.scandb.vo;

import com.hoolai.ccgames.center.vo.TopPlayers;
import com.hoolai.ccgames.scandb.utils.MinHeap;
import com.hoolai.ccgames.skeleton.codec.json.JsonUtil;

import java.util.function.BiFunction;

public class TopPlayerBuf {

    public int count = 0;
    public TopPlayers.TopPlayer[] topPlayers;
    public MinHeap< TopPlayers.TopPlayer > heap = new MinHeap<>();
    public BiFunction< TopPlayers.TopPlayer, TopPlayers.TopPlayer, Integer > comparator;

    public TopPlayerBuf( int n, BiFunction< TopPlayers.TopPlayer, TopPlayers.TopPlayer, Integer > cmp ) {
        count = n;
        topPlayers = new TopPlayers.TopPlayer[n+1];
        for( int i = 0; i <= n; ++i ) topPlayers[i] = new TopPlayers.TopPlayer( cmp );
        heap.elements = topPlayers;
        comparator = cmp;
    }

    public void add( TopPlayers.TopPlayer player ) {
        topPlayers[count] = player;
        player.comparator = this.comparator;
        heap.push_heap( count );
    }

    public static void main( String[] args ) {
        TopPlayerBuf a = new TopPlayerBuf( 10, ( x, y ) -> {
            return x.level - y.level;
        } );
        a.add( new TopPlayers.TopPlayer( 123, 123, 123, 123, 123, "", "" ) );
        a.heap.sort_heap( a.count );
        System.out.println( JsonUtil.toString( a ) );
    }
}
