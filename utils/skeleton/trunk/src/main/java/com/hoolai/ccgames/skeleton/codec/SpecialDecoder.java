package com.hoolai.ccgames.skeleton.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hoolai on 2016/7/14.
 */
public class SpecialDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger( SpecialDecoder.class );

    private static final byte[] CROSS_DOMAIN_REQ = "<policy-file-request/>\0".getBytes();
    private static final ByteBuf CROSS_DOMAIN_RESP = UnpooledByteBufAllocator.DEFAULT.buffer()
            .writeBytes( ( "<cross-domain-policy>" +
                    "<allow-access-from domain=\"*\" to-ports=\"*\"/>" +
                    "</cross-domain-policy>\0" ).getBytes() );

    private boolean shouldParseTGW = false;
    private boolean shouldParseCrossDomain = false;
    private byte[] TGW_REQ = null;
    private byte[] TGW_TMP = null;
    private byte[] CROSS_DOMAIN_TMP = null;

    public SpecialDecoder() {
        shouldParseTGW = false;
        shouldParseCrossDomain = false;
    }

    public SpecialDecoder( boolean shouldParseCrossDomain, boolean shouldParseTGW, String... args ) {
        this.shouldParseCrossDomain = shouldParseCrossDomain;
        if( shouldParseCrossDomain ) {
            CROSS_DOMAIN_TMP = new byte[CROSS_DOMAIN_REQ.length];
        }
        this.shouldParseTGW = shouldParseTGW;
        if( shouldParseTGW ) {
            TGW_REQ = ("tgw_l7_forward\r\nHost: " + args[0] + ":" + args[1] + "\r\n\r\n").getBytes();
            TGW_TMP = new byte[TGW_REQ.length];
        }
    }

    private static int compareBytes( byte[] prefix, int prelen, byte[] ref ) {
        int pos = 0;
        for( pos = 0; pos < prelen; ++pos ) {
            if( prefix[pos] != ref[pos] ) break;
        }
        if( pos == ref.length ) return 0;
        if( pos == prelen ) return 1;
        return -1;
    }

    /**
     * 处理时考虑几种情况：
     *     1 没有TGW和CrossDomain
     *     2 TGW和CrossDomain发送了，但顺序未定
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected final void decode( ChannelHandlerContext ctx, ByteBuf in, List< Object > out ) throws Exception {
        boolean pass = true;
        if( shouldParseTGW ) {
            int len = Math.min( in.readableBytes(), TGW_REQ.length );
            in.getBytes( in.readerIndex(), TGW_TMP, 0, len );
            int cmp = compareBytes( TGW_TMP, len, TGW_REQ );
            if( cmp == 0 ) {
                in.skipBytes( TGW_REQ.length );
                shouldParseTGW = false;
                logger.debug( "=== skip TGW" );
            }
            else if( cmp == 1 ) pass = false;
        }
        if( shouldParseCrossDomain ) {
            int len = Math.min( in.readableBytes(), CROSS_DOMAIN_REQ.length );
            in.getBytes( in.readerIndex(), CROSS_DOMAIN_TMP, 0, len );
            int cmp = compareBytes( CROSS_DOMAIN_TMP, len, CROSS_DOMAIN_REQ );
            if( cmp == 0 ) {
                in.skipBytes( CROSS_DOMAIN_REQ.length );
                if( ctx != null ) ctx.writeAndFlush( CROSS_DOMAIN_RESP.duplicate().retain() );
                shouldParseTGW = false;
                logger.debug( "=== skip CrossDomain" );
            }
            else if( cmp == 1 ) pass = false;
        }
        if( pass ) {
            int readable = in.readableBytes();
            if( readable > 0 ) {
                int readerIndex = in.readerIndex();
                out.add( in.slice( readerIndex, readable ).retain() );
                in.readerIndex( readerIndex + readable );
            }
            logger.debug( "=== pass " + readable + " bytes" );
        }
        else {
            logger.debug( "=== not pass" );
        }
    }

    @Override
    public void decodeLast( ChannelHandlerContext ctx, ByteBuf msg,
                            List< Object > out ) throws Exception {
        // do nothing
    }

    public static void main( String[] args ) {
        ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer();
        System.out.println( "buf readable " + buf.readableBytes() );
//        buf.writeBytes( "hello,world!".getBytes() );
        buf.writeBytes( "<policy-file-request/>\0".getBytes() );
        System.out.println( "buf readable " + buf.readableBytes() );
        ByteBuf piece = buf.slice( buf.readerIndex(), 6 );
        System.out.println( "buf readable " + buf.readableBytes() );
        System.out.println( "piece readable " + piece.readableBytes() );
        piece.readBytes( new byte[6] );
        System.out.println( "buf readable " + buf.readableBytes() );
        System.out.println( "piece readable " + piece.readableBytes() );

        List< Object > list = new LinkedList<>();
        SpecialDecoder sd = new SpecialDecoder( true, false );
        try {
            sd.decode( null, buf, list );
            sd.decode( null, buf, list );
            System.out.println( "refCnt = " + CROSS_DOMAIN_RESP.refCnt() );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

}
