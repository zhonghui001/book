package com.wanneng.test;

import org.junit.Test;

import com.wanneng.mail.MailWorker;

public class TestMail {
	
	@Test
	public void testE()throws Exception{
		MailWorker worker = new MailWorker();
		worker.forward("2", "qqpy789@163.com");
	}

}
