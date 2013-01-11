package com.opensource.orm.ibatis.sharding.executor;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author luolishu
 * 
 */
public abstract class ShardingHelper {
	static final Map<String, Boolean> shardingMap = new HashMap<String, Boolean>();

	public static boolean isShardingSupport(MappedStatement mappedStatement) {
		if (mappedStatement.getId().endsWith("selectKey")) {
			return false;
		}
		Boolean flag = shardingMap.get(mappedStatement.getId());
		return flag == null ? true : flag.booleanValue();
	}

	public static boolean isShardingParsedSupport(
			MappedStatement mappedStatement) {
		if (mappedStatement.getId().endsWith("selectKey")) {
			return false;
		}
		Boolean flag = shardingMap.get(mappedStatement.getId());
		return flag == null ? false : flag.booleanValue();
	}

	public static void setSharding(String key, boolean flag) {
		shardingMap.put(key, flag);
	}
}
