package com.opensource.orm.sharding.sql.parser;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.ValueSupport;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import com.opensource.orm.sharding.ShardingNotSupportException;
import com.opensource.orm.sharding.StatementType;
import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableConfig;
import com.opensource.orm.sharding.info.JdbcShardingInfo;
import com.opensource.orm.sharding.info.ShardingInfo;

public class DefaultShardingSqlParser implements ShardingSqlParser {
	ValueResolver valueResolver = new DefaultValueResolver();
	static final CCJSqlParserManager parserManager = new CCJSqlParserManager();

	private ConfigurationManager getConfigurationManager() {
		return DefaultConfigurationManager.getInstance();
	}

	public ShardingInfo parse(String sql, Object... parameters)
			throws ShardingNotSupportException {
		if (sql == null) {
			return null;
		}
		ShardingInfo shardInfo = null;
		try {
			Statement statement = parserManager.parse(new StringReader(sql));
			if (statement instanceof Insert) {
				shardInfo = this.parse(sql, (Insert) statement, parameters);
			}
			if (statement instanceof Delete) {
				shardInfo = this.parse(sql, (Delete) statement, parameters);
			}
			if (statement instanceof Select) {
				shardInfo = this.parse(sql, (Select) statement, parameters);
			}
			if (statement instanceof Update) {
				shardInfo = this.parse(sql, (Update) statement, parameters);
			}
		} catch (Exception e) {
			throw new ShardingNotSupportException("parse error!", e);
		}

		return shardInfo;
	}

	public String[] findColumns(Insert insertSmt) {
		String[] columns = null;
		if (insertSmt.getColumns() != null && insertSmt.getColumns().size() > 0) {
			columns = new String[insertSmt.getColumns().size()];
			int i = 0;
			for (Column column : insertSmt.getColumns()) {
				columns[i] = insertSmt.getColumns().get(i).getColumnName();
				i++;
			}
		} else {
			// TODO find columns from database
		}
		return columns;
	}

	private ShardingInfo parse(String sql, Insert insertSmt,
			Object... parameters) throws ShardingNotSupportException {
		JdbcShardingInfo shardInfo = new JdbcShardingInfo();
		Table table = insertSmt.getTable();
		String tableName = table.getName();
		String tableColumns[] = findColumns(insertSmt);
		TableConfig tableShardingConfig = getConfigurationManager()
				.getTableConfig(tableName);
		if (tableShardingConfig == null) {
			throw new ShardingNotSupportException(
					"configuration for table name=" + tableName
							+ " is not exist!\n sql=" + sql);
		}
		shardInfo.setStatement(insertSmt);
		shardInfo.setTable(table);
		shardInfo.setSql(sql);
		shardInfo.setConfigHashColumns(tableShardingConfig.getColumns());
		shardInfo.setTableName(tableName);
		shardInfo.setSchema(table.getSchemaName());
		shardInfo.setStatementType(StatementType.INSERT);
		Set<String> hashColumns = new HashSet<String>(
				Arrays.asList(tableShardingConfig.getColumns()));
		List<String> usedColumnList = new LinkedList<String>();
		List<Object> usedParameterList = new LinkedList<Object>();
		for (int i = 0; i < tableColumns.length; i++) {
			String item = tableColumns[i];
			if (hashColumns.contains(item)) {
				usedColumnList.add(item);
				usedParameterList.add(parameters[i]);
			}
		}
		shardInfo.setHashColumns(usedColumnList.toArray(new String[] {}));
		shardInfo.setHashColumnsValues(usedParameterList
				.toArray(new Object[] {}));
		return shardInfo;
	}

