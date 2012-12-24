package com.opensource.orm.ibatis.sharding.executor;

import java.util.List;

import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class DefaultParametersResolver implements ParametersResolver {
	static final Object EMPTY_OBJECT[] = new Object[] {};
 
	public Object[] resolve(MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) {
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		if (parameterMappings != null) {
			MetaObject metaObject = parameterObject == null ? null
					: mappedStatement.getConfiguration().newMetaObject(
							parameterObject);
			Object parameters[] = new Object[parameterMappings.size()];
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) { // issue
																			// #448
																			// ask
																			// first
																			// for
																			// additional
																			// params
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (mappedStatement.getConfiguration()
							.getTypeHandlerRegistry()
							.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						value = metaObject == null ? null : metaObject
								.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException(
								"There was no TypeHandler found for parameter "
										+ propertyName + " of statement "
										+ mappedStatement.getId());
					}
					JdbcType jdbcType = parameterMapping.getJdbcType();
					if (value == null && jdbcType == null)
						jdbcType = mappedStatement.getConfiguration()
								.getJdbcTypeForNull();
					parameters[i] = value;
				}
			}
			return parameters;
		}
		return EMPTY_OBJECT;
	}

}
