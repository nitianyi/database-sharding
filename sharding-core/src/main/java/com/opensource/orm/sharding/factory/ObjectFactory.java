package com.opensource.orm.sharding.factory;

public interface ObjectFactory {
	
	public Object getObject(String id);
	public <T>T getObject(String id,Class<T> claz);
}
