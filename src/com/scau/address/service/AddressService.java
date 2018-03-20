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
 * �û������ࣺ
 * 1.������ϵ��
 * @author Administrator
 *
 */
public class AddressService {
    
	/*������������������ѯ��ϵ��*/
	public static List<AddressBean> search(String text,List<AddressBean> beans) {
		List<AddressBean> list = new ArrayList<AddressBean>();//��ѯ�����
		
		if(text == null || text.trim().isEmpty()) {
			return null;//�������Ϊ�գ�����null
		}
		
		/*���������� ��ƥ���������ж�Ҫ�Ժ���������������*/
		//��������Ϊ��������ʱ
		Pattern pt1 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
		Matcher mt1 = pt1.matcher(text);
		if(mt1.find()) { 
			for(AddressBean ab:beans) {//ѭ����ѯ
				if(ab.getName().contains(text)) {
					list.add(ab);
				}
			}
		}
		
		//��������Ϊ�ֻ������绰ʱ
		Pattern pt2 = Pattern.compile("^[0-9]*$");//��������Ϊ�绰�����ֻ�
		Matcher mt2 = pt2.matcher(text);
		if(mt2.find()) { 
			for(AddressBean ab:beans) {//ѭ����ѯ
				
				if(ab.getMobilephone().contains(text) || ab.getTelephone().contains(text)){
					list.add(ab);
				}
			}
		}
		
		//��������Ϊƴ������ƴ����дʱ
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
		  AddressBean bean = new AddressBean(); bean.setName("����");
		 bean.setTelephone("0663-3133456"); bean.setMobilephone("15819610734");
		 bean.setEmail("chen@163.com"); bean.setAddress(";;��ɽ·;����;�㶫;515555;�й�");
		 bean.setWorkplace("�����й�"); bean.setGroup("����"); bean.setPostcode("a");
		 bean.setRemarks("e"); bean.setBirthday("2000-11-11"); list.add(bean);
		 System.out.println(search("ziyan", list));*/
		
		List<AddressBean> list = new ArrayList<>();
		for(int i=0;i<10;i++) {
			AddressBean ab = new AddressBean();
			list.add(ab);
		}
		
		list.get(0).setName("����");
		list.get(1).setName("��һ");
		list.get(2).setName("�Ŵ��");
		list.get(3).setName("С��");
		list.get(4).setName("����");
		list.get(5).setName("����");
		list.get(6).setName("����");
		list.get(7).setName("�¼���");
		list.get(8).setName("�±�");
		list.get(9).setName("����");
		
		
		
		for(AddressBean b:search("135",list)) {
			System.out.println(b);
		}
		
	}
}
