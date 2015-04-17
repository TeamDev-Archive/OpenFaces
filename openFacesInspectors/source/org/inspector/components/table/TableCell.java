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

package org.inspector.components.table;

import org.inspector.components.ElementWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class TableCell extends ElementWrapper {
    public static final String TAG_NAME = "td";

    public TableCell(WebDriver webDriver, String id) {
        super(webDriver, id, TAG_NAME);
    }

    public TableCell(WebDriver driver, WebElement element) {
        super(driver, element, TAG_NAME);
    }

    public String text(){
        return element().getText();
    }

    public int colSpan(){
        final String colSpan = attribute("colSpan");
        return parseInt(colSpan, 1);
    }

    public int rowSpan(){
        final String rowSpan = attribute("rowSpan");
        return parseInt(rowSpan, 1);
    }

    public boolean isChecked(){
        final WebElement input = findElement(By.xpath("input"));
        return input != null && "true".equals(input.getAttribute("value"));
    }
}
