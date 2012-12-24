/**
 * 
 */
package com.opensource.orm.ibatis.sharding.executor;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/**
 * @author luolishu
 * 
 */
public class ShardingExecutor implements Executor {
	final Executor executor;
	
	public ShardingExecutor(Executor executor) {
		this.executor = executor;
	}

	@Override
	public int update(MappedStatement ms, Object parameter) throws SQLException {
		try {
			ExecutorContext context = new ExecutorContext();
			ExecutorContext.setContext(context);
			context.setMappedStatement(ms);
			context.setParameter(parameter);
			context.setExecutor(this);
			return this.executor.update(ms, parameter);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter,
			RowBounds rowBounds, ResultHandler resultHandler,
			CacheKey cacheKey, BoundSql boundSql) throws SQLException {
		try {
			ExecutorContext context = new ExecutorContext();
			ExecutorContext.setContext(context);
			context.setMappedStatement(ms);
			context.setParameter(parameter);
			context.setExecutor(this);
			return this.executor.query(ms, parameter, rowBounds, resultHandler,
					cacheKey, boundSql);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter,
			RowBounds rowBounds, ResultHandler resultHandler)
			throws SQLException {
		try {
			ExecutorContext context = new ExecutorContext();
			ExecutorContext.setContext(context);
			context.setMappedStatement(ms);
			context.setParameter(parameter);
			context.setExecutor(this);
			return this.executor.query(ms, parameter, rowBounds, resultHandler);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	@Override
	public List<BatchResult> flushStatements() throws SQLException {
		return this.executor.flushStatements();
	}

	@Override
	public void commit(boolean required) throws SQLException {
		this.executor.flushStatements();
	}

	@Override
	public void rollback(boolean required) throws SQLException {
		this.executor.rollback(required);
	}

	@Override
	public CacheKey createCacheKey(MappedStatement ms, Object parameterObject,
			RowBounds rowBounds, BoundSql boundSql) {
		return this.executor.createCacheKey(ms, parameterObject, rowBounds,
				boundSql);
	}

	@Override
	public boolean isCached(MappedStatement ms, CacheKey key) {
		return this.executor.isCached(ms, key);
	}

	@Override
	public void clearLocalCache() {
		this.executor.clearLocalCache();
	}

	@Override
	public void deferLoad(MappedStatement ms, MetaObject resultObject,
			String property, CacheKey key) {
		this.executor.deferLoad(ms, resultObject, property, key);
	}

	@Override
	public Transaction getTransaction() {
		return this.executor.getTransaction();
	}

	@Override
	public void close(boolean forceRollback) {
		this.executor.close(forceRollback);
	}

	@Override
	public boolean isClosed() {
		return this.executor.isClosed();
	}

}
