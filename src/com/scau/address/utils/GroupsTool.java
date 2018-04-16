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
 * 得到导入文件的分组情况
 * 
 * @author Administrator
 *
 */
public class GroupsTool {

	/* 得到所有分组与相应的联系人列表 */
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

	/* 保存 所有的组名到文件中 */
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

	/* 得到所有的组名 */
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
