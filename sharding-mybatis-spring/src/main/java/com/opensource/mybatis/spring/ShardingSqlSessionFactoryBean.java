/**
 * 
 */
package com.opensource.mybatis.spring;

import org.mybatis.spring.SqlSessionFactoryBean;

import com.opensource.orm.ibatis.sharding.transaction.ShardingManagedTransactionFactory;

/**
 * @author luolishu
 * 
 */
public class ShardingSqlSessionFactoryBean extends SqlSessionFactoryBean {
	public void afterPropertiesSet() throws Exception {
		this.setTransactionFactory(new ShardingManagedTransactionFactory());
		super.afterPropertiesSet();
		
	}
}
