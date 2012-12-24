/**
 * 
 */
package com.opensource.orm.sharding.sql.parser;

import com.opensource.orm.sharding.sql.parser.DefaultShardingSqlParser.ColumnInfo;

/**
 * @author luolishu
 * 
 */
public class DefaultValueResolver implements ValueResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jd.orm.sharding.sql.parser.ValueResolver#resolveValue(java.lang.String
	 * , com.jd.orm.sharding.sql.parser.DefaultShardingSqlParser.ColumnInfo,
	 * java.lang.Object[])
	 */
	public Object resolveValue(JdbcParameterInfos infos, ColumnInfo item, Object[] parameters) {
		if (item.isJdbcParameter) {
			int i = this.getIndex(infos, item);
			return parameters[i];
		}
		return item.value;
	}

	private int getIndex(JdbcParameterInfos infos, ColumnInfo item) {
		return infos.getIndexMap().get(item.jdbcParameter);
	}

}
