package com.googlecode.mycontainer.grid.parallels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MultipleWindowTest {
	
	//@Test
	public static void main(String[] args) {		
		MultipleWindowTest test = new MultipleWindowTest();
		
		List<Thread> threads = new ArrayList<Thread>(); 
		
		for(int i = 1; i <= 40; i++) {
			Runner runner = test.new Runner(i);
			threads.add(new Thread(runner));
		}
		
		for(Thread t : threads) {
			t.start();			
		}
	}
	
	class Runner implements Runnable {

		private int id;
		private FirefoxDriver driver;
		private Random rand = new Random(System.currentTimeMillis());
				
		public Runner(int id) {
			super();
			this.id = id;
			
			System.out.println("iniciando driver " + id + "...");
			FirefoxBinary binary = new FirefoxBinary();
	        binary.setEnvironmentProperty("DISPLAY", System.getProperty("xvfb.display", ":99"));
						
			driver = new FirefoxDriver(binary, null);			
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().setSize(new Dimension(1920, 1080));
			
			System.out.println("driver " + id + " ok.");			
		}

		@Override
		public void run() {
			while(true) {
				System.out.println("navegando " + id + "...");
				driver.navigate().to("http://www.google.com.br/?as_qdr=all");
				driver.findElementById("lst-ib").sendKeys("viva la revolucion");
				driver.findElementByName("btnK").click();
				System.out.println("navegando " + id + " ok.");
				//boolean ok = driver.findElementById("resultStats") != null;
				try {
					Thread.sleep(rand.nextInt(1500));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}		
	}
}
