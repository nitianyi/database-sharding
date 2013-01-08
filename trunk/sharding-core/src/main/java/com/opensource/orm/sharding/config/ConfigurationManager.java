/**
 * 
 */
package com.opensource.orm.sharding.config;

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sql.DataSource;

import com.opensource.orm.sharding.StatementType;
import com.opensource.orm.sharding.factory.ObjectFactory;
import com.opensource.orm.sharding.hash.HashGenerator;
import com.opensource.orm.sharding.router.DatabaseRouter;

/**
 * @author luolishu
 * 
 */
public interface ConfigurationManager {
	DatabaseRouter getRouter();

	public DataSource getShardDataSource(StatementType statementType,
			String shardTableName);

	TableConfig getTableConfig(String shardTableName);

	public TableDatabaseConfig getDatabaseConfig(String tableName);

	public ObjectFactory getObjectFactory();

	public Set<String> getShardedTables();

	public DataSource getDataSource(String id);

	public ThreadPoolExecutor getQueryThreadPool();

	HashGenerator getHashGenerator(String id);
}
