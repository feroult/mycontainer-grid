package com.googlecode.mycontainer.grid.util;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;

public abstract class GridSeleniumBase {

	Logger logger = Logger.getLogger(GridSeleniumBase.class);

	private int threadCount;

	private MyContainerGrid grid;

	protected ThreadLocal<GridWebDriver> webDriverHolder = new ThreadLocal<GridWebDriver>();

	private WebDriverPool pool;

	@BeforeSuite(alwaysRun = true)
	public void setupBeforeSuite(ITestContext context) {
		pool= new WebDriverPool();
		setupGrid(context);
		startGrid();
		startWebDrivers();
	}

	@BeforeMethod
	public void setupBeforeTest() {
		webDriverHolder.set(pool.lock());
	}

	@AfterMethod
	public void setupAfterTest() {
		pool.unlock(webDriverHolder.get());
	}

	@AfterSuite
	public void setupAfterSuite() {
		pool.destroyPool();
	}

	private void setupGrid(ITestContext context) {
		setThreadCount(context.getSuite().getXmlSuite().getThreadCount());
	}

	private void startGrid() {
		grid = setupMyContainerGrid();
		grid.run();
	}

	public abstract MyContainerGrid setupMyContainerGrid();

	public GridWebDriver getDriver() {
		return webDriverHolder.get();
	}

	public int getThreadCount() {
		return threadCount;
	}

	private void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public WebDriverPool getPool() {
		return pool;
	}

	private void startWebDrivers() {
		for(String partition:grid.getPartitionNames()){
			pool.add(startWebDriver(), partition);
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

	public void navegarNaPagina(String url) {
		String urlToNavigate = "http://"+webDriverHolder.get().getPartition()+".grid/"+url;
		logger.info("Navigating to {} " + urlToNavigate);
		webDriverHolder.get().getWebDriver().navigate().to(urlToNavigate);
	}

}
