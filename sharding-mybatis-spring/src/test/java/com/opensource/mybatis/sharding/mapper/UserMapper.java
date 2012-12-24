/**
 * 
 */
package com.opensource.mybatis.sharding.mapper;

import java.util.List;

import com.opensource.mybatis.sharding.domain.User;

/**
 * @author luolishu
 * 
 */
public interface UserMapper {
	User findById(Long id);

	public void insert(User user);
	
	public int update(User user);
	
	List<User> listByParam(String name);
}
