package com.scau.address.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * ������Ϣ
 * @author Administrator
 *
 */
public class WarnTool {
	/*������ʾ��*/
     public static void warn(Stage stage,String message) {
    	 Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("��Ϣ");
         alert.setHeaderText("");
         alert.setContentText(message);
         alert.initOwner(stage);
         alert.show(); 
     }
}
