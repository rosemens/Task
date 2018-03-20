package com.scau.address.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.scau.address.bean.AddressBean;
import com.scau.address.convertpy.tools.ConvertTool;

/**
 * 用户功能类：
 * 1.搜索联系人
 * @author Administrator
 *
 */
public class AddressService {
    
	/*根据搜索框内容来查询联系人*/
	public static List<AddressBean> search(String text,List<AddressBean> beans) {
		List<AddressBean> list = new ArrayList<AddressBean>();//查询结果集
		
		if(text == null || text.trim().isEmpty()) {
			return null;//如果条件为空，返回null
		}
		
		/*利用正则表达 来匹配条件，判断要以何种条件进行搜索*/
		//输入条件为中文名字时
		Pattern pt1 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
		Matcher mt1 = pt1.matcher(text);
		if(mt1.find()) { 
			for(AddressBean ab:beans) {//循环查询
				if(ab.getName().contains(text)) {
					list.add(ab);
				}
			}
		}
		
		//输入条件为手机号码或电话时
		Pattern pt2 = Pattern.compile("^[0-9]*$");//输入条件为电话或者手机
		Matcher mt2 = pt2.matcher(text);
		if(mt2.find()) { 
			for(AddressBean ab:beans) {//循环查询
				
				if(ab.getMobilephone().contains(text) || ab.getTelephone().contains(text)){
					list.add(ab);
				}
			}
		}
		
		//输入条件为拼音或者拼音缩写时
		Pattern pt3 = Pattern.compile("^[A-Za-z]+$");
		Matcher mt3 = pt3.matcher(text);
		if(mt3.find()) {
			for(AddressBean ab:beans) {
				if(ConvertTool.getPingYin(ab.getName()).equals(text)
				   || ConvertTool.getPinYinHeadChar(ab.getName()).equals(text))
			     list.add(ab);
			}
		}
	   
		return list;
	}
	
	@Test
	public void test(){
		/*List<AddressBean> list = new ArrayList<>();
		  AddressBean bean = new AddressBean(); bean.setName("子言");
		 bean.setTelephone("0663-3133456"); bean.setMobilephone("15819610734");
		 bean.setEmail("chen@163.com"); bean.setAddress(";;五山路;揭阳;广东;515555;中国");
		 bean.setWorkplace("广州中国"); bean.setGroup("亲人"); bean.setPostcode("a");
		 bean.setRemarks("e"); bean.setBirthday("2000-11-11"); list.add(bean);
		 System.out.println(search("ziyan", list));*/
		
		List<AddressBean> list = new ArrayList<>();
		for(int i=0;i<10;i++) {
			AddressBean ab = new AddressBean();
			list.add(ab);
		}
		
		list.get(0).setName("张天");
		list.get(1).setName("张一");
		list.get(2).setName("张大大");
		list.get(3).setName("小张");
		list.get(4).setName("王五");
		list.get(5).setName("李折");
		list.get(6).setName("李四");
		list.get(7).setName("陈佳乐");
		list.get(8).setName("陈冰");
		list.get(9).setName("王子");
		
		
		
		for(AddressBean b:search("135",list)) {
			System.out.println(b);
		}
		
	}
}
