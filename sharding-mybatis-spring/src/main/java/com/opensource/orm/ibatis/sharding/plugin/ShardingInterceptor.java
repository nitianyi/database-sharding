/**
 * 
 */
package com.opensource.orm.ibatis.sharding.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import com.opensource.orm.ibatis.sharding.executor.ShardingExecutor;
import com.opensource.orm.ibatis.sharding.executor.statement.ShardingStatementHandler;

/**
 * @author luolishu
 * 
 */
public class ShardingInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return invocation.proceed();
	}

	 
	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return new ShardingExecutor((Executor) target);
		}
		if (target instanceof StatementHandler) {
			return new ShardingStatementHandler((StatementHandler) target);
		}
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jd.orm.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

}
