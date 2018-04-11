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
    
	private Object flag = 1;                           //表示t1,t2中选中的TreeItem
	
	@FXML
	private List<String> list = new ArrayList<String>();  //已存在的组
	@FXML
	private Stage primaryStage;
	@FXML
	private AddressService service
	            = new AddressService();         //业务对象（增、删、改...）
	@FXML
	private List<AddressBean> beans 
	            = new ArrayList<AddressBean>(); // 导入文件中的所有联系人
	@FXML
	public  List<AddressBean> total
	            = new ArrayList<AddressBean>(); // 当前用户所有联系人
	@FXML
	public Map<String,List<AddressBean>> map 
	            = new HashMap<>();              //所有组及其对应的联系人列表

	/* 初始化操作 */
	public void init(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		total = service.getAll();                     //得到所有联系人
        
		map = GroupsTool.getGroups(total);
		for(String key:map.keySet()) {
			if(!key.equals("未分组"))
			 list.add(key);
		}
		
		list.add("新建组");
		for (String group : list) {
			MenuItem item1 = new MenuItem(group);
			MenuItem item2 = new MenuItem(group);
			m1.getItems().addAll(item1);
			m2.getItems().addAll(item2);
		}
		
		initAllBeans(map);                      //动态设置联系人标签
		initAllGroups(map);                     //动态设置组的标签
		
		update();
		
		if(total != null && total.size() >0)
		fillTable(total);
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
			AddController ac = fxmlLoader.getController();    //得到新建联系人的fxml的控制器对象
			Scene scene = new Scene(pane,485,340);
			Stage stage = new Stage();
			ac.init(this,stage);                              //初始化新建面板
			stage.setScene(scene);
			stage.setTitle("新建联系人");
			stage.setIconified(false);  //禁止最小化
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*删除选中的列表中的联系人*/
	@FXML
	public void delete() {
		int[] index = new int[300];            //被选中删除的对象在对应组中的下标
		int count = 0;                         //统计被选中的联系人个数
		
		if(flag.equals(1)) {                   //将选中的联系人从所有组中删除
           removeBean(index,count,total);
		}else if(flag.equals(2) && map.get("未分组")!=null) {
		   removeBean(index,count,map.get("未分组"));
		}else {
			for(String key:map.keySet()) {
				if(flag.equals(key)) {
					removeBean(index,count,map.get(key));
					break;
				}
			}
		}
		
	}
	
	
	private void removeBean(int[] index,int count,List<AddressBean> slist) {
		for(int i=0;i<slist.size();i++) {
      	  if(slist.get(i).getCb().isSelected()) {
      		  index[count++] = i;
      	  }
         }
         for(int i=0;i<count;i++) {
      	  slist.remove(index[i]);
         }
         fillTable(slist);
	}
	
	/*搜索联系人，返回整个列表*/
	@FXML
	public void searchBeans(){
		List<AddressBean> result = new ArrayList<AddressBean>(); //存放搜索结果集
		String text = search.getText();
		result = service.search(text, total);
		if(result != null)
		  fillTable(result);
	}
	
	/* 导入CSV */
	@FXML
	public void importCSV(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if(file!=null) {
			beans = CSVTool.importCsvFile(file);   //导入
			total = CheckTool.merge(total, beans); //将不重复的联系人加到列表
			fillTable(total);                      //填充表格
		}
	}

	/* 导入VCF */
	@FXML
	public void importVCF(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导入文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("VCard", "*.vcf"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if(file!=null) {
			beans = VCFTool.importVCFFile(file);   //导入
			total = CheckTool.merge(total, beans); //将不重复的联系人加到列表
			fillTable(total);                      //填充表格
		}
	}
	
	/*导出CSV格式的文件*/
	@FXML
	public void exportCSV() {
        if(total == null || total.size()<1) {
        	return;
        }
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if(file!=null) {
			CSVTool.exportCsvFile(total, file);    //导出
		}
	}
	
	/*导出VCF格式的文件*/
	@FXML
	public void exportVCF() {
		if(total == null || total.size()<1) {
        	return;
        }
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出文件");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Vcard", "*.vcf"));
		File file = fileChooser.showSaveDialog(primaryStage);
		if(file!=null) {
			VCFTool.exportVcfFile(total, file);    //导出
		}
	}
	
	/*选中左侧栏目时，实时更新右边表格*/
	public void update() {
		//联系人方面
		t1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
			    flag = (int) newValue;
				if(newValue.equals(1))
					fillTable(total);
				else if(map.get("未分组")!=null) {
					fillTable(map.get("未分组"));
				}
			}
		});
		
		//联系组方面
		t2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				String text = newValue.getValue();        //判断选中哪个组
				flag = newValue.getValue();       
				for(String key:map.keySet()) {
					if(text.equals(key)) {
						fillTable(map.get(key));
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
	}
	
	/*动态更新所有组的标签*/
	public void initAllGroups(Map<String, List<AddressBean>> map) {
		TreeItem<String> root2 = new TreeItem<String>("联系组");
		root2.setExpanded(true);
		for(String group:map.keySet())
	      if(!group.equals("未分组"))
		   root2.getChildren().add(new TreeItem<String>(group));
		t2.setRoot(root2);
	}
    
	/*动态更新联系人列表的标签*/
	public void initAllBeans(Map<String, List<AddressBean>> map) {
		TreeItem<String> root1 = new TreeItem<String>("联系人");
		root1.setExpanded(true);
		root1.getChildren().add(new TreeItem<String>("所有联系人("+total.size()+")"));
		if(map.get("未分组")!=null)
		root1.getChildren().add(new TreeItem<String>("未分组联系人("+map.get("未分组").size()+")"));
		else root1.getChildren().add(new TreeItem<String>("未分组联系人(0)"));
		t1.setRoot(root1);
	}
}
