package com.wanneng.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;

public class MailSessionFactory {
	
	static Message[] messages;
	static Session session;
	
	public static Session newInstance()throws Exception{
		if(session==null){
			Properties props = ConfigUtils.getMailConfig();
			String account = MailAccountManager.getMailAccount();
			String password = MailAccountManager.getMailPassword();
			session = Session.getDefaultInstance(props, new SimpleAuthenticator(
					account, password));
		}
		return session;
	}
	

}
