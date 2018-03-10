package com.scau.address.csv.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.scau.address.bean.AddressBean;
import com.scau.address.bean.AddressBeanTool;


/**
 * 读写CSV格式文件的工具类
 * @author Administrator
 *
 */

public class CSVTool {
	/** 读取联系人文件(csv格式),用于导入时调用 */
	public static List<AddressBean> importCsvFile(File file) {
		List<AddressBean> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine();// 标题信息，不用解析
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] item = line.split(",");// 以逗号分隔每一行数据
				AddressBean bean = AddressBeanTool.toBean(item);// 封装所得数据到AddressBean对象
				if (bean != null)
					list.add(bean);
			}
			reader.close();
			return list;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** 将联系人信息写到文件(csv格式)中，用于导出时所用 */
	public static void exportCsvFile(List<AddressBean> list, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("姓名,家庭电话,移动电话,邮件地址,生日,工作地址,家庭地址,邮编,联系组,备注");// 标题行，包含所有的信息
			writer.newLine();// 创建新行
			for (AddressBean bean : list) {
				writer.write(bean.getName()+",");
				writer.write(bean.getTelephone()+",");
				writer.write(bean.getMobilephone()+",");
				writer.write(bean.getEmail()+",");
				writer.write(bean.getBirthday()+",");
				writer.write(bean.getWorkplace()+",");
				writer.write(bean.getAddress()+",");
				writer.write(bean.getPostcode()+",");
				writer.write(bean.getGroup()+",");
				writer.write(bean.getRemarks());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void test() {
		/*测试导出一个csv文件*/
		List<AddressBean> list = new ArrayList<>();
		AddressBean bean = new AddressBean();
		bean.setName("子言");
		bean.setTelephone("0663-3133456");
		bean.setMobilephone("15819610734");
		bean.setEmail("chen@163.com");
		bean.setAddress(";;五山路;揭阳;广东;515555;中国");
		bean.setWorkplace("广州中国");
		bean.setGroup("亲人");
		bean.setPostcode("515555");
		bean.setRemarks("好人啊");
		bean.setBirthday("2000-11-11");
		list.add(bean);
		File file = new File("D:/mypbook.csv");
		exportCsvFile(list, file);
	}
}
