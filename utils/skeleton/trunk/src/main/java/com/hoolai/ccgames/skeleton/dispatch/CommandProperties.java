package com.hoolai.ccgames.skeleton.dispatch;

import io.netty.channel.ChannelHandlerContext;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;

public class CommandProperties {

    public String serviceClassName = null;
    public String serviceMethodName = null;
    public String remoteObjectClassName = null;
    public String domain = null;

    public Class< ? > remoteObjectClass = null;
    public Object remoteObjectInst = null;  // for protobuf
    public FastMethod method = null;
    public Object proxyObj = null;

    public CommandProperties() {
    }

    public void init() throws Exception {

        remoteObjectClass = Class.forName( remoteObjectClassName );
        // for protobuf
        try {
            Method createInstMethod = remoteObjectClass.getDeclaredMethod( "getDefaultInstance" );
            remoteObjectInst = createInstMethod.invoke( null );
        }
        catch( NoSuchMethodException e ) {
            remoteObjectInst = null;
        }

        // 某些应用只需要命令号和数据类型，这时允许提供null作为服务名
        // 直接跳过这样的配置
        if( "null".equals( serviceClassName ) ) return;

        Class< ? > serviceClass = Class.forName( serviceClassName );
        Method serviceMethod = serviceClass.getDeclaredMethod( serviceMethodName, Integer.class, remoteObjectClass, ChannelHandlerContext.class );
        FastClass fclazz = FastClass.create( serviceClass );
        method = fclazz.getMethod( serviceMethod );
        if( method == null ) {
            throw new RuntimeException( "[CommandProperties::init] fail for " + serviceClassName + "::" + serviceMethodName );
        }
        extraInit();
    }

    public void extraInit() throws Exception {
        proxyObj = Class.forName( serviceClassName ).newInstance();
    }
}
