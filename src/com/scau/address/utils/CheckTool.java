package com.scau.address.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;

/**
 * У�鹤����
 * @author Administrator
 *
 */
public class CheckTool { 
	
	/*�ϲ�����list,�޳���ȫ��ͬ������,������绰���ٶ���ͬ*/
	public static List<AddressBean> merge(List<AddressBean> total,List<AddressBean> beans,Map<String,List<AddressBean>> map){
		int flag = 0;      
		
		for(AddressBean ab:beans) { //�޳���ͬ����ϵ��
			flag = 0;
			if(!total.contains(ab)) {
				total.add(ab);
				for(String key:map.keySet()) {
					if(ab.getGroup().contains(key)) {
						map.get(key).add(ab);
						flag = 1;
					}
				}
				if(flag == 0) {
					List<AddressBean> list = new ArrayList<AddressBean>();
					list.add(ab);
					map.put(ab.getGroup(), list);
				}
			}
		}
		return total;
	}
	
	/*У���ַ����Ƿ����������ʽ*/
	public static boolean isMatched(String regEx,String str) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		boolean rs = matcher.matches();
		return rs;
	}
}
