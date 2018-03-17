package com.scau.address.bean;


/**
 * 联系人的模型类
 * 
 * @author Administrator
 *
 */
public class AddressBean {
	private String name;// 联系人姓名
	private String telephone;// 联系人电话
	private String mobilephone;// 手机号码
	private String email;// 电子邮箱
	private String birthday;// 生日
	private String index;//个人主页
	private String workplace;// 工作单位
	private String address;// 家庭住址
	private String postcode;// 邮编
	private String group;// 所属组
	private String remarks;// 备注

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "AddressBean [name=" + name + ", telephone=" + telephone + ", mobilephone=" + mobilephone + ", email="
				+ email + ", birthday=" + birthday + ", index=" + index + ", workplace=" + workplace + ", address="
				+ address + ", postcode=" + postcode + ", group=" + group + ", remarks=" + remarks + "]";
	}
	
}
