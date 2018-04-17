package com.scau.address.panes;

import java.util.ArrayList;

import com.scau.address.bean.AddressBean;
import com.scau.address.service.AddressService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddGroupController {
   @FXML
   private TextField t1;
   @FXML
   private Button b1;
   @FXML
   private Button b2;
   
   private MyController myController;
   private Stage stage;
   private AddressService service = new AddressService();
   
   /* ��ʼ�� */
   public void init(MyController myController, Stage stage) {
	   this.myController = myController;
	   this.stage = stage;
	   
   }
   
   /* ȷ���޸� */
   @FXML
   public void confirm() {
	   String newGroup = t1.getText();
	   if(newGroup.trim().isEmpty()) {
		   stage.close();
		   return;
	   }
	   myController.list.add(newGroup);
	   myController.map.put(newGroup, new ArrayList<AddressBean>());
	   myController.initAllGroups(myController.map);
	   myController.updateGroupButton();
	   service.save(myController.total);
	   stage.close();
   }
   
   /* ȡ���޸�  */
   @FXML
   public void cancel() {
	   stage.close();
   }
   
}