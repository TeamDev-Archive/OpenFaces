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

package org.inspector.components;

import org.inspector.api.Input;
import org.inspector.api.impl.ControlImpl;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class InputText extends ControlImpl implements Input {
    public InputText(WebDriver webDriver, String id) {
        super(webDriver, id, "text");
    }

    @Override
    public void clear() {
        element().clear();
    }

    @Override
    public String getText() {
        return element().getText();
    }

    @Override
    public void setText(String text) {
        element().sendKeys(text);
    }

    @Override
    public String getValue() {
        return getAttribute("value");
    }

    @Override
    public void setValue(CharSequence... sequence) {
//        getDriver().executeScript("document.getElementById('elementID').setAttribute('value', 'new value for element')");
    }
}
