package com.hit.mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

import com.wanneng.mail.SimpleAuthenticator;


public class TestMail2 {
	
	@Test
	public void test()throws Exception{
	    Properties props = new Properties();
	    props.setProperty("mail.store.protocol", "imap");  
        props.setProperty("mail.imap.host", "imap.163.com");  
        props.setProperty("mail.transport.protocol","smtp");  
        props.setProperty("mail.smtp.host", "smtp.163.com");  
        props.setProperty("mail.smtp.auth", "true");  
	    Session session = Session.getDefaultInstance(props,new SimpleAuthenticator("wangnengtushu001@163.com", "zhong3912"));
	    Store store = session.getStore("imap");
	    store.connect("wangnengtushu001@163.com", "zhong3912");
	    Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        if (messages.length != 0) {
        	for (Message message:messages) {
        		Multipart multipart = (Multipart)message.getContent();
        		// 创建转发邮件信息
        		Message forward = new MimeMessage(session);
        		// 设置主题
        		forward.setSubject("Fwd: " + message.getSubject());
        		forward.setFrom(new InternetAddress("wangnengtushu001@163.com"));
        		forward.addRecipient(Message.RecipientType.TO, new InternetAddress(
        				"qqpy789@163.com"));
        		// 文字邮件体部分
        		BodyPart messageBodyPart = new MimeBodyPart();
        		messageBodyPart.setText("邮件转发");
        		// 创建Multipart的容器
        		Multipart mul= new MimeMultipart();
        		mul.addBodyPart(messageBodyPart);
        		// 被转发的文字邮件体部分
        		messageBodyPart = new MimeBodyPart();
        		messageBodyPart.setDataHandler(message.getDataHandler());
        		// 添加到Multipart容器
        		mul.addBodyPart(messageBodyPart);
        		forward.setContent(mul);
        		// 发送
        		Transport.send(forward);
        		System.out.println("msg forward ....");
				System.out.println("----------------------------------------------------");
			}
        }
	}

}
