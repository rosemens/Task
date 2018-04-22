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
 * ��дCSV��ʽ�ļ��Ĺ�����
 * @author Administrator
 *
 */

public class CSVTool {
	/** ��ȡ��ϵ���ļ�(csv��ʽ),���ڵ���ʱ���� */
	public static List<AddressBean> importCsvFile(File file) {
		List<AddressBean> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine();// ������Ϣ�����ý���
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] item = line.split(",");// �Զ��ŷָ�ÿһ������
				AddressBean bean = AddressBeanTool.toBean(item);// ��װ�������ݵ�AddressBean����
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

	/** ����ϵ����Ϣд���ļ�(csv��ʽ)�У����ڵ���ʱ���� */
	public static void exportCsvFile(List<AddressBean> list, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("����,��ͥ�绰,�ƶ��绰,�ʼ���ַ,����,������ҳ,������ַ,��ͥ��ַ,�ʱ�,��ϵ��,��ע");// �����У��������е���Ϣ
			writer.newLine();// ��������
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
