/**
 * 
 */
package com.opensource.mybatis.sharding.generator;

import com.opensource.orm.sharding.hash.HashGenerator;

/**
 * @author luolishu
 * 
 */
public class CustomGenerator implements HashGenerator {
 
	@Override
	public String generateTableName(String tableName, String[] hashColumns,
			Object[] hashValues) {
		// TODO Auto-generated method stub
		return "customer5";
	}

}
