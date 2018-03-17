package com.scau.address.vcf.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import com.scau.address.bean.AddressBean;

/**
 * 读写VCF格式文件的工具类
 * 
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
				if (bean.getTelephone() != null && !bean.getTelephone().trim().isEmpty())
					reader.write("TEL;WORK;VOICE:" + bean.getTelephone() + "\r\n");

				/* 手机号码 */
				if ( bean.getMobilephone() != null && !bean.getMobilephone().trim().isEmpty())
					reader.write("TEL;CELL;VOICE:" + bean.getMobilephone() + "\r\n");

				/* 邮箱地址 */
				if (bean.getEmail() != null && !bean.getEmail().trim().isEmpty())
					reader.write("EMAIL;PREF;INTERNET:" + bean.getEmail() + "\r\n");

				/* 生日 */
				if (bean.getBirthday() != null && !bean.getBirthday().trim().isEmpty())
					reader.write("BDAY:" + bean.getBirthday() + "\r\n");
				
				/* 个人主页*/
				if(bean.getIndex() != null && !bean.getIndex().trim().isEmpty())
					reader.write("URL:" + bean.getIndex() + "\r\n");

				/* 工作单位 */
				if (bean.getWorkplace() != null && !bean.getWorkplace().trim().isEmpty())
					reader.write("ADR;WORK;POSTAL;PARCEL:" + bean.getWorkplace() + "\r\n");

				/* 家庭住址 */
				if (bean.getAddress() != null && !bean.getAddress().trim().isEmpty())
					reader.write("ADR;HOME;POSTAL;PARCEL:" + bean.getAddress() + "\r\n");

				/* 邮编 */
				if (bean.getPostcode() != null && !bean.getPostcode().trim().isEmpty())
					reader.write("X-PostCode:" + bean.getPostcode() + "\r\n");

				/* 所属组 */
				if (bean.getGroup() != null && !bean.getGroup().trim().isEmpty())
					reader.write("X-Group:" + bean.getGroup() + "\r\n");

				/* 备注 */
				if (bean.getRemarks() != null && !bean.getRemarks().trim().isEmpty())
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

	/** 读取vcf格式文件，并将数据封装到一个List<AddressBean>中 */
	public List<AddressBean> importVCFFile(File file) {
		List<AddressBean> list = new ArrayList<AddressBean>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = null;

			/* 读取文件的每一行数据 */
			while ((line = nextLine(reader)) != null) {
				sb.append(line + "\r\n");
			}

			/* 给定文件的匹配模式,针对一对VCard */
			Pattern p = Pattern.compile("BEGIN:VCARD(\\r\\n)([\\s\\S\\r\\n\\.]*?)END:VCARD"); // 匹配模式对象
			Matcher m = p.matcher(sb.toString()); // 进行匹配操作的对象

			/* 对每一对Vcard的数据进行提取,封装到AddressBean对象中 */
			while (m.find()) {
				AddressBean bean = new AddressBean();
				String str = m.group(0); // 分组

				/* 提取name (?m)^FN([^:]+):(.+)$*/
				Pattern pn = Pattern.compile("FN:((.+)$*)");// 分组，
				Matcher mn = pn.matcher(str);
				while (mn.find()) {
					String name = "";
					name = mn.group(0).substring("FN:".length());// 截取name
					bean.setName(name);
				}

				/* 截取手机号 */
				String cell = "";
				Pattern p1 = Pattern.compile("TEL;CELL;VOICE:(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}");// 分组，
				Matcher m1 = p1.matcher(str);
				while (m1.find()) {
					cell = m1.group(0).substring("TEL;CELL;VOICE:".length());
				}
				bean.setMobilephone(cell);

				/* 截取电话 */
				String work = "";
				Pattern p2 = Pattern.compile("TEL;WORK;VOICE:[0-9-()（）]{7,18}");// 分组，
				Matcher m2 = p2.matcher(str);
				while (m2.find()) {
					work = m2.group(0).substring(m2.group(0).indexOf("TEL;WORK;VOICE:") + "TEL;WORK;VOICE:".length());
				}
				bean.setTelephone(work);
		
				/* 截取Email */
				String email = "";
				Pattern p4 = Pattern.compile("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");// 分组，
				Matcher m4 = p4.matcher(str);
				while (m4.find()) {
					email = m4.group(0);
				}
				bean.setEmail(email);

				/* 截取生日 */
				String birthday = "";
				Pattern p5 = Pattern.compile(
						"([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))");
				Matcher m5 = p5.matcher(str);
				while (m5.find()) {
					birthday = m5.group(0);
				}
				bean.setBirthday(birthday);

				/* 截取工作住址和家庭住址 */
				String workplace = "";// 工作地址
				String home = "";// 家庭住址
				Pattern p6 = Pattern.compile("(?m)^ADR([^:]+):(.+)$");
				Matcher m6 = p6.matcher(str);
				while (m6.find()) {
					if (m6.group(0).contains("ADR;WORK;POSTAL;PARCEL:"))
						workplace = m6.group(0).substring("ADR;WORK;POSTAL;PARCEL:".length());
					else if (m6.group(0).contains("ADR;HOME;POSTAL;PARCEL:"))
						home = m6.group(0).substring("ADR;HOME;POSTAL;PARCEL:".length());
				}
				bean.setAddress(workplace);
				bean.setWorkplace(home);
				
				/*截取个人主页*/
				String url = "";
				Pattern p7 = Pattern.compile("URL:[a-zA-z]+://[^\\s]*");
				Matcher m7 = p7.matcher(str);
				while(m7.find()) {
					url = m7.group(0).substring("URL:".length());
				}
				bean.setIndex(url);
				
				/*截取邮编*/
				String postcode = "";
				Pattern p8 = Pattern.compile("X-PostCode:[1-9]\\d{5}(?!\\d)");
				Matcher m8 = p8.matcher(str);
				while(m8.find()) {
					postcode = m8.group(0).substring("X-PostCode:".length());
				}
				bean.setPostcode(postcode);
				
				/*截取分组信息*/
				String group = "";
				Pattern p9 = Pattern.compile("X-Group:((.+)$*)");
				Matcher m9 = p9.matcher(str);
				while(m9.find()) {
					group = m9.group(0).substring("X-Group:".length());
				}
				bean.setGroup(group);
				
				/*截取备注信息*/
				String notes = "";
				Pattern p10 = Pattern.compile("NOTE;ENCODING=QUOTED-PRINTABLE:((.+)$*)");
				Matcher m10 = p10.matcher(str);
				while(m10.find()) {
					notes = m10.group(0).substring("NOTE;ENCODING=QUOTED-PRINTABLE:".length());
				}
				bean.setRemarks(notes);

				list.add(bean);
			}
			reader.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	/* 将vcf格式的文件转换成一个字符串 */
	private String nextLine(BufferedReader reader) throws Exception {
		String line;
		String nextLine;
		do {
			line = reader.readLine();
			if (line == null) {
				return null;
			}
		} while (line.length() == 0);
		while (line.endsWith("=")) {
			line = line.substring(0, line.length() - 1);
			line += reader.readLine();
		}
		reader.mark(1000);
		nextLine = reader.readLine();
		if ((nextLine != null) && (nextLine.length() > 0)
				&& ((nextLine.charAt(0) == 0x20) || (nextLine.charAt(0) == 0x09))) {
			line += nextLine.substring(1);
		} else {
			reader.reset();
		}
		line = line.trim();
		return line;
	}

	/* 测试方法 */
	@Test
	public void test() {

		/* 测试导出一个vcf文件 */
		List<AddressBean> list = new ArrayList<>();
		
		  AddressBean bean = new AddressBean(); bean.setName("子言");
		 bean.setTelephone("0663-3133456"); bean.setMobilephone("15819610734");
		 bean.setEmail("chen@163.com"); bean.setAddress(";;五山路;揭阳;广东;515555;中国");
		 bean.setWorkplace("广州中国"); bean.setGroup("亲人"); bean.setPostcode("a");
		 bean.setRemarks("e"); bean.setBirthday("2000-11-11"); list.add(bean);
		 
		/*File file = new File("D:/mypbook.vcf");
		list = importVCFFile(file);
		System.out.println(list);*/
		File file = new File("D:/myTest.vcf");
		exportVcfFile(list, file);
	}
}
