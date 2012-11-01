package com.googlecode.mycontainer.grid.testapp;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class SimpleTest {

	Logger logger = Logger.getLogger(SimpleTest.class);

	private FirefoxDriver driver;

	private void setupDriver() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "pt-br,en-us,en");
		profile.setPreference("network.http.phishy-userpass-length", 255);

		FirefoxBinary binary = new FirefoxBinary();
		driver = new FirefoxDriver(binary, profile);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1920, 1080));
	}

	@Before
	public void setup() {
		setupDriver();
	}

	@After
	public void teardown() {
		driver.close();
	}

	@Test
	public void testHelloWorld() {
		driver.navigate().to("http://localhost:8080/testapp/hello_world");
		String text = driver.findElement(By.id("hello_world")).getText();
		Assert.assertTrue("hello word", text.equals("hello world"));
	}

}
