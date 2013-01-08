/**
 * 
 */
package com.opensource.orm.sharding.hash;

/**
 * @author luolishu
 * 
 */
public class DefaultScriptExecutorFactory implements ScriptExecutorFactory {

	@Override
	public ScriptExecutor create(String language) {
		return new JavascriptScriptExecutor();
	}

}
