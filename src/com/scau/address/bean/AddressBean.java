package com.scau.address.bean;


/**
 * ��ϵ�˵�ģ����
 * 
 * @author Administrator
 *
 */
public class AddressBean {
	private String name;// ��ϵ������
	private String telephone;// ��ϵ�˵绰
	private String mobilephone;// �ֻ�����
	private String email;// ��������
	private String birthday;// ����
	private String index;//������ҳ
	private String workplace;// ������λ
	private String address;// ��ͥסַ
	private String postcode;// �ʱ�
	private String group;// ������
	private String remarks;// ��ע

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
