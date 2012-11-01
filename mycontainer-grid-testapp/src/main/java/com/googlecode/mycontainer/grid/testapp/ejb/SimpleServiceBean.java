package com.googlecode.mycontainer.grid.testapp.ejb;

import javax.ejb.Stateless;

import com.googlecode.mycontainer.annotation.MycontainerLocalBinding;

@Stateless
@MycontainerLocalBinding(value = SimpleService.JNDI_LOCAL)
public class SimpleServiceBean implements SimpleService {

	@Override
	public String getHelloWorld() {
		return "hello world";
	}

}
