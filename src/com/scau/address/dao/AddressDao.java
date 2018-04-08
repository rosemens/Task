package com.scau.address.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scau.address.bean.AddressBean;
import com.scau.address.utils.ConvertTool;

/**
 * ���ݲ����㣺�ļ�����
 * 
 * @author Administrator
 *
 */
public class AddressDao {
	/* �õ�������ϵ�� */
	public List<AddressBean> getAll() {
		return null;
	}

	/* ������������������ѯ��ϵ�� */
	public List<AddressBean> search(String text) {
		List<AddressBean> beans = getAll();
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
	public void add(AddressBean ab) {
		
	}
	
	/*ɾ����ϵ��*/
	public void delete(AddressBean ab) {
		
	}
	
	/*�޸���ϵ��*/
	public void modify() {
		
	}

}
