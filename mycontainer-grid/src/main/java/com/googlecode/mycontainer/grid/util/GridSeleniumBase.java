package com.googlecode.mycontainer.grid.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;

public abstract class GridSeleniumBase {

	private int threadCount;

	private MyContainerGrid grid;

	private WebDriverPool pool;

	private ThreadLocal<WebDriver> webDriverHolder = new ThreadLocal<WebDriver>();

	@BeforeSuite(alwaysRun = true)
	private void setupBeforeSuite(ITestContext context) {
		setupGrid(context);
		startGrid();
		startWebDrivers();
	}

	@BeforeTest
	private void setupBeforeTest() {
		webDriverHolder.set(pool.lock());
	}

	@AfterTest
	private void setupAfterTest() {
		pool.unlock(webDriverHolder.get());
	}

	private void setupGrid(ITestContext context) {
		setThreadCount(context.getSuite().getXmlSuite().getThreadCount());
	}

	private void startGrid() {
		grid = setupMyContainerGrid();
		grid.run(getThreadCount());
	}

	private void startWebDrivers() {
		pool = new WebDriverPool();

		for (int i = 0; i < getThreadCount(); i++) {
			FirefoxDriver driver = startWebDriver();
			pool.add(driver);
		}
	}

	private FirefoxDriver startWebDriver() {
		FirefoxBinary binary = new FirefoxBinary();
		binary.setEnvironmentProperty("DISPLAY",
				System.getProperty("xvfb.display", ":0"));

		Proxy proxy = new Proxy();
		proxy.setProxyAutoconfigUrl("http://localhost:8080/_mycontainergrid/partition_proxy.js");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, proxy);

		FirefoxDriver driver = new FirefoxDriver(binary, null, capabilities);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1920, 1080));
		return driver;
	}

	public abstract MyContainerGrid setupMyContainerGrid();

	public WebDriver getDriver() {
		return webDriverHolder.get();
	}

	public int getThreadCount() {
		return threadCount;
	}

	private void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
}
