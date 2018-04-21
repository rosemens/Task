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
    private AddressBean bean;    //要发信的联系人
    private String host;         //主机，每种邮箱对应邮箱服务器
    private String user;         //当前用户的邮箱
    private String password;     //当前用户的密码
    private String sname;        //没有邮箱后缀的用户名
    
    public void init(Stage stage,AddressBean bean) {
    	this.stage = stage;
    	this.bean = bean;
    	to.setText(bean.getName()+"<"+bean.getEmail()+">");
    	load();
    }
    
    //加载文件
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
			pro.setProperty("mail.host", host);     //设置邮箱服务器
			pro.setProperty("mail.smtp.auth","true"); //设置是否认证
			Authenticator au = new Authenticator() {  //得到 Session所需要的参数
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sname, password);
				}
			};
			Session session = Session.getInstance(pro,au); //得到Session
			MimeMessage message = new MimeMessage(session);//邮件
			message.setFrom(user);                         //设置发件人
			message.setRecipients(RecipientType.TO, bean.getEmail());//设置收件人
			message.setSubject(subject.getText());
			MimeMultipart multipart = new MimeMultipart(); //部件组合体
			MimeBodyPart part1 = new MimeBodyPart();       //部件1,文本方面
			part1.setContent(content.getText(),"text/html;charset=utf-8"); //文本内容
			multipart.addBodyPart(part1);
			
			MimeBodyPart part2 = new MimeBodyPart();       //部件2，附件
			if(file!=null) {
				part2.attachFile(file);                        
				part2.setFileName(MimeUtility.encodeText(file.getName()));
				multipart.addBodyPart(part2);
			}
		    message.setContent(multipart);              //设置邮件体
		    
		    Transport.send(message);
			System.out.println("发送成功");
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
		fileChooser.setTitle("选择");
	    file = fileChooser.showOpenDialog(stage);
    }
    
    
    
}
