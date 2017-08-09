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

import com.hoolai.ccgames.center.datatypes.Safebox;
import com.hoolai.ccgames.center.utils.DataConverter;

public class SafeboxTranscoder extends BaseSerializingTranscoder implements
		Transcoder< Safebox > {

	public final static Logger logger = LoggerFactory
			.getLogger( SafeboxTranscoder.class );

	public SafeboxTranscoder() {
		super( CachedData.MAX_SIZE );
	}

	@Override
	public CachedData encode( Safebox o ) {

		// 第一个int目前只存储了version
		// 将来也可以考虑存储更多信息

		int ver = 0;
		int pos = 0;
		byte[] b = new byte[4];
		
		pos += DataConverter.writeBytes( ver, b, pos );

		return new CachedData( 0, b, getMaxSize() );
	}

	@Override
	public Safebox decode( CachedData d ) {

		byte[] b = d.getData();
		Safebox box = new Safebox();
		logger.debug( "{} bytes", b.length );

		int ver = DataConverter.readInt( b, 0 );
		logger.debug( "ver {}", ver );
		if( ver == 0 ) {
			return box;
		}

		return null;
	}
}
