package com.googlecode.mycontainer.grid.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.googlecode.mycontainer.grid.util.ServiceLocator;

public class PartitionSelectorFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		setPartition(request);
		chain.doFilter(request, response);
	}

	private void setPartition(ServletRequest request) {
		// servers are selected by the start of the host names: partition1.localhost, etc.
		HttpServletRequest req = (HttpServletRequest) request;
		String[] split = req.getServerName().split("\\.");

		if (split.length > 0 && split[0].startsWith("partition")) {
			ServiceLocator.setPartition(split[0]);
		}
	}
}
