/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-2-29
 */
public abstract class ContextUtil {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E getBean(String beanName, Class<E> classType) {
		return (E) ContextUtil.getBean(beanName);
	}
	
	public static void init() {
		
	}
}
