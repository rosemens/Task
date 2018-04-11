package com.scau.address.service;

import java.util.List;
import org.junit.Test;
import com.scau.address.bean.AddressBean;
import com.scau.address.dao.AddressDao;

/**
 * �û������ࣺ
 * 1.������ϵ��
 * 2.�½���ϵ��
 * @author Administrator
 *
 */
public class AddressService {
	private AddressDao dao = new AddressDao();
	
	public List<AddressBean> getAll(){
		return dao.getAll();
	}
	
	/*��������*/
	public List<AddressBean> search(String text,List<AddressBean> beans){
		return dao.search(text,beans);
	}
	
	/*�����ϵ�� */
	public void add(AddressBean ab) {
		dao.add(ab);
	}
	
	/*ɾ����ϵ��*/
	public void delete(AddressBean ab) {
		
	}
	
	@Test
	public void test(){
	}
}
