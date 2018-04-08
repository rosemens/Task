package com.scau.address.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 * ��ϵ�˵�ģ���ࣺû����Ƭ
 * 
 * @author Administrator
 *
 */
public class AddressBean {
	private SimpleStringProperty name = new SimpleStringProperty();// ��ϵ������
	private SimpleStringProperty telephone = new SimpleStringProperty();// ��ϵ�˵绰
	private SimpleStringProperty mobilephone = new SimpleStringProperty();// �ֻ�����
	private SimpleStringProperty email = new SimpleStringProperty();// ��������
	private SimpleStringProperty birthday = new SimpleStringProperty();// ����
	private SimpleStringProperty index = new SimpleStringProperty();//������ҳ
	private SimpleStringProperty workplace = new SimpleStringProperty();// ������λ
	private SimpleStringProperty address = new SimpleStringProperty();// ��ͥסַ
	private SimpleStringProperty postcode = new SimpleStringProperty();// �ʱ�
	private SimpleStringProperty group = new SimpleStringProperty();// ������
	private SimpleStringProperty remarks = new SimpleStringProperty();// ��ע
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
