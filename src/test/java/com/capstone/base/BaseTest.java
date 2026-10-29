// ABOUTME: TestNG base class that initializes Extent Reports and REST Assured logging.
// ABOUTME: All test classes extend this to get automatic report generation per test method.

package com.capstone.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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
    protected ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        // Set up the HTML report file
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");
        reporter.config().setDocumentTitle("REST Assured Capstone - Test Report");
        reporter.config().setReportName("API Automation Report");

        // Create the report and attach the reporter
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Environment", System.getProperty("env", "qa"));

        // Enable REST Assured request and response logging to console
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeMethod
    public void setupTest(Method method) {
        // Create a new entry in the report for each test method
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void teardownTest(ITestResult result) {
        // Log the result of each test into the report
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip(result.getThrowable());
        } else {
            test.pass("Test passed");
        }
    }

    @AfterSuite
    public void teardownSuite() {
        // Write the report to disk
        extent.flush();
    }
}
