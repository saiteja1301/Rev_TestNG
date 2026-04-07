package Utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import BasePackage.BaseClass;


/*Creating a class for genering extent reports*/
public class ExtentReportUtilities implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;
	/*This Method will be implemented on the start of the testcase*/
	/*The UI of the extents reports is designed here and some basic information is given to the report*/
	public void onStart(ITestContext testContext) {
		
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String currentdatetimestamp=df.format(dt);
		*/
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("Be Cognizant Home Page Header View Automation Report"); // Title of report
		sparkReporter.config().setReportName("Header View Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "Presta Shop Live demo");
		extent.setSystemInfo("Module", "Headers");
//		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", "testAvengers");
		extent.setSystemInfo("Environemnt", "QA");
		
//		String os = testContext.getCurrentXmlTest().getParameter("os");
//		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}
	/*This Method will be implemented When the test case is successfully executed and result is updated in the report*/
	public void onTestSuccess(ITestResult result) {
	
		test = extent.createTest(result.getName());
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS,result.getName()+" got successfully executed");
		try {
//			new BasePackage.BaseClass();
			Object obj = result.getInstance();
			BaseClass base = (BaseClass)obj;
			WebDriver driver = base.getDriver();
			String imgPath = base.captureScreen(result.getName(),driver);
			test.addScreenCaptureFromPath(imgPath);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	/*This Method will be implemented incase of the test failure*/
	/*reason for failure and screenshot of the failure is updated in the reports*/
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
//		
		try {
//			String imgPath = BaseClass.captureScreen(result.getName());
//			test.addScreenCaptureFromPath(imgPath);
			Object obj = result.getInstance();
			BaseClass base = (BaseClass)obj;
			WebDriver driver = base.getDriver();
			String imgPath = base.captureScreen(result.getName(),driver);
			test.addScreenCaptureFromPath(imgPath);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/*This Method will be implemented when is testcase is skipped*/
	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}
	/*This Method will be implemented after all the test cases are executed*/
	public void onFinish(ITestContext testContext) {
		
		extent.flush();
		
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());//used to open the report after the test is executed
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

