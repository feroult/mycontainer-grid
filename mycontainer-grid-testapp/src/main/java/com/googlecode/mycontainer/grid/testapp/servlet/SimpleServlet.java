package com.googlecode.mycontainer.grid.testapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.mycontainer.grid.testapp.ejb.ServiceLocator;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;

public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = -3388076540989725515L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String msg = getSimpleService().getHelloWorld();

		resp.setContentType("text/html");
		resp.getWriter().println("<span id=\"hello_world\">" + msg + "</span>");
	}

	public SimpleService getSimpleService() {
		return ServiceLocator.lookup(SimpleService.JNDI_LOCAL, SimpleService.class);
	}
}
