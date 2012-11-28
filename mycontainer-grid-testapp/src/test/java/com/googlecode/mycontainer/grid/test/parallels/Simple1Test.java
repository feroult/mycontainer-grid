package com.googlecode.mycontainer.grid.test.parallels;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.googlecode.mycontainer.grid.infra.BootTests;
import com.googlecode.mycontainer.grid.server.MyContainerGrid;
import com.googlecode.mycontainer.grid.util.GridSeleniumBase;

public class Simple1Test extends GridSeleniumBase{

	Logger logger = Logger.getLogger(Simple1Test.class);

	public void testCreate() throws InterruptedException {
		Thread.sleep( 5000 );
		String mensagem = this.getDriver().getPartition();
		navegarNaPagina(this.getDriver().getWebDriver(), this.getDriver().getPartition(), "testapp/create");
		WebElement mensagemTextArea = this.getDriver().getWebDriver().findElement(By.id("mensagemTextArea"));
		mensagemTextArea.sendKeys(mensagem);
		WebElement sendButton = this.getDriver().getWebDriver().findElement(By.id("sendButtom"));
		sendButton.click();
		WebElement mensagemText = this.getDriver().getWebDriver().findElement(By.id("mensagemText"));
		AssertJUnit.assertEquals(mensagem, mensagemText.getText());
	}

	@Test
	public void testCreate1() throws InterruptedException{
		this.testCreate();
	}

	@Test
	public void testCreate2() throws InterruptedException{
		this.testCreate();
	}

	@Override
	public MyContainerGrid setupMyContainerGrid() {
		BootTests boot = new BootTests();
		return boot.setupMyContainerGrid(getThreadCount());
	}

}
