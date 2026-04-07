package PageObject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePageObj extends BasePageObj {
	
	
	//Constructor
	public HomePageObj(WebDriver driver, WebDriverWait mywait, JavascriptExecutor js) {
		super(driver,mywait,js);
	}
	
	//By Locators
	By prodPrice = By.cssSelector(".product__price");
	By onSaleProdLoc = By.xpath("//section[@class='ps-specials']//article/div[1]/div[1]/div[1]/a");
	
	//FindBy Web Elements
	@FindBy(css=".product__prices-block .product__discount-percentage")
	public WebElement productDisc;
	
	
	//For waiting until the product page is loaded.
	public WebElement specific_prodPrice() {
		WebElement productPrice = mywait.until(ExpectedConditions.visibilityOfElementLocated(prodPrice));
		return productPrice;
	}
	
	//For waiting until the main page loads and select the list of on sale products
	public List<WebElement> getOnSaleProdList(){
		List<WebElement> onSaleProdList = mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(onSaleProdLoc));
		return onSaleProdList;
	}
	
	//ScrollIntoView using js
	public void scrollIntoView(WebElement ele) {
		js.executeScript("arguments[0].scrollIntoView();", ele);
	}
	
	//Click using JS
	public void jsClick(WebElement ele) {
		js.executeScript("arguments[0].click();", ele);
	}
}
