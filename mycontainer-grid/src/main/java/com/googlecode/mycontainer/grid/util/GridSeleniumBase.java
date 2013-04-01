package com.googlecode.mycontainer.grid.util;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.googlecode.mycontainer.grid.server.MyContainerGrid;

public abstract class GridSeleniumBase {

	private static Logger logger = Logger.getLogger(GridSeleniumBase.class);

	private int threadCount;

	private MyContainerGrid grid;

	protected static ThreadLocal<GridWebDriver> webDriverHolder = new ThreadLocal<GridWebDriver>();

	private static WebDriverPool pool= new WebDriverPool();

	@BeforeSuite(alwaysRun = true)
	public void setupBeforeSuite(ITestContext context) {
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

	public static GridWebDriver getDriver() {
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
		FirefoxBinary binary = configureFirefoxBinary();		
		DesiredCapabilities capabilities = configureDesiredCapabilites();
		FirefoxProfile profile = configureFirefoxProfile();		
		
		FirefoxDriver driver = new FirefoxDriver(binary, profile, capabilities);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1920, 1080));
		return driver;
	}

	private FirefoxProfile configureFirefoxProfile() {		
		FirefoxProfile profile = new FirefoxProfile();
	
		if (System.getProperty("firebug.path") != null) {
			try {
				profile.addExtension(new File(System.getProperty("firebug.path")));
			} catch (IOException e) {			
				throw new RuntimeException(e);
			}
			
			// TODO extract from file name or get firebug version from argument 
		    profile.setPreference("extensions.firebug.currentVersion", "1.11.2"); 
		    profile.setPreference("extensions.firebug.onByDefault", true);
		    profile.setPreference("extensions.firebug.allPagesActivation", "on"); 
		    profile.setPreference("extensions.firebug.addonBarOpened", true); 
		    profile.setPreference("extensions.firebug.defaultPanelName", "net");
		    profile.setPreference("extensions.firebug.net.enableSites", true);
		    profile.setPreference("extensions.firebug.net.persistent", true);			
		}
	
		
		
		return profile;
	}

	private DesiredCapabilities configureDesiredCapabilites() {
		Proxy proxy = new Proxy();
		proxy.setProxyAutoconfigUrl("http://localhost:8080/_mycontainergrid/partition_proxy.js");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		return capabilities;
	}

	private FirefoxBinary configureFirefoxBinary() {
		FirefoxBinary binary = new FirefoxBinary();
		if (System.getProperty("firefox-driver-bin") != null) {
			System.out.println("Firefox bin");
			binary = new FirefoxBinary(new File(
					System.getProperty("firefox-driver-bin")));
		}
		binary.setEnvironmentProperty("DISPLAY",
				System.getProperty("xvfb.display", ":0"));
		return binary;
	}

	public void navegarNaPagina(String url) {
		String urlToNavigate = "http://"+getDriver().getPartition()+".grid/"+url;
		logger.info("Navigating to {} " + urlToNavigate);
		getDriver().getWebDriver().navigate().to(urlToNavigate);
	}


	public EntityManager buscarEntityManager() {
		return (EntityManager) GridServiceLocator.lookup("confidence-pu",EntityManager.class);
	}

}
