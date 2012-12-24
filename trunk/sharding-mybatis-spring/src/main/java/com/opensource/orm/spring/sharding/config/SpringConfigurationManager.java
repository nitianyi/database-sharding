package com.opensource.orm.spring.sharding.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.spring.sharding.datasource.SpringDataSourceFactory;

/**
 * @author luolishu
 * 
 */
public class SpringConfigurationManager extends DefaultConfigurationManager
		implements ApplicationContextAware {
	ApplicationContext applicationContext;

	public SpringConfigurationManager() {
		super();
	}

	public SpringConfigurationManager(String location) {
		super(location);
		SpringDataSourceFactory factory=new SpringDataSourceFactory();
		factory.setApplicationContext(applicationContext);
		dataSourceFactory.addDataSourceFactory(factory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
