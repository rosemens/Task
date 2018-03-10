package com.scau.address.vcf.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.scau.address.bean.AddressBean;

/**
 * 读写VCF格式文件的工具类
 * @author Administrator
 *
 */

public class VCFTool {
	
	/** 将联系人信息写到文件(vcard格式)中，用于导出时所用 */
	public static void exportVcfFile(List<AddressBean> list, File file) {
		try {
			if (file.exists()) {
				file.createNewFile();
			}
			BufferedWriter reader = new BufferedWriter(new PrintWriter(file));
			for (AddressBean bean : list) {
				/* 开始信息与基本标签 */
				reader.write("BEGIN:VCARD");
				reader.write("\r\n");
				reader.write("VERSION:2.1");
				reader.write("\r\n");

				/* 写入姓名 */
				reader.write("FN:" + bean.getName());
				reader.write("\r\n");

				/* 电话号码 */
				if (!bean.getTelephone().trim().isEmpty() || bean.getTelephone() != null)
					reader.write("TEL;WORK;VOICE:" + bean.getTelephone() + "\r\n");

				/* 手机号码 */
				if (!bean.getMobilephone().trim().isEmpty() || bean.getMobilephone() != null)
					reader.write("TEL;CELL;VOICE:" + bean.getMobilephone() + "\r\n");

				/* 邮箱地址 */
				if (!bean.getEmail().trim().isEmpty() || bean.getEmail() != null)
					reader.write("EMAIL;PREF;INTERNET:" + bean.getEmail() + "\r\n");

				/* 生日 */
				if (!bean.getBirthday().trim().isEmpty() || bean.getBirthday() != null)
					reader.write("BDAY:" + bean.getBirthday() + "\r\n");

				/* 工作单位 */
				if (!bean.getWorkplace().trim().isEmpty() || bean.getWorkplace() != null)
					reader.write("ADR;WORK;POSTAL;PARCEL:" + bean.getWorkplace() + "\r\n");

				/* 家庭住址 */
				if (!bean.getAddress().trim().isEmpty() || bean.getAddress() != null)
					reader.write("ADR;HOME;POSTAL;PARCEL:" + bean.getAddress() + "\r\n");

				/* 邮编 */
				if (!bean.getPostcode().trim().isEmpty() || bean.getPostcode() != null)
					reader.write("X-PostCode:" + bean.getPostcode() + "\r\n");

				/* 所属组 */
				if (!bean.getGroup().trim().isEmpty() || bean.getGroup() != null)
					reader.write("X-Group:" + bean.getGroup() + "\r\n");

				/* 备注 */
				if (!bean.getRemarks().trim().isEmpty() || bean.getRemarks() != null)
					reader.write("NOTE;ENCODING=QUOTED-PRINTABLE:" + bean.getRemarks() + "\r\n");

				/* 结束标志 */
				reader.write("END:VCARD");
				reader.write("\r\n");
			}
			reader.flush();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*读取vcf格式文件，并将数据封装到一个List<AddressBean>中*/
	public List<AddressBean> importVCFFile(){
		return null;
	}
	
	/*测试方法*/
	@Test
	public void test() {
		
		/*测试导出一个vcf文件*/
		List<AddressBean> list = new ArrayList<>();
		AddressBean bean = new AddressBean();
		bean.setName("子言");
		bean.setTelephone("0663-3133456");
		bean.setMobilephone("15819610734");
		bean.setEmail("chen@163.com");
		bean.setAddress(";;五山路;揭阳;广东;515555;中国");
		bean.setWorkplace("广州中国");
		bean.setGroup("亲人");
		bean.setPostcode("a");
		bean.setRemarks("e");
		bean.setBirthday("2000-11-11");
		list.add(bean);
		File file = new File("D:/mypbook.vcf");
	    exportVcfFile(list, file);
	    
	    
	}
}
