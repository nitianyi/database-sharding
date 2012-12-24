/**
 * 
 */
package com.opensource.orm.sharding.router;

import java.util.Arrays;

import javax.sql.DataSource;

import com.opensource.orm.sharding.DatabaseTarget;
import com.opensource.orm.sharding.StatementType;

/**
 * @author luolishu
 * 
 */
public class DefaultDatabaseTarget implements DatabaseTarget {
	DataSource datasource;
	String sql;
	StatementType statementType;
	Object[] parameters;

	public DataSource getDataSource() {
		return datasource;
	}

	public String getSql() {
		return sql;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public StatementType getStatmentType() {
		return statementType;
	}

	@Override
	public String toString() {
		return "DefaultDatabaseTarget [datasource=" + datasource + ", sql="
				+ sql + ", statementType=" + statementType + ", parameters="
				+ Arrays.toString(parameters) + "]";
	}

}
