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
	private Button ok;     //ȷ��
	@FXML
	private Button cancel; //ȡ��
	@FXML
	private Button choose; //ѡ����
	@FXML
	private Label mychoice;//��ѡ�е���

	private String regEx; // ������ʽ
	private String agroup = ""; // ��
	private MyController mcontroller; // �����������
	private Stage stage; // ��ǰ�½���ϵ�˵���̨
	private AddressService service = new AddressService();

	public void init(MyController mcontroller, Stage stage) {
		this.mcontroller = mcontroller;
		this.stage = stage;
     
	}
	
	/*Ϊ�½�����ϵ��ѡ��һ����*/
	@FXML
	public void chooseGroup() {
		 BorderPane bPane = new BorderPane();
	     GridPane pane = new GridPane();
	     int count = 0;
	     for(String gname:mcontroller.list) {
	    	 if(!gname.equals("�½���")) {
	    		 CheckBox c = new CheckBox();
	    		 c.setText(gname);
	    		 c.setPrefWidth(130);
	    		 c.setStyle("-fx-background-color:white");
	    		 pane.add(c, 0, count++);
	    	 }
	     }
	     TextField text = new TextField();
         text.setPromptText("�½���");
	     pane.add(text, 0, count);
	     HBox h1 = new HBox(10);
	     Button save = new Button("����");
	     Button back = new Button("����");
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
	    	 StringBuilder sb = new StringBuilder();   //��¼����Ϣ
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

	/* �����ϵ�� */
	@FXML
	public void add() {
		try {
			AddressBean bean = newAddressBean();
			mcontroller.total.add(bean);
			
			String[] items = bean.getGroup().split(" ");
			if(items.length == 0) {// δ������
				mcontroller.map.get("δ����").add(bean);
			}else {
				for(String item:items) {
					if(mcontroller.map.keySet().contains(item)) {   //�Ѵ�������飬��ֱ�������ϵ��
						mcontroller.map.get(item).add(bean);
					}else if(!item.trim().isEmpty()){               //����������飬����������Ϊ��,���½���
						List<AddressBean> list = new ArrayList<AddressBean>();
						list.add(bean);
						mcontroller.map.put(item, list);
						mcontroller.list.add(item);
					}
				}
			}
            
			mcontroller.fillTable(mcontroller.total); // ��̬���±���е���ϵ���б�
			mcontroller.initAllBeans(mcontroller.map); // ��̬�������treeview�б����ϵ����Ŀ
			mcontroller.initAllGroups(mcontroller.map); // ��̬�������treeview�б����ϵ����Ŀ
			service.add(bean); // ���½���ϵ����ӵ��ļ���
			stage.close();
		} catch (Exception e) {
			WarnTool.warn(stage, e.getMessage());
		}
	}

	/* ȡ���½���ϵ�� */
	@FXML
	public void cancel() {
		stage.close();
	}

	

	/* ��װ���ݵ���ϵ�˶���,�������֣��ֻ���email����Ϊ�� */
	private AddressBean newAddressBean() throws Exception {
		String[] items = new String[11];

		// ����
		String aname = name.getText();
		if (aname == null || aname.trim().isEmpty()) { // У������
			throw new Exception("���ֲ���Ϊ��");
		} else
			items[0] = aname;

		// �绰
		String atele = telephone.getText();
		regEx = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
		if (!atele.trim().isEmpty() && !CheckTool.isMatched(regEx, atele)) {
			throw new Exception("�绰�����ʽ����");
		}
		items[1] = atele;

		// �ֻ�
		String amobile = mobilephone.getText();
		regEx = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
		if (amobile == null || amobile.trim().isEmpty()) {
			throw new Exception("�ֻ����벻��Ϊ��");
		}
		if (!CheckTool.isMatched(regEx, amobile)) {
			throw new Exception("�ֻ������ʽ����");
		}
		items[2] = amobile;

		// ����
		String aemail = email.getText();
		regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		if (aemail == null || aemail.trim().isEmpty()) {
			throw new Exception("email����Ϊ��");
		}
		if (!CheckTool.isMatched(regEx, aemail)) {
			throw new Exception("email��ʽ����");
		}
		items[3] = aemail;

		// ����
		String abrith = birthday.getText();
		regEx = "^\\d{4}-\\d{1,2}-\\d{1,2}";
		if (!abrith.trim().isEmpty() && !CheckTool.isMatched(regEx, abrith)) {
			throw new Exception("���ո�ʽ����");
		}
		items[4] = abrith;

		// ������ҳ
		String aindex = index.getText();
		items[5] = aindex;

		// �ʱ�
		String apostcode = postcode.getText();
		items[8] = apostcode;

		// ������ַ
		StringBuilder sb = new StringBuilder();
		sb.append(";");
		sb.append(";");
		String wst = wstreet.getText(); // �ֵ�
		if (wst == null || wst.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wst);
		String wct = wcity.getText(); // ����
		if (wct == null || wct.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wct);
		if (apostcode == null || apostcode.trim().isEmpty()) { // �ʱ�
			sb.append(";");
		} else
			sb.append(";" + apostcode);
		String wpro = wprovince.getText(); // ʡ��
		if (wpro == null || wpro.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wpro);
		String wco = wcountry.getText(); // ����
		if (wco == null || wco.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wco);
		items[6] = sb.toString();

		// ��ͥסַ
		StringBuilder sbu = new StringBuilder();
		sbu.append(";");
		sbu.append(";");
		String hst = hstreet.getText(); // �ֵ�
		if (hst == null || hst.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(hst);
		String hct = hcity.getText(); // ����
		if (hct == null || hct.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hct);
		if (apostcode == null || apostcode.trim().isEmpty()) { // �ʱ�
			sbu.append(";");
		} else
			sbu.append(";" + apostcode);
		String hpro = hprovince.getText(); // ʡ��
		if (hpro == null || hpro.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hpro);
		String hco = hcountry.getText(); // ����
		if (hco == null || hco.trim().isEmpty())
			sbu.append(";");
		else
			sbu.append(";" + hco);
		items[7] = sbu.toString();

		// ����
		items[9] = mychoice.getText();

		// ��ע
		String atips = tips.getText();
		items[10] = atips;

		return AddressBeanTool.toBean(items);
	}

}
