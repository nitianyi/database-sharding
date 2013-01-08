/**
 * 
 */
package com.opensource.orm.sharding.hash;

import java.util.HashMap;
import java.util.Map;

import com.opensource.orm.sharding.config.ConfigurationManager;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;
import com.opensource.orm.sharding.config.TableConfig;

/**
 * @author luolishu
 * 
 */
public class ScriptHashGenerator implements HashGenerator {
	final ScriptExecutor scriptExecutor = new JavascriptScriptExecutor();

	public String generateTableName(String tableName, String[] hashColumns,
			Object[] hashValues) {
		ConfigurationManager configurationManager = DefaultConfigurationManager
				.getInstance();
		TableConfig tableConfig = configurationManager
				.getTableConfig(tableName);
		Map<String, Object> context = new HashMap<String, Object>();
		for (int i = 0; i < hashColumns.length; i++) {
			context.put(hashColumns[i], hashValues[i]);
		}
		Object value = scriptExecutor.execute(tableConfig.getScript(), context);
		if (value != null) {
			return value.toString();
		}
		return null;

	}

}
