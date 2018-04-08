package com.scau.address.panes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.scau.address.bean.AddressBean;
import com.scau.address.service.AddressService;
import com.scau.address.utils.CSVTool;
import com.scau.address.utils.CheckTool;
import com.scau.address.utils.VCFTool;

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

	@FXML
	private List<String> list = new ArrayList<String>();
	@FXML
	private Stage primaryStage;
	@FXML
	private AddressService service
	            = new AddressService();         //业务对象（增、删、改...）
	@FXML
	private List<AddressBean> beans 
	            = new ArrayList<AddressBean>(); // 导入文件中的所有联系人
	@FXML
	private List<AddressBean> total
	            = new ArrayList<AddressBean>(); // 当前用户所有联系人

	/* 初始化操作 */
	public void init(Stage primaryStage) {
		
		this.primaryStage = primaryStage;

		list.add("新建组");
		for (String group : list) {
			MenuItem item1 = new MenuItem(group);
			MenuItem item2 = new MenuItem(group);
			m1.getItems().add(item1);
			m2.getItems().add(item2);
		}

		TreeItem<String> root1 = new TreeItem<String>("联系人");
		root1.setExpanded(true);
		root1.getChildren().add(new TreeItem<String>("所有联系人"));
		root1.getChildren().add(new TreeItem<String>("未分组联系人(3)"));
		t1.setRoot(root1);

		TreeItem<String> root2 = new TreeItem<String>("联系组");
		root2.setExpanded(true);
		root2.getChildren().add(new TreeItem<String>("亲人"));
		t2.setRoot(root2);
		
		if(total != null && total.size() > 0)
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
			Scene scene = new Scene(pane,485,340);
			Stage stage = new Stage();
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

	/* 填充表格数据 */
	private void fillTable(List<AddressBean> total) {
			col0.setCellValueFactory(new PropertyValueFactory<AddressBean, CheckBox>("cb"));
			col1.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("name"));
			col2.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("email"));
			col3.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("mobilephone"));
			col4.setCellValueFactory(new PropertyValueFactory<AddressBean, String>("group"));
			table.setItems(FXCollections.observableArrayList(total));
	}
}
