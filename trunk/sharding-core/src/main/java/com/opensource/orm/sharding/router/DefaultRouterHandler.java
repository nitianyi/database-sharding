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
public class DefaultRouterHandler implements RouterHandler {

	/* (non-Javadoc)
	 * @see com.jd.orm.sharding.router.RouterHandler#handle(com.jd.orm.sharding.ShardingInfo, java.lang.Object[])
	 */
	public List<DatabaseTarget> handle(ShardingInfo shardInfo,
			Object... parameters) {
		throw new IllegalArgumentException("statment type="
				+ shardInfo.getStatementType() + " can not be handled!");
	}

}
