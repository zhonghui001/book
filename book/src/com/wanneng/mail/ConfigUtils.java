package com.wanneng.mail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigUtils {
	private static final String MAIL_PROPERTIES="mail.properties";
	static Properties prop;
	
	public static Properties getMailConfig()throws Exception{
		if(prop==null){
			prop = new Properties();
			prop.load(new FileInputStream(new File("conf"+File.separator+MAIL_PROPERTIES)));	
		}
		return prop;
	}

	
	public static void main(String[] args)throws Exception {
		Properties props = ConfigUtils.getMailConfig();
		System.out.println(props.getProperty(MailConstants.mail_account));
	}
}
