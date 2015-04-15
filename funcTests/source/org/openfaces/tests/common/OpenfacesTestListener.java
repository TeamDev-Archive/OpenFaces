/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.tests.common;

import com.google.common.base.Joiner;
import org.inspector.webriver.PropertyTestConfiguration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Max Yurin
 */
public class OpenfacesTestListener implements ITestListener {
    public static final String IGNORE = "ignore";
    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";
    public static final String SEPARATOR = "/";
    public static final String SCREEN_SHOOT = "_screenShoot_";
    public static final String PNG = ".png";
    private static String browserName = "firefox";

    private static ScreenShotter screenShotter;
    private static PropertyTestConfiguration properties;

    private void init(ITestResult iTestResult) {
        final Object iTestResultInstance = iTestResult.getInstance();

        if (iTestResultInstance instanceof BaseSeleniumTest) {
            final BaseSeleniumTest instance = (BaseSeleniumTest) iTestResultInstance;
            final WebDriver webDriver = instance.getDriver();
            properties = BaseSeleniumTest.getProperties();

            if (webDriver != null && webDriver instanceof RemoteWebDriver) {
                final Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
                if (capabilities != null) {
                    browserName = capabilities.getBrowserName();
                }
            }

            screenShotter = new ScreenShotter(webDriver);
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("Instance: " + iTestResult.getInstanceName() + ", Test " + iTestResult.getName() + " Started");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        final String testName = iTestResult.getName();
        System.out.println("Test " + testName + " successfully completed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        init(iTestResult);


        final String[] strings = iTestResult.getInstanceName().split("\\.");
        String className = strings.length > 0 ? strings[strings.length - 1] : "";

        final String testName = iTestResult.getName();
        System.out.println("Test " + testName + "failure");
        screenShotter.makeScreenShot(getPath(FAILURE, className, testName));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("Test " + iTestResult.getName() + " Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test " + iTestResult.getName() + " Failed but without successful percentage");
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("Browser: " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("Test " + iTestContext.getName() + " Started");
    }

    private String getPath(String status, String className, String testName) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        final String prefix = FAILURE.equals(status) ? properties.getFailTestsFolder() : properties.getCompleteTestsFolder();
        final String formattedDate = dateFormat.format(new Date());

        Joiner joiner = Joiner.on("").skipNulls();

        return joiner.join(properties.getBaseReportFolder(), prefix, browserName,SEPARATOR, className,
                SEPARATOR, testName, SCREEN_SHOOT, formattedDate);
    }
}
