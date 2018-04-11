package com.scau.address.utils;

import java.util.List;
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
	public static List<AddressBean> merge(List<AddressBean> total,List<AddressBean> beans){
		for(AddressBean ab:beans) { //�޳���ͬ����ϵ��
			if(!total.contains(ab))
				total.add(ab);
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
