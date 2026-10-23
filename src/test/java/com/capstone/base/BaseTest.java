// ABOUTME: TestNG base class that initializes Extent Reports and REST Assured logging.
// ABOUTME: All test classes extend this to get automatic report generation per test method.

package com.capstone.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {

    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite
    public void setupSuite() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        spark.config().setDocumentTitle("REST Assured Capstone - Test Report");
        spark.config().setReportName("API Automation Report");
        spark.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework", "REST Assured + TestNG");
        extent.setSystemInfo("Environment", System.getProperty("env", "qa"));

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeMethod
    public void setupTest(Method method) {
        ExtentTest extentTest = extent.createTest(method.getName());
        test.set(extentTest);
    }

    @AfterMethod
    public void teardownTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.get().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.get().skip(result.getThrowable());
        } else {
            test.get().pass("Test passed");
        }
    }

    @AfterSuite
    public void teardownSuite() {
        extent.flush();
    }
}
