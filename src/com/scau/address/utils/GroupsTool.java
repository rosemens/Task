package com.scau.address.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scau.address.bean.AddressBean;

/**
 * 得到导入文件的分组情况
 * 
 * @author Administrator
 *
 */
public class GroupsTool {

	/* 得到所有分组情况 */
	public static Map<String, List<AddressBean>> getGroups(List<AddressBean> list) {
		Map<String, List<AddressBean>> map = new HashMap<>();// 每个组对应的联系人
		map.put("未分组", new ArrayList<AddressBean>()); // 初始化
		for (AddressBean ab : list) {
			if (ab.getGroup() == null || ab.getGroup().trim().isEmpty() || ab.getGroup().equals(",")) {
				map.get("未分组").add(ab);
			} else {
				String[] groups = ab.getGroup().split(" ");
				if (groups != null)
					for (String group : groups) {
						if (!map.containsKey(group)) {
							List<AddressBean> tol = new ArrayList<AddressBean>();
							tol.add(ab);
							map.put(group, tol);
						} else
							map.get(group).add(ab);
					}
			}
		}
		return map;
	}
}
