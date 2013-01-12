/**
 * 
 */
package com.opensource.orm.ibatis.sharding.executor.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.sql.DataSource;

import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensource.orm.ibatis.sharding.transaction.ShardingManagedTransaction;
import com.opensource.orm.sharding.DatabaseTarget;

/**
 * @author luolishu
 * 
 */
public class QueryTask implements Runnable {
	static Logger logger = LoggerFactory.getLogger(QueryTask.class);
	List results;
	final StatementHandler statementHandler;
	final DatabaseTarget target;
	final Transaction transaction;
	final ResultHandler resultHandler;
	final MappedStatement mappedStatement;
	SQLException sqlException;
	RuntimeException runtimeException;
	CyclicBarrier barrier;

	public QueryTask(StatementHandler statementHandler,
			DatabaseTarget databaseTarget,
			ShardingManagedTransaction transaction,
			ResultHandler resultHandler, MappedStatement mappedStatement) {
		this.statementHandler = statementHandler;
		this.target = databaseTarget;
		Transaction newTransaction = this.createTransaction(target
				.getDataSource());
		transaction.addTransaction(newTransaction);
		this.transaction = newTransaction;
		this.resultHandler = resultHandler;
		this.mappedStatement = mappedStatement;
	}

	@Override
	public void run() {
		if (logger.isDebugEnabled()) {
			logger.debug("routing to target is:=" + target);
		}

		try {
			Statement statement = prepare(transaction.getConnection(),
					target.getSql());
			statementHandler.parameterize(statement);
			try {
				results = statementHandler.query(statement, resultHandler);
			} finally {
				closeStatement(statement);
			}
			if (barrier != null) {
				try {
					barrier.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (SQLException e) {
			sqlException = e;
		} catch (RuntimeException e) {
			runtimeException = e;
		}
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
		Integer timeout = mappedStatement.getTimeout();
		Integer defaultTimeout = mappedStatement.getConfiguration()
				.getDefaultStatementTimeout();
		if (timeout != null) {
			stmt.setQueryTimeout(timeout);
		} else if (defaultTimeout != null) {
			stmt.setQueryTimeout(defaultTimeout);
		}
	}

	protected void setFetchSize(Statement stmt) throws SQLException {
		Integer fetchSize = mappedStatement.getFetchSize();
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

	public List getResults() {
		return results;
	}

	public void setBarrier(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

}
