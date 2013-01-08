/**
 * 
 */
package com.opensource.orm.ibatis.sharding.executor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.orm.ibatis.sharding.executor.DefaultParametersResolver;
import com.opensource.orm.ibatis.sharding.executor.ExecutorContext;
import com.opensource.orm.ibatis.sharding.executor.ParametersResolver;
import com.opensource.orm.ibatis.sharding.executor.task.ConcurrentQueryTaskExecutor;
import com.opensource.orm.ibatis.sharding.executor.task.QueryTask;
import com.opensource.orm.ibatis.sharding.executor.task.QueryTaskExecutor;
import com.opensource.orm.ibatis.sharding.transaction.ShardingManagedTransaction;
import com.opensource.orm.sharding.DatabaseTarget;
import com.opensource.orm.sharding.ShardingNotSupportException;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.router.DatabaseRouter;

/**
 * @author luolishu
 * 
 */
public class ShardingStatementHandler implements StatementHandler {
	static Logger logger = LoggerFactory
			.getLogger(ShardingStatementHandler.class);
	final StatementHandler statementHandler;
	static final Map<String, Boolean> shardingMap = new HashMap<String, Boolean>();
	static final ParametersResolver parametersResolver = new DefaultParametersResolver();
	static final QueryTaskExecutor taskExecutor = new ConcurrentQueryTaskExecutor();

	public ShardingStatementHandler(StatementHandler statementHandler) {
		this.statementHandler = statementHandler;
	}

	@Override
	public Statement prepare(Connection connection) throws SQLException {
		return statementHandler.prepare(connection);
	}

	@Override
	public void parameterize(Statement statement) throws SQLException {
		statementHandler.parameterize(statement);
	}

	@Override
	public void batch(Statement statement) throws SQLException {
		statementHandler.batch(statement);
	}

	@Override
	public int update(Statement statement) throws SQLException {
		if (isShardingSupport(ExecutorContext.getContext().getMappedStatement())) {
			return this.shardingUpdate(statement);
		}
		return statementHandler.update(statement);
	}

	@Override
	public <E> List<E> query(Statement statement, ResultHandler resultHandler)
			throws SQLException {
		ExecutorContext context = ExecutorContext.getContext();
		MappedStatement mappedStatement = context.getMappedStatement();
		if (isShardingSupport(ExecutorContext.getContext().getMappedStatement())
				&& SqlCommandType.SELECT.equals(mappedStatement
						.getSqlCommandType())) {
			return this.shardingQuery(statement, resultHandler);
		}
		return statementHandler.query(statement, resultHandler);
	}

	public <E> List<E> shardingQuery(Statement statement,
			ResultHandler resultHandler) throws SQLException {
		ExecutorContext context = ExecutorContext.getContext();
		switch (context.getMappedStatement().getStatementType()) {
		case STATEMENT:
		case PREPARED:
			DatabaseRouter router = this.getDatabaseRouter();

			MappedStatement mappedStatement = context.getMappedStatement();
			List<DatabaseTarget> targets = null;
			try {

				targets = router.route(
						getBoundSql().getSql(),
						parametersResolver.resolve(mappedStatement,
								this.getBoundSql(), context.getParameter()));
			} catch (ShardingNotSupportException e) {
				shardingMap.put(mappedStatement.getId(), false);
				e.printStackTrace();
			}
			if (targets != null && !targets.isEmpty()) {
				shardingMap.put(mappedStatement.getId(), true);
				return this.doShardingQuery(targets, mappedStatement,
						resultHandler);
			} else {// give chance to the normal query
				shardingMap.put(mappedStatement.getId(), false);
				return statementHandler.query(statement, resultHandler);
			}
		default:
			break;
		}
		return statementHandler.query(statement, resultHandler);
	}

	private <E> List<E> doShardingQuery(List<DatabaseTarget> targets,
			MappedStatement mappedStatement, ResultHandler resultHandler)
			throws SQLException {
		ShardingManagedTransaction transaction = (ShardingManagedTransaction) ExecutorContext
				.getContext().getExecutor().getTransaction();
		List<E> results;
		try {
			List<QueryTask> tasks = new ArrayList<QueryTask>(targets.size());
			for (DatabaseTarget target : targets) {
				QueryTask task = new QueryTask(statementHandler, target,
						transaction, resultHandler, mappedStatement);
				tasks.add(task);
			}
			results = taskExecutor.execute(tasks);
		} finally {
			transaction.reset();
		}
		return results;
	}

	public Statement prepare(Connection connection, String sql)
			throws SQLException {
		Statement statement = null;
		try {
			statement = instantiateStatement(connection, sql);
			setStatementTimeout(statement);
			setFetchSize(statement);
			return statement;
		} catch (SQLException e) {
			closeStatement(statement);
			throw e;
		} catch (Exception e) {
			closeStatement(statement);
			throw new ExecutorException("Error preparing statement.  Cause: "
					+ e, e);
		}
	}

