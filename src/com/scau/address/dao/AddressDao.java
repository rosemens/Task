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
 * ���ݲ����㣺�ļ�����
 * 
 * @author Administrator
 *
 */
public class AddressDao {
	private File file = new File("src/mydatas.csv");      //�洢��ϵ�˵��ļ� 
	
	/* �õ�������ϵ�� */
	public List<AddressBean> getAll() {
		return CSVTool.importCsvFile(file);
	}

	/* ������������������ѯ��ϵ�� */
	public List<AddressBean> search(String text,List<AddressBean> beans) {
		List<AddressBean> list = new ArrayList<AddressBean>();// ��ѯ�����

		if (text == null || text.trim().isEmpty()) {
			return null;// �������Ϊ�գ�����null
		}
		/* ���������� ��ƥ���������ж�Ҫ�Ժ��������������� */
		// ��������Ϊ��������ʱ
		Pattern pt1 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
		Matcher mt1 = pt1.matcher(text);
		if (mt1.find()) {
			for (AddressBean ab : beans) {// ѭ����ѯ
				if (ab.getName().contains(text)) {
					list.add(ab);
				}
			}
		}

		// ��������Ϊ�ֻ������绰ʱ
		Pattern pt2 = Pattern.compile("^[0-9]*$");// ��������Ϊ�绰�����ֻ�
		Matcher mt2 = pt2.matcher(text);
		if (mt2.find()) {
			for (AddressBean ab : beans) {// ѭ����ѯ
				if ((ab.getMobilephone() != null && !ab.getMobilephone().trim().isEmpty()
						&& ab.getMobilephone().contains(text))
						|| (ab.getTelephone() != null && !ab.getMobilephone().trim().isEmpty()
								&& ab.getTelephone().contains(text))) {
					list.add(ab);
				}
			}
		}

		// ��������Ϊƴ������ƴ����дʱ
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
    
	/*�����ϵ��*/
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
	
	/*ɾ����ϵ��*/
	public void delete(AddressBean ab) {
		
	}
	
	/*�޸���ϵ��*/
	public void modify() {
		
	}

}
