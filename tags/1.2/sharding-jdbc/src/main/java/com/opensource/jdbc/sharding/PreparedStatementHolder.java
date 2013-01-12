/**
 * 
 */
package com.opensource.jdbc.sharding;

import java.sql.PreparedStatement;

/**
 * @author luolishu
 *
 */
public class PreparedStatementHolder {
	String sql;
	PreparedStatement preparedStatement;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
	

}
