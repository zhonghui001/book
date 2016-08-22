package com.hit.mail;

import java.util.Properties;

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
import javax.mail.search.BodyTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import com.wanneng.mail.SimpleAuthenticator;

public class MailMoudle {

	String account = "wangnengtushu001@163.com";
	String password = "zhong3912";
	String query;
	String toEmail;

	public MailMoudle(String query, String toEmail) {
		this.query = query;
		this.toEmail = toEmail;
	}

	static Properties props = new Properties();
	static {
		props.setProperty("mail.store.protocol", "imap");
		props.setProperty("mail.imap.host", "imap.163.com");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", "smtp.163.com");
		props.setProperty("mail.smtp.auth", "true");
	}

	Message[] messages;
	Session session;

	public void list() throws Exception {
		session = Session.getDefaultInstance(props, new SimpleAuthenticator(
				account, password));
		Store store = session.getStore("imap");
		store.connect(account,password);
		Folder folder = store.getFolder("inbox");
		folder.open(Folder.READ_ONLY);
		//messages=new Message[]{folder.getMessage(1)};
		messages = folder.search(new SearchTerm() {
			@Override
			public boolean match(Message msg) {
				String subject;
				try {
					subject = msg.getSubject();
					return subject.contains("n");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	public void forward() throws Exception {
		if (messages.length != 0) {
			for (Message message : messages) {
				Multipart multipart = (Multipart) message.getContent();
				// ����ת���ʼ���Ϣ
				Message forward = new MimeMessage(session);
				// ��������
				forward.setSubject("Fwd: " + message.getSubject());
				forward.setFrom(new InternetAddress(account));
				forward.addRecipient(Message.RecipientType.TO,
						new InternetAddress(toEmail));
				// �����ʼ��岿��
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("�ʼ�ת��");
				// ����Multipart������
				Multipart mul = new MimeMultipart();
				mul.addBodyPart(messageBodyPart);
				// ��ת���������ʼ��岿��
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(message.getDataHandler());
				// ��ӵ�Multipart����
				mul.addBodyPart(messageBodyPart);
				forward.setContent(mul);
				// ����
				Transport.send(forward);
			}
		}else{
        	System.out.println("δ��ѯ�����ʵ��ʼ�");
        }
	}

	public static void main(String[] args)throws Exception {
		MailMoudle mailMoudle = new MailMoudle("n", "qqpy789@163.com");
		mailMoudle.list();
		mailMoudle.forward();
	}

}
