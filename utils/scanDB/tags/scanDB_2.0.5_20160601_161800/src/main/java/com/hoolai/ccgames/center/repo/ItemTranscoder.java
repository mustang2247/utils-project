/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.center.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.datatypes.ItemList;
import com.hoolai.ccgames.center.datatypes.ItemUnit;
import com.hoolai.ccgames.center.utils.DataConverter;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.BaseSerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;

public class ItemTranscoder extends BaseSerializingTranscoder implements
		Transcoder< ItemList > {

	public final static Logger logger = LoggerFactory.getLogger( ItemTranscoder.class );
	
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
		
		logger.debug( "{} bytes", b.length );

		int ver = DataConverter.readInt( b, 0 );
		logger.debug( "ver {}", ver );
		if( ver == 1 ) {
			int cnt = DataConverter.readInt( b, 4 );
			logger.debug( "cnt {}", cnt );
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
