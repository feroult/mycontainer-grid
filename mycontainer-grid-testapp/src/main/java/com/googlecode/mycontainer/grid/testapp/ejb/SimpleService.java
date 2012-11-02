package com.googlecode.mycontainer.grid.testapp.ejb;

import javax.ejb.Local;


@Local
public interface SimpleService {

	public static String JNDI_LOCAL = "SimpleService/local";

	public String getHelloWorld();

	public SimpleEntity create(String mensagem);

	public SimpleEntity findById(Long id);

}
