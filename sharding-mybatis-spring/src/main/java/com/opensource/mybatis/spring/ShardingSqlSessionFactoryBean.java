/**
 * 
 */
package com.opensource.mybatis.spring;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensource.orm.ibatis.sharding.transaction.ShardingManagedTransactionFactory;
import com.opensource.orm.spring.sharding.config.SpringConfigurationManager;

/**
 * @author luolishu
 * 
 */
public class ShardingSqlSessionFactoryBean extends SqlSessionFactoryBean
		implements ApplicationContextAware {
	private String shardingConfigLocation;
	ApplicationContext applicationContext;

	public void setShardingConfigLocation(String shardingConfigLocation) {
		this.shardingConfigLocation = shardingConfigLocation;
	}

	public void afterPropertiesSet() throws Exception {
		SpringConfigurationManager shardingConfigManager = new SpringConfigurationManager(
				shardingConfigLocation, applicationContext);
		this.setTransactionFactory(new ShardingManagedTransactionFactory());
		shardingConfigManager.init();
		super.afterPropertiesSet();

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
