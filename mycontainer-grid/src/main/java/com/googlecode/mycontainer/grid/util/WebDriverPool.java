package com.googlecode.mycontainer.grid.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class WebDriverPool {

	private List<GridWebDriver> locked, unlocked;
	Logger logger = Logger.getLogger(WebDriverPool.class);

	public WebDriverPool() {
		locked = new ArrayList<GridWebDriver>();
		unlocked = new ArrayList<GridWebDriver>();
	}

	public synchronized void add(WebDriver driver,String partitionName) {
		unlocked.add(new GridWebDriver(driver, partitionName));
	}

	public synchronized GridWebDriver lock() {
		while(unlocked.size() == 0) {
			logger.info("Esperando por driver...");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}

		GridWebDriver driver = unlocked.remove(0);
		locked.add(driver);
		return driver;
	}

	public synchronized void unlock(GridWebDriver driver) {
		locked.remove(driver);
		unlocked.add(driver);
	}

	public void destroyPool(){
		List<GridWebDriver>allDrivers=new ArrayList<GridWebDriver>();
		allDrivers.addAll(locked);
		allDrivers.addAll(unlocked);
		for(GridWebDriver driver:allDrivers){
			driver.getWebDriver().close();
		}
	}

}