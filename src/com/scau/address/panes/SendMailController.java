package com.scau.address.panes;

import com.scau.address.bean.AddressBean;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendMailController {
    @FXML
    private Label to;
    @FXML
    private Label extra;
    @FXML
    private TextArea content;
    @FXML
    private TextField subject;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    
    private Stage stage;
    private AddressBean bean;
    
    public void init(Stage stage,AddressBean bean) {
    	this.stage = stage;
    	this.bean = bean;
    	to.setText(bean.getName()+"<"+bean.getEmail()+">");
    }
    
    @FXML
    public void sendMail() {
    	stage.close();
    }
    
    @FXML
    public void cancel() {
    	stage.close();
    }
    
    @FXML
    public void addExtra() {
    	stage.close();
    }
    
    
    
}
