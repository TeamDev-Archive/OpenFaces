/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.fileupload;

import org.junit.Test;
import org.openfaces.test.BaseSeleniumTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Vladislav Lubenskiy
 */
public class FileUploadTest  extends BaseSeleniumTest {
    final static private String NORMAL_FILE_ADDRESS = "c:\\fileUploadTestResources\\normal_size.bmp";
    final static private String NORMAL_FILE_NAME = "normal_size.bmp";
    final static private String TOO_BIG_FILE = "c:\\fileUploadTestResources\\too_big_size.bmp";
    final static private String WRONG_TYPE_FILE = "c:\\fileUploadTestResources\\wrong_type.txt";

     @Test
    public void testSingleFileUpload() {
        testAppFunctionalPage("/components/fileupload/singleFileUpload.jsf");
        assertTrue(element("formID:fileUpload::dragArea").elementExists());
        assertTrue(element("formID:fileUpload::actionButtonContainer").elementExists());
        assertTrue(element("formID:fileUpload::addButton").elementExists());
        assertTrue(element("formID:fileUpload::addButton::title").elementExists());
        assertTrue(element("formID:fileUpload::addButton::forInput").elementExists());

        WebElement progressBar = getDriver().findElement(By.className("o_progress_bar"));
        WebElement input = getDriver().findElement(By.id("formID:fileUpload::addButton::forInput")).
                findElements(By.tagName("input")).get(0);
        WebElement actionButtonContainer = getDriver().findElement(By.id("formID:fileUpload::actionButtonContainer"));

        assertFalse(progressBar.isDisplayed());
        input.sendKeys(NORMAL_FILE_ADDRESS);
        assertTrue(actionButtonContainer.findElement(By.tagName("input")).getAttribute("value").equals("Stop it!"));
        assertTrue(progressBar.isDisplayed());
        sleep(7000);
        assertTrue(getDriver().findElement(By.id("formID:fileName")).getText().equals(NORMAL_FILE_NAME));
        assertTrue(getDriver().findElement(By.id("formID:fileSize")).getText().length() > 1);
    }

     @Test
    public void testWrongMimeTypeAndSize() {
        testAppFunctionalPage("/components/fileupload/singleFileUpload.jsf");
        WebElement input = getDriver().
                findElement(By.id("formID:fileUpload::addButton::forInput")).findElements(By.tagName("input")).
                get(0);

        input.sendKeys(WRONG_TYPE_FILE);
        assertTrue(isAlertPresent());
        acceptAlert();
        input.sendKeys(TOO_BIG_FILE);
        sleep(7000);
        assertTrue(getDriver().findElement(By.id("formID:fileSize")).getText().equals("SIZE_LIMIT_EXCEEDED"));
    }

}
