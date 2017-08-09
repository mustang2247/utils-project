/**
 *
 */
package com.bitop.common.skeleton.dispatch;

import com.bitop.common.skeleton.utils.PackageClassUtil;
import com.bitop.common.skeleton.utils.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令的注册类。
 * <p>
 * 服务器启动时需要调用注册方法，然后将ID号对应的属性全部注册进来。
 *
 * @author Cedric(TaoShuang)
 * @create 2012-2-29
 */
public class CommandRegistry {

    private static final Logger logger = LoggerFactory.getLogger( CommandRegistry.class );

    private Map< Integer, CommandProperties > mapping = new HashMap<>();

    /**
     * 返回该命令的属性。
     *
     * @param messageKindID
     * @return
     * @author Cedric(TaoShuang)
     */
    public CommandProperties get( int messageKindID ) {
        return mapping.get( messageKindID );
    }

    /**
     * 放入一条命令。
     *
     * @param key
     * @param command
     * @author Cedric(TaoShuang)
     */
    public CommandProperties put( Integer key, CommandProperties command ) {
        return this.mapping.put( key, command );
    }

    public void initByAnnotation( String packageName, Class< ? extends CommandProperties > class0 ) throws Exception {
        PackageClassUtil.getClasses(packageName).forEach(clazz -> {
            String className = clazz.getName();
            if( className != null ) {
                for( Method method : clazz.getDeclaredMethods() ) {
                    MessageHandler handler = method
                            .getAnnotation( MessageHandler.class );
                    if( handler != null ) {
                        CommandProperties command = null; // new CommandProperties();
                        try {
                            command = class0.newInstance();
                        }
                        catch( Exception e ) {
                            logger.error( e.getMessage(), e );
                            throw new RuntimeException( "[CommandRegistry::initByAnnotation]" );
                        }
                        command.remoteObjectClassName = method
                                .getParameterTypes()[1].getName();
                        command.serviceClassName = className;
                        command.serviceMethodName = method.getName();
                        command.domain = handler.domain();

                        put( handler.kindId(), command );
                        logger.info( "CommandRegistry::initByAnnotation load {} {} {} {} {}",
                                handler.kindId(),
                                command.remoteObjectClassName,
                                command.serviceClassName,
                                command.serviceMethodName,
                                command.domain );
                    }
                }
            }
        } );

        for( CommandProperties prop : mapping.values() ) {
            prop.init();
        }
    }

    public void initByAnnotation( String packageName ) throws Exception {
        initByAnnotation( packageName, CommandProperties.class );
    }

    public void initByAnnotationWithoutService( String packageName, Class< ? extends CommandProperties > class0 ) throws Exception {

        PackageClassUtil.getClasses( packageName ).forEach( clazz -> {
            String className = clazz.getName();
            if( className != null ) {
                for( Method method : clazz.getDeclaredMethods() ) {
                    MessageHandler handler = method
                            .getAnnotation( MessageHandler.class );
                    if( handler != null ) {
                        CommandProperties command = null; // new CommandProperties();
                        try {
                            command = class0.newInstance();
                        }
                        catch( Exception e ) {
                            logger.error( e.getMessage(), e );
                            throw new RuntimeException( "[CommandRegistry::initByAnnotationWithoutService]" );
                        }
                        command.remoteObjectClassName = handler.remoteClass().equals( Object.class ) ?
                                method.getReturnType().getName() :
                                handler.remoteClass().getName();
                        command.serviceClassName = "null";
                        command.serviceMethodName = "null";
                        command.domain = "null";

                        put( handler.kindId(), command );
                        logger.info( "CommandRegistry::initByAnnotationWithoutService load {} {} {} {} {}",
                                handler.kindId(),
                                command.remoteObjectClassName,
                                command.serviceClassName,
                                command.serviceMethodName,
                                command.domain );
                    }
                }
            }
        } );

        for( CommandProperties prop : mapping.values() ) {
            prop.init();
        }
    }

    public void initByAnnotationWithoutService( String packageName ) throws Exception {
        initByAnnotationWithoutService( packageName, CommandProperties.class );
    }

    public void init( String filePath ) throws Exception {

        SAXParserFactory
                .newInstance()
                .newSAXParser()
                .parse( PropUtil.getInputStream( filePath ), new CommandLoader(
                        this ) );

        for( CommandProperties prop : mapping.values() ) {
            prop.init();
        }
    }
}
