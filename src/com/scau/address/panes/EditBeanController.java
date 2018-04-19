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
	private String agroup = ""; // ��¼δ�޸�ǰ��ϵ����������
	private String oldgroup = "";

	public void init(AddressBean bean, Stage stage, MyController mcontroller) {
		this.bean = bean;
		this.mcontroller = mcontroller;
		this.stage = stage;
		group.getItems().addAll(FXCollections.observableArrayList(mcontroller.list));
		group.getItems().add("�½���");
		fillBean();

	}

	/* ����ϵ����Ϣ��ʾ������� */
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
			oldgroup = "δ����";
		remarks.setText(bean.getRemarks());

		fillAddress(bean.getAddress().split(";"), "h"); // h��ʾ���Ǽ�ͥ��ַ��w��ʾ���ǹ�����ַ
		fillAddress(bean.getWorkplace().split(";"), "w");
	}

	/* ����ַ,differ�����ж��Ǽ�ͥ��ַ���ǹ�����ַ ,����ȱ�� */
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

	/* �õ���ǰѡ����� */
	@FXML
	public void getChoice() {
		agroup = group.getValue();
		// �ж��Ƿ�ѡ���½���
		if (agroup.equals("�½���"))
			newGroup(bean);               //�����½���Ի���
	}

	/* �༭��ϵ�� */
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

	/* ��װ���ݵ���ϵ�˶���,�������֣��ֻ���email����Ϊ�� */
	private void modifyBean(AddressBean bean) throws Exception {

		String regEx = "";

		// ����
		String aname = name.getText();
		if (aname == null || aname.trim().isEmpty()) { // У������
			throw new Exception("���ֲ���Ϊ��");
		}
		bean.setName(aname);

		// �绰
		String atele = telephone.getText();
		regEx = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
		if (!atele.trim().isEmpty() && !CheckTool.isMatched(regEx, atele)) {
			throw new Exception("�绰�����ʽ����");
		}
		bean.setTelephone(atele);

		// �ֻ�
		String amobile = moiblephone.getText();
		regEx = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
		if (amobile == null || amobile.trim().isEmpty()) {
			throw new Exception("�ֻ����벻��Ϊ��");
		}
		if (!CheckTool.isMatched(regEx, amobile)) {
			throw new Exception("�ֻ������ʽ����");
		}
		bean.setMobilephone(amobile);

		// ����
		String aemail = email.getText();
		regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		if (aemail == null || aemail.trim().isEmpty()) {
			throw new Exception("email����Ϊ��");
		}
		if (!CheckTool.isMatched(regEx, aemail)) {
			throw new Exception("email��ʽ����");
		}
		bean.setEmail(aemail);

		// ����
		String abrith = birthday.getText();
		regEx = "^\\d{4}-\\d{1,2}-\\d{1,2}";
		if (!abrith.trim().isEmpty() && !CheckTool.isMatched(regEx, abrith)) {
			throw new Exception("���ո�ʽ����");
		}
		bean.setBirthday(abrith);

		// ������ҳ
		String aindex = index.getText();
		bean.setIndex(aindex);

		// �ʱ�
		String apostcode = postcode.getText();
		bean.setPostcode(apostcode);

		// ������ַ
		StringBuilder sb = new StringBuilder();
		sb.append(";");
		sb.append(";");
		String wst = wstreet.getText(); // �ֵ�
		if (wst.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wst + ";");

		String wct = wcity.getText(); // ����
		if (wct.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wct + ";");

		String wpro = wprovince.getText(); // ʡ��
		if (wpro.trim().isEmpty())
			sb.append(";");
		else
			sb.append(wpro + ";");
		String wco = wcountry.getText(); // ����
		if (wco.trim().isEmpty())
			sb.append(";");
		else
			sb.append(";" + wco);
		bean.setWorkplace(sb.toString());

		// ��ͥסַ
		StringBuilder sbc = new StringBuilder();
		sbc.append(";");
		sbc.append(";");
		String hst = hstreet.getText(); // �ֵ�
		if (hst.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hst + ";");

		String hct = hcity.getText(); // ����
		if (hct.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hct + ";");

		String hpro = hprovince.getText(); // ʡ��
		if (hpro.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(hpro + ";");
		String hco = hcountry.getText(); // ����
		if (hco.trim().isEmpty())
			sbc.append(";");
		else
			sbc.append(";" + hco);
		bean.setAddress(sbc.toString());
		
		//����
		if(!agroup.equals("�½���"))
		bean.setGroup(agroup);

		// ��ע
		String atips = remarks.getText();
		bean.setRemarks(atips);

	}

	/* �޸ķ���ʱҪ�½������ */
	private void newGroup(AddressBean bean) {
		try {
			try {
				URL location = getClass().getResource("AddGroup.fxml");
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(location);
				fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
				Parent pane = fxmlLoader.load();
				AddGroupController ac = fxmlLoader.getController(); // �õ��½���ϵ�˵�fxml�Ŀ���������
				Scene scene = new Scene(pane, 383, 233);
				Stage addstage = new Stage();
				ac.init(mcontroller, addstage); // ��ʼ���½����
				addstage.setScene(scene);
				addstage.setTitle("�½���");
				addstage.setIconified(false); // ��ֹ��С��
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
