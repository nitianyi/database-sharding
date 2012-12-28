package com.opensource.orm.spring.sharding.factory;

import org.springframework.context.ApplicationContext;

import com.opensource.orm.sharding.factory.ObjectFactory;

/**
 * @author luolishu
 * 
 */
public class SpringObjectFactory implements ObjectFactory {
	ApplicationContext applicationContext;

	public SpringObjectFactory(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object getObject(String id) {
		return applicationContext.getBean(id);
	}

	@Override
	public <T> T getObject(String id, Class<T> claz) {
		return applicationContext.getBean(id, claz);
	}

}
