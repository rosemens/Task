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
 * �õ������ļ��ķ������
 * @author Administrator
 *
 */
public class GroupsTool {
   
	/*�õ����з������*/
	public static Map<String,List<AddressBean>> getGroups(List<AddressBean> list){
		Map<String,List<AddressBean>> map = new HashMap<>();//ÿ�����Ӧ����ϵ��
		for(AddressBean ab:list) {
			if(ab.getGroup() == null || ab.getGroup().trim().isEmpty() || ab.getGroup().equals(",")) {
				if(map.containsKey("δ����")) {
					map.get("δ����").add(ab);
				}else {
					List<AddressBean> group = new ArrayList<>();
					group.add(ab);
					map.put("δ����", group);
				}
			}else if(map.containsKey(ab.getGroup())) {  //map������������飬��������ϵ�˼ӵ���Ӧ����б���
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
