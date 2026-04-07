package BasePackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

public class BaseClass {
	public WebDriver driver;
	public WebDriverWait mywait;
	public JavascriptExecutor js;
	public XSSFWorkbook wb1;
	public XSSFWorkbook wb2;
	
	public FileOutputStream file1;
	public FileInputStream read1;
	public FileInputStream read2;
	public Properties prop;
	protected static final Logger log = LogManager.getLogger(BaseClass.class);;
	
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
//	public static int rownum=1;
	@BeforeTest
	@Parameters({"excelName"})
	public void driverSetup(String excelName) throws InterruptedException, IOException {
		FileReader conFile = new FileReader(".//src/test/resources/config.properties");
		prop = new Properties();
		prop.load(conFile);
		
		if(prop.getProperty("execution_env").equals("remote")) {
			
			
			DesiredCapabilities cap = new DesiredCapabilities();
			if(prop.getProperty("os").equals("windows")) {
				cap.setPlatform(Platform.WIN11);
			}
			else {
				cap.setPlatform(Platform.MAC);
			}
			if(prop.getProperty("browser").equals("chrome")) {
				cap.setBrowserName("chrome");
			}
			else {
				cap.setBrowserName("MicrosoftEdge");
			}
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			log.info("Driver instance created on "+prop.getProperty("execution_env")+" environment");
			log.info(prop.getProperty("browser")+" browser launched");
		}
		else {
			if(prop.getProperty("browser").equals("chrome")) {
				driver=new ChromeDriver();
				log.info("Chrome browser launched on local machine");
			}
			else {
				driver=new EdgeDriver();
				log.info("Edge browser launched on local machine");
			}
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(prop.getProperty("url"));
		mywait = new WebDriverWait(driver, Duration.ofSeconds(30));
		js = (JavascriptExecutor) driver;
		read1 = new FileInputStream(System.getProperty("user.dir")+"/ExcelData_output/"+excelName+".xlsx");
		read2 = new FileInputStream(".//src/test/resources/RegisterInput.xlsx");
		//new FileOutputStream(System.getProperty("user.dir")+"/exceldata/cucumberExcelData.xlsx");
		wb1 = new XSSFWorkbook(read1);
		wb2 = new XSSFWorkbook(read2);
	}
	
	@AfterTest
	@Parameters({"excelName"})
	public void driverClose(String excelName) throws IOException {
		file1= new FileOutputStream(System.getProperty("user.dir")+"/ExcelData_output/"+excelName+".xlsx");
		wb1.write(file1);
		log.info("Writing the data in the workbook to the final file");
		wb1.close();
		wb2.close();
		driver.quit();
		log.info("WebDriver has been closed.");
	}
	
	public String captureScreen(String methodName, WebDriver inDriver) {
		TakesScreenshot screenshot = (TakesScreenshot)inDriver;
		File src = screenshot.getScreenshotAs(OutputType.FILE);
		String target = System.getProperty("user.dir")+"\\screenshots\\"+methodName+"__T"+Thread.currentThread().getId()+System.currentTimeMillis()+".png";
		File targetPath = new File(target);
		src.renameTo(targetPath);
		return target;
	}
}
