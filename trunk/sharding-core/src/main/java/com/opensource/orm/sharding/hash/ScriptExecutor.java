/**
 * 
 */
package com.opensource.orm.sharding.hash;

import java.util.Map;

/**
 * @author luolishu
 *
 */
public interface ScriptExecutor  {
    /**
     * 执行脚本
     * @param script
     * @param context
     * @return
     */
	public Object execute(String script,Map<String, Object> context);
	
	 
}
