package com.googlecode.mycontainer.grid.util;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridWebDriver {

	private WebDriver webDriver;
	private String partition;

	private static final Logger logger = LoggerFactory.getLogger(GridWebDriver.class);

	public GridWebDriver(WebDriver webDriver, String partition){
		this.partition=partition;
		this.webDriver = webDriver;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public String getPartition() {
		return partition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((partition == null) ? 0 : partition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridWebDriver other = (GridWebDriver) obj;
		if (partition == null) {
			if (other.partition != null)
				return false;
		} else if (!partition.equals(other.partition))
			return false;
		return true;
	}


}
