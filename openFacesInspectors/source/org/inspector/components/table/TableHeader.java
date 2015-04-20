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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class TableHeader extends TableRowContainer {
    public static final String TAG_NAME = "thead";

    public TableHeader(WebDriver webDriver, String id) {
        super(webDriver, id, TAG_NAME);
    }

    public TableHeader(WebDriver webDriver, WebElement element){
        super(webDriver, element, TAG_NAME);
    }
}
