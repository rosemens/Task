package com.scau.address.utils;

import com.scau.address.bean.AddressBean;

/**
 * 工具类: 1.封装数据到AddressBean对象
 * 
 * @author Administrator
 *
 */
public class AddressBeanTool {
	/** 将所得数据封装成AddressBean对象 */
	public static AddressBean toBean(String[] item) {
		if (item.length != 0) {
			AddressBean bean = new AddressBean();
			if (item[0] != null)
				bean.setName(item[0]);
			else
				bean.setName("");
			if (item[1] != null)
				bean.setTelephone(item[1]);
			else
				bean.setTelephone("");
			if (item[2] != null)
				bean.setMobilephone(item[2]);
			else
				bean.setMobilephone("");
			if (item[3] != null)
				bean.setEmail(item[3]);
			else
				bean.setEmail("");
			if (item[4] != null)
				bean.setBirthday(item[4]);
			else
				bean.setBirthday("");
			if (item[5] != null)
				bean.setIndex(item[5]);
			else
				bean.setIndex("");
			if (item[6] != null)
				bean.setWorkplace(item[6]);
			else
				bean.setWorkplace("");
			if (item[7] != null)
				bean.setAddress(item[7]);
			else
				bean.setAddress("");
			if (item[8] != null)
				bean.setPostcode(item[8]);
			else
				bean.setPostcode("");
			if (item[9] != null)
				bean.setGroup(item[9]);
			else
				bean.setGroup("");
			if (item[10] != null)
				bean.setRemarks(item[10]);
			else
				bean.setRemarks("");
			return bean;
		}
		return null;
	}

}
