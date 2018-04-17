package com.scau.address.panes;

import com.scau.address.bean.AddressBean;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ShowController {
   @FXML
   private TextField name;
   @FXML
   private TextField index;
   @FXML
   private TextField email;
   @FXML
   private TextField postcode;
   @FXML
   private TextField moiblephone;
   @FXML
   private TextField telephone;
   @FXML
   private TextField birthday;
   @FXML
   private TextField group;
   @FXML
   private TextField home;
   @FXML
   private TextField work;
   @FXML
   private TextArea remarks;
   
   private AddressBean bean;
   
   
   public void init(AddressBean bean) {
	   this.bean = bean;
	   name.setText(bean.getName());name.setEditable(false);
	   index.setText(bean.getIndex());index.setEditable(false);
	   email.setText(bean.getEmail());email.setEditable(false);
	   postcode.setText(bean.getPostcode());postcode.setEditable(false);
	   moiblephone.setText(bean.getMobilephone());moiblephone.setEditable(false);
	   telephone.setText(bean.getTelephone());telephone.setEditable(false);
	   birthday.setText(bean.getBirthday());birthday.setEditable(false);
	   group.setText(bean.getGroup());group.setEditable(false);
	   home.setText(bean.getAddress());home.setEditable(false);
	   work.setText(bean.getWorkplace());work.setEditable(false);
	   remarks.setText(bean.getRemarks());remarks.setEditable(false);
   }
   
}
