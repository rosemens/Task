package com.scau.address.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.scau.address.bean.AddressBean;
import com.scau.address.service.AddressService;
import com.scau.address.utils.AddressBeanTool;
import com.scau.address.utils.CheckTool;
import com.scau.address.utils.WarnTool;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
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
	private Button ok;     //确定
	@FXML
	private Button cancel; //取消
	@FXML
	private Button choose; //选择组
	@FXML
	private Label mychoice;//我选中的组

	private String regEx; // 正则表达式
	private String agroup = ""; // 组
	private MyController mcontroller; // 主控制类对象
	private Stage stage; // 当前新建联系人的舞台
	private AddressService service = new AddressService();

	public void init(MyController mcontroller, Stage stage) {
		this.mcontroller = mcontroller;
		this.stage = stage;
     
	}
	
	/*为新建的联系人选择一个组*/
	@FXML
	public void chooseGroup() {
		 BorderPane bPane = new BorderPane();
	     GridPane pane = new GridPane();
	     int count = 0;
	     for(String gname:mcontroller.list) {
	    	 if(!gname.equals("新建组")) {
	    		 CheckBox c = new CheckBox();
	    		 c.setText(gname);
	    		 c.setPrefWidth(130);
	    		 c.setStyle("-fx-background-color:white");
	    		 pane.add(c, 0, count++);
	    	 }
	     }
	     TextField text = new TextField();
         text.setPromptText("新建组");
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
	    	 StringBuilder sb = new StringBuilder();   //记录组信息
	    	 int num = 0;                              //
	    	for(Node c:pane.getChildren()) {
	    		if(c instanceof CheckBox) {
	    			if(((CheckBox) c).isSelected()) {
	    				num++;
	    				sb.append(((CheckBox) c).getText());
	    				if(num < pane.getChildren().size() - 1) {
	    					sb.append(" ");
	    				}
	    			}
	    		}
	    	}
	    	if(!text.getText().trim().isEmpty()) {
	    		if(num != 0)
	    			sb.append(" ");
	    		sb.append(text.getText());
	    	}
	    	mychoice.setText(sb.toString());
	    	num = 0;
	    	cstage.close();
	     });
	}

	/* 添加联系人 */
	@FXML
	public void add() {
		try {
			AddressBean bean = newAddressBean();
			mcontroller.total.add(bean);
			
			String[] items = bean.getGroup().split(" ");
			if(items.length == 0) {// 未设置组
				mcontroller.map.get("未分组").add(bean);
			}else {
				for(String item:items) {
					if(mcontroller.map.keySet().contains(item)) {   //已存在这个组，则直接添加联系人
						mcontroller.map.get(item).add(bean);
					}else if(!item.trim().isEmpty()){               //不存在这个组，并且组名不为空,则新建组
						List<AddressBean> list = new ArrayList<AddressBean>();
						list.add(bean);
						mcontroller.map.put(item, list);
						mcontroller.list.add(item);
					}
				}
			}
            
			mcontroller.fillTable(mcontroller.total); // 动态更新表格中的联系人列表
			mcontroller.initAllBeans(mcontroller.map); // 动态更新左侧treeview列表的联系人项目
			mcontroller.initAllGroups(mcontroller.map); // 动态更新左侧treeview列表的联系组项目
			service.add(bean); // 将新建联系人添加到文件中
			stage.close();
		} catch (Exception e) {
			WarnTool.warn(stage, e.getMessage());
		}
	}

	/* 取消新建联系人 */
	@FXML
	public void cancel() {
		stage.close();
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
		items[9] = mychoice.getText();

		// 备注
		String atips = tips.getText();
		items[10] = atips;

		return AddressBeanTool.toBean(items);
	}

}
