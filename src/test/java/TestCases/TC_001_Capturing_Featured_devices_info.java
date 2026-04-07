package TestCases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

import BasePackage.BaseClass;
import PageObject.HomePageObj;
import junit.framework.Assert;

import Utilities.*;

public class TC_001_Capturing_Featured_devices_info extends BaseClass{
	ExcelFileWriter ef;
	ExcelFileReader er;
	HomePageObj hpo;
	@Test(priority = 2)
	public void capture_featured_products() throws IOException {
		hpo = new HomePageObj(driver, mywait, js);
		ef = new ExcelFileWriter(wb1,"Sheet1");
//		log.info("Switching to the iframe");
//		driver.switchTo().frame("framelive");
		log.info("Capturing the list of onsale prods");
		List<WebElement> onSaleProds = hpo.getOnSaleProdList();
		int numOfOnSaleProds = onSaleProds.size();
		int countDiscProds = 0;
		log.info("Iterating through the captured list of onsale prods to get the price and discount info");
		for(int i=0;i<numOfOnSaleProds;i++) {
			log.info("Re-Capturing the list of onsale prods as there might be a chance of getting a staleElementReference Exception due to forth and back Navigation");
			onSaleProds = hpo.getOnSaleProdList();
			log.info("Scroll into the view");
			hpo.scrollIntoView(onSaleProds.get(i));
			log.info("Click on the onsale item "+(i+1));
			hpo.jsClick(onSaleProds.get(i));
			String prod_curPrice = hpo.specific_prodPrice().getText();
			log.info("Get the discounted price and load it in the work book");
			ef.setCellValue(0, prod_curPrice);
			System.out.println(prod_curPrice);
			String discount = hpo.productDisc.getText();
			log.info("Get the discount percentage and load it in the work book");
			ef.setCellValue(1, discount);
			ef.rowInc();
			System.out.println(discount);
			if(discount.contains("Save 20%")) {
				countDiscProds++;
			}
			log.info("Navigate back to the home page");
			driver.navigate().back();
			log.info("After Navigation, We need to switch to iframe again as the driver instance is rooted back to default content");
			driver.switchTo().frame("framelive");
		}
		Assert.assertEquals(numOfOnSaleProds, countDiscProds);
	}

	@Test(priority=1)
	public void createCustomer() throws InterruptedException {
		log.info("Switching to the iframe");
		driver.switchTo().frame("framelive");
		er = new ExcelFileReader(wb2,"Sheet1");
		HashMap<String,String> map = er.readFile();
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ps-customersignin a"))).click();
//		driver.findElement(By.cssSelector(".ps-customersignin a")).click();
		try {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".login__register-prompt a"))).click();
		}
		catch(ElementClickInterceptedException e) {
			WebElement ele = driver.findElement(By.cssSelector(".login__register-prompt a"));
			js.executeScript("arguments[0].click();", ele);
		}
//		driver.findElement(By.cssSelector(".login__register-prompt a")).click();
		if(map.get("gender").equals("field-id_gender_1")) {
			driver.findElement(By.id("field-id_gender_1")).click();
		}
		else {
			driver.findElement(By.id("field-id_gender_2")).click();
		}
		driver.findElement(By.id("field-firstname")).sendKeys(map.get("firstName"));
		driver.findElement(By.id("field-lastname")).sendKeys(map.get("lastName"));
		driver.findElement(By.id("field-email")).sendKeys(map.get("email"));
		driver.findElement(By.id("field-password")).sendKeys(map.get("password"));
		driver.findElement(By.id("field-birthday")).sendKeys(map.get("birthdate"));
		try {
			driver.findElement(By.id("field-psgdpr")).click();
		}
		catch(ElementClickInterceptedException e) {
			WebElement ele = driver.findElement(By.id("field-psgdpr"));
			js.executeScript("arguments[0].click();", ele);
		}
		try {
			driver.findElement(By.id("field-customer_privacy")).click();
		}
		catch(ElementClickInterceptedException e) {
			WebElement ele = driver.findElement(By.id("field-customer_privacy"));
			js.executeScript("arguments[0].click();", ele);
		}
		try {
			driver.findElement(By.xpath("(//footer/button)[1]")).click();
		}
		catch(ElementClickInterceptedException e) {
			WebElement ele = driver.findElement(By.xpath("(//footer/button)[1]"));
			js.executeScript("arguments[0].click();", ele);
		}
	}
}