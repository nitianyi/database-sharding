package com.opensource.mybatis.test;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensource.mybatis.sharding.domain.Customer;
import com.opensource.mybatis.sharding.mapper.CustomerMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis-bug.xml" })
public class TestCustomerMapper2 {
	@Resource
	CustomerMapper customerMapper;

	@Test
	public void testInsert() {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			Customer customer = new Customer();
			customer.setName("Hello?" + i);
			customer.setAddress("dafdaslfasjdlfaskj");
			customer.setCreateTime(new Date());
			customerMapper.insert(customer);
			/* System.out.println(customer); */
		}
		System.out
				.println("spent time=:" + (System.currentTimeMillis() - time));
	}

	@Test
	public void testUpdate() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("Hello?0");
		customer.setAddress("----------");
		customer.setCreateTime(new Date());
		int count = customerMapper.update(customer);
		System.out.println(count);
		customer.setId(2L);
		customer.setName("Hello22?6699cccccccccc");
		customer.setAddress("dafdaslfasjdlfaskjcccccccccc");
		customer.setCreateTime(new Date());
		count = customerMapper.update(customer);
		System.out.println(count);
	}

	@Test
	public void testFindById() {
		Customer customer = customerMapper.findById(2L);
		System.out.println(customer);
	}
	@Test
	public void testFindByName() {
		Customer customer = customerMapper.findByName("Hello?0");
		System.out.println(customer);
	}

	@Test
	public void testListById() {
		List<Customer> customerList = customerMapper.listById(999L);
		for (Customer customer : customerList)
			System.out.println(customer);
	}

	@Test
	public void testListByParam() {
		List<Customer> userList = customerMapper.listByParam("1");
		System.out.println("query result size:" + userList.size());
	}

	@Test
	public void testOut() {
	}
}
