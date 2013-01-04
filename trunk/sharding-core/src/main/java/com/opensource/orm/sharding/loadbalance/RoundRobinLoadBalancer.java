/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableDatabaseConfig.DatabaseItem;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 * 
 */
public class RoundRobinLoadBalancer extends ReadWriteLoadBalancer implements
		LoadBalancer {
	private final ConcurrentMap<String, AtomicInteger> counters =new ConcurrentHashMap<String, AtomicInteger>();

	@Override
	<T extends DatabaseItem> DataSource doDispatch(List<T> databaseItems,
			ShardingInfo shardInfo) {
		String key = shardInfo.getShardTableName();
		int count = incrementCounter(key);
		//TODO with weight
		DatabaseItem item = databaseItems.get(count % databaseItems.size());
		ConfigurationManager configurationManager = DefaultConfigurationManager
				.getInstance();
		return configurationManager.getDataSource(item.getDataSourceId());
	}

	private int incrementCounter(String key) {
		AtomicInteger c = counters.get(key);
		if (c == null) {
			c = new AtomicInteger(-1);
			counters.putIfAbsent(key, c);
		}
		return c.incrementAndGet();
	}

}
