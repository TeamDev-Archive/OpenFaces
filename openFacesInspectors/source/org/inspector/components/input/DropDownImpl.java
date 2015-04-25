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

package org.inspector.components.input;

import org.inspector.api.DropDown;
import org.inspector.api.Input;
import org.inspector.api.Link;
import org.inspector.components.ElementWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Max Yurin
 */
public class DropDownImpl extends ElementWrapper implements DropDown {
    public static final String SEARCH_COMPONENT = "--searchComponent";
    public static final String BUTTON = "::button";
    public static final String INPUT_FIELD = "::field";

    private Input inputField;
    private Link link;

    public DropDownImpl(WebDriver webDriver, String elementId) {
        super(webDriver, elementId, DropDown.TAG_NAME);
    }

    @Override
    public Input getInputField() {
        if (inputField == null) {
            inputField = new InputText(driver(), id() + INPUT_FIELD);
        }
        return inputField;
    }

    @Override
    public WebElement getButton() {
        return element().findElement(By.id(id().split("::")[0] + "::button"));
    }

    @Override
    public void select(int index) {
        Select select = new Select(element());
        select.selectByIndex(index);
    }

    @Override
    public void select(String desiredValue) {
        Select select = new Select(element());
        select.selectByValue(desiredValue);
    }

    @Override
    public String getSelectedOption() {
        final WebElement element = findElement(By.xpath("//select[@id='" + id() + "']//option[@selected='selected']"));
        return element.getText();
    }

    @Override
    public List<String> itemsList() {
        Select select = new Select(element());
        final List<WebElement> options = select.getOptions();
        List<String> optionsValue = newArrayList();

        for (WebElement option : options) {
            optionsValue.add(option.getText());
        }

        return optionsValue;
    }
}
