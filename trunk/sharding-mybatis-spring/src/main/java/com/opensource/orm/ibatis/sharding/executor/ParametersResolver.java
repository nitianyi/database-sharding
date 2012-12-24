package com.opensource.orm.ibatis.sharding.executor;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author luolishu
 *
 */
public interface ParametersResolver {

	public Object[] resolve(MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject);
}
