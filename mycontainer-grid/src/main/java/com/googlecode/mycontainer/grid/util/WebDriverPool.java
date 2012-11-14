package com.googlecode.mycontainer.grid.util;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

public class WebDriverPool {

	private List<WebDriver> locked, unlocked;

	public WebDriverPool() {
		locked = new ArrayList<WebDriver>();
		unlocked = new ArrayList<WebDriver>();
	}

	public synchronized void add(WebDriver driver) {
		unlocked.add(driver);
	}

	public synchronized WebDriver lock() {
		if (unlocked.size() == 0) {
			throw new RuntimeException("no available webdrivers");
		}

		WebDriver driver = unlocked.remove(0);
		locked.add(driver);
		return driver;
	}

	public synchronized void unlock(WebDriver driver) {
		locked.remove(driver);
		unlocked.add(driver);
	}
}