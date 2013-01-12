/**
 * 
 */
package com.opensource.orm.sharding.info;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;

import com.opensource.orm.sharding.StatementType;

/**
 * @author luolishu
 * 
 */
public interface ShardingInfo {
	String getSql();

	StatementType getStatementType();

	String getSchema();

	String getTableName();

	String getTargetTableName();

	void setTargetTableName(String name);

	String[] getConfigHashColumns();

	String[] getHashColumns();

	Object[] getHashColumnsValues();

	Table getTable();

	public Statement getStatement();

	public String generateNewSql();
 

}
