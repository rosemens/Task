package com.scau.address.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;
import com.scau.address.utils.ConvertTool;

/**
 * 数据操作层：文件操作
 * 
 * @author Administrator
 *
 */
public class AddressDao {
	/* 得到所有联系人 */
	public List<AddressBean> getAll() {
		return null;
	}

	/* 根据搜索框内容来查询联系人 */
	public List<AddressBean> search(String text) {
		List<AddressBean> beans = getAll();
		List<AddressBean> list = new ArrayList<AddressBean>();// 查询结果集

		if (text == null || text.trim().isEmpty()) {
			return null;// 如果条件为空，返回null
		}
		/* 利用正则表达 来匹配条件，判断要以何种条件进行搜索 */
		// 输入条件为中文名字时
		Pattern pt1 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
		Matcher mt1 = pt1.matcher(text);
		if (mt1.find()) {
			for (AddressBean ab : beans) {// 循环查询
				if (ab.getName().contains(text)) {
					list.add(ab);
				}
			}
		}

		// 输入条件为手机号码或电话时
		Pattern pt2 = Pattern.compile("^[0-9]*$");// 输入条件为电话或者手机
		Matcher mt2 = pt2.matcher(text);
		if (mt2.find()) {
			for (AddressBean ab : beans) {// 循环查询
				if ((ab.getMobilephone() != null && !ab.getMobilephone().trim().isEmpty()
						&& ab.getMobilephone().contains(text))
						|| (ab.getTelephone() != null && !ab.getMobilephone().trim().isEmpty()
								&& ab.getTelephone().contains(text))) {
					list.add(ab);
				}
			}
		}

		// 输入条件为拼音或者拼音缩写时
		Pattern pt3 = Pattern.compile("^[A-Za-z]+$");
		Matcher mt3 = pt3.matcher(text);
		if (mt3.find()) {
			for (AddressBean ab : beans) {
				if (ConvertTool.getPingYin(ab.getName()).equals(text)
						|| ConvertTool.getPinYinHeadChar(ab.getName()).equals(text))
					list.add(ab);
			}
		}
		return list;
	}
    
	/*添加联系人*/
	public void add(AddressBean ab) {
		
	}
	
	/*删除联系人*/
	public void delete(AddressBean ab) {
		
	}
	
	/*修改联系人*/
	public void modify() {
		
	}

}
