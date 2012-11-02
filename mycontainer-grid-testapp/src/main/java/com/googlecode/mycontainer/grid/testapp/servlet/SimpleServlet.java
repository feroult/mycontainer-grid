package com.googlecode.mycontainer.grid.testapp.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.mycontainer.grid.testapp.ejb.SimpleEntity;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;
import com.googlecode.mycontainer.grid.util.ServiceLocator;

public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = -3388076540989725515L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String last = getLastBitFromUrl(req.getRequestURL().toString());

		if (last.equals("create")) {
			getCreate(req, resp);
			return;
		}

		int id = Integer.parseInt(last);
		// TODO get
	}

	private void getCreate(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		getStaticFile(resp, "/create.html");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String last = getLastBitFromUrl(req.getRequestURL().toString());

		if (last.equals("create")) {
			postCreate(req, resp);
			return;
		}
	}

	private void postCreate(HttpServletRequest req, HttpServletResponse resp) {
		String mensagem = req.getParameter("mensagem");
		SimpleEntity entity = getSimpleService().create(mensagem);
	}

	private void getStaticFile(HttpServletResponse resp, String path)
			throws IOException {
		ServletContext servletContext = getServletContext();

		File file = new File(servletContext.getRealPath(path));

		InputStream is = servletContext.getResourceAsStream(path);
		ServletOutputStream os = resp.getOutputStream();

		int length = (int) file.length();

		resp.setContentType("text/html");
		resp.setContentLength(length);

		byte[] buffer = new byte[length];

		is.read(buffer);
		os.write(buffer);
		os.flush();
	}

	private SimpleService getSimpleService() {
		return ServiceLocator.lookup(SimpleService.JNDI_LOCAL,
				SimpleService.class);
	}

	private String getLastBitFromUrl(final String url) {
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
}
