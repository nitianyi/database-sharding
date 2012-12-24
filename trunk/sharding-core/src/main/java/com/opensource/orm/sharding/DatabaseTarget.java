/**
 * 
 */
package com.opensource.orm.sharding;

import javax.sql.DataSource;

/**
 * @author luolishu
 * 
 */
public interface DatabaseTarget {
	DataSource getDataSource();

	String getSql();

	Object[] getParameters();
	
	StatementType getStatmentType();

}
