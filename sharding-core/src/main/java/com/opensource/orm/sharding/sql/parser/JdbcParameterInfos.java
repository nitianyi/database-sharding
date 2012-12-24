package com.opensource.orm.sharding.sql.parser;

import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

public class JdbcParameterInfos {
	private List<JdbcParameter> jdbcParameters;
	private List<EqualsTo> equalsToList;
	Map<JdbcParameter, EqualsTo> equalsMap;
	Map<JdbcParameter,Integer> indexMap;
	public List<JdbcParameter> getJdbcParameters() {
		return jdbcParameters;
	}
	public void setJdbcParameters(List<JdbcParameter> jdbcParameters) {
		this.jdbcParameters = jdbcParameters;
	}
	public List<EqualsTo> getEqualsToList() {
		return equalsToList;
	}
	public void setEqualsToList(List<EqualsTo> equalsToList) {
		this.equalsToList = equalsToList;
	}
	public Map<JdbcParameter, EqualsTo> getEqualsMap() {
		return equalsMap;
	}
	public void setEqualsMap(Map<JdbcParameter, EqualsTo> equalsMap) {
		this.equalsMap = equalsMap;
	}
	public Map<JdbcParameter, Integer> getIndexMap() {
		return indexMap;
	}
	public void setIndexMap(Map<JdbcParameter, Integer> indexMap) {
		this.indexMap = indexMap;
	}
	
}
