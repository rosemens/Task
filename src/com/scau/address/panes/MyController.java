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
import com.scau.address.utils.WarnTool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private ComboBox<String> c1;
	@FXML
	private ComboBox<String> c2;

	@FXML
	private TreeView<String> t1;
	@FXML
	private TreeView<String> t2;
	@FXML
	private Button b1; // add
	@FXML
	private Button b2; // delete
	@FXML
	private Button b3; // delete group
	@FXML
	private Button b4; // edit group
	@FXML
	private Button b5; // new group

	@FXML
	private TextField search;// search

	@FXML
	public TableView<AddressBean> table;
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

	@FXML
	private Stage primaryStage;
	@FXML
	private AddressService service = new AddressService(); // ҵ���������ɾ����...��
	@FXML
	private List<AddressBean> beans = new ArrayList<AddressBean>(); // �����ļ��е�������ϵ��
	@FXML
	public List<String> list = new ArrayList<String>(); // ��������,������ʾ�ƶ������븴�Ƶ���
	@FXML
	public List<AddressBean> total = new ArrayList<AddressBean>(); // ��ǰ�û�������ϵ��
	@FXML
	public Map<String, List<AddressBean>> map = new HashMap<>(); // �����鼰���Ӧ����ϵ���б�

	private int f = 0; // 0��ʾɾ������ϵ���Ǵ���������ɾ��
	public String flag = "������ϵ��"; // ��ʾt1,t2��ѡ�е�TreeItem

	/* ��ʼ������ */
	public void init(Stage primaryStage) {
        
		this.primaryStage = primaryStage;
		total = service.getAll(); // �õ�������ϵ��
		map = GroupsTool.getGroups(total); // �õ����еķ�����ÿ���������ϵ���б�
		List<String> groupname = GroupsTool.getGroupsName(); // �õ����е�����
		for (String name : groupname) {
			if (!map.keySet().contains(name)) { // �Ƿ����û����ϵ�˵���
				map.put(name, new ArrayList<AddressBean>());
			}
		}

		for (String key : groupname) { // ������������
			if (!key.equals("δ����"))
				list.add(key);
		}
		list.add("�½���");

		updateGroupButton(); // ���¡��ƶ����顯�롮���Ƶ��顯
		copy(); // ���Ƶ���Ķ����¼�
		move(); // �ƶ�����Ķ����¼�

		initAllBeans(map); // ��̬������ϵ�˱�ǩ
		initAllGroups(map); // ��̬������ı�ǩ

		updateTreeView(); // �������treeview,ʵʱ��������

		if (total != null && total.size() > 0)
			fillTable(total);

		showBean();
		
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
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon3.png")));
			stage.show();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/* ɾ��ѡ�е��б��е���ϵ�� */
	@FXML
	public void delete() {
		List<AddressBean> delist = new ArrayList<AddressBean>(); // ��ѡ��ɾ���Ķ�����б�

		if (flag.contains("������ϵ��") || flag.contains("δ������ϵ��")) { // ѡ�е��Ƿ��ǡ�ȫ����ϵ�ˡ��е���ϵ��
			isSelected(delist,total);
			removeBean(delist, total); // �����ǡ�δ���顯����ϵ��,�����������ɾ��
		} else {
			for (String key : map.keySet()) { // �ж�ѡ�е����ĸ������ϵ��
				if (flag.equals(key)) {
					isSelected(delist,map.get(key));
					removeBean(delist, map.get(key));
					break;
				}
			}
		}
		initAllBeans(map);
		initAllGroups(map);
	}
	
	public void isSelected(List<AddressBean> delist,List<AddressBean> slist) {
		boolean mark = false;//�����ж�����ѡ����ϵ��
		for (AddressBean bean : slist) { // ѭ������Ҫɾ������ϵ�ˣ��������б���
			if (bean.getCb().isSelected()) {
				delist.add(bean);
				mark = true;
			}
		}
		if(!mark) {
			WarnTool.warn(primaryStage, "��ѡ��Ҫɾ������ϵ��");
			return;
		}
	}

	/* �½��� */
	@FXML
	public void addGroup() {
		try {
			URL location = getClass().getResource("AddGroup.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			AddGroupController ac = fxmlLoader.getController(); // �õ��½���ϵ�˵�fxml�Ŀ���������
			Scene scene = new Scene(pane, 383, 233);
			Stage stage = new Stage();
			ac.init(this, stage); // ��ʼ���½����
			stage.setScene(scene);
			stage.setTitle("�½���");
			stage.setIconified(false); // ��ֹ��С��
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon3.png")));
			stage.show();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/* ɾ���� */
	@FXML
	public void deleteGroup() {
		if (flag.contains("������ϵ��") || flag.contains("δ������ϵ��")) {// ���ûѡ���飬ʲôҲ����
		   WarnTool.warn(primaryStage, "����ѡ��Ҫɾ������");
		   return;
		}
		else {
			for (AddressBean bean : map.get(flag)) { // ѭ�����ñ�ѡ�������ϵ��
				String[] gitems = bean.getGroup().split(" ");
				if (gitems.length == 1) { // ��ϵ��ֻ����һ����
					bean.setGroup("");
					map.get("δ����").add(bean);
				} else if (gitems.length > 1) { // ��ϵ�����ڶ����
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < gitems.length; i++) {
						if (!flag.equals(gitems[i])) {
							sb.append(gitems[i]);
							if (i < gitems.length - 1)
								sb.append(" ");
						}
					}
					bean.setGroup(sb.toString());
				}
			}
			map.remove(flag);
			list.remove((String)flag);
		}
		initAllBeans(map);
		initAllGroups(map);
		updateGroupButton();
		fillTable(new ArrayList<AddressBean>());
	}

	@FXML
	/* �༭�� */
	public void edit() {
		if (flag.contains("������ϵ��") || flag.contains("δ������ϵ��")) {
			WarnTool.warn(primaryStage, "����ѡ��Ҫ�޸ĵ���");
			return;
		}
		try {
			URL location = getClass().getResource("EditGroup.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			EditController ec = fxmlLoader.getController();

			Scene scene = new Scene(pane, 383, 233);
			Stage stage = new Stage();
			ec.init(this, stage);

			stage.setScene(scene);
			stage.setTitle("�༭��");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon4.png")));
			stage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
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
			total = CheckTool.merge(total, beans, map, list); // �����ظ�����ϵ�˼ӵ��б�,���ӵ���Ӧ������б���
			updateGroupButton();// ����MenuButton
			fillTable(total); // �����
		}
		initAllBeans(map);
		initAllGroups(map);
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
			total = CheckTool.merge(total, beans, map, list); // �����ظ�����ϵ�˼ӵ��б�
			updateGroupButton();// ����MenuButton
			fillTable(total); // �����
		}

		initAllBeans(map);
		initAllBeans(map);
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
	
	/* ����'�ƶ�����'��'���Ƶ���'*/
	public void updateGroupButton() {
		c1.getItems().clear();
		c1.setPromptText("���Ƶ���");
		c2.getItems().clear();
		c2.setPromptText("�ƶ�����");
		list.remove("�½���");       //�����½����������һ��
		list.add("�½���");
		c1.getItems().addAll(FXCollections.observableArrayList(list));
		c2.getItems().addAll(FXCollections.observableArrayList(list));
	}

	/* ѡ�������Ŀʱ��ʵʱ�����ұ߱�� */
	public void updateTreeView() {
		// ��ϵ�˷���
		t1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				if (newValue != null) {
					flag = newValue.getValue();
					table.getSelectionModel().clearSelection(); // ȡ��ѡ�б���һ��
					t2.getSelectionModel().clearSelection(); // ȡ��ѡ�������ϵ��
					if (newValue.getValue().contains("������ϵ��")) {
						fillTable(total);
					} else if (newValue.getValue().contains("δ������ϵ��")) {
						fillTable(map.get("δ����"));
					}
					updateGroupButton();
				}
			}
		});

		// ��ϵ�鷽��
		t2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				if (newValue != null) { // �����ѡ�еĻ�
					table.getSelectionModel().clearSelection(); // ȡ�������ĳһ�е�ѡ��
					t1.getSelectionModel().clearSelection(); // ȡ�������ϵ�˱�ǩ��ѡ��
					// �ж�ѡ���ĸ���
					flag = newValue.getValue();
					for (String key : map.keySet())
						if (key.equals(flag))
							fillTable(map.get(flag));
					updateGroupButton();
				}
			}
		});
	}
	
	/*���Ƶ���*/
	public void copy() {
		c1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue!=null) {
					if(newValue.equals("�½���")) {   // ���Ƶ��½���
						//��δ���
						return;
					}
					boolean f = false;  //�����ж���û��ѡ����ϵ��
					for (AddressBean bean : total) {
						if (bean.getCb().isSelected()) { // �ж�ѡ�е�����Щ��ϵ��
							f = true;
							if (bean.getGroup().contains(newValue)) { // ��ϵ���Ѿ�����������
								continue;
							} else {
								if (bean.getGroup().trim().isEmpty()) {// ��ϵ�˲������κ���
									bean.setGroup(newValue);
									map.get("δ����").remove(bean);
								}
								else {
									StringBuilder sb = new StringBuilder();
									sb.append(bean.getGroup() + " " + newValue);
									bean.setGroup(sb.toString());
								}
								map.get(newValue).add(bean);
							}
						}
						bean.getCb().setSelected(false);
					}
					if(!f) {
						WarnTool.warn(primaryStage, "����ѡ����ϵ��");
					}else {
						initAllBeans(map);
						initAllGroups(map);
						fillTable(map.get(newValue));
						f = false;
					}
				}	
			}
		});
	}
	
	/*�ƶ�����*/
	public void move() {
		c2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				if(newValue!=null) {
					if (flag.contains("������ϵ��") || flag.contains("δ������ϵ��")) { // û���ƶ�Ȩ��
						WarnTool.warn(primaryStage, "����ѡ��ĳ�����ٽ����ƶ�");
						return;
					}else {
						if(newValue.equals("�½���")) {   // ���Ƶ��½���
							//��δ���
							return;
						}
						boolean f = false;
						for (AddressBean bean : total) {
							if (bean.getCb().isSelected()) { // �ж�ѡ�е�����Щ��ϵ��
								f = true;
								if (bean.getGroup().contains(newValue)) { // ��ϵ���Ѿ�����������
									bean.getCb().setSelected(false);
									continue;
								} else {
									if (bean.getGroup().trim().isEmpty()) // ��ϵ�˲������κ���
										bean.setGroup(newValue);
									else {
										String[] items = bean.getGroup().split(" ");
										if(items.length == 1)              //��ϵ��ֻ����һ����
									    bean.setGroup(newValue);
										else {
										StringBuilder sb = new StringBuilder();
										for(int i=0;i<items.length;i++) {
											if(flag.equals(items[i])) {    //��ǰ��ϵ����������
												items[i] = newValue;        
											}
											if(i<=items.length-1)
											sb.append(items[i]+" ");
											else sb.append(items[i]);
										}
										bean.setGroup(sb.toString());
										}
									}
									map.get(flag).remove(bean);
									map.get(newValue).add(bean);
								}
							}
							bean.getCb().setSelected(false);
						}
						if(!f) {
							WarnTool.warn(primaryStage, "����ѡ����ϵ��");
						}else {
							initAllBeans(map);
							initAllGroups(map);
							fillTable(map.get(newValue));
							f=false;
						}
					
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
		table.getSelectionModel().clearSelection(); // ȡ��ѡ��ĳ��
	}

	/* ��̬����������ı�ǩ */
	public void initAllGroups(Map<String, List<AddressBean>> map) {
		t2.getSelectionModel().clearSelection();
		 Node rootIcon = new ImageView(  
			        new Image(getClass().getResourceAsStream("/com/scau/address/images/icon.jpg"))  
			    ); 
		TreeItem<String> root2 = new TreeItem<String>("��ϵ��",rootIcon);
		root2.setExpanded(true);
		for (String group : map.keySet())
			if (!group.equals("δ����"))
				root2.getChildren().add(new TreeItem<String>(group));
		t2.setRoot(root2);

	}

	/* ��̬������ϵ���б�ı�ǩ */
	public void initAllBeans(Map<String, List<AddressBean>> map) {
		t1.getSelectionModel().clearSelection();
		Node rootIcon = new ImageView(  
		        new Image(getClass().getResourceAsStream("/com/scau/address/images/icon2.png"))  
		    );
		TreeItem<String> root1 = new TreeItem<String>("��ϵ��",rootIcon);
		root1.setExpanded(true);
		root1.getChildren().add(new TreeItem<String>("������ϵ��(" + total.size() + ")"));
		if (map.get("δ����") != null)
			root1.getChildren().add(new TreeItem<String>("δ������ϵ��(" + map.get("δ����").size() + ")"));
		else
			root1.getChildren().add(new TreeItem<String>("δ������ϵ��(0)"));
		t1.setRoot(root1);
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
		if (flag.contains("δ������ϵ��"))
			fillTable(map.get("δ����"));
		else
			fillTable(slist);
	}

	/* �������Ƴ���ϵ�� */
	private void remove(List<AddressBean> delist, List<AddressBean> slist) {
		 if (slist != total) { // ���Ǵ������鶼ɾ����ϵ��
			for (AddressBean bean : delist) {
				slist.remove(bean);
				bean.getCb().setSelected(false);
				String[] gitems = bean.getGroup().split(" ");
				if (gitems.length == 1 && f == 1) { // ��ǰɾ������ϵ��ֻ����һ����
					bean.setGroup("");
					map.get("δ����").add(bean);
				} else if (gitems.length > 1 && f == 1) { // ��ǰɾ����ϵ�����ڶ����
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < gitems.length; i++) {
						if (!flag.equals(gitems[i])) {
							sb.append(gitems[i]);
							if (i < gitems.length - 1)
								sb.append(" ");
						}
					}
					bean.setGroup(sb.toString());
				}
			}
		} else // ����������ɾ����ϵ��
			for (AddressBean bean : delist) {
				slist.remove(bean);
			}
	}

	/* ��ʾ��ϵ�˵�������Ϣ */
	public void showBean() {

		try {
			URL location = getClass().getResource("ShowBean.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();

			ShowBeanController sbc = fxmlLoader.getController();
			Scene scene = new Scene(pane, 603, 474);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("��ϸ��Ϣ");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon2.png")));
			MyController mc = this;
			table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AddressBean>() {
				@Override
				public void changed(ObservableValue<? extends AddressBean> observable, AddressBean oldValue,
						AddressBean newValue) {
					if (newValue != null) {
						sbc.init(newValue, stage, mc, primaryStage);
						stage.show();
					}
				}
			});

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
