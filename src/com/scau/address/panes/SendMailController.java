package com.scau.address.panes;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.scau.address.bean.AddressBean;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
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
    private File file = null;
    private AddressBean bean;    //Ҫ���ŵ���ϵ��
    private String host;         //������ÿ�������Ӧ���������
    private String user;         //��ǰ�û�������
    private String password;     //��ǰ�û�������
    private String sname;        //û�������׺���û���
    
    public void init(Stage stage,AddressBean bean) {
    	this.stage = stage;
    	this.bean = bean;
    	to.setText(bean.getName()+"<"+bean.getEmail()+">");
    	load();
    }
    
    //�����ļ�
    private void load() {
		try {
			Properties pro = new Properties();
			pro.load(this.getClass().getResourceAsStream("/com/scau/address/email-emtemate.properties"));
		    host = pro.getProperty("host");
			user = pro.getProperty("uname");
			sname = pro.getProperty("sname");
			password = pro.getProperty("upassword");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**/
    @FXML
    public void sendMail() {
    	try {
			Properties pro = new Properties();
			pro.setProperty("mail.host", host);     //�������������
			pro.setProperty("mail.smtp.auth","true"); //�����Ƿ���֤
			Authenticator au = new Authenticator() {  //�õ� Session����Ҫ�Ĳ���
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sname, password);
				}
			};
			Session session = Session.getInstance(pro,au); //�õ�Session
			MimeMessage message = new MimeMessage(session);//�ʼ�
			message.setFrom(user);                         //���÷�����
			message.setRecipients(RecipientType.TO, bean.getEmail());//�����ռ���
			message.setSubject(subject.getText());
			MimeMultipart multipart = new MimeMultipart(); //���������
			MimeBodyPart part1 = new MimeBodyPart();       //����1,�ı�����
			part1.setContent(content.getText(),"text/html;charset=utf-8"); //�ı�����
			multipart.addBodyPart(part1);
			
			MimeBodyPart part2 = new MimeBodyPart();       //����2������
			if(file!=null) {
				part2.attachFile(file);                        
				part2.setFileName(MimeUtility.encodeText(file.getName()));
				multipart.addBodyPart(part2);
			}
		    message.setContent(multipart);              //�����ʼ���
		    
		    Transport.send(message);
			System.out.println("���ͳɹ�");
			stage.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
    }
    
    @FXML
    public void cancel() {
    	stage.close();
    }
    
    @FXML
    public void addExtra() {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("ѡ��");
	    file = fileChooser.showOpenDialog(stage);
    }
    
    
    
}
