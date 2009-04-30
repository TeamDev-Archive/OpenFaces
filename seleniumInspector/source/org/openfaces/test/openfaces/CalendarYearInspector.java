/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test.openfaces;

import org.openfaces.renderkit.calendar.CalendarRenderer;
import org.openfaces.test.ElementByLocatorInspector;
import org.openfaces.test.ElementByReferenceInspector;
import org.openfaces.test.ElementInspector;

/**
 * @author Andrii Gorbatov
 */
public class CalendarYearInspector extends ElementByReferenceInspector {

    public CalendarYearInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public CalendarYearInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector index(int index) {
        return new ElementByLocatorInspector(id() + CalendarRenderer.DROP_SUFFIX + "_" + index);
    }

    public ElementInspector drop() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.DROP_SUFFIX);
    }
}
