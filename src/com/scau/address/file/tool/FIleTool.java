package com.scau.address.file.tool;

import java.io.File;
import java.util.List;

import com.scau.address.bean.AddressBean;
import com.scau.address.csv.tool.CSVTool;

/**
 * ���ܣ��½���ɾ�������޸��û��ĵײ�ʵ��
 * @author Administrator
 *
 */

public class FIleTool {
   /*�����в��������ϵ���б�д�ص��ļ���*/
   public static void Rewrite(List<AddressBean> list) {
	   CSVTool.exportCsvFile(list, new File("D:/mytest1.csv"));
   }
}
