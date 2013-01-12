/**
 * 
 */
package com.opensource.mybatis.sharding.domain;

import java.util.Date;

/**
 * @author luolishu
 * 
 */
public class Customer {

	private Long id;
	private Date createTime;
	private String name;
	private String address;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", createTime=" + createTime + ", name="
				+ name + ", address=" + address + "]";
	}

	 

}
