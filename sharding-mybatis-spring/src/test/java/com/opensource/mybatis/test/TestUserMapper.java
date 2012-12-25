package com.opensource.mybatis.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensource.mybatis.sharding.domain.User;
import com.opensource.mybatis.sharding.mapper.UserMapper;
import com.opensource.orm.sharding.config.DefaultConfigurationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-main.xml" })
public class TestUserMapper {
	@Resource
	UserMapper userMapper;

	@Test
	public void testInsert() {
		User user = new User();
		user.setAge(20);
		user.setName("Hello?");
		userMapper.insert(user);
	}

	@Test
	public void testUpdate() {
	 	User user = new User();
		user.setId(1L);
		user.setAge(20);
		user.setName("Hello22?6699");
		userMapper.update(user);
		user.setId(2L);
		user.setAge(20);
		user.setName("abcHello22?77");
		userMapper.update(user);

	}

	@Test
	public void testFindById() {
	 	User user = userMapper.findById(2L);
		System.out.println(user.getName());
	}

	@Test
	public void testListByParam() {
		List<User> userList = userMapper.listByParam("abcHello22?");
		System.out.println("query result size:" + userList.size());
	}

	@Test
	public void testOut() {
	}
}
