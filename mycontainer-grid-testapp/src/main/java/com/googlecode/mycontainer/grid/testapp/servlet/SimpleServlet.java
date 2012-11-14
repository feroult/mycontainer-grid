package com.googlecode.mycontainer.grid.testapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.googlecode.mycontainer.grid.testapp.ejb.SimpleEntity;
import com.googlecode.mycontainer.grid.testapp.ejb.SimpleService;
import com.googlecode.mycontainer.grid.util.GridServiceLocator;

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

		Long id = Long.valueOf(last);
		getSimpleEntity(req, resp, id);
	}

	private void getSimpleEntity(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
		SimpleEntity entity = getSimpleService().findById(id);


		Map<String, String> values = new HashMap<String, String>();
		values.put("id", id.toString());
		values.put("mensagem", entity == null ? "NAO ENCONTRADO" : entity.getMensagem());

		getStaticFile(resp, "/show.html", values);
	}

	private void getCreate(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		getStaticFile(resp, "/create.html", null);
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

	private void postCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String mensagem = req.getParameter("mensagem");
		SimpleEntity entity = getSimpleService().create(mensagem);
		resp.sendRedirect("/testapp/" + entity.getId());
	}

	private void getStaticFile(HttpServletResponse resp, String path, Map<String, String> values)
			throws IOException {
		resp.setContentType("text/html");
		String parsedTemplate = parseTemplate(path, values);
		resp.getWriter().print(parsedTemplate);
	}

	private String parseTemplate(String path, Map<String, String> values) throws IOException {
		ServletContext servletContext = getServletContext();

		String template = IOUtils.toString(servletContext.getResourceAsStream(path));

		if( values != null ) {
			for(String chave : values.keySet()) {
				String value = values.get(chave);
				template = template.replaceAll("\\$\\{" + chave + "\\}", value);
			}
		}

		return template;
	}

	private SimpleService getSimpleService() {
		return GridServiceLocator.lookup(SimpleService.JNDI_LOCAL,
				SimpleService.class);
	}

	private String getLastBitFromUrl(final String url) {
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
}
