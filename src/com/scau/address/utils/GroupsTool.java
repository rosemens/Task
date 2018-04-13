package com.scau.address.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scau.address.bean.AddressBean;

/**
 * �õ������ļ��ķ������
 * 
 * @author Administrator
 *
 */
public class GroupsTool {

	/* �õ����з������ */
	public static Map<String, List<AddressBean>> getGroups(List<AddressBean> list) {
		Map<String, List<AddressBean>> map = new HashMap<>();// ÿ�����Ӧ����ϵ��
		map.put("δ����", new ArrayList<AddressBean>()); // ��ʼ��
		for (AddressBean ab : list) {
			if (ab.getGroup() == null || ab.getGroup().trim().isEmpty() || ab.getGroup().equals(",")) {
				map.get("δ����").add(ab);
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
