package com.opensource.orm.sharding.loadbalance;

import javax.sql.DataSource;

import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 * 
 */
public interface LoadBalancer {

	DataSource dispatch(ShardingInfo shardInfo);

}
