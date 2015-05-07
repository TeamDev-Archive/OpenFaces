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

import org.inspector.api.ComboBox;
import org.inspector.api.Link;
import org.inspector.components.CommandLink;
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
public class ComboBoxImpl extends ElementWrapper implements ComboBox {
    public static final String BUTTON = "::button";
    public static final String INPUT_FIELD = "::field";

    private Link link;

    public ComboBoxImpl(WebDriver webDriver, String elementId) {
        super(webDriver, elementId, ComboBox.TAG_NAME);
    }

    private Link getButton() {
        if (link == null) {
            link = new CommandLink(driver(), id() + BUTTON);
        }
        return link;
    }

    @Override
    public void toggle() {
        getButton().click();
    }

    @Override
    public String select(int index) {
        Select select = new Select(element());
        select.selectByIndex(index);

        return select.getFirstSelectedOption().getText();
    }

    @Override
    public void select(String desiredValue) {
        Select select = new Select(element());
        select.selectByValue("u-" + desiredValue);
    }

    @Override
    public String getSelectedOption() {
        final WebElement element = findElement(By.xpath("//select[@id='" + id() + "']//option[@selected='selected']"));
        return element.getText();
    }

    @Override
    public List<String> getItemsList() {
        Select select = new Select(element());
        final List<WebElement> options = select.getOptions();
        List<String> optionsValue = newArrayList();

        for (WebElement option : options) {
            optionsValue.add(option.getText());
        }

        return optionsValue;
    }
}
