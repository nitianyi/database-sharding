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
public interface DataSourceFactory {
	DataSource create(DataSourceConfig config)throws Exception ;
}
