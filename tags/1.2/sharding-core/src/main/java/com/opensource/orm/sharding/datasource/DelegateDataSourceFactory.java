/**
 * 
 */
package com.opensource.orm.sharding.datasource;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.opensource.orm.sharding.config.DataSourceConfig;

/**
 * @author luolishu
 * 
 */
public class DelegateDataSourceFactory implements DataSourceFactory {

	public static final List<DataSourceFactory> factorys = new ArrayList<DataSourceFactory>();

	public DelegateDataSourceFactory() {
	}

	public DelegateDataSourceFactory(List<DataSourceFactory> factorys) {
		factorys.addAll(factorys);
	}

	public DataSource create(DataSourceConfig config) throws Exception {
		if(config.isAbstract()){
			return null;
		}
		for (DataSourceFactory factory : factorys) {
			try {
				DataSource dataSource = factory.create(config);
				if (dataSource != null) {
					return dataSource;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void addDataSourceFactory(DataSourceFactory factory) {
		factorys.add(factory);
	}

}
