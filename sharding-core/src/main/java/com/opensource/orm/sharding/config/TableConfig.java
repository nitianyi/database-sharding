/**
 * 
 */
package com.opensource.orm.sharding.config;

import java.util.Arrays;
import java.util.List;

/**
 * @author luolishu
 * 
 */
public class TableConfig {
	String name;
	String[] columns;
	String[] tables;
	String script;
	String generator;
	List<TableDatabaseConfig> databaseConfigs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public String[] getTables() {
		return this.tables;
	}

	public void setTables(String[] tables) {
		this.tables = tables;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public List<TableDatabaseConfig> getDatabaseConfigs() {
		return databaseConfigs;
	}

	public void setDatabaseConfigs(List<TableDatabaseConfig> databaseConfigs) {
		this.databaseConfigs = databaseConfigs;
	}

	@Override
	public String toString() {
		return "TableConfig [name=" + name + ", columns="
				+ Arrays.toString(columns) + ", tables="
				+ Arrays.toString(tables) + ", script=" + script
				+ ", generator=" + generator + ", databaseConfigs="
				+ databaseConfigs + "]";
	}

	 
}
