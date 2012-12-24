/**
 * 
 */
package com.opensource.orm.sharding.router;

import java.util.List;

import com.opensource.orm.sharding.DatabaseTarget;
import com.opensource.orm.sharding.ShardingNotSupportException;
import com.opensource.orm.sharding.StatementType;
import com.opensource.orm.sharding.info.ShardingInfo;
import com.opensource.orm.sharding.sql.parser.DefaultShardingSqlParser;
import com.opensource.orm.sharding.sql.parser.ShardingSqlParser;

/**
 * @author luolishu
 * 
 */
public class DefaultDatabaseRouter implements DatabaseRouter {
	static final ShardingSqlParser shardingSqlParser = new DefaultShardingSqlParser();

	public List<DatabaseTarget> route(String sql, Object... parameters)
			throws ShardingNotSupportException {
		ShardingInfo shardInfo = shardingSqlParser.parse(sql, parameters);
		if (shardInfo == null) {
			return null;
		}
		this.checkShard(shardInfo);
		RouterHandler handler = RouterHandlerFactory.create(shardInfo
				.getStatementType());
		return handler.handle(shardInfo, parameters);
	}

	private void checkShard(ShardingInfo shardInfo)
			throws ShardingNotSupportException {

		if (shardInfo.getConfigHashColumns() == null
				|| shardInfo.getConfigHashColumns().length <= 0) {
			throw new ShardingNotSupportException(
					"not hash columns config for shardInfo=" + shardInfo);
		}
		if (StatementType.INSERT.equals(shardInfo.getStatementType())) {
			if (shardInfo.getHashColumns() == null
					|| shardInfo.getHashColumns().length <= 0) {
				throw new ShardingNotSupportException(
						"insert error!no sharding column found! shardInfo="
								+ shardInfo);
			}
		}
	}

}
