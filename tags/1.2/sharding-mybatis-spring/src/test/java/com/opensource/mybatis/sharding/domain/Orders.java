/**
 * 
 */
package com.opensource.mybatis.sharding.domain;

import java.util.Date;

/**
 * @author luolishu
 * 
 */
public class Orders {

	private Long id;
	private Date createTime;
	private Date trxTime;
	private String name;
	private String address;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
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
		return "Orders [id=" + id + ", createTime=" + createTime + ", trxTime="
				+ trxTime + ", name=" + name + ", address=" + address + "]";
	}

	 

	 

}
