/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.scandb.database.trans;

import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.ItemUnit;
import com.hoolai.ccgames.scandb.database.DataConverter;
import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.BaseSerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;

public class ItemTranscoder extends BaseSerializingTranscoder implements
        Transcoder< ItemList > {

    public ItemTranscoder() {
        super( CachedData.MAX_SIZE );
    }

    @Override
    public CachedData encode( ItemList o ) {

        // 第一个int目前只存储了version
        // 将来也可以考虑存储更多信息

        int ver = 1;
        int pos = 0;
        int cnt = o.items.length;

        byte[] b = new byte[8 + o.getMemBytes()];

        pos += DataConverter.writeBytes( ver, b, pos );
        pos += DataConverter.writeBytes( cnt, b, pos );

        for( int i = 0; i < cnt; ++i ) {
            pos += DataConverter.writeBytes( o.items[i].itemID, b, pos );
            pos += DataConverter.writeBytes( o.items[i].itemCount, b, pos );
            pos += DataConverter.writeBytes( o.items[i].expireTime, b, pos );
        }

        return new CachedData( 0, b, getMaxSize() );
    }

    @Override
    public ItemList decode( CachedData d ) {

        byte[] b = d.getData();

        int ver = DataConverter.readInt( b, 0 );
        if( ver == 1 ) {
            int cnt = DataConverter.readInt( b, 4 );
            ItemUnit[] items = new ItemUnit[cnt];
            int pos = 8;
            for( int i = 0; i < cnt; ++i ) {
                int itemID = DataConverter.readInt( b, pos );
                pos += 4;
                int itemCount = DataConverter.readInt( b, pos );
                pos += 4;
                long expire = DataConverter.readLong( b, pos );
                pos += 8;
                items[i] = new ItemUnit( itemID, itemCount, expire );
            }

            return new ItemList( items );
        }

        return null;
    }
}
