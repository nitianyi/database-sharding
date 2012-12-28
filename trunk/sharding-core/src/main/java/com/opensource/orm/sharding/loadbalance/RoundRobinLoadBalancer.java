/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import javax.sql.DataSource;

/**
 * @author luolishu
 * 
 */
public class RoundRobinLoadBalancer extends ReadWriteLoadBalancer implements LoadBalancer {

	@Override
	DataSource doDispatch(String[] dataSourceNames) {
		// TODO Auto-generated method stub
		return null;
	}

	 
 
}
