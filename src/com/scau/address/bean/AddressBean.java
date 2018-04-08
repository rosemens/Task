package com.scau.address.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 * 联系人的模型类：没有照片
 * 
 * @author Administrator
 *
 */
public class AddressBean {
	private SimpleStringProperty name = new SimpleStringProperty();// 联系人姓名
	private SimpleStringProperty telephone = new SimpleStringProperty();// 联系人电话
	private SimpleStringProperty mobilephone = new SimpleStringProperty();// 手机号码
	private SimpleStringProperty email = new SimpleStringProperty();// 电子邮箱
	private SimpleStringProperty birthday = new SimpleStringProperty();// 生日
	private SimpleStringProperty index = new SimpleStringProperty();//个人主页
	private SimpleStringProperty workplace = new SimpleStringProperty();// 工作单位
	private SimpleStringProperty address = new SimpleStringProperty();// 家庭住址
	private SimpleStringProperty postcode = new SimpleStringProperty();// 邮编
	private SimpleStringProperty group = new SimpleStringProperty();// 所属组
	private SimpleStringProperty remarks = new SimpleStringProperty();// 备注
	private CheckBox cb = new CheckBox();

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);;
	}

	public String getTelephone() {
		return telephone.get();
	}

	public void setTelephone(String telephone) {
		this.telephone.set(telephone);
	}

	public String getMobilephone() {
		return mobilephone.get();
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone.set(mobilephone);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getBirthday() {
		return birthday.get();
	}

	public void setBirthday(String birthday) {
		this.birthday.set(birthday);
	}

	public String getIndex() {
		return index.get();
	}

	public void setIndex(String index) {
		this.index.set(index);
	}

	public String getWorkplace() {
		return workplace.get();
	}

	public void setWorkplace(String workplace) {
		this.workplace.set(workplace);
	}

	public String getAddress() {
		return address.get();
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getPostcode() {
		return postcode.get();
	}

	public void setPostcode(String postcode) {
		this.postcode.set(postcode);
	}

	public String getGroup() {
		return group.get();
	}

	public void setGroup(String group) {
		this.group.set(group);
	}

	public String getRemarks() {
		return remarks.get();
	}

	public void setRemarks(String remarks) {
		this.remarks.set(remarks);
	}

	public CheckBox getCb() {
		return cb;
	}

	public void setCb(CheckBox cb) {
		this.cb = cb;
	}

	@Override
	public String toString() {
		return "AddressBean [name=" + name.get() + ", telephone=" + telephone.get() + ", mobilephone=" + mobilephone.get() + ", email="
				+ email.get() + ", birthday=" + birthday.get() + ", index=" + index.get() + ", workplace=" + workplace.get() + ", address="
				+ address.get() + ", postcode=" + postcode.get() + ", group=" + group.get() + ", remarks=" + remarks.get() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getClass() != obj.getClass())
			return false;
		AddressBean ab = (AddressBean)obj;
		if(!this.getEmail().equals(ab.getEmail()))
			return false;
		if(!this.getMobilephone().equals(ab.getMobilephone()))
			return false;
		return true;
	}
	
	
	
}
