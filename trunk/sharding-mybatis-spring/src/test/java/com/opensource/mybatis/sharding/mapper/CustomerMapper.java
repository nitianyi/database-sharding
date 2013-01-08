/**
 * 
 */
package com.opensource.mybatis.sharding.mapper;

import java.util.List;

import com.opensource.mybatis.sharding.domain.Customer;

/**
 * @author luolishu
 * 
 */
public interface CustomerMapper {
	Customer findById(Long id);

	Customer findByName(String name);

	public void insert(Customer user);

	public int update(Customer user);

	List<Customer> listByParam(String name);

	List<Customer> listById(Long id);
}
