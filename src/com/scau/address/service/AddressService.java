package com.scau.address.service;

import java.util.List;
import org.junit.Test;
import com.scau.address.bean.AddressBean;
import com.scau.address.dao.AddressDao;

/**
 * 用户功能类：
 * 1.搜索联系人
 * 2.新建联系人
 * @author Administrator
 *
 */
public class AddressService {
	private AddressDao dao = new AddressDao();
	
	public List<AddressBean> getAll(){
		return dao.getAll();
	}
	
	/*条件搜索*/
	public List<AddressBean> search(String text,List<AddressBean> beans){
		return dao.search(text,beans);
	}
	
	/*添加联系人 */
	public void add(AddressBean ab) {
		dao.add(ab);
	}
	
	/*删除联系人*/
	public void delete(AddressBean ab) {
		
	}
	
	@Test
	public void test(){
	}
}
