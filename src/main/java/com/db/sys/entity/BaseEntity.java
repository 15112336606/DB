package com.db.sys.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 共性写到父类
 * 特性写到子类
 * @author UID
 */

public class BaseEntity implements Serializable{
	private static final long serialVersionUID = -4390668451464528886L;
	private Date createTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	

}
