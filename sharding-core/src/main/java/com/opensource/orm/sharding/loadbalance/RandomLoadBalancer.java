/**
 * 
 */
package com.opensource.orm.sharding.loadbalance;

import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.TableDatabaseConfig.DatabaseItem;

/**
 * @author luolishu
 * 
 */
public class RandomLoadBalancer extends ReadWriteLoadBalancer implements
		LoadBalancer {
	private final Random random = new Random();

	@Override
	DataSource doDispatch(List<DatabaseItem> databaseItems) {
		int index = random.nextInt(databaseItems.size());

		String name = dataSourceNames[index];

		return null;
	}

}
