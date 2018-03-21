package com.scau.address.groups.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.scau.address.bean.AddressBean;
import com.scau.address.csv.tool.CSVTool;

/**
 * 得到导入文件的分组情况
 * @author Administrator
 *
 */
public class GroupsTool {
   
	/*得到所有分组情况*/
	public static Map<String,List<AddressBean>> getGroups(List<AddressBean> list){
		Map<String,List<AddressBean>> map = new HashMap<>();//每个组对应的联系人
		for(AddressBean ab:list) {
			if(ab.getGroup() == null || ab.getGroup().trim().isEmpty() || ab.getGroup().equals(",")) {
				if(map.containsKey("未分组")) {
					map.get("未分组").add(ab);
				}else {
					List<AddressBean> group = new ArrayList<>();
					group.add(ab);
					map.put("未分组", group);
				}
			}else if(map.containsKey(ab.getGroup())) {  //map中已有这个分组，则把这个联系人加到对应组的列表中
				map.get(ab.getGroup()).add(ab);
			}else {
				List<AddressBean> group = new ArrayList<>();
				group.add(ab);
				map.put(ab.getGroup(), group);
			}	
		}
		return map;
	}
	
	@Test
	public void test() {
		File file = new File("D:/myaddress.csv");
		List<AddressBean> list = CSVTool.importCsvFile(file);
		Map<String,List<AddressBean>> map = getGroups(list);
		System.out.println(map);
	}
}
