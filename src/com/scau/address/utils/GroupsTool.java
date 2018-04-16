package com.scau.address.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scau.address.bean.AddressBean;

/**
 * �õ������ļ��ķ������
 * 
 * @author Administrator
 *
 */
public class GroupsTool {

	/* �õ����з�������Ӧ����ϵ���б� */
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

	/* ���� ���е��������ļ��� */
	public static void saveGroups(File file, Set<String> groups) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String group : groups) {
				writer.write(group);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* �õ����е����� */
	public static List<String> getGroupsName() {
		try {
			File file = new File("src/mygroups.csv");
			List<String> groupsname = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null) {
            	groupsname.add(line.trim());
            }
            reader.close();
			return groupsname;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
