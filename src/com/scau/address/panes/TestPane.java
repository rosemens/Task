package com.scau.address.panes;

import com.scau.address.bean.AddressBean;
import com.scau.address.bean.AddressBeanTool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * 用来测试将数据封装成对象的AddressBeanTool工具类
 * @author Administrator
 *
 */
public class TestPane extends Application{

	public static void main(String[] args) {
		Application.launch(args);
    
	}

	@Override
	public void start(Stage stage) throws Exception {
		GridPane pane = new GridPane();
		TextField[] tf = new TextField[11];
		Label[] lab = new Label[11];
		for(int i=0;i<11;i++) {
			tf[i] = new TextField();
			lab[i] = new Label("label"+i);
		}
		pane.addColumn(0, lab);
		pane.addColumn(1, tf);
		
		Button bt = new Button("add");
		pane.add(bt, 0, 11);
		
		Scene scene = new Scene(pane,300,300);
		stage.setScene(scene);
		stage.show();
		
		bt.setOnAction(e->{
			String[] items = new String[11];
			for(int i = 0;i<11;i++) {
				if(tf[i].getText()==null||tf[i].getText().trim().isEmpty())
					items[i] = ",";
				else items[i] = tf[i].getText();
			}
			AddressBean bean = AddressBeanTool.toBean(items);
		    System.out.println(bean);
		});
		
		
	}

}
