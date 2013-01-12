 
package com.opensource.jdbc.sharding;

import java.util.List;

import com.opensource.jdbc.sharding.wrapper.WrapperPreparedStatement;
import com.opensource.jdbc.sharding.wrapper.WrapperStatement;

/**
 * @author luolishu
 *
 */
public class StatementDispatcher {
	
	public List<PreparedStatementHolder> createStatements(WrapperPreparedStatement wrapper,String sql){
		return null;
	}
	
	public List<PreparedStatementHolder> createPreparedStatement(WrapperPreparedStatement wrapper,String sql,Object[] arguments){
		return null;
	}

	public List<StatementHolder> createStatements(WrapperStatement wrapper,String sql){
		return null;
	}
	
	public List<StatementHolder> createPreparedStatement(WrapperStatement wrapper,String sql,Object[] arguments){
		return null;
	}

}
