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

import org.inspector.api.DateChooser;
import org.inspector.api.ElementWrapper;
import org.openqa.selenium.WebDriver;

import java.util.Date;

/**
 * @author Max Yurin
 */
public class Calendar extends ElementWrapper implements DateChooser {
    public Calendar(WebDriver driver, String id) {
        super(driver, id, "");

    }

    @Override
    public void set(Date date) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
