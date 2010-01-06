/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.openfaces.renderkit.input.DateChooserRenderer;
import org.openfaces.renderkit.input.DropDownComponentRenderer;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;

/**
 * @author Dmitry Pikhulya
 */
public class DateChooserInspector extends DropDownComponentInspector {

    private CalendarInspector calendar;

    public DateChooserInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public DateChooserInspector(ElementInspector element) {
        super(element);
    }

    public CalendarInspector calendar() {
        if (calendar == null) {
            calendar = new CalendarInspector(popup().id() + DateChooserRenderer.CALENDAR_SUFFIX) {

                public void selectCalendarCell(int row, int col) {
                    body().subElement("tr[" + (row + 1) + "]/td[" + (col) + "]/div").mouseUp();
                }
            };
        }

        return calendar;
    }

    public ElementInspector button() {
        return new ElementByLocatorInspector(id() + DropDownComponentRenderer.BUTTON_SUFFIX);
    }

}
