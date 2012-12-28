/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import java.util.List;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.TableDatabaseConfig.DatabaseItem;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 * 
 */
public abstract class ReadWriteLoadBalancer implements LoadBalancer {

	@Override
	public DataSource dispatch(ShardingInfo shardInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	abstract DataSource doDispatch(List<DatabaseItem> databaseItems);

}
