/**
 * 
 */
package com.opensource.orm.sharding.sql.parser;

import com.opensource.orm.sharding.sql.parser.DefaultShardingSqlParser.ColumnInfo;

/**
 * @author luolishu
 *
 */
public interface ValueResolver {
	Object resolveValue(JdbcParameterInfos infos,ColumnInfo item,Object[] parameters);
}
