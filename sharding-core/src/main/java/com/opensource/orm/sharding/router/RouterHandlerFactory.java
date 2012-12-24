/**
 * 
 */
package com.opensource.orm.sharding.router;

import java.util.HashMap;
import java.util.Map;

import com.opensource.orm.sharding.StatementType;

/**
 * @author luolishu
 * 
 */
public abstract class RouterHandlerFactory {
	static final Map<StatementType, RouterHandler> handlers = new HashMap<StatementType, RouterHandler>();
	static final RouterHandler defaultRouterHandler = new DefaultRouterHandler();
	static{
		handlers.put(StatementType.INSERT, new InsertRouterHandler());
		handlers.put(StatementType.UPDATE, new UpdateRouterHandler());
		handlers.put(StatementType.SELECT, new SelectRouterHandler());
		handlers.put(StatementType.DELETE, new DeleteRouterHandler());
		
	}
	public static RouterHandler create(StatementType statementType) {
		RouterHandler handler = handlers.get(statementType);
		if (handler == null) {
			handler = defaultRouterHandler;
		}
		return handler;
	}
}
