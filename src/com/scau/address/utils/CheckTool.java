package com.scau.address.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;

/**
 * У�鹤����
 * 
 * @author Administrator
 *
 */
public class CheckTool {

	/* �ϲ�����list,�޳���ȫ��ͬ������,������绰���ٶ���ͬ */
	public static List<AddressBean> merge(List<AddressBean> total, List<AddressBean> beans,
			Map<String, List<AddressBean>> map, List<String> group) {
		for (AddressBean bean : beans) {
			if (total.contains(bean)) // �޳���ͬ��ϵ��
				continue;
			else total.add(bean);
			if (bean.getGroup().trim().isEmpty()) // ��ϵ�˲������飬����ϵ�˼ӵ�δ����
				map.get("δ����").add(bean);
			else {
				String[] items = bean.getGroup().split(" ");
				if (items.length == 1) { // ��ϵ��ֻ����һ����
					if (map.keySet().contains(bean.getGroup())) // �����Ѿ��������
						map.get(bean.getGroup()).add(bean);
					else { // �����������
						List<AddressBean> list = new ArrayList<AddressBean>();
						list.add(bean);
						map.put(bean.getGroup(), list);
					}
				} else {
					for (String item : items) { // ѭ��������ϵ�˵�������
						if (!item.trim().isEmpty() && map.keySet().contains(item))
							map.get(item).add(bean);
						else if (!item.trim().isEmpty() && !map.keySet().contains(item)) {
							List<AddressBean> list = new ArrayList<AddressBean>();
							list.add(bean);
							map.put(bean.getGroup(), list);
						}
					}
				}
			}
		}
		return total;
	}

	/* У���ַ����Ƿ����������ʽ */
	public static boolean isMatched(String regEx, String str) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		boolean rs = matcher.matches();
		return rs;
	}
}
