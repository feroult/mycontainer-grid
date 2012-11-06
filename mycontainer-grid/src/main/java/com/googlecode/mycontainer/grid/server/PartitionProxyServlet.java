package com.googlecode.mycontainer.grid.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class PartitionProxyServlet extends HttpServlet {

	private static final long serialVersionUID = -6244761805294114270L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/javascript");
		IOUtils.copy(getClass().getResourceAsStream("/com/googlecode/mycontainer/grid/server/util/partition_proxy.js"), resp.getOutputStream());
		resp.getOutputStream().flush();		
	}
}
