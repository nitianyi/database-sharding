package com.opensource.orm.sharding.info;

import java.util.Arrays;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import com.opensource.orm.sharding.StatementType;

public class JdbcShardingInfo implements ShardingInfo {
	private String sql;
	private StatementType statementType;
	private String schema;
	private String tableName;
	private String[] configHashColumns;

	private String shardTableName;

	String[] hashColumns;
	Object[] hashColumnsValues;
	
	Table table;
	Statement statement;
	public String getSql() {
		return sql;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getShardTableName() {
		return shardTableName;
	}

	public void setShardTableName(String shardTableName) {
		this.shardTableName = shardTableName;
	}

	public String[] getConfigHashColumns() {
		return configHashColumns;
	}

	public void setConfigHashColumns(String[] configHashColumns) {
		this.configHashColumns = configHashColumns;
	}

	public String[] getHashColumns() {
		return hashColumns;
	}

	public void setHashColumns(String[] hashColumns) {
		this.hashColumns = hashColumns;
	}

	public Object[] getHashColumnsValues() {
		return hashColumnsValues;
	}

	public void setHashColumnsValues(Object[] hashColumnsValues) {
		this.hashColumnsValues = hashColumnsValues;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String generateNewSql() {
		StatementDeParser deParser = new StatementDeParser(new StringBuilder());
		statement.accept(deParser);
		return deParser.getBuffer().toString();
	}

	@Override
	public String toString() {
		return "JdbcShardingInfo [sql=" + sql + ", statementType="
				+ statementType + ", schema=" + schema + ", tableName="
				+ tableName + ", configHashColumns="
				+ Arrays.toString(configHashColumns) + ", shardTableName="
				+ shardTableName + ", hashColumns="
				+ Arrays.toString(hashColumns) + ", hashColumnsValues="
				+ Arrays.toString(hashColumnsValues) + ", table=" + table
				+ ", statement=" + statement + "]";
	}

 
}
