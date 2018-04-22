package com.scau.address.panes;
	
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.scau.address.service.AddressService;
import com.scau.address.utils.GroupsTool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application { 
	private AddressService service = new AddressService();
	public void start(Stage primaryStage) {
		try {
			URL location = getClass().getResource("Surface.fxml");
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(location);
	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
	        Parent root = fxmlLoader.load();
			Scene scene = new Scene(root,1100,536);
			primaryStage.setScene(scene);
			primaryStage.setTitle("通讯录");
			MyController mc = fxmlLoader.getController();  //获取控制器
			mc.init(primaryStage);
			primaryStage.show();
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/scau/address/images/bean.png")));
			
			
			
			primaryStage.setOnCloseRequest(e->{            //监听是否关闭窗口
				//保存所有联系人
				service.save(mc.total);
				//保存操作完成后所有的组名	
				Set<String> gname = new HashSet<String>();
				for(String name:mc.map.keySet()) {
					if(mc.map.get(name).size() != 0) {
						gname.add(name);
					}
				}
				GroupsTool.saveGroups(new File("mygroups.csv"), gname);
				
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
