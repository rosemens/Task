package com.scau.address.panes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scau.address.bean.AddressBean;
import com.scau.address.service.AddressService;
import com.scau.address.utils.CSVTool;
import com.scau.address.utils.CheckTool;
import com.scau.address.utils.GroupsTool;
import com.scau.address.utils.VCFTool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyController {
	@FXML
	private SplitPane s1;
	@FXML
	private SplitPane s2;
	@FXML
	private SplitPane s3;

	@FXML
	private MenuButton m1;
	@FXML
	private MenuButton m2;

	@FXML
	private TreeView<String> t1;
	@FXML
	private TreeView<String> t2;
	@FXML
	private Button b1; // add
	@FXML
	private Button b2; // delete

	@FXML
	private TextField search;// search

	@FXML
	private TableView<AddressBean> table;
	@FXML
	private TableColumn<AddressBean, CheckBox> col0;
	@FXML
	private TableColumn<AddressBean, String> col1;
	@FXML
	private TableColumn<AddressBean, String> col2;
	@FXML
	private TableColumn<AddressBean, String> col3;
	@FXML
	private TableColumn<AddressBean, String> col4;

	private Object flag = 1; // ��ʾt1,t2��ѡ�е�TreeItem

	@FXML
	public List<String> list = new ArrayList<String>(); // �Ѵ��ڵ���
	@FXML
	private Stage primaryStage;
	@FXML
	private AddressService service = new AddressService(); // ҵ���������ɾ����...��
	@FXML
	private List<AddressBean> beans = new ArrayList<AddressBean>(); // �����ļ��е�������ϵ��
	@FXML
	public List<AddressBean> total = new ArrayList<AddressBean>(); // ��ǰ�û�������ϵ��
	@FXML
	public Map<String, List<AddressBean>> map = new HashMap<>(); // �����鼰���Ӧ����ϵ���б�

	private String cgroup = ""; // ��ѡ�е���
	private int f = 0; // 0��ʾɾ������ϵ���Ǵ���������ɾ��

	/* ��ʼ������ */
	public void init(Stage primaryStage) {

		this.primaryStage = primaryStage;
		total = service.getAll(); // �õ�������ϵ��

		map = GroupsTool.getGroups(total);
		for (String key : map.keySet()) {
			if (!key.equals("δ����"))
				list.add(key);
		}

		list.add("�½���");
		for (String group : list) {
			MenuItem item1 = new MenuItem(group);
			MenuItem item2 = new MenuItem(group);
			m1.getItems().addAll(item1);
			m2.getItems().addAll(item2);
		}

		initAllBeans(map); // ��̬������ϵ�˱�ǩ
		initAllGroups(map); // ��̬������ı�ǩ

		update();

		if (total != null && total.size() > 0)
			fillTable(total);
	}

	/* �����ϵ�� */
	@FXML
	public void add(ActionEvent event) {
		try {
			URL location = getClass().getResource("newBean.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			AddController ac = fxmlLoader.getController(); // �õ��½���ϵ�˵�fxml�Ŀ���������
			Scene scene = new Scene(pane, 485, 340);
			Stage stage = new Stage();
			ac.init(this, stage); // ��ʼ���½����
			stage.setScene(scene);
			stage.setTitle("�½���ϵ��");
			stage.setIconified(false); // ��ֹ��С��
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* ɾ��ѡ�е��б��е���ϵ�� */
	@FXML
	public void delete() {
		List<AddressBean> delist = new ArrayList<AddressBean>(); // ��ѡ��ɾ���Ķ�����б�

		if (flag.equals(1) || flag.equals(2)) { // ѡ�е��Ƿ��ǡ�ȫ����ϵ�ˡ��е���ϵ��
			removeBean(delist, total); // �����ǡ�δ���顯����ϵ��,�����������ɾ��
		} else {
			for (String key : map.keySet()) { // �ж�ѡ�е����ĸ������ϵ��
				if (flag.equals(key)) {
					removeBean(delist, map.get(key));
					break;
				}
			}
		}
		initAllBeans(map);
		initAllGroups(map);
		service.delete(total);

	}

	/* �Ƴ���ϵ�� */
	private void removeBean(List<AddressBean> delist, List<AddressBean> slist) {
		if (slist == total) { // ����������ɾ����ϵ��
			f = 0;
			remove(delist, slist);
			for (Map.Entry<String, List<AddressBean>> entrySet : map.entrySet()) {
				if (entrySet.getValue() != null && entrySet.getValue().size() > 0) {
					remove(delist, entrySet.getValue());
				}
			}
		} else { // ��ĳ������ɾ����ϵ��
			f = 1;
			remove(delist, slist);
		}
		if (flag.equals(2))
			fillTable(map.get("δ����"));
		else fillTable(slist);
	}

	/* �������Ƴ���ϵ�� */
	private void remove(List<AddressBean> delist, List<AddressBean> slist) {
		for (AddressBean bean : slist) { // ѭ������Ҫɾ������ϵ�ˣ��������б���
			if (bean.getCb().isSelected()) {
				delist.add(bean);
			}
		}

		if (slist != total) { // ���Ǵ������鶼ɾ����ϵ��
			for (AddressBean bean : delist) {
				slist.remove(bean);
				bean.getCb().setSelected(false);
				String[] gitems = bean.getGroup().split(" ");
				if (gitems.length == 1 && f == 1) { // ��ǰɾ������ϵ��ֻ����һ����
					bean.setGroup("");
					map.get("δ����").add(bean);
				} else if (gitems.length > 1 && f == 1) { // ��ǰɾ����ϵ�����ڶ����

				}

			}
		} else // ����������ɾ����ϵ��
			for (AddressBean bean : delist) {
				slist.remove(bean);
			}

	}

	/* ������ϵ�ˣ����������б� */
	@FXML
	public void searchBeans() {
		List<AddressBean> result = new ArrayList<AddressBean>(); // ������������
		String text = search.getText();
		result = service.search(text, total);
		if (result != null)
			fillTable(result);
	}

	/* ����CSV */
	@FXML
	public void importCSV(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("�����ļ�");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			beans = CSVTool.importCsvFile(file); // ����
			total = CheckTool.merge(total, beans, map); // �����ظ�����ϵ�˼ӵ��б�,���ӵ���Ӧ������б���
			fillTable(total); // �����
		}

		initAllBeans(map);
		initAllGroups(map);
		service.save(total); // ������ϵ�˵������ļ���
	}

	/* ����VCF */
	@FXML
	public void importVCF(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("�����ļ�");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("VCard", "*.vcf"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			beans = VCFTool.importVCFFile(file); // ����
			total = CheckTool.merge(total, beans, map); // �����ظ�����ϵ�˼ӵ��б�
			fillTable(total); // �����
		}

		initAllBeans(map);
		initAllBeans(map);
		service.save(total); // ������ϵ�˵������ļ���
	}

	/* ����CSV��ʽ���ļ� */
	@FXML
	public void exportCSV() {
		if (total == null || total.size() < 1) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("�����ļ�");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			CSVTool.exportCsvFile(total, file); // ����
		}
	}

	/* ����VCF��ʽ���ļ� */
	@FXML
	public void exportVCF() {
		if (total == null || total.size() < 1) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("�����ļ�");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Vcard", "*.vcf"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			VCFTool.exportVcfFile(total, file); // ����
		}
	}

	/* ѡ�������Ŀʱ��ʵʱ�����ұ߱�� */
	public void update() {
		// ��ϵ�˷���
		t1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				flag = newValue;
				if (flag != null && flag.equals(1))
					fillTable(total);
				else if (flag != null && flag.equals(2)) {
					fillTable(map.get("δ����"));
				}
			}
		});

		// ��ϵ�鷽��
		t2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				if (newValue != null) { // �����ѡ�еĻ�
					cgroup = newValue.getValue(); // �ж�ѡ���ĸ���
					flag = newValue.getValue();
				}

				for (String key : map.keySet()) {
					if (cgroup.equals(key)) {
						fillTable(map.get(key));
					}
				}
			}
		});
	}

	/* ��������� */
	public void fillTable(List<AddressBean> total) {
		col0.setCellValueFactory(new PropertyValueFactory<AddressBean, CheckBox>("cb"));
		col1.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("name"));
		col2.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("email"));
		col3.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("mobilephone"));
		col4.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("group"));
		table.setItems(FXCollections.observableArrayList(total));
	}

	/* ��̬����������ı�ǩ */
	public void initAllGroups(Map<String, List<AddressBean>> map) {
		TreeItem<String> root2 = new TreeItem<String>("��ϵ��");
		root2.setExpanded(true);
		for (String group : map.keySet())
			if (!group.equals("δ����"))
				root2.getChildren().add(new TreeItem<String>(group));
		t2.setRoot(root2);
	}

	/* ��̬������ϵ���б�ı�ǩ */
	public void initAllBeans(Map<String, List<AddressBean>> map) {
		TreeItem<String> root1 = new TreeItem<String>("��ϵ��");
		root1.setExpanded(true);
		root1.getChildren().add(new TreeItem<String>("������ϵ��(" + total.size() + ")"));
		if (map.get("δ����") != null)
			root1.getChildren().add(new TreeItem<String>("δ������ϵ��(" + map.get("δ����").size() + ")"));
		else
			root1.getChildren().add(new TreeItem<String>("δ������ϵ��(0)"));
		t1.setRoot(root1);
	}
}
