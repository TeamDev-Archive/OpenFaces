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

package org.inspector.navigator;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author Max Yurin
 */
public class DocumentReadyCondition implements ExpectedCondition<Boolean> {

    public Boolean apply(WebDriver webDriver) {
        Object result = ((JavascriptExecutor) webDriver).executeScript(
                "return document['readyState'] ? 'complete' == document.readyState : true");
        return result != null && result instanceof Boolean && (Boolean) result;
    }
}
