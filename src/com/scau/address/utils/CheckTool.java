package com.scau.address.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;

/**
 * 校验工具类
 * 
 * @author Administrator
 *
 */
public class CheckTool {

	/* 合并两个list,剔除完全相同的数据,邮箱与电话至少都不同 */
	public static List<AddressBean> merge(List<AddressBean> total, List<AddressBean> beans,
			Map<String, List<AddressBean>> map, List<String> group) {
		for (AddressBean bean : beans) {
			if (total.contains(bean)) // 剔除相同联系人
				continue;
			else total.add(bean);
			if (bean.getGroup().trim().isEmpty()) // 联系人不属于组，将联系人加到未分组
				map.get("未分组").add(bean);
			else {
				String[] items = bean.getGroup().split(" ");
				if (items.length == 1) { // 联系人只属于一个组
					if (map.keySet().contains(bean.getGroup())) // 并且已经有这个组
						map.get(bean.getGroup()).add(bean);
					else { // 不存在这个组
						List<AddressBean> list = new ArrayList<AddressBean>();
						list.add(bean);
						map.put(bean.getGroup(), list);
					}
				} else {
					for (String item : items) { // 循环遍历联系人的所有组
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

	/* 校验字符串是否符合正则表达式 */
	public static boolean isMatched(String regEx, String str) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		boolean rs = matcher.matches();
		return rs;
	}
}
