package com.scau.address.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * 弹框信息
 * @author Administrator
 *
 */
public class WarnTool {
	/*弹出提示框*/
     public static void warn(Stage stage,String message) {
    	 Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("信息");
         alert.setHeaderText("");
         alert.setContentText(message);
         alert.initOwner(stage);
         alert.show(); 
     }
}
