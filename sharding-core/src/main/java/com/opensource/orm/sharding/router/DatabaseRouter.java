/**
 * 
 */
package com.opensource.orm.sharding.router;

import java.util.List;

import com.opensource.orm.sharding.DatabaseTarget;
import com.opensource.orm.sharding.ShardingNotSupportException;

/**
 * @author luolishu
 * 
 */
public interface DatabaseRouter {

	public List<DatabaseTarget> route(String sql, Object... parameters)throws ShardingNotSupportException;
}
