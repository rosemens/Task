package com.scau.address.panes;
	
import java.net.URL;
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
			primaryStage.setTitle("ͨѶ¼");
			MyController mc = fxmlLoader.getController();  //��ȡ������
			mc.init(primaryStage);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}