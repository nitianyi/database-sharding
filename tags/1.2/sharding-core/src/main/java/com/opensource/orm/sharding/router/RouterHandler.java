/**
 * 
 */
package com.opensource.orm.sharding.router;

import java.util.List;

import com.opensource.orm.sharding.DatabaseTarget;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 *
 */
public interface RouterHandler {
	List<DatabaseTarget> handle(ShardingInfo shardInfo,
			Object... parameters);
}
