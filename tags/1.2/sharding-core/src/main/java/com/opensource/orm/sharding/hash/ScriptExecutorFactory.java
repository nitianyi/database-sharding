/**
 * 
 */
package com.opensource.orm.sharding.hash;



/**
 * @author luolishu
 *
 */
public interface ScriptExecutorFactory {
 
	
	public ScriptExecutor create(String language);
}
