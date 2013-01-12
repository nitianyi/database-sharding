/**
 * 
 */
package com.opensource.orm.ibatis.sharding.executor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author luolishu
 * 
 */
public class ExecutorContext {
	private static final ThreadLocal<ExecutorContext> threadLocal = new ThreadLocal<ExecutorContext>();
	MappedStatement mappedStatement;
	Object parameter;
	Executor executor;
	

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	protected ExecutorContext() {
	}

	public static void setContext(ExecutorContext context) {
		threadLocal.set(context);
	}

	public static ExecutorContext getContext() {
		return threadLocal.get();
	}

	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}

	public void setMappedStatement(MappedStatement mappedStatement) {
		this.mappedStatement = mappedStatement;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
}
