/**
 * 
 */
package com.opensource.mybatis.sharding.generator;

import com.opensource.orm.sharding.hash.HashGenerator;
import com.opensource.orm.sharding.hash.ShardContext;

/**
 * @author luolishu
 * 
 */
public class CustomGenerator implements HashGenerator {
 
	@Override
	public String generateTableName(ShardContext hashInfo) {
		String tableName=hashInfo.getTableName();
		String[] hashColumns=hashInfo.getColumns();
		Object[] hashValues=hashInfo.getValues();
		return "customer5";
	}

}
