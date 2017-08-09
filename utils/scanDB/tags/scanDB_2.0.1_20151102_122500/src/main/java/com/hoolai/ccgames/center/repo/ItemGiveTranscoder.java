/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.center.repo;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.BaseSerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.datatypes.ItemGiveList;
import com.hoolai.ccgames.center.datatypes.ItemGiveRecord;
import com.hoolai.ccgames.center.utils.DataConverter;

public class ItemGiveTranscoder extends BaseSerializingTranscoder implements
		Transcoder< ItemGiveList > {

	public final static Logger logger = LoggerFactory
			.getLogger( ItemGiveTranscoder.class );

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
		byte[] b = new byte[8 + ItemGiveRecord.SIZE_BYTES * cnt];

		pos += DataConverter.writeBytes( ver, b, pos );
		pos += DataConverter.writeBytes( cnt, b, pos );
		
		for( ItemGiveRecord r : o.records ) {
			pos += r.writeBytes( b, pos );
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
				pos += r.readBytes( b, pos );
				list.addRecord( r );
			}
			
			return list;
		}

		return null;
	}
}