	private ShardingInfo parse(String sql, Delete deleteSmt,
			Object... parameters) throws ShardingNotSupportException {
		JdbcShardingInfo shardInfo = new JdbcShardingInfo();
		Table table = deleteSmt.getTable();
		String tableName = table.getName();
		TableConfig tableShardingConfig = getConfigurationManager()
				.getTableConfig(tableName);
		if (tableShardingConfig == null) {
			throw new ShardingNotSupportException(
					"configuration for table name=" + tableName
							+ " is not exist!\n sql=" + sql);
		}
		shardInfo.setStatement(deleteSmt);
		shardInfo.setTable(table);
		shardInfo.setSql(sql);
		shardInfo.setConfigHashColumns(tableShardingConfig.getColumns());
		shardInfo.setTableName(tableName);
		shardInfo.setSchema(table.getSchemaName());
		shardInfo.setStatementType(StatementType.DELETE);

		Expression where = deleteSmt.getWhere();
		if (where == null) {
			return shardInfo;
		}
		List<ColumnInfo> columnsInfos = new LinkedList<ColumnInfo>();
		this.parseWhere(
				(BinaryExpression) where,
				columnsInfos,
				new HashSet<String>(Arrays.asList(shardInfo
						.getConfigHashColumns())));
		String[] shardColumns = new String[columnsInfos.size()];
		Object[] shardParameters = new Object[columnsInfos.size()];
		JdbcParameterFinder jdbcParametersFinder = new JdbcParameterFinder();
		JdbcParameterInfos infos = jdbcParametersFinder
				.getJdbcParameters(deleteSmt);
		for (int i = 0; i < columnsInfos.size(); i++) {
			ColumnInfo item = columnsInfos.get(i);
			shardColumns[i] = item.name;
			shardParameters[i] = valueResolver.resolveValue(infos, item,
					parameters);
		}
		shardInfo.setHashColumns(shardColumns);
		shardInfo.setHashColumnsValues(shardParameters);
		return shardInfo;
	}

	private ShardingInfo parse(String sql, Select selectSmt,
			Object... parameters) throws ShardingNotSupportException {
		JdbcShardingInfo shardInfo = new JdbcShardingInfo();

		PlainSelect plainSelect = (PlainSelect) selectSmt.getSelectBody();
		Table table = (Table) plainSelect.getFromItem();
		if (table == null) {
			return null;
		}
		String tableName = table.getName();
		TableConfig tableShardingConfig = getConfigurationManager()
				.getTableConfig(tableName);
		if (tableShardingConfig == null) {
			throw new ShardingNotSupportException(
					"configuration for table name=" + tableName
							+ " is not exist!\n sql=" + sql);
		}
		shardInfo.setStatement(selectSmt);
		shardInfo.setTable(table);
		shardInfo.setSql(sql);
		shardInfo.setConfigHashColumns(tableShardingConfig.getColumns());
		shardInfo.setTableName(tableName);
		shardInfo.setSchema(table.getSchemaName());
		shardInfo.setStatementType(StatementType.SELECT);
		Expression where = plainSelect.getWhere();
		if (where == null) {
			return shardInfo;
		}
		List<ColumnInfo> columnsInfos = new LinkedList<ColumnInfo>();
		this.parseWhere(
				(BinaryExpression) where,
				columnsInfos,
				new HashSet<String>(Arrays.asList(shardInfo
						.getConfigHashColumns())));
		String[] shardColumns = new String[columnsInfos.size()];
		Object[] shardParameters = new Object[columnsInfos.size()];
		JdbcParameterFinder jdbcParametersFinder = new JdbcParameterFinder();
		JdbcParameterInfos infos = jdbcParametersFinder
				.getJdbcParameters(selectSmt);
		for (int i = 0; i < columnsInfos.size(); i++) {
			ColumnInfo item = columnsInfos.get(i);
			shardColumns[i] = item.name;
			shardParameters[i] = valueResolver.resolveValue(infos, item,
					parameters);
		}
		shardInfo.setHashColumns(shardColumns);
		shardInfo.setHashColumnsValues(shardParameters);
		return shardInfo;
	}

