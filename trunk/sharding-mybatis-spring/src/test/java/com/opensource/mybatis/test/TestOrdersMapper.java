package com.opensource.mybatis.test;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensource.mybatis.sharding.domain.Customer;
import com.opensource.mybatis.sharding.domain.Orders;
import com.opensource.mybatis.sharding.mapper.OrdersMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-main.xml" })
public class TestOrdersMapper {
	@Resource
	OrdersMapper ordersMapper;

	@Test
	public void testInsert() {
		long time=System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			Orders order = new Orders();
			order.setName("Hello?"+i);
			order.setTrxTime(new Date());
			order.setAddress("dafdaslfasjdlfaskj");
			order.setCreateTime(new Date());
			ordersMapper.insert(order);
			/*System.out.println(customer);*/
		}
		System.out.println("spent time=:"+(System.currentTimeMillis()-time));
	}

	@Test
	public void testUpdate() {
		Orders order = new Orders();
		
		order.setId(1L);
		order.setName("Hello?0");
		order.setAddress("----------");
		order.setCreateTime(new Date());
		int count=ordersMapper.update(order);
		System.out.println(count);
		order.setId(2L);
		order.setName("Hello22?6699cccccccccc");
		order.setAddress("dafdaslfasjdlfaskjcccccccccc");
		order.setCreateTime(new Date());
		count=ordersMapper.update(order);
		System.out.println(count);
	}

	@Test
	public void testFindById() {
		Orders order = ordersMapper.findById(2L);
		System.out.println(order.getName());
	}

	@Test
	public void testListByParam() {
		List<Orders> userList = ordersMapper.listByParam("1");
		System.out.println("query result size:" + userList.size());
	}

	@Test
	public void testOut() {
	}
}
