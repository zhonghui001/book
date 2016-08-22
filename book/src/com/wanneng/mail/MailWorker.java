package com.wanneng.mail;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

public class MailWorker {
	
	String account;
	String password;
	final StringBuilder query=new StringBuilder();
	String toEmail;
	
	Message[] messages;
	Session session;
	
	public MailWorker()throws Exception {
		this.account = MailAccountManager.getMailAccount();
		this.password = MailAccountManager.getMailPassword();
	}
	
	private void list() throws Exception {
		session = MailSessionFactory.newInstance();
		Store store = session.getStore("imap");
		store.connect(account,password);
		Folder folder = store.getFolder("inbox");
		folder.open(Folder.READ_ONLY);
		messages = folder.search(new SearchTerm() {
			@Override
			public boolean match(Message msg) {
				String subject;
				try {
					subject = msg.getSubject();
					return subject.contains(query);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	private void send() throws Exception {
		if (messages.length != 0) {
			for (Message message : messages) {
				Message forward = new MimeMessage(session);
				forward.setSubject("Fwd: " + message.getSubject());
				forward.setFrom(new InternetAddress(account));
				forward.addRecipient(Message.RecipientType.TO,
						new InternetAddress(toEmail));
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("转发");
				Multipart mul = new MimeMultipart();
				mul.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(message.getDataHandler());
				mul.addBodyPart(messageBodyPart);
				forward.setContent(mul);
				Transport.send(forward);
			}
		}else{
        	System.out.println("没有查询到符合条件的邮件");
        }
	}
	
	/**
	 * 转发邮件
	 * @throws Exception
	 */
	public void forward(String query, String toEmail)throws Exception{
		this.query.append(query);
		this.toEmail = toEmail;
		list();
		send();
	}
	
	public void sendEmail()throws Exception{
		
	}
	
	
}
