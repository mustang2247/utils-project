/**
 *
 */
package com.bitop.common.skeleton.dispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义方法注解。凡是用此注解注释的方法。都是前后端进行通信的接口
 *
 * @author Administrator
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface MessageHandler {
    int kindId();                     // 消息号

    String domain() default "DIRECT"; // 使用哪个线程池执行。默认直接在IO线程执行

    Class< ? > remoteClass() default Object.class;  // 远程消息类名
}