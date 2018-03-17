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
 * ��дVCF��ʽ�ļ��Ĺ�����
 * 
 * @author Administrator
 *
 */

public class VCFTool {

	/** ����ϵ����Ϣд���ļ�(vcard��ʽ)�У����ڵ���ʱ���� */
	public static void exportVcfFile(List<AddressBean> list, File file) {
		try {
			if (file.exists()) {
				file.createNewFile();
			}
			BufferedWriter reader = new BufferedWriter(new PrintWriter(file));
			for (AddressBean bean : list) {
				/* ��ʼ��Ϣ�������ǩ */
				reader.write("BEGIN:VCARD");
				reader.write("\r\n");
				reader.write("VERSION:2.1");
				reader.write("\r\n");

				/* д������ */
				reader.write("FN:" + bean.getName());
				reader.write("\r\n");

				/* �绰���� */
				if (bean.getTelephone() != null && !bean.getTelephone().trim().isEmpty())
					reader.write("TEL;WORK;VOICE:" + bean.getTelephone() + "\r\n");

				/* �ֻ����� */
				if ( bean.getMobilephone() != null && !bean.getMobilephone().trim().isEmpty())
					reader.write("TEL;CELL;VOICE:" + bean.getMobilephone() + "\r\n");

				/* �����ַ */
				if (bean.getEmail() != null && !bean.getEmail().trim().isEmpty())
					reader.write("EMAIL;PREF;INTERNET:" + bean.getEmail() + "\r\n");

				/* ���� */
				if (bean.getBirthday() != null && !bean.getBirthday().trim().isEmpty())
					reader.write("BDAY:" + bean.getBirthday() + "\r\n");
				
				/* ������ҳ*/
				if(bean.getIndex() != null && !bean.getIndex().trim().isEmpty())
					reader.write("URL:" + bean.getIndex() + "\r\n");

				/* ������λ */
				if (bean.getWorkplace() != null && !bean.getWorkplace().trim().isEmpty())
					reader.write("ADR;WORK;POSTAL;PARCEL:" + bean.getWorkplace() + "\r\n");

				/* ��ͥסַ */
				if (bean.getAddress() != null && !bean.getAddress().trim().isEmpty())
					reader.write("ADR;HOME;POSTAL;PARCEL:" + bean.getAddress() + "\r\n");

				/* �ʱ� */
				if (bean.getPostcode() != null && !bean.getPostcode().trim().isEmpty())
					reader.write("X-PostCode:" + bean.getPostcode() + "\r\n");

				/* ������ */
				if (bean.getGroup() != null && !bean.getGroup().trim().isEmpty())
					reader.write("X-Group:" + bean.getGroup() + "\r\n");

				/* ��ע */
				if (bean.getRemarks() != null && !bean.getRemarks().trim().isEmpty())
					reader.write("NOTE;ENCODING=QUOTED-PRINTABLE:" + bean.getRemarks() + "\r\n");

				/* ������־ */
				reader.write("END:VCARD");
				reader.write("\r\n");
			}
			reader.flush();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡvcf��ʽ�ļ����������ݷ�װ��һ��List<AddressBean>�� */
	public List<AddressBean> importVCFFile(File file) {
		List<AddressBean> list = new ArrayList<AddressBean>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = null;

			/* ��ȡ�ļ���ÿһ������ */
			while ((line = nextLine(reader)) != null) {
				sb.append(line + "\r\n");
			}

			/* �����ļ���ƥ��ģʽ,���һ��VCard */
			Pattern p = Pattern.compile("BEGIN:VCARD(\\r\\n)([\\s\\S\\r\\n\\.]*?)END:VCARD"); // ƥ��ģʽ����
			Matcher m = p.matcher(sb.toString()); // ����ƥ������Ķ���

			/* ��ÿһ��Vcard�����ݽ�����ȡ,��װ��AddressBean������ */
			while (m.find()) {
				AddressBean bean = new AddressBean();
				String str = m.group(0); // ����

				/* ��ȡname (?m)^FN([^:]+):(.+)$*/
				Pattern pn = Pattern.compile("FN:((.+)$*)");// ���飬
				Matcher mn = pn.matcher(str);
				while (mn.find()) {
					String name = "";
					name = mn.group(0).substring("FN:".length());// ��ȡname
					bean.setName(name);
				}

				/* ��ȡ�ֻ��� */
				String cell = "";
				Pattern p1 = Pattern.compile("TEL;CELL;VOICE:(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}");// ���飬
				Matcher m1 = p1.matcher(str);
				while (m1.find()) {
					cell = m1.group(0).substring("TEL;CELL;VOICE:".length());
				}
				bean.setMobilephone(cell);

				/* ��ȡ�绰 */
				String work = "";
				Pattern p2 = Pattern.compile("TEL;WORK;VOICE:[0-9-()����]{7,18}");// ���飬
				Matcher m2 = p2.matcher(str);
				while (m2.find()) {
					work = m2.group(0).substring(m2.group(0).indexOf("TEL;WORK;VOICE:") + "TEL;WORK;VOICE:".length());
				}
				bean.setTelephone(work);
		
				/* ��ȡEmail */
				String email = "";
				Pattern p4 = Pattern.compile("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");// ���飬
				Matcher m4 = p4.matcher(str);
				while (m4.find()) {
					email = m4.group(0);
				}
				bean.setEmail(email);

				/* ��ȡ���� */
				String birthday = "";
				Pattern p5 = Pattern.compile(
						"([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))");
				Matcher m5 = p5.matcher(str);
				while (m5.find()) {
					birthday = m5.group(0);
				}
				bean.setBirthday(birthday);

				/* ��ȡ����סַ�ͼ�ͥסַ */
				String workplace = "";// ������ַ
				String home = "";// ��ͥסַ
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
				
				/*��ȡ������ҳ*/
				String url = "";
				Pattern p7 = Pattern.compile("URL:[a-zA-z]+://[^\\s]*");
				Matcher m7 = p7.matcher(str);
				while(m7.find()) {
					url = m7.group(0).substring("URL:".length());
				}
				bean.setIndex(url);
				
				/*��ȡ�ʱ�*/
				String postcode = "";
				Pattern p8 = Pattern.compile("X-PostCode:[1-9]\\d{5}(?!\\d)");
				Matcher m8 = p8.matcher(str);
				while(m8.find()) {
					postcode = m8.group(0).substring("X-PostCode:".length());
				}
				bean.setPostcode(postcode);
				
				/*��ȡ������Ϣ*/
				String group = "";
				Pattern p9 = Pattern.compile("X-Group:((.+)$*)");
				Matcher m9 = p9.matcher(str);
				while(m9.find()) {
					group = m9.group(0).substring("X-Group:".length());
				}
				bean.setGroup(group);
				
				/*��ȡ��ע��Ϣ*/
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

	/* ��vcf��ʽ���ļ�ת����һ���ַ��� */
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

	/* ���Է��� */
	@Test
	public void test() {

		/* ���Ե���һ��vcf�ļ� */
		List<AddressBean> list = new ArrayList<>();
		
		  AddressBean bean = new AddressBean(); bean.setName("����");
		 bean.setTelephone("0663-3133456"); bean.setMobilephone("15819610734");
		 bean.setEmail("chen@163.com"); bean.setAddress(";;��ɽ·;����;�㶫;515555;�й�");
		 bean.setWorkplace("�����й�"); bean.setGroup("����"); bean.setPostcode("a");
		 bean.setRemarks("e"); bean.setBirthday("2000-11-11"); list.add(bean);
		 
		/*File file = new File("D:/mypbook.vcf");
		list = importVCFFile(file);
		System.out.println(list);*/
		File file = new File("D:/myTest.vcf");
		exportVcfFile(list, file);
	}
}