	private ShardingInfo parse(String sql, Update updateSmt,
			Object... parameters) throws ShardingNotSupportException {
		JdbcShardingInfo shardInfo = new JdbcShardingInfo();
		Table table = updateSmt.getTable();
		String tableName = table.getName();
		TableConfig tableShardingConfig = getConfigurationManager()
				.getTableConfig(tableName);
		if (tableShardingConfig == null) {
			throw new ShardingNotSupportException(
					"configuration for table name=" + tableName
							+ " is not exist!\n sql=" + sql);
		}
		shardInfo.setStatement(updateSmt);
		shardInfo.setTable(table);
		shardInfo.setSql(sql);
		shardInfo.setConfigHashColumns(tableShardingConfig.getColumns());
		shardInfo.setTableName(tableName);
		shardInfo.setSchema(table.getSchemaName());
		shardInfo.setStatementType(StatementType.UPDATE);

		Expression where = updateSmt.getWhere();
		if (where == null) {
			return shardInfo;
		}
		List<ColumnInfo> columnsInfos = new LinkedList<ColumnInfo>();
		this.parseWhere(
				(BinaryExpression) where,
				columnsInfos,
				new HashSet<String>(Arrays.asList(shardInfo
						.getConfigHashColumns())));
		String[] shardColumns = new String[columnsInfos.size()];
		Object[] shardParameters = new Object[columnsInfos.size()];
		JdbcParameterFinder jdbcParametersFinder = new JdbcParameterFinder();
		JdbcParameterInfos infos = jdbcParametersFinder
				.getJdbcParameters(updateSmt);
		for (int i = 0; i < columnsInfos.size(); i++) {
			ColumnInfo item = columnsInfos.get(i);
			shardColumns[i] = item.name;
			shardParameters[i] = valueResolver.resolveValue(infos, item,
					parameters);
		}
		shardInfo.setHashColumns(shardColumns);
		shardInfo.setHashColumnsValues(shardParameters);
		return shardInfo;
	}

	void parseWhere(BinaryExpression where, List<ColumnInfo> columnInfos,
			Set<String> columns) {

		if (where instanceof EqualsTo) {
			EqualsTo eq = (EqualsTo) where;
			if (where.getLeftExpression() instanceof BinaryExpression) {
				this.parseWhere((BinaryExpression) where.getLeftExpression(),
						columnInfos, columns);

			}
			if (where.getRightExpression() instanceof BinaryExpression) {
				this.parseWhere((BinaryExpression) where.getRightExpression(),
						columnInfos, columns);
			}
			if (!(eq.getLeftExpression() instanceof Column)) {
				return;
			}
			if (!(eq.getRightExpression() instanceof ValueSupport)) {
				return;
			}
			Column column = (Column) eq.getLeftExpression();
			if (!columns.contains(column.getColumnName())) {
				return;
			}
			ValueSupport value = (ValueSupport) eq.getRightExpression();
			ColumnInfo info = new ColumnInfo();
			columnInfos.add(info);
			info.value = value.getValue();
			info.name = column.getColumnName();
			if (value instanceof JdbcParameter) {
				info.isJdbcParameter = true;
				info.jdbcParameter = (JdbcParameter) value;
			}
			return;
		}

		if (where.getLeftExpression() instanceof BinaryExpression) {
			this.parseWhere((BinaryExpression) where.getLeftExpression(),
					columnInfos, columns);
		}
		if (where.getRightExpression() instanceof BinaryExpression) {
			this.parseWhere((BinaryExpression) where.getRightExpression(),
					columnInfos, columns);
		}
	}

	class ColumnInfo implements Comparable<ColumnInfo> {
		String name;
		Object value;
		boolean isJdbcParameter;
		JdbcParameter jdbcParameter;

		@Override
		public String toString() {
			return "ColumnInfo [name=" + name + ", value=" + value
					+ ", isJdbcParameter=" + isJdbcParameter + "]";
		}

		public int compareTo(ColumnInfo o) {
			return name.hashCode() - o.name.hashCode();
		}

	}

	public static void main(String... args) throws ShardingNotSupportException,
			Exception {
		List<ColumnInfo> columnsInfos = new LinkedList<ColumnInfo>();
		DefaultShardingSqlParser shardingSqlParser = new DefaultShardingSqlParser();
		String sql = "select * from abc where abc=1 and bce='2' and abc=? and abc in (1,2,3)";
		PlainSelect plainSelect = (PlainSelect) ((Select) parserManager
				.parse(new StringReader(sql))).getSelectBody();
		BinaryExpression where = (BinaryExpression) plainSelect.getWhere();
		shardingSqlParser.parseWhere(where, columnsInfos,
				new HashSet(Arrays.asList(new String[] { "bce", "abc" })));
		Collections.sort(columnsInfos);
		System.out.println(columnsInfos);
	}
}
