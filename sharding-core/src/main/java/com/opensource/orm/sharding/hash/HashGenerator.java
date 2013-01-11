package com.opensource.orm.sharding.hash;


/**
 * @author luolishu
 * 
 */
public interface HashGenerator {

	public String generateTableName(ShardContext context);
 

}
