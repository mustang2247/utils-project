/**
 *
 */
package com.bitop.common.skeleton.dispatch;

import com.bitop.common.skeleton.arch.ThreadPoolRegistry;
import com.bitop.common.skeleton.base.SynEnvBase;
import com.bitop.common.skeleton.base.SynEnvTask;
import com.bitop.common.skeleton.base.SynEnvExtractor;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;

public class MessageDispatcher {

    private static final Logger logger = LoggerFactory.getLogger( MessageDispatcher.class );
    private CommandRegistry cmdReg = null;
    private ThreadPoolRegistry tpReg = null;

    /**
     * 使用线程池
     *
     * @param cmdReg
     * @param tpReg
     */
    public MessageDispatcher( CommandRegistry cmdReg, ThreadPoolRegistry tpReg ) {
        this.cmdReg = cmdReg;
        this.tpReg = tpReg;
    }

    /**
     * 只使用默认DIRECT
     *
     * @param cmdReg
     */
    public MessageDispatcher( CommandRegistry cmdReg ) {
        this( cmdReg, null );
    }

    /**
     * 如果使用了SYNC，一定要提供一个提取SynEnvBase的接口
     *
     * @param kindId
     * @param msgId
     * @param message
     * @param ctx
     * @param synEnvExtractor
     */
    public void dispatch( final int kindId, final int msgId, final Object message,
                          final ChannelHandlerContext ctx,
                          SynEnvExtractor synEnvExtractor ) {

        final CommandProperties properties = cmdReg.get( kindId );
        if( properties == null ) {
            logger.error( "[MessageDispatcher::dispatch] not find kindID {} from {}", kindId, ctx.channel().remoteAddress() );
            return;
        }

        try {

            if( "DIRECT".equals( properties.domain ) ) {
                properties.method.invoke( properties.proxyObj, new Object[]{ msgId, message, ctx } );
                return;
            }

            ExecutorService ses = tpReg.get( properties.domain );
            if( ses == null ) {
                logger.error( "[MessageDispatcher::dispatch] not find domain {} from {}", properties.domain, ctx.channel().remoteAddress() );
                return;
            }

            if( "SYNC".equals( properties.domain ) ) {
                SynEnvBase seb = synEnvExtractor.extract( message );
                if( seb == null ) {
                    logger.error( "[MessageDispatcher::dispatch] SynEnvBase not found from message[{}] ip[{}]",
                            message.toString(), ctx.channel().remoteAddress() );
                    return;
                }
                ses.execute( new SynEnvTask( seb ) {
                    @Override
                    public void processTask() {
                        try {
                            properties.method.invoke( properties.proxyObj, new Object[]{ msgId, message, ctx } );
                        }
                        catch( InvocationTargetException e ) {
                            logger.error( e.getMessage(), e );
                        }
                    }
                } );
            }
            else {
                ses.execute( () -> {
                    try {
                        if( properties.method == null ) logger.debug( "method is null" );
                        if( properties.proxyObj == null ) logger.debug( "proxyObj is null" );
                        if( message == null ) logger.debug( "message is null" );

                        properties.method.invoke( properties.proxyObj, new Object[]{ msgId, message, ctx } );
                    }
                    catch( InvocationTargetException e ) {
                        logger.error( e.getMessage(), e );
                    }
                } );
            }

        }
        catch( Exception e ) {
            logger.error( e.getMessage(), e );
        }
    }

    /**
     * 如果没有使用SYNC，走这个接口
     *
     * @param kindId
     * @param msgId
     * @param message
     * @param ctx
     */
    public void dispatch( final int kindId, final int msgId, final Object message,
                          final ChannelHandlerContext ctx ) {
        dispatch( kindId, msgId, message, ctx, null );
    }
}
