package com.scau.address.utils;

import com.scau.address.bean.AddressBean;

/**
 * 工具类:
 * 1.封装数据到AddressBean对象
 * @author Administrator
 *
 */
public class AddressBeanTool {
    /**将所得数据封装成AddressBean对象*/
	public static AddressBean toBean(String[] item) {
		if(item.length != 0) {
			AddressBean bean = new AddressBean();
			bean.setName(item[0]);
			bean.setTelephone(item[1]);
			bean.setMobilephone(item[2]);
			bean.setEmail(item[3]);
			bean.setBirthday(item[4]);
			bean.setIndex(item[5]);
			bean.setWorkplace(item[6]);
			bean.setAddress(item[7]);
			bean.setPostcode(item[8]);
			bean.setGroup(item[9]);
			bean.setRemarks(item[10]);
			return bean;
		}
		return null;
	}
    
}
