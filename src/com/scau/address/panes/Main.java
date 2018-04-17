package com.scau.address.panes;
	
import java.io.File;
import java.net.URL;

import com.scau.address.utils.GroupsTool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {  
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
			
			primaryStage.setOnCloseRequest(e->{            //监听是否关闭窗口
				//保存操作完成后所有的组名
				GroupsTool.saveGroups(new File("mygroups.csv"), mc.map.keySet());
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
