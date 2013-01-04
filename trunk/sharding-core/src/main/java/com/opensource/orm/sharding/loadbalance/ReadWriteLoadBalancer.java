/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import java.util.List;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableDatabaseConfig;
import com.opensource.orm.sharding.config.TableDatabaseConfig.DatabaseItem;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 * 
 */
public abstract class ReadWriteLoadBalancer implements LoadBalancer {

	@Override
	public DataSource dispatch(ShardingInfo shardInfo) {
		ConfigurationManager configurationManager = DefaultConfigurationManager
				.getInstance();
		TableDatabaseConfig databaseConfig = configurationManager
				.getDatabaseConfig(shardInfo.getShardTableName());
		if (databaseConfig.isReadwrite()) {
			switch (shardInfo.getStatementType()) {
			case INSERT:
			case UPDATE:
			case DELETE:
				return this.doDispatch(databaseConfig.getMasters(),shardInfo);
			case SELECT:
				return this.doDispatch(databaseConfig.getSlaves(),shardInfo);
			}
		} else {
			return this.doDispatch(databaseConfig.getMasters(),shardInfo);
		}
		return null;
	}

	abstract <T extends DatabaseItem> DataSource doDispatch(
			List<T> databaseItems,ShardingInfo shardInfo);

}
