package com.scau.address.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.scau.address.bean.AddressBean;
import com.scau.address.utils.CSVTool;
import com.scau.address.utils.ConvertTool;

/**
 * 数据操作层：文件操作
 * 
 * @author Administrator
 *
 */
public class AddressDao {
	private File file = new File("src/mydatas.csv");      //存储联系人的文件 
	
	/* 得到所有联系人 */
	public List<AddressBean> getAll() {
		return CSVTool.importCsvFile(file);
	}

	/* 根据搜索框内容来查询联系人 */
	public List<AddressBean> search(String text,List<AddressBean> beans) {
		List<AddressBean> list = new ArrayList<AddressBean>();// 查询结果集

		if (text == null || text.trim().isEmpty()) {
			return null;// 如果条件为空，返回null
		}
		/* 利用正则表达 来匹配条件，判断要以何种条件进行搜索 */
		// 输入条件为中文名字时
		Pattern pt1 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
		Matcher mt1 = pt1.matcher(text);
		if (mt1.find()) {
			for (AddressBean ab : beans) {// 循环查询
				if (ab.getName().contains(text)) {
					list.add(ab);
				}
			}
		}

		// 输入条件为手机号码或电话时
		Pattern pt2 = Pattern.compile("^[0-9]*$");// 输入条件为电话或者手机
		Matcher mt2 = pt2.matcher(text);
		if (mt2.find()) {
			for (AddressBean ab : beans) {// 循环查询
				if ((ab.getMobilephone() != null && !ab.getMobilephone().trim().isEmpty()
						&& ab.getMobilephone().contains(text))
						|| (ab.getTelephone() != null && !ab.getMobilephone().trim().isEmpty()
								&& ab.getTelephone().contains(text))) {
					list.add(ab);
				}
			}
		}

		// 输入条件为拼音或者拼音缩写时
		Pattern pt3 = Pattern.compile("^[A-Za-z]+$");
		Matcher mt3 = pt3.matcher(text);
		if (mt3.find()) {
			for (AddressBean ab : beans) {
				if (ConvertTool.getPingYin(ab.getName()).equals(text)
						|| ConvertTool.getPinYinHeadChar(ab.getName()).equals(text))
					list.add(ab);
			}
		}
		return list;
	}
    
	/*添加联系人*/
	public void add(AddressBean bean) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
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
			
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*删除联系人*/
	public void delete(AddressBean ab) {
		
	}
	
	/*修改联系人*/
	public void modify() {
		
	}

}
