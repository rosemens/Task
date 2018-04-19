package com.scau.address.panes;

import java.io.IOException;
import java.net.URL;

import com.scau.address.bean.AddressBean;
import com.scau.address.utils.CheckTool;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditBeanController {
	@FXML
	private TextField name;
	@FXML
	private TextField index;
	@FXML
	private TextField email;
	@FXML
	private TextField postcode;
	@FXML
	private TextField moiblephone;
	@FXML
	private TextField telephone;
	@FXML
	private TextField birthday;
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
	private TextArea remarks;
	@FXML
	private Button b1;
	@FXML
	private Button b2;

	private AddressBean bean;
	private Stage stage;
	private MyController mcontroller;
	private String agroup = ""; // 记录未修改前联系人所属的组
	private String oldgroup = "";

	public void init(AddressBean bean, Stage stage, MyController mcontroller) {
		this.bean = bean;
		this.mcontroller = mcontroller;
		this.stage = stage;
		group.getItems().addAll(FXCollections.observableArrayList(mcontroller.list));
		group.getItems().add("新建组");
		fillBean();

	}

	/* 将联系人信息显示在面板中 */
	private void fillBean() {
		name.setText(bean.getName());
		index.setText(bean.getIndex());
		email.setText(bean.getEmail());
		postcode.setText(bean.getPostcode());
		moiblephone.setText(bean.getMobilephone());
		telephone.setText(bean.getTelephone());
		birthday.setText(bean.getBirthday());
		group.setValue(bean.getGroup());
		oldgroup = bean.getGroup();
		if (oldgroup.trim().isEmpty())
			oldgroup = "未分组";
		remarks.setText(bean.getRemarks());

		fillAddress(bean.getAddress().split(";"), "h"); // h表示的是家庭地址，w表示的是工作地址
		fillAddress(bean.getWorkplace().split(";"), "w");
	}

	/* 填充地址,differ用来判断是家庭地址还是工作地址 ,仍有缺陷 */
	private void fillAddress(String[] items, String differ) {
		if (differ.equals("h")) {
			if (items.length == 7)
				for (int i = 0; i < items.length; i++) {
					if (i == 2)
						hstreet.setText(items[i]);
					if (i == 3)
						hcity.setText(items[i]);
					if (i == 4)
						hprovince.setText(items[i]);
					if (i == 5)
						postcode.setText(items[i]);
					if (i == 6)
						hcountry.setText(items[i]);
				}
		} else if (differ.equals("w")) {
			if (items.length == 7)
				for (int i = 0; i < items.length; i++) {
					if (i == 2)
						wstreet.setText(items[i]);
					if (i == 3)
						wcity.setText(items[i]);
					if (i == 4)
						wprovince.setText(items[i]);
					if (i == 5)
						postcode.setText(items[i]);
					if (i == 6)
						wcountry.setText(items[i]);
				}
		}
	}

	/* 得到当前选择的项 */
	@FXML
	public void getChoice() {
		agroup = group.getValue();
		// 判断是否选择新建组
		if (agroup.equals("新建组"))
			newGroup(bean);               //弹出新建组对话框
	}

	/* 编辑联系人 */
	@FXML
	public void confirm() {
		try {
			mcontroller.map.get(oldgroup).remove(bean);
			modifyBean(bean);
			mcontroller.map.get(agroup).add(bean);
			mcontroller.table.getSelectionModel().clearSelection();
			mcontroller.initAllBeans(mcontroller.map);
			mcontroller.initAllGroups(mcontroller.map);
			mcontroller.fillTable(mcontroller.map.get(agroup));
			mcontroller.updateGroupButton();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		stage.close();
	}

	/* 封装数据到联系人对象,其中名字，手机，email不能为空 */
	private void modifyBean(AddressBean bean) throws Exception {

		String regEx = "";

		// 姓名
		String aname = name.getText();
		if (aname == null || aname.trim().isEmpty()) { // 校验名字
			throw new Exception("名字不能为空");
		}
		bean.setName(aname);

		// 电话
		String atele = telephone.getText();
		regEx = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
		if (!atele.trim().isEmpty() && !CheckTool.isMatched(regEx, atele)) {
			throw new Exception("电话号码格式错误");
		}
		bean.setTelephone(atele);

		// 手机
		String amobile = moiblephone.getText();
		regEx = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
		if (amobile == null || amobile.trim().isEmpty()) {
			throw new Exception("手机号码不能为空");
		}
		if (!CheckTool.isMatched(regEx, amobile)) {
			throw new Exception("手机号码格式错误");
		}
		bean.setMobilephone(amobile);

		// 邮箱
		String aemail = email.getText();
		regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		if (aemail == null || aemail.trim().isEmpty()) {
			throw new Exception("email不能为空");
		}
		if (!CheckTool.isMatched(regEx, aemail)) {
			throw new Exception("email格式错误");
		}
		bean.setEmail(aemail);

		// 生日
		String abrith = birthday.getText();
		regEx = "^\\d{4}-\\d{1,2}-\\d{1,2}";
		if (!abrith.trim().isEmpty() && !CheckTool.isMatched(regEx, abrith)) {
			throw new Exception("生日格式错误");
		}
		bean.setBirthday(abrith);

		// 个人主页
		String aindex = index.getText();
		bean.setIndex(aindex);

		// 邮编
		String apostcode = postcode.getText();
		bean.setPostcode(apostcode);

		// 工作地址
		StringBuilder sb = new StringBuilder();
		sb.append(";");
		sb.append(";");
		String wst = wstreet.getText(); // 街道
		if (wst.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wst + ";");

		String wct = wcity.getText(); // 城市
		if (wct.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wct + ";");

		String wpro = wprovince.getText(); // 省份
		if (wpro.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wpro + ";");
		String wco = wcountry.getText(); // 国家
		if (wco.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wco);
		bean.setWorkplace(sb.toString());

		// 家庭住址
		StringBuilder sbc = new StringBuilder();
		sbc.append(";");
		sbc.append(";");
		String hst = hstreet.getText(); // 街道
		if (hst.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hst + ";");

		String hct = hcity.getText(); // 城市
		if (hct.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hct + ";");

		String hpro = hprovince.getText(); // 省份
		if (hpro.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hpro + ";");
		String hco = hcountry.getText(); // 国家
		if (hco.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(";" + hco);
		bean.setAddress(sbc.toString());
		
		//分组
		if(!agroup.equals("新建组"))
		bean.setGroup(agroup);

		// 备注
		String atips = remarks.getText();
		bean.setRemarks(atips);

	}

	/* 修改分组时要新建别的组 */
	private void newGroup(AddressBean bean) {
		try {
			try {
				URL location = getClass().getResource("AddGroup.fxml");
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(location);
				fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
				Parent pane = fxmlLoader.load();
				AddGroupController ac = fxmlLoader.getController(); // 得到新建联系人的fxml的控制器对象
				Scene scene = new Scene(pane, 383, 233);
				Stage addstage = new Stage();
				ac.init(mcontroller, addstage); // 初始化新建面板
				addstage.setScene(scene);
				addstage.setTitle("新建组");
				addstage.setIconified(false); // 禁止最小化
				addstage.initModality(Modality.WINDOW_MODAL);
				addstage.initOwner(stage);
				addstage.show();

			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	public void cancel() {
		stage.close();
	}
}
