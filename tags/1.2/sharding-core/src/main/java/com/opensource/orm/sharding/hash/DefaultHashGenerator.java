/**
 * 
 */
package com.opensource.orm.sharding.hash;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableConfig;
import com.opensource.orm.sharding.utils.HashUtils;

/**
 * @author luolishu
 *
 */
public class DefaultHashGenerator implements HashGenerator {

 
	public String generateTableName(ShardContext context) {
		String tableName=context.tableName;
		String[] hashColumns=context.columns;
		Object[] hashValues=context.values;
		ConfigurationManager configurationManager=DefaultConfigurationManager.getInstance();
		TableConfig tableConfig=configurationManager.getTableConfig(tableName);
		String []tables=tableConfig.getTables();
		int len=tables.length;
		long hash=HashUtils.hash(hashValues);
		int i=(int) (hash%len);
		return tables[i];
	}
	 

}
