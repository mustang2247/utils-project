/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.scandb.database.trans;

import com.hoolai.ccgames.center.vo.ItemGiveList;
import com.hoolai.ccgames.center.vo.ItemGiveRecord;
import com.hoolai.ccgames.scandb.database.DataConverter;
import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.BaseSerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;

public class ItemGiveTranscoder extends BaseSerializingTranscoder implements
        Transcoder< ItemGiveList > {

    public ItemGiveTranscoder() {
        super( CachedData.MAX_SIZE );
    }

    @Override
    public CachedData encode( ItemGiveList o ) {

        // 第一个int目前只存储了version
        // 将来也可以考虑存储更多信息

        int ver = 1;
        int cnt = o.records.size();
        int pos = 0;
        byte[] b = new byte[8 + (8 * 3 + 4 * 3) * cnt];

        pos += DataConverter.writeBytes( ver, b, pos );
        pos += DataConverter.writeBytes( cnt, b, pos );

        for( ItemGiveRecord r : o.records ) {
            pos += DataConverter.writeBytes( r.senderID, b, pos );
            pos += DataConverter.writeBytes( r.receiverID, b, pos );
            pos += DataConverter.writeBytes( r.time, b, pos );
            pos += DataConverter.writeBytes( r.itemID, b, pos );
            pos += DataConverter.writeBytes( r.itemCount, b, pos );
            pos += DataConverter.writeBytes( r.status, b, pos );
        }

        return new CachedData( 0, b, getMaxSize() );
    }

    @Override
    public ItemGiveList decode( CachedData d ) {

        byte[] b = d.getData();
        ItemGiveList list = new ItemGiveList();

        int ver = DataConverter.readInt( b, 0 );
        if( ver == 1 ) {
            int cnt = DataConverter.readInt( b, 4 );
            int pos = 8;
            for( int i = 0; i < cnt; ++i ) {
                ItemGiveRecord r = new ItemGiveRecord();
                r.senderID = DataConverter.readLong( b, pos );
                pos += 8;
                r.receiverID = DataConverter.readLong( b, pos );
                pos += 8;
                r.time = DataConverter.readLong( b, pos );
                pos += 8;
                r.itemID = DataConverter.readInt( b, pos );
                pos += 4;
                r.itemCount = DataConverter.readInt( b, pos );
                pos += 4;
                r.status = DataConverter.readInt( b, pos );
                pos += 4;
                list.addRecord( r );
            }

            return list;
        }

        return null;
    }
}
