package com.googlecode.mycontainer.grid.testapp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SimpleTest {

	Logger logger = Logger.getLogger(SimpleTest.class);

	private FirefoxDriver driver;

	private static long snapShotCount = 1;

	private void setupDriver() {
		FirefoxBinary binary = new FirefoxBinary();
        binary.setEnvironmentProperty("DISPLAY", System.getProperty("xvfb.display", ":99"));

		driver = new FirefoxDriver(binary, null);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1920, 1080));
	}

	@Before
	public void setup() {
		setupDriver();
	}

	@After
	public void teardown() throws IOException {
        takeSnapShot();
		driver.close();
	}

	private void takeSnapShot() throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("/tmp/gridsnapshot" + snapShotCount++ + ".png"));
	}

	@Test
	public void testHelloWorld() {
		driver.navigate().to("http://localhost:8080/testapp/hello_world");
		String text = driver.findElement(By.id("hello_world")).getText();
		Assert.assertTrue("hello word", text.equals("hello world"));
	}

}
