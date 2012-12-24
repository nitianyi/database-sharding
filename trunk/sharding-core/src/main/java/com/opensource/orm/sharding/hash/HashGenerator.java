package com.opensource.orm.sharding.hash;


/**
 * @author luolishu
 * 
 */
public interface HashGenerator {

	public String generateTableName(String tableName, String[] hashColumns,
			Object[] hashValues);
 

}
