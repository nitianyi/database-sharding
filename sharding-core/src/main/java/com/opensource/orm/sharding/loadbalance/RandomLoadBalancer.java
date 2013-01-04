/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableDatabaseConfig.DatabaseItem;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 * random balancer
 */
public class RandomLoadBalancer extends ReadWriteLoadBalancer implements
		LoadBalancer {
	private final Random random = new Random();

	@Override
	<T extends DatabaseItem> DataSource doDispatch(List<T> databaseItems,
			ShardingInfo shardInfo) {
		//TODO with weight
		int index = random.nextInt(databaseItems.size());
		DatabaseItem item = databaseItems.get(index);
		ConfigurationManager configurationManager = DefaultConfigurationManager
				.getInstance();
		return configurationManager.getDataSource(item.getDataSourceId());
	}

}
