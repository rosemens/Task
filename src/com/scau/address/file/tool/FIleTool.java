package com.scau.address.file.tool;

import java.io.File;
import java.util.List;

import com.scau.address.bean.AddressBean;
import com.scau.address.csv.tool.CSVTool;

/**
 * 功能：新建，删除，和修改用户的底层实现
 * @author Administrator
 *
 */

public class FIleTool {
   /*将进行操作后的联系人列表写回到文件中*/
   public static void Rewrite(List<AddressBean> list) {
	   CSVTool.exportCsvFile(list, new File("D:/mytest1.csv"));
   }
}
