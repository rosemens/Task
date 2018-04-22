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
	private AddressService service = new AddressService(); // 业务对象（增、删、改...）
	@FXML
	private List<AddressBean> beans = new ArrayList<AddressBean>(); // 导入文件中的所有联系人
	@FXML
	public List<String> list = new ArrayList<String>(); // 所有组名,用在显示移动分组与复制到组
	@FXML
	public List<AddressBean> total = new ArrayList<AddressBean>(); // 当前用户所有联系人
	@FXML
	public Map<String, List<AddressBean>> map = new HashMap<>(); // 所有组及其对应的联系人列表

	private int f = 0; // 0表示删除的联系人是从所有组中删除
	public String flag = "所有联系人"; // 表示t1,t2中选中的TreeItem

	/* 初始化操作 */
	public void init(Stage primaryStage) {
        
		this.primaryStage = primaryStage;
		total = service.getAll(); // 得到所有联系人
		map = GroupsTool.getGroups(total); // 得到所有的分组与每个分组的联系人列表
		List<String> groupname = GroupsTool.getGroupsName(); // 得到所有的组名
		for (String name : groupname) {
			if (!map.keySet().contains(name)) { // 是否存在没有联系人的组
				map.put(name, new ArrayList<AddressBean>());
			}
		}

		for (String key : groupname) { // 保存所有组名
			if (!key.equals("未分组"))
				list.add(key);
		}
		list.add("新建组");

		updateGroupButton(); // 更新‘移动到组’与‘复制到组’
		copy(); // 复制到组的动作事件
		move(); // 移动到组的动作事件

		initAllBeans(map); // 动态设置联系人标签
		initAllGroups(map); // 动态设置组的标签

		updateTreeView(); // 监听左侧treeview,实时更新数据

		if (total != null && total.size() > 0)
			fillTable(total);

		showBean();
		
	}

	/* 添加联系人 */
	@FXML
	public void add(ActionEvent event) {
		try {
			URL location = getClass().getResource("newBean.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			AddController ac = fxmlLoader.getController(); // 得到新建联系人的fxml的控制器对象
			Scene scene = new Scene(pane, 485, 340);
			Stage stage = new Stage();
			ac.init(this, stage); // 初始化新建面板
			stage.setScene(scene);
			stage.setTitle("新建联系人");
			stage.setIconified(false); // 禁止最小化
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon3.png")));
			stage.show();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/* 删除选中的列表中的联系人 */
	@FXML
	public void delete() {
		List<AddressBean> delist = new ArrayList<AddressBean>(); // 被选中删除的对象的列表

		if (flag.contains("所有联系人") || flag.contains("未分组联系人")) { // 选中的是否是‘全部联系人’中的联系人
			isSelected(delist,total);
			removeBean(delist, total); // 或者是‘未分组’的联系人,是则从所有组删除
		} else {
			for (String key : map.keySet()) { // 判断选中的是哪个组的联系人
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
		boolean mark = false;//用来判断有无选中联系人
		for (AddressBean bean : slist) { // 循环遍历要删除的联系人，并放入列表中
			if (bean.getCb().isSelected()) {
				delist.add(bean);
				mark = true;
			}
		}
		if(!mark) {
			WarnTool.warn(primaryStage, "请选择要删除的联系人");
			return;
		}
	}

	/* 新建组 */
	@FXML
	public void addGroup() {
		try {
			URL location = getClass().getResource("AddGroup.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent pane = fxmlLoader.load();
			AddGroupController ac = fxmlLoader.getController(); // 得到新建联系人的fxml的控制器对象
			Scene scene = new Scene(pane, 383, 233);
			Stage stage = new Stage();
			ac.init(this, stage); // 初始化新建面板
			stage.setScene(scene);
			stage.setTitle("新建组");
			stage.setIconified(false); // 禁止最小化
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon3.png")));
			stage.show();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/* 删除组 */
	@FXML
	public void deleteGroup() {
		if (flag.contains("所有联系人") || flag.contains("未分组联系人")) {// 如果没选择组，什么也不做
		   WarnTool.warn(primaryStage, "请先选择要删除的组");
		   return;
		}
		else {
			for (AddressBean bean : map.get(flag)) { // 循环设置被选中组的联系人
				String[] gitems = bean.getGroup().split(" ");
				if (gitems.length == 1) { // 联系人只属于一个组
					bean.setGroup("");
					map.get("未分组").add(bean);
				} else if (gitems.length > 1) { // 联系人属于多个组
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
	/* 编辑组 */
	public void edit() {
		if (flag.contains("所有联系人") || flag.contains("未分组联系人")) {
			WarnTool.warn(primaryStage, "请先选择要修改的组");
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
			stage.setTitle("编辑组");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/icon4.png")));
			stage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* 搜索联系人，返回整个列表 */
	@FXML
	public void searchBeans() {
		List<AddressBean> result = new ArrayList<AddressBean>(); // 存放搜索结果集
		String text = search.getText();
		result = service.search(text, total);
		if (result != null)
			fillTable(result);
	}

	/* 导入CSV */
	@FXML
	public void importCSV(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			beans = CSVTool.importCsvFile(file); // 导入
			total = CheckTool.merge(total, beans, map, list); // 将不重复的联系人加到列表,并加到相应的组的列表中
			updateGroupButton();// 更新MenuButton
			fillTable(total); // 填充表格
		}
		initAllBeans(map);
		initAllGroups(map);
	}

	/* 导入VCF */
	@FXML
	public void importVCF(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("VCard", "*.vcf"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			beans = VCFTool.importVCFFile(file); // 导入
			total = CheckTool.merge(total, beans, map, list); // 将不重复的联系人加到列表
			updateGroupButton();// 更新MenuButton
			fillTable(total); // 填充表格
		}

		initAllBeans(map);
		initAllBeans(map);
	}

	/* 导出CSV格式的文件 */
	@FXML
	public void exportCSV() {
		if (total == null || total.size() < 1) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			CSVTool.exportCsvFile(total, file); // 导出
		}
	}

	/* 导出VCF格式的文件 */
	@FXML
	public void exportVCF() {
		if (total == null || total.size() < 1) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Vcard", "*.vcf"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			VCFTool.exportVcfFile(total, file); // 导出
		}
	}
	
	/* 更新'移动到组'与'复制到组'*/
	public void updateGroupButton() {
		c1.getItems().clear();
		c1.setPromptText("复制到组");
		c2.getItems().clear();
		c2.setPromptText("移动到组");
		list.remove("新建组");       //保持新建组总在最后一行
		list.add("新建组");
		c1.getItems().addAll(FXCollections.observableArrayList(list));
		c2.getItems().addAll(FXCollections.observableArrayList(list));
	}

	/* 选中左侧栏目时，实时更新右边表格 */
	public void updateTreeView() {
		// 联系人方面
		t1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				if (newValue != null) {
					flag = newValue.getValue();
					table.getSelectionModel().clearSelection(); // 取消选中表格的一行
					t2.getSelectionModel().clearSelection(); // 取消选中左侧联系组
					if (newValue.getValue().contains("所有联系人")) {
						fillTable(total);
					} else if (newValue.getValue().contains("未分组联系人")) {
						fillTable(map.get("未分组"));
					}
					updateGroupButton();
				}
			}
		});

		// 联系组方面
		t2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				if (newValue != null) { // 如果有选中的话
					table.getSelectionModel().clearSelection(); // 取消表格中某一行的选中
					t1.getSelectionModel().clearSelection(); // 取消左侧联系人标签的选中
					// 判断选中哪个组
					flag = newValue.getValue();
					for (String key : map.keySet())
						if (key.equals(flag))
							fillTable(map.get(flag));
					updateGroupButton();
				}
			}
		});
	}
	
	/*复制到组*/
	public void copy() {
		c1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue!=null) {
					if(newValue.equals("新建组")) {   // 复制到新建组
						//尚未完成
						return;
					}
					boolean f = false;  //用来判断有没有选中联系人
					for (AddressBean bean : total) {
						if (bean.getCb().isSelected()) { // 判断选中的是哪些联系人
							f = true;
							if (bean.getGroup().contains(newValue)) { // 联系人已经在组里面了
								continue;
							} else {
								if (bean.getGroup().trim().isEmpty()) {// 联系人不属于任何组
									bean.setGroup(newValue);
									map.get("未分组").remove(bean);
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
						WarnTool.warn(primaryStage, "请先选择联系人");
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
	
	/*移动到组*/
	public void move() {
		c2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				if(newValue!=null) {
					if (flag.contains("所有联系人") || flag.contains("未分组联系人")) { // 没有移动权限
						WarnTool.warn(primaryStage, "请先选中某个组再进行移动");
						return;
					}else {
						if(newValue.equals("新建组")) {   // 复制到新建组
							//尚未完成
							return;
						}
						boolean f = false;
						for (AddressBean bean : total) {
							if (bean.getCb().isSelected()) { // 判断选中的是哪些联系人
								f = true;
								if (bean.getGroup().contains(newValue)) { // 联系人已经在组里面了
									bean.getCb().setSelected(false);
									continue;
								} else {
									if (bean.getGroup().trim().isEmpty()) // 联系人不属于任何组
										bean.setGroup(newValue);
									else {
										String[] items = bean.getGroup().split(" ");
										if(items.length == 1)              //联系人只属于一个组
									    bean.setGroup(newValue);
										else {
										StringBuilder sb = new StringBuilder();
										for(int i=0;i<items.length;i++) {
											if(flag.equals(items[i])) {    //当前联系人所属的组
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
							WarnTool.warn(primaryStage, "请先选择联系人");
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

	/* 填充表格数据 */
	public void fillTable(List<AddressBean> total) {
		col0.setCellValueFactory(new PropertyValueFactory<AddressBean, CheckBox>("cb"));
		col1.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("name"));
		col2.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("email"));
		col3.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("mobilephone"));
		col4.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("group"));
		table.setItems(FXCollections.observableArrayList(total));
		table.getSelectionModel().clearSelection(); // 取消选中某行
	}

	/* 动态更新所有组的标签 */
	public void initAllGroups(Map<String, List<AddressBean>> map) {
		t2.getSelectionModel().clearSelection();
		 Node rootIcon = new ImageView(  
			        new Image(getClass().getResourceAsStream("/com/scau/address/images/icon.jpg"))  
			    ); 
		TreeItem<String> root2 = new TreeItem<String>("联系组",rootIcon);
		root2.setExpanded(true);
		for (String group : map.keySet())
			if (!group.equals("未分组"))
				root2.getChildren().add(new TreeItem<String>(group));
		t2.setRoot(root2);

	}

	/* 动态更新联系人列表的标签 */
	public void initAllBeans(Map<String, List<AddressBean>> map) {
		t1.getSelectionModel().clearSelection();
		Node rootIcon = new ImageView(  
		        new Image(getClass().getResourceAsStream("/com/scau/address/images/icon2.png"))  
		    );
		TreeItem<String> root1 = new TreeItem<String>("联系人",rootIcon);
		root1.setExpanded(true);
		root1.getChildren().add(new TreeItem<String>("所有联系人(" + total.size() + ")"));
		if (map.get("未分组") != null)
			root1.getChildren().add(new TreeItem<String>("未分组联系人(" + map.get("未分组").size() + ")"));
		else
			root1.getChildren().add(new TreeItem<String>("未分组联系人(0)"));
		t1.setRoot(root1);
	}

	/* 移除联系人 */
	private void removeBean(List<AddressBean> delist, List<AddressBean> slist) {
		if (slist == total) { // 从所有组中删除联系人
			f = 0;
			remove(delist, slist);
			for (Map.Entry<String, List<AddressBean>> entrySet : map.entrySet()) {
				if (entrySet.getValue() != null && entrySet.getValue().size() > 0) {
					remove(delist, entrySet.getValue());
				}
			}
		} else { // 从某个组中删除联系人
			f = 1;
			remove(delist, slist);
		}
		if (flag.contains("未分组联系人"))
			fillTable(map.get("未分组"));
		else
			fillTable(slist);
	}

	/* 从组里移除联系人 */
	private void remove(List<AddressBean> delist, List<AddressBean> slist) {
		 if (slist != total) { // 不是从所有组都删除联系人
			for (AddressBean bean : delist) {
				slist.remove(bean);
				bean.getCb().setSelected(false);
				String[] gitems = bean.getGroup().split(" ");
				if (gitems.length == 1 && f == 1) { // 当前删除的联系人只属于一个组
					bean.setGroup("");
					map.get("未分组").add(bean);
				} else if (gitems.length > 1 && f == 1) { // 当前删除联系人属于多个组
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
		} else // 从所有组中删除联系人
			for (AddressBean bean : delist) {
				slist.remove(bean);
			}
	}

	/* 显示联系人的所有信息 */
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
			stage.setTitle("详细信息");
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
