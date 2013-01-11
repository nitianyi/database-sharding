package com.opensource.orm.ibatis.sharding.executor;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

public class ShardingSimpleExecutor extends SimpleExecutor {

	public ShardingSimpleExecutor(Configuration configuration,
			Transaction transaction) {
		super(configuration, transaction);
	}

	@Override
	public int doUpdate(MappedStatement ms, Object parameter)
			throws SQLException {
		Configuration configuration = ms.getConfiguration();
		StatementHandler handler = configuration.newStatementHandler(this, ms,
				parameter, RowBounds.DEFAULT, null, null);

		return handler.update(null);

	}

	@Override
	public List<BatchResult> doFlushStatements(boolean isRollback)
			throws SQLException {
		return super.doFlushStatements(isRollback);
	}

	@Override
	public <E> List<E> doQuery(MappedStatement ms, Object parameter,
			RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql)
			throws SQLException {
		Configuration configuration = ms.getConfiguration();
		StatementHandler handler = configuration.newStatementHandler(this, ms,
				parameter, rowBounds, resultHandler, boundSql);
		return handler.<E> query(null, resultHandler);

	}

}
