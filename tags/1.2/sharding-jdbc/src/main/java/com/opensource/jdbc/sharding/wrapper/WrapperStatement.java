package com.opensource.jdbc.sharding.wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.opensource.jdbc.sharding.StatementHolder;
import com.opensource.jdbc.sharding.StatementDispatcher;

public class WrapperStatement implements Statement {

	StatementDispatcher dispatcher;
	List<PreparedStatement> preparedStatements=new LinkedList<PreparedStatement>();
	PreparedStatement currentPreparedStatement;
	WrapperResultSet resultSet;
	String sql;
	public Object[] getParameters(){
		return null;
	}
	@Override
	public ResultSet executeQuery(String sql) throws SQLException { 
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters);
		
		WrapperResultSet resultSets=new WrapperResultSet();
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(currentPreparedStatement);
			ResultSet resultSet=currentPreparedStatement.executeQuery(holder.getSql());
			resultSets.addResultSet(resultSet);
		}
		resultSet=resultSets;
		return resultSets;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		int count=0;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			count+=holder.getPreparedStatement().executeUpdate(holder.getSql());
		}
		return count;
	}

	@Override
	public void close() throws SQLException {
		for(PreparedStatement statement:preparedStatements){
			statement.close();
		}
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxRows() throws SQLException {
		int rows=0;
		for(PreparedStatement statement:preparedStatements){
			rows+=statement.getMaxRows();
		}
		
		return rows;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws SQLException {
		for(PreparedStatement statement:preparedStatements){
			statement.cancel();
		}
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(String sql) throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		boolean flag=false;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			flag=flag&&holder.getPreparedStatement().execute(holder.getSql());
		}
		return flag;
	}

	@Override
	public ResultSet getResultSet() throws SQLException { 
		return resultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		int count=0;
		for(PreparedStatement statement:preparedStatements){
			count+=statement.getUpdateCount();
		}		
		return count;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		boolean flag=false;
		for(PreparedStatement statement:preparedStatements){
			flag=flag||statement.getMoreResults();
		}		
		return flag;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		int count=0;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			count+=holder.getPreparedStatement().executeUpdate(holder.getSql(),autoGeneratedKeys);
		}
		return count;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		int count=0;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			count+=holder.getPreparedStatement().executeUpdate(holder.getSql(),columnIndexes);
		}
		return count;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		int count=0;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			count+=holder.getPreparedStatement().executeUpdate(holder.getSql(),columnNames);
		}
		return count;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		boolean flag=false;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			flag=flag&&holder.getPreparedStatement().execute(holder.getSql(),autoGeneratedKeys);
		}
		return flag;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		boolean flag=false;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			flag=flag&&holder.getPreparedStatement().execute(holder.getSql(),columnIndexes);
		}
		return flag;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		Object[] parameters=getParameters();
		List<StatementHolder> preparedStatementList=dispatcher.createPreparedStatement(this,sql, parameters); 
		boolean flag=false;
		for(StatementHolder holder:preparedStatementList){
			currentPreparedStatement=holder.getPreparedStatement();
			preparedStatements.add(holder.getPreparedStatement());
			flag=flag&&holder.getPreparedStatement().execute(holder.getSql(),columnNames);
		}
		return flag;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		boolean flag=false;
		for(PreparedStatement statement:preparedStatements){
			flag=flag&&statement.getMoreResults();
		}		
		return flag;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		for(PreparedStatement statement:preparedStatements){
			statement.setPoolable(poolable);
		}
	}

	@Override
	public boolean isPoolable() throws SQLException {
		boolean flag=false;
		for(PreparedStatement statement:preparedStatements){
			flag=flag&&statement.isPoolable();
		}		
		return flag;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	

	

	 
 

}
