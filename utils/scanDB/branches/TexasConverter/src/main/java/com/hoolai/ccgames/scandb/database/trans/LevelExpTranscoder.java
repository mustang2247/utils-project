package com.hoolai.ccgames.scandb.database.trans;

import com.hoolai.ccgames.center.vo.LevelExp;
import com.hoolai.ccgames.scandb.database.DataConverter;
import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.BaseSerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelExpTranscoder extends BaseSerializingTranscoder implements
	Transcoder< LevelExp > {

	public final static Logger logger = LoggerFactory.getLogger( LevelExpTranscoder.class );

    public LevelExpTranscoder() {
    	super( CachedData.MAX_SIZE );
    }

    @Override
    public CachedData encode( LevelExp o ) {
    
        int pos = 0;
        byte[] b = new byte[LevelExp.getMemBytes()];
        pos += DataConverter.writeBytes( o.level, b, pos );
        pos += DataConverter.writeBytes( o.curExp, b, pos );
        pos += DataConverter.writeBytes( o.maxExp, b, pos );
        
        return new CachedData( 0, b, getMaxSize() );
    }

    @Override
    public LevelExp decode( CachedData d ) {
    
        byte[] b = d.getData();
        
        LevelExp levelExp = new LevelExp();
        
        if( b.length != LevelExp.getMemBytes() ) {
        	logger.error( "LevelExpTranscoder::decode err {} bytes, not {} bytes",
        			b.length, LevelExp.getMemBytes() );
        	return null;
        }
        
        int pos = 0;
        levelExp.level = DataConverter.readInt( b, pos );
        pos += 4;
        levelExp.curExp = DataConverter.readInt( b, pos );
        pos += 4;
        levelExp.maxExp = DataConverter.readInt( b, pos );
        
        return levelExp;
    }
}
