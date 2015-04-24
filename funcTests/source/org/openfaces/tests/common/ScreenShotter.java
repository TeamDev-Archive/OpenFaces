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

import org.apache.commons.io.FileUtils;
import org.inspector.webriver.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

/**
 * @author Max Yurin
 */
public class ScreenShotter {
    private static final String PNG = ".png";
    private static final String JPEG = ".jpeg";

    public ScreenShotter() {}

    private WebDriver getDriver(){
        return WebDriverManager.getWebDriver();
    }

    public void makeScreenShot(String path) {
        try {
            File srcFile = null;
            final WebDriver driver = getDriver();
            if (driver != null) {
                if (driver.getClass().equals(RemoteWebDriver.class)) {
                    srcFile = ((TakesScreenshot) new Augmenter().augment(driver)).getScreenshotAs(OutputType.FILE);
                } else {
                    srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                }
            }

            if (srcFile != null) {
                FileUtils.copyFile(srcFile, new File(path + PNG));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
