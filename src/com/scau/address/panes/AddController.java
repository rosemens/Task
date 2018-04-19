package com.scau.address.panes;

import java.util.ArrayList;
import java.util.List;

import com.scau.address.bean.AddressBean;
import com.scau.address.service.AddressService;
import com.scau.address.utils.AddressBeanTool;
import com.scau.address.utils.CheckTool;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController {
	@FXML
	private TextField name;
	@FXML
	private TextField birthday;
	@FXML
	private TextField email;
	@FXML
	private TextField index;
	@FXML
	private TextField mobilephone;
	@FXML
	private TextField postcode;
	@FXML
	private TextField telephone;
	@FXML
	private ComboBox<String> group;
	@FXML
	private TextField hstreet;
	@FXML
	private TextField hcity;
	@FXML
	private TextField hprovince;
	@FXML
	private TextField hcountry;
	@FXML
	private TextField wstreet;
	@FXML
	private TextField wcity;
	@FXML
	private TextField wprovince;
	@FXML
	private TextField wcountry;
	@FXML
	private TextArea tips;
	@FXML
	private Button ok;
	@FXML
	private Button cancel;

	private String regEx; // 正则表达式
	private String agroup = ""; // 组
	private MyController mcontroller; // 主控制类对象
	private Stage stage; // 当前新建联系人的舞台
	private AddressService service = new AddressService();

	public void init(MyController mcontroller, Stage stage) {
		this.mcontroller = mcontroller;
		this.stage = stage;

		group.getItems().addAll(FXCollections.observableArrayList(mcontroller.list));
		group.getItems().add("新建组");
	}

	/* 添加联系人 */
	@FXML
	public void add() {
		try {
			AddressBean bean = newAddressBean();
			String group = bean.getGroup();
			String flag = "";

			if (group.trim().isEmpty()) { // 未设置组
				mcontroller.map.get("未分组").add(bean);
			} else {
				for (String key : mcontroller.map.keySet()) { // 循环遍历看是否存在这个组
					if (group.equals(key)) {
						mcontroller.map.get(key).add(bean);
						flag = key;
						break;
					}
				}
				if (!group.equals(flag)) { // 不存在这个组则将这个组送进map中
					List<AddressBean> list = new ArrayList<AddressBean>();
					list.add(bean);
					mcontroller.map.put(group, list);
				}

			}

			mcontroller.total.add(bean);
			mcontroller.fillTable(mcontroller.total); // 动态更新表格中的联系人列表
			mcontroller.initAllBeans(mcontroller.map); // 动态更新左侧treeview列表的联系人项目
			mcontroller.initAllGroups(mcontroller.map); // 动态更新左侧treeview列表的联系组项目
			service.add(bean); // 将新建联系人添加到文件中
			stage.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* 取消新建联系人 */
	@FXML
	public void cancel() {
		stage.close();
	}

	/* 选中组 */
	@FXML
	public void chooseGroup() {
		agroup = group.getValue();
	}

	/* 封装数据到联系人对象,其中名字，手机，email不能为空 */
	private AddressBean newAddressBean() throws Exception {
		String[] items = new String[11];

		// 姓名
		String aname = name.getText();
		if (aname == null || aname.trim().isEmpty()) { // 校验名字
			throw new Exception("名字不能为空");
		} else
			items[0] = aname;

		// 电话
		String atele = telephone.getText();
		regEx = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
		if (!atele.trim().isEmpty() && !CheckTool.isMatched(regEx, atele)) {
			throw new Exception("电话号码格式错误");
		}
		items[1] = atele;

		// 手机
		String amobile = mobilephone.getText();
		regEx = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
		if (amobile == null || amobile.trim().isEmpty()) {
			throw new Exception("手机号码不能为空");
		}
		if (!CheckTool.isMatched(regEx, amobile)) {
			throw new Exception("手机号码格式错误");
		}
		items[2] = amobile;

		// 邮箱
		String aemail = email.getText();
		regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		if (aemail == null || aemail.trim().isEmpty()) {
			throw new Exception("email不能为空");
		}
		if (!CheckTool.isMatched(regEx, aemail)) {
			throw new Exception("email格式错误");
		}
		items[3] = aemail;

		// 生日
		String abrith = birthday.getText();
		regEx = "^\\d{4}-\\d{1,2}-\\d{1,2}";
		if (!abrith.trim().isEmpty() && !CheckTool.isMatched(regEx, abrith)) {
			throw new Exception("生日格式错误");
		}
		items[4] = abrith;

		// 个人主页
		String aindex = index.getText();
		items[5] = aindex;

		// 邮编
		String apostcode = postcode.getText();
		items[8] = apostcode;

		// 工作地址
		StringBuilder sb = new StringBuilder();
		sb.append(";");
		sb.append(";");
		String wst = wstreet.getText(); // 街道
		if (wst == null || wst.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wst);
		String wct = wcity.getText(); // 城市
		if (wct == null || wct.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wct);
		if (apostcode == null || apostcode.trim().isEmpty()) { // 邮编
			sb.append(";");
		} else
			sb.append(";" + apostcode);
		String wpro = wprovince.getText(); // 省份
		if (wpro == null || wpro.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wpro);
		String wco = wcountry.getText(); // 国家
		if (wco == null || wco.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wco);
		items[6] = sb.toString();

		// 家庭住址
		StringBuilder sbu = new StringBuilder();
		sbu.append(";");
		sbu.append(";");
		String hst = hstreet.getText(); // 街道
		if (hst == null || hst.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(hst);
		String hct = hcity.getText(); // 城市
		if (hct == null || hct.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hct);
		if (apostcode == null || apostcode.trim().isEmpty()) { // 邮编
			sbu.append(";");
		} else
			sbu.append(";" + apostcode);
		String hpro = hprovince.getText(); // 省份
		if (hpro == null || hpro.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hpro);
		String hco = hcountry.getText(); // 国家
		if (hco == null || hco.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hco);
		items[7] = sbu.toString();

		// 分组
		items[9] = agroup;

		// 备注
		String atips = tips.getText();
		items[10] = atips;

		return AddressBeanTool.toBean(items);
	}

}
