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

import org.inspector.api.AjaxSupport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Max Yurin
 */
public class TreeTable extends Table {
    public static final String DEFAULT_TOGGLE_CLASS_NAME = "o_treetable_expansionToggle";

    private AjaxSupport ajaxSupport;

    public TreeTable(WebDriver webDriver, String id) {
        super(webDriver, id);
    }

    public TreeTable(WebDriver webDriver, By locator) {
        super(webDriver, locator);
    }

    public void toggleAllNodes(){
    }

    public void expandAllNodes(){
        int i=0;
        while(true){
            final List<WebElement> elementList = findElements(By.tagName("img"));
            if(elementList.size() <= i){
                break;
            }
            final WebElement element = elementList.get(i);
            if(DEFAULT_TOGGLE_CLASS_NAME.equals(element.getAttribute("class"))){
                element.click();
                ajaxSupport.waitAjaxProcess();
            }
        }
    }
}
