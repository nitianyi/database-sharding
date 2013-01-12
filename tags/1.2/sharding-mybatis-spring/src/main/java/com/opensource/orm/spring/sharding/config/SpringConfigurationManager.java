package com.opensource.orm.spring.sharding.config;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.datasource.DbcpDataSourceFactory;
import com.opensource.orm.sharding.utils.StringUtils;
import com.opensource.orm.spring.sharding.datasource.SpringDataSourceFactory;
import com.opensource.orm.spring.sharding.factory.SpringObjectFactory;

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

	public SpringConfigurationManager(String location,
			ApplicationContext applicationContext) {

		super(location);
		this.applicationContext = applicationContext;

	}

	public void init() throws FileNotFoundException, Exception {
		this.objectFactory = new SpringObjectFactory(applicationContext);
		SpringDataSourceFactory factory = new SpringDataSourceFactory();
		factory.setApplicationContext(applicationContext);
		dataSourceFactory.addDataSourceFactory(factory);
		dataSourceFactory.addDataSourceFactory(new DbcpDataSourceFactory());
		if (StringUtils.isNotBlank(location)) {
			ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourceLoader.getResources(location);
			for (Resource res : resources) {
				this.parseConfiguration(res.getInputStream());
			}
		}

		threadPoolExecutor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(500);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
