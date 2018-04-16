package com.scau.address.panes;

import com.scau.address.bean.AddressBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditController {
   @FXML
   private TextField t1;
   @FXML
   private Button b1;
   @FXML
   private Button b2;
   
   private MyController myController;
   private Stage stage;
   
   /* ��ʼ�� */
   public void init(MyController myController, Stage stage) {
	   this.myController = myController;
	   this.stage = stage;
	   
	   t1.setPromptText((String)myController.flag);
   }
   
   /* ȷ���޸� */
   @FXML
   public void confirm() {
	   String newGroup = t1.getText();
	   Object flag = myController.flag;
	   if(newGroup.trim().isEmpty()) {
		   stage.close();
		   return;
	   }
	   else {
		   for(AddressBean bean:myController.map.get(flag)) {
			   String[] gitems = bean.getGroup().split(" ");
			   if (gitems.length == 1) { // ��ǰ�༭�������ϵ��ֻ���������
				   bean.setGroup(newGroup);
			   } else if (gitems.length > 1) { // ��ǰ�༭����ϵ�����ڶ����
				   StringBuilder sb = new StringBuilder();
				   for (int i = 0; i < gitems.length; i++) {
					   if (!flag.equals(gitems[i])) {
						   sb.append(gitems[i]);
					   }else sb.append(newGroup);
					   if(i != gitems.length-1)
						   sb.append(" ");
				   }
				   bean.setGroup(sb.toString());
			   }
		   }
		  myController.list.remove((String)flag);
		  myController.list.add(newGroup);
		  myController.map.put(newGroup,myController.map.remove(flag)); 	
	   }
	  
	   myController.updateGroupButton();
	   myController.initAllBeans(myController.map);
	   myController.initAllGroups(myController.map);
	   myController.fillTable(myController.total);
	   stage.close();
   }
   
   /* ȡ���޸�  */
   @FXML
   public void cancel() {
	   stage.close();
   }
   
}
