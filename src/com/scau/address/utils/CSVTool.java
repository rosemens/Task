package com.scau.address.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.scau.address.bean.AddressBean;



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
			writer.write("姓名,家庭电话,移动电话,邮件地址,生日,个人主页,工作地址,家庭地址,邮编,联系组,备注");// 标题行，包含所有的信息
			writer.newLine();// 创建新行
			for (AddressBean bean : list) {
				if(bean.getName().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getName()+",");
				
				if(bean.getTelephone().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getTelephone()+",");
				
				if(bean.getMobilephone().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getMobilephone()+",");
				
				if(bean.getEmail().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getEmail()+",");
				
				if(bean.getBirthday().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getBirthday()+",");
				
				if(bean.getIndex().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getIndex()+",");
				
				if(bean.getWorkplace().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getWorkplace()+",");
				
				if(bean.getAddress().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getAddress()+",");
				
				if(bean.getPostcode().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getPostcode()+",");
				
				if(bean.getGroup().trim().isEmpty())
					writer.write(",");
				else writer.write(bean.getGroup()+",");
				
				if(bean.getRemarks().trim().isEmpty())
					writer.write(" ");
				else writer.write(bean.getRemarks());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
