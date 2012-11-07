package com.googlecode.mycontainer.grid.parallels;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Simple1Test {

	Logger logger = Logger.getLogger(Simple1Test.class);

	private FirefoxDriver driver;

	private static long snapShotCount = 1;

	private void setupDriver() {
		FirefoxBinary binary = new FirefoxBinary();
        binary.setEnvironmentProperty("DISPLAY", System.getProperty("xvfb.display", ":0"));

        Proxy proxy = new Proxy();
        proxy.setProxyAutoconfigUrl("http://localhost:8080/_mycontainergrid/partition_proxy.js");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);        

        driver = new FirefoxDriver(binary, null, capabilities);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(1920, 1080));
	}

	@BeforeMethod
	public void setup() {
		setupDriver();
	}

	@AfterMethod
	public void teardown() throws IOException {
        takeSnapShot();
		driver.close();
	}

	private void takeSnapShot() throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("/tmp/gridsnapshot" + snapShotCount++ + ".png"));
	}

	@Test
	public void testCreate() throws InterruptedException {
		//just to slow down the tests and fell the parallels...
		Thread.sleep( 5000 );
		
		String mensagem = "hey ow fool, i'm gonna kill you";
		driver.navigate().to("http://partition1.grid/testapp/create");
		WebElement mensagemTextArea = driver.findElement(By.id("mensagemTextArea"));
		mensagemTextArea.sendKeys(mensagem);

		WebElement sendButton = driver.findElement(By.id("sendButtom"));
		sendButton.click();

		WebElement mensagemText = driver.findElement(By.id("mensagemText"));
		AssertJUnit.assertEquals(mensagem, mensagemText.getText());
	}

}
