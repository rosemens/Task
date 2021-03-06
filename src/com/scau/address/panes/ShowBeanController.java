package com.scau.address.panes;

import java.net.URL;

import com.scau.address.bean.AddressBean;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowBeanController {
	@FXML
	private Label name;
	@FXML
	private Label email;
	@FXML
	private Label mobilephone;
	@FXML
	private Label telephone;
	@FXML
	private Label home;
	@FXML
	private Label work;
	@FXML
	private Label birthday;
	@FXML
	private Label index;
	@FXML
	private Label postcode;
	@FXML
	private Label group;
	@FXML
	private Label remarks;
	@FXML
	private Button b1;
	@FXML
	private Button b2;

	private AddressBean bean;
	private Stage stage;
	private MyController mcontroller;
	private Stage primaryStage;

	public void init(AddressBean bean,Stage stage,MyController mcontroller,Stage primaryStage) {
		this.bean = bean;
		this.stage = stage;
		this.primaryStage = primaryStage;
		this.mcontroller = mcontroller;
		name.setText(bean.getName());
		email.setText(bean.getEmail());
		mobilephone.setText(bean.getMobilephone());
		telephone.setText(bean.getTelephone());
		birthday.setText(bean.getBirthday());
		index.setText(bean.getIndex());
		postcode.setText(bean.getPostcode());
		group.setText(bean.getGroup());
		remarks.setText(bean.getRemarks());
		home.setText(get(bean.getAddress()));
		work.setText(get(bean.getWorkplace()));
		
		/*提示信息*/
		name.setTooltip(new Tooltip(name.getText()));
		email.setTooltip(new Tooltip(email.getText()));
		mobilephone.setTooltip(new Tooltip(mobilephone.getText()));
		telephone.setTooltip(new Tooltip(telephone.getText()));
		birthday.setTooltip(new Tooltip(birthday.getText()));
		index.setTooltip(new Tooltip(index.getText()));
		postcode.setTooltip(new Tooltip(postcode.getText()));
		group.setTooltip(new Tooltip(group.getText()));
		remarks.setTooltip(new Tooltip(remarks.getText()));
		home.setTooltip(new Tooltip(home.getText()));
		work.setTooltip(new Tooltip(work.getText()));

	}

	/* 得到具体地址并去掉分号 */
	private String get(String address) {
		String[] items = address.split(";");
		StringBuilder sb = new StringBuilder();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				if (!items[i].equals(";"))
					sb.append(items[i] + " ");
			}
		}
		return sb.toString();
	}

	/* 编辑联系人 */
	@FXML
	public void edit() {
		try {
			URL location = getClass().getResource("EditBean.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			
			stage.close();                         //关闭显示窗口
			EditBeanController ebc = fxmlLoader.getController();
			Scene scene = new Scene(pane,603,474);
			Stage showstage = new Stage();
			ebc.init(bean,showstage,mcontroller);
			showstage.setScene(scene);
			showstage.setTitle("修改联系人");
			showstage.initModality(Modality.WINDOW_MODAL);
			showstage.initOwner(primaryStage);
			showstage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon2.png")));
			showstage.show();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	/* 发送消息 */
	@FXML
	public void send() {
		try {
			URL location = getClass().getResource("SendMail.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			
			stage.close();                         //关闭显示窗口
			SendMailController smc = fxmlLoader.getController();
			Scene scene = new Scene(pane,603,474);
			Stage showstage = new Stage();
			smc.init(showstage,bean);
			showstage.setScene(scene);
			showstage.setTitle("发送消息");
			showstage.initModality(Modality.WINDOW_MODAL);
			showstage.initOwner(primaryStage);
			showstage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon5.png")));
			showstage.show();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
