package com.hit.mail;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import com.wanneng.mail.SimpleAuthenticator;

public class ForwardMail {

	private static final String HOST_NAME = "imap.163.com";
	private static final String SEND_HOST_NAME = "smtp.163.com";
	private static final String PASSWORD = "zh3912";
	private static final String EMAIL_FROM = "wangnengtushu001@163.com";
	private static final String USER_NAME = "wangnengtushu001@163.com";
	private static final String PROTOCOL = "imap";
	private static final String SEND_PROTOCOL = "smtp";

	public static void forwardMail(Session session, Message message)
			throws Exception {
		// ����ת���ʼ���Ϣ
		Message forward = new MimeMessage(session);
		// ��������
		forward.setSubject("Fwd: " + message.getSubject());
		forward.setFrom(new InternetAddress(EMAIL_FROM));
		forward.addRecipient(Message.RecipientType.TO, new InternetAddress(
				"********@sina.com"));
		// �����ʼ��岿��
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText("�ʼ�ת��");
		// ����Multipart������
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		// ��ת���������ʼ��岿��
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(message.getDataHandler());
		// ��ӵ�Multipart����
		multipart.addBodyPart(messageBodyPart);
		forward.setContent(multipart);
		// ����
		Transport.send(forward);
		System.out.println("msg forward ....");
	}

	public static void listMail() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", SEND_HOST_NAME);
		props.put("mail.pop.host", HOST_NAME);
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props,
				new SimpleAuthenticator(USER_NAME, PASSWORD));
		Store store = session.getStore(PROTOCOL);
		store.connect(HOST_NAME, USER_NAME, PASSWORD);
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message[] messages = folder.getMessages();
		InternetAddress address;
		for (int i = messages.length - 1; i >= 0; i--) {
			address = (InternetAddress) messages[i].getFrom()[0];
			/*
			 * if(address != null) { System.out.println(address.getPersonal());
			 * }
			 */
			if (null != address && "�����ͯ��".equals(address.getPersonal())) {
				System.out.println("��" + i + "����" + messages[i].getSubject());
				// ת���ʼ�
				forwardMail(session, messages[i]);
			}
		}
		folder.close(true);
		store.close();
	}

	public static void main(String[] args) {
		try {
			listMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
