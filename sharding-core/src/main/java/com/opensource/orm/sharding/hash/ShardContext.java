/**
 * 
 */
package com.opensource.orm.sharding.hash;

/**
 * @author luolishu
 * 
 */
public class ShardContext {
	String tableName;
	String[] columns;
	Object[] values;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

}
