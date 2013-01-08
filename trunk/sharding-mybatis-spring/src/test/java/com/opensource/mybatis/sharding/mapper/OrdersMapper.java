/**
 * 
 */
package com.opensource.mybatis.sharding.mapper;

import java.util.List;

import com.opensource.mybatis.sharding.domain.Orders;

/**
 * @author luolishu
 * 
 */
public interface OrdersMapper {
	Orders findById(Long id);

	public void insert(Orders order);

	public int update(Orders order);

	List<Orders> listByParam(String name);
}
