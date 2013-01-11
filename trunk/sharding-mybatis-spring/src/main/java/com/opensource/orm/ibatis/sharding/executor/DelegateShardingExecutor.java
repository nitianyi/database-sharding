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
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/**
 * @author luolishu
 * 
 */
public class DelegateShardingExecutor implements Executor {
	Executor executor;

	public DelegateShardingExecutor(Executor executor) {
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
			if (ShardingHelper.isShardingParsedSupport(ms)) {
				return this.shardingUpdate(ms, parameter);
			}
			return this.executor.update(ms, parameter);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	public int shardingUpdate(MappedStatement ms, Object parameter)
			throws SQLException {
		executor = createExecutor(ExecutorType.SIMPLE, ms.getConfiguration(),
				this.getTransaction());
		return executor.update(ms, parameter);
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
			if (ShardingHelper.isShardingParsedSupport(ms)) {
				return this.shardingQuery(ms, parameter, rowBounds,
						resultHandler, cacheKey, boundSql);
			}
			return this.executor.query(ms, parameter, rowBounds, resultHandler,
					cacheKey, boundSql);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	public <E> List<E> shardingQuery(MappedStatement ms, Object parameter,
			RowBounds rowBounds, ResultHandler resultHandler,
			CacheKey cacheKey, BoundSql boundSql) throws SQLException {
		executor = createExecutor(ExecutorType.SIMPLE, ms.getConfiguration(),
				this.getTransaction());
		return this.executor.query(ms, parameter, rowBounds, resultHandler,
				cacheKey, boundSql);
	}

	private Executor createExecutor(ExecutorType execType,
			Configuration configuration, Transaction transaction) {
		switch (execType) {
		case SIMPLE:
			return new ShardingSimpleExecutor(configuration,
					this.getTransaction());
		case REUSE:
			return new ShardingReuseExecutor(configuration,
					this.getTransaction());
		}
		return executor;
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
			if (ShardingHelper.isShardingParsedSupport(ms)) {
				return this.shardingQuery(ms, parameter, rowBounds,
						resultHandler);
			}
			return this.executor.query(ms, parameter, rowBounds, resultHandler);
		} finally {
			ExecutorContext.setContext(null);
		}
	}

	public <E> List<E> shardingQuery(MappedStatement ms, Object parameter,
			RowBounds rowBounds, ResultHandler resultHandler)
			throws SQLException {
		executor = createExecutor(ExecutorType.SIMPLE, ms.getConfiguration(),
				this.getTransaction());
		return this.executor.query(ms, parameter, rowBounds, resultHandler);

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
