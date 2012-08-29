/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
import org.openfaces.test.OpenFacesTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;

/**
 * @author Vladislav Lubenskiy
 */
public class SingleFileUploadTest extends OpenFacesTestCase {
    @Test
    public void testFileUpload() {
        testAppFunctionalPage("/components/fileupload/singleFileUpload_base.jsf");
        File file = new File("C:\\avatar.jpg");
        File file1 = new File("C:\\avatar1.jpg");

        List<WebElement> buttons = getDriver().findElements(By.xpath("//input[@type='file']"));
        WebElement uploadButton = buttons.get(0);
        WebElement uploadButton1 = buttons.get(1);
//        WebElement uploadButton1 = getDriver().findElement(By.xpath("//input[@type='file'][2]"));
        uploadButton1.sendKeys(file1.getAbsolutePath());
//        uploadButton1.sendKeys(file.getAbsolutePath());
//        uploadButton.sendKeys(file.getAbsolutePath());

        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(By.id("formID:fileName"), "avatar.jpg"));
        } catch (TimeoutException e) {
            assertTrue("File isn't found", false);
        }
        assertTrue(true);
    }
}

