/**
 * 
 */
package com.opensource.orm.spring.sharding.datasource;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensource.orm.sharding.config.DataSourceConfig;
import com.opensource.orm.sharding.datasource.DataSourceFactory;

/**
 * @author luolishu
 * 
 */
public class SpringDataSourceFactory implements DataSourceFactory,
		ApplicationContextAware {
	ApplicationContext applicationContext;

	@Override
	public DataSource create(DataSourceConfig config) throws Exception {
		if(config.getRef()!=null&&config.getRef().trim().length()>0){
			return (DataSource) applicationContext.getBean(config.getRef());
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

}