	protected Statement instantiateStatement(Connection connection, String sql)
			throws SQLException {
		MappedStatement mappedStatement = ExecutorContext.getContext()
				.getMappedStatement();

		switch (mappedStatement.getStatementType()) {
		case STATEMENT:
			if (mappedStatement.getResultSetType() != null) {
				return connection.createStatement(mappedStatement
						.getResultSetType().getValue(),
						ResultSet.CONCUR_READ_ONLY);
			} else {
				return connection.createStatement();
			}
		case PREPARED:
			if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
				String[] keyColumnNames = mappedStatement.getKeyColumns();
				if (keyColumnNames == null) {
					return connection.prepareStatement(sql,
							PreparedStatement.RETURN_GENERATED_KEYS);
				} else {
					return connection.prepareStatement(sql, keyColumnNames);
				}
			} else if (mappedStatement.getResultSetType() != null) {
				return connection.prepareStatement(sql, mappedStatement
						.getResultSetType().getValue(),
						ResultSet.CONCUR_READ_ONLY);
			} else {
				return connection.prepareStatement(sql);
			}
		default:
			throw new ExecutorException("Unknown statement type: "
					+ mappedStatement.getStatementType());
		}

	}

	protected void setStatementTimeout(Statement stmt) throws SQLException {
		Integer timeout = ExecutorContext.getContext().getMappedStatement()
				.getTimeout();
		Integer defaultTimeout = ExecutorContext.getContext()
				.getMappedStatement().getConfiguration()
				.getDefaultStatementTimeout();
		if (timeout != null) {
			stmt.setQueryTimeout(timeout);
		} else if (defaultTimeout != null) {
			stmt.setQueryTimeout(defaultTimeout);
		}
	}

	protected void setFetchSize(Statement stmt) throws SQLException {
		Integer fetchSize = ExecutorContext.getContext().getMappedStatement()
				.getFetchSize();
		if (fetchSize != null) {
			stmt.setFetchSize(fetchSize);
		}
	}

	public void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			// ignore
		}
	}

	private Transaction createTransaction(DataSource dataSource) {
		return new SpringManagedTransaction(dataSource);
	}

	public int shardingUpdate(Statement statement) throws SQLException {
		ExecutorContext context = ExecutorContext.getContext();
		switch (context.getMappedStatement().getStatementType()) {
		case STATEMENT:
		case PREPARED:
			DatabaseRouter router = this.getDatabaseRouter();

			MappedStatement mappedStatement = context.getMappedStatement();
			List<DatabaseTarget> targets = null;
			try {

				targets = router.route(
						getBoundSql().getSql(),
						parametersResolver.resolve(mappedStatement,
								this.getBoundSql(), context.getParameter()));
			} catch (ShardingNotSupportException e) {
				shardingMap.put(mappedStatement.getId(), false);
				e.printStackTrace();
			}
			if (targets != null && !targets.isEmpty()) {
				shardingMap.put(mappedStatement.getId(), true);
				return this.doShardingUpdate(targets);
			} else {// give chance to the normal query
				shardingMap.put(mappedStatement.getId(), false);
				return statementHandler.update(statement);
			}
		default:
			break;
		}
		return statementHandler.update(statement);
	}

	public int doShardingUpdate(List<DatabaseTarget> targets)
			throws SQLException {
		ShardingManagedTransaction transaction = (ShardingManagedTransaction) ExecutorContext
				.getContext().getExecutor().getTransaction();
		int updateCount = 0;
		try {
			for (DatabaseTarget target : targets) {
				if (logger.isDebugEnabled()) {
					logger.debug("routing to target is:=" + target);
				}
				Transaction newTransaction = this.createTransaction(target
						.getDataSource());
				transaction.addTransaction(newTransaction);
				Statement statement = prepare(newTransaction.getConnection(),
						target.getSql());
				statementHandler.parameterize(statement);
				try {
					updateCount += statementHandler.update(statement);
				} finally {
					closeStatement(statement);
				}
			}
		} finally {
			transaction.reset();
		}
		return updateCount;
	}

	private boolean isShardingSupport(MappedStatement mappedStatement) {
		if (mappedStatement.getId().endsWith("selectKey")) {
			return false;
		}
		Boolean flag = shardingMap.get(mappedStatement.getId());
		return flag == null ? true : flag.booleanValue();
	}

	@Override
	public BoundSql getBoundSql() {
		return statementHandler.getBoundSql();
	}

	@Override
	public ParameterHandler getParameterHandler() {
		return statementHandler.getParameterHandler();
	}

	DatabaseRouter getDatabaseRouter() {
		return DefaultConfigurationManager.getInstance().getRouter();
	}
}
