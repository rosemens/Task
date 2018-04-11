package com.scau.address.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;

/**
 * 校验工具类
 * @author Administrator
 *
 */
public class CheckTool {  
	
	/*合并两个list,剔除完全相同的数据,邮箱与电话至少都不同*/
	public static List<AddressBean> merge(List<AddressBean> total,List<AddressBean> beans){
		for(AddressBean ab:beans) { //剔除相同的联系人
			if(!total.contains(ab))
				total.add(ab);
		}
		return total;
	}
	
	/*校验字符串是否符合正则表达式*/
	public static boolean isMatched(String regEx,String str) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		boolean rs = matcher.matches();
		return rs;
	}
}
