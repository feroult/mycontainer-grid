package com.googlecode.mycontainer.grid.server.setup;

import java.util.Properties;

import javax.mail.Authenticator;

import com.googlecode.mycontainer.mail.MailDeployer;
import com.googlecode.mycontainer.mail.MailInfoBuilder;

public class MailSetup {

	private String name;
	private Properties props;
	private Authenticator  auth;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public Authenticator getAuth() {
		return auth;
	}

	public void setAuth(Authenticator auth) {
		this.auth = auth;
	}

	public void set(MailDeployer mail, String partition) {
		mail.setName(this.getName());
		MailInfoBuilder mailInfo = mail.getInfo();
		Properties properties = mailInfo.getProperties();
		properties.putAll(this.getProps());
		mailInfo.setAuthenticator(auth);
	}


}
