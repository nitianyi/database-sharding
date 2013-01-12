/**
 * 
 */
package com.opensource.orm.sharding.datasource;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.DataSourceConfig;

/**
 * @author luolishu
 *
 */
public class C3p0DataSourceFactory implements DataSourceFactory {

	 
	public DataSource create(DataSourceConfig config) {
		if (config.getRef() != null && config.getRef().trim().length() > 0) {
			return null;
		}
		return null;
	}

}
