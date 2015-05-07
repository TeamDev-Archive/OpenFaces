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
import org.inspector.api.Popup;
import org.inspector.components.ElementWrapper;
import org.inspector.components.popup.PopupBasedOnTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class DropDownImpl extends ElementWrapper implements DropDown {
    public static final String SEARCH_COMPONENT = "--searchComponent";
    public static final String POPUP = "--popup::innerTable";
    public static final String BUTTON = "::button";
    public static final String INPUT_FIELD = "::field";

    private Input inputField;
    private Popup popup;

    public DropDownImpl(WebDriver webDriver, String elementId) {
        super(webDriver, elementId, DropDown.TAG_NAME);
    }

    @Override
    public Input inputField() {
        if (inputField == null) {
            inputField = new InputText(driver(), id() + INPUT_FIELD);
        }
        return inputField;
    }

    @Override
    public WebElement button() {
        final WebElement table = getParent("table");
        return table.findElement(By.id(table.getAttribute("id") + BUTTON));
    }

    @Override
    public Popup popup() {
        if (popup == null) {
            popup = new PopupBasedOnTable(driver(), By.id(id() + POPUP));
        }

        return popup;
    }

    @Override
    public void setValue(String desiredValue) {
        inputField().setValue(desiredValue);
    }

    @Override
    public DropDown togglePopup() {
        this.button().click();

        return this;
    }

    @Override
    public DropDown showPopup() {
        int count = 5;

        while (!popup().isDisplayed()) {
            if (count > 0) {
                togglePopup();
            } else {
                throw new RuntimeException("Popup is not displayed. DRopDown: " + id());
            }

            count--;
        }

        return this;
    }

    @Override
    public DropDown hidePopup() {
        int count = 5;

        while (popup().isDisplayed()) {
            if (count > 0) {
                togglePopup();
            } else {
                throw new RuntimeException("Popup is not displayed. DRopDown: " + id());
            }

            count--;
        }

        return this;
    }
}
