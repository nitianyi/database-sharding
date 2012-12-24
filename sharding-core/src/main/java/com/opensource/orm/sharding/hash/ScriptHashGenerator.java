/**
 * 
 */
package com.opensource.orm.sharding.hash;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;

/**
 * @author luolishu
 *
 */
public class ScriptHashGenerator implements HashGenerator {

	/* (non-Javadoc)
	 * @see com.jd.orm.sharding.hash.HashGenerator#generateTableName(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	public String generateTableName(String tableName, String[] hashColumns,
			Object[] hashValues) {
		ConfigurationManager configurationManager=DefaultConfigurationManager.getInstance();
		return null;
	}

}
