package com.wanneng.mail;

import java.util.Properties;

public class MailAccountManager {
	
	public static String getMailAccount()throws Exception{
		Properties props = ConfigUtils.getMailConfig();
		String account = props.getProperty(MailConstants.mail_account);
		return account;
	}
	
	public static String getMailPassword()throws Exception{
		Properties props = ConfigUtils.getMailConfig();
		String password = props.getProperty(MailConstants.mail_password);
		return password;
	}

}
