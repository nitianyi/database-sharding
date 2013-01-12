/**
 * 
 */
package com.opensource.orm.sharding.hash;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.orm.sharding.utils.StringUtils;
 

/**
 * @author luolishu
 * 
 */
public class JavascriptScriptExecutor implements ScriptExecutor {
	static Logger logger = LoggerFactory
			.getLogger(JavascriptScriptExecutor.class);
	private static final ThreadLocal<ScriptEngine> threadLocal = new ThreadLocal<ScriptEngine>();

	ScriptEngine getEngine() {
		ScriptEngine scriptEngine = threadLocal.get();
		if (scriptEngine != null) {
			return scriptEngine;
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		scriptEngine = manager.getEngineByName("javascript");
		threadLocal.set(scriptEngine);
		return scriptEngine;
	} 

	@Override
	public Object execute(String script, 
			Map<String, Object> context) {
		if (StringUtils.isBlank(script)) {
			return null;
		}
		ScriptEngine engine = getEngine();
		if(context!=null){
		for(Map.Entry<String, Object> entry:context.entrySet()){
			engine.put(entry.getKey(), entry.getValue()); 
		}
		} 
		Object value = null;
		try {
			value = engine.eval(script);
		} catch (ScriptException e) {
			logger.error("execute script error!", e);
		}
		return value;
	}

}
