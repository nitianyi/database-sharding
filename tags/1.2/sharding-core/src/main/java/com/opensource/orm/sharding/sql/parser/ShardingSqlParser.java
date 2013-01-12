/**
 * 
 */
package com.opensource.orm.sharding.sql.parser;

import com.opensource.orm.sharding.ShardingNotSupportException;
import com.opensource.orm.sharding.info.ShardingInfo;

/**
 * @author luolishu
 *
 */
public interface ShardingSqlParser {
	
	public ShardingInfo parse(String sql, Object... parameters)throws ShardingNotSupportException ;

}
