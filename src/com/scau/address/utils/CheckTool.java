package com.scau.address.utils;

import java.util.List;

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
}
