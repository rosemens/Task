package com.scau.address.panes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.scau.address.bean.AddressBean;
import com.scau.address.utils.CheckTool;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	private Label group;
	@FXML
	private Label mod;

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
	private String oldgroup = ""; // 记录未修改前联系人所属的组

	public void init(AddressBean bean, Stage stage, MyController mcontroller) {
		this.bean = bean;
		this.mcontroller = mcontroller;
		this.stage = stage;
		
		oldgroup = bean.getGroup();
		group.setTooltip(new Tooltip(group.getText()));
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
		group.setText(bean.getGroup());
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
    
	@FXML
	/* 得到当前选择的项 */
	public void modify() {
		String[] items = bean.getGroup().split(" "); 
	     BorderPane bPane = new BorderPane();
	     GridPane pane = new GridPane();
	     int count = 0;
	     for(String gname:mcontroller.list) {
	    	 if(!gname.equals("新建组")) {
	    		 CheckBox c = new CheckBox();
	    		 c.setText(gname);
	    		 c.setPrefWidth(130);
	    		 c.setStyle("-fx-background-color:white");
	    		 for(String item:items) {
	    			 if(gname.equals(item) )
	    				 c.setSelected(true);
	    		 }
	    		 pane.add(c, 0, count++);
	    	 }
	     }
	     TextField text = new TextField();
         text.setPromptText("输入新组名");
	     pane.add(text, 0, count);
	     HBox h1 = new HBox(10);
	     Button save = new Button("保存");
	     Button back = new Button("返回");
	     h1.getChildren().addAll(save,back);
	     h1.setAlignment(Pos.CENTER_RIGHT);
	    	
	     bPane.setCenter(pane);
	     bPane.setBottom(h1);
	     pane.setAlignment(Pos.CENTER);
	     pane.setVgap(20);
	     bPane.setStyle("-fx-background-color: #D1EEEE");
	     pane.setStyle("-fx-background-color: #D1EEEE");
	     h1.setStyle("-fx-background-color: #D1EEEE");
	     Scene scene = new Scene(bPane,300,200);
	     Stage cstage = new Stage();
	     cstage.setScene(scene);
	     cstage.initModality(Modality.WINDOW_MODAL);
	     cstage.initOwner(stage);
	     cstage.show();
	     save.setOnAction(e->{
	    	 StringBuilder sb = new StringBuilder();
	    	 int num = 0;
	    	 for(Node c:pane.getChildren()) {
	    		 if(c instanceof CheckBox) {
	    			 if(((CheckBox) c).isSelected()) {
	    				 num++;
	    				 sb.append(((CheckBox)c).getText());
	    				 if(num < pane.getChildren().size() - 1) {
	    					 sb.append(" ");
	    				 }
	    			 }
	    		 }
	    	 }
	    	 if(!text.getText().trim().isEmpty()) {
	    		 if(num != 0)           //没有选中其他组
	    		 sb.append(" ");
	    		 sb.append(text.getText());
	    	 }
	    	 num = 0;
             group.setText(sb.toString());
             group.getTooltip().setText(sb.toString());
             cstage.close();
	     });
	     
	     back.setOnAction(e->{
	    	 cstage.close();
	     });
	}

	/* 编辑联系人 */
	@FXML
	public void confirm() {
		try {
			for(String item:oldgroup.split(" ")) {  //将联系人从原来的组里面移除
				if(!item.trim().isEmpty())
				mcontroller.map.get(item).remove(bean);
				else mcontroller.map.get("未分组").remove(bean);  //如果联系人原来不属于任何组
			}
			modifyBean(bean);
			bean.setGroup(group.getText());
			if(bean.getGroup().trim().isEmpty()) {     //不属于任何分组
				mcontroller.map.get("未分组").add(bean);
			}
			else for(String item:bean.getGroup().split(" ")) {
				if(mcontroller.map.keySet().contains(item))
				mcontroller.map.get(item).add(bean);
				else if(!item.trim().isEmpty()){
					List<AddressBean> ngroup = new ArrayList<AddressBean>();
					ngroup.add(bean);
					mcontroller.map.put(item, ngroup);
					mcontroller.list.add(item);
				}
			}
			mcontroller.table.getSelectionModel().clearSelection();
			mcontroller.initAllBeans(mcontroller.map);
			mcontroller.initAllGroups(mcontroller.map);
			mcontroller.updateGroupButton();
			mcontroller.fillTable(mcontroller.total);

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

		// 备注
		String atips = remarks.getText();
		bean.setRemarks(atips);

	}

	@FXML
	public void cancel() {
		stage.close();
	}
}
