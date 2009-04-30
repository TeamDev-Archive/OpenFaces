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
 * @author Dmitry Pikhulya
 */
public class CalendarInspector extends ElementByReferenceInspector {

    private ElementInspector body;

    public CalendarInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public CalendarInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector body() {
        if (body == null)
            body = new ElementByLocatorInspector(id() + CalendarRenderer.BODY_SUFFIX);
        return body;
    }

    public ElementInspector today() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.TODAY_SELECTOR_SUFFIX);
    }

    public CalendarMonthInspector month() {
        return new CalendarMonthInspector(id() + CalendarRenderer.MONTH_SELECTOR_SUFFIX);
    }

    public ElementInspector monthIncrease() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.MONTH_INCREASE_SELECTOR_SUFFIX);
    }

    public ElementInspector monthDecrease() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.MONTH_DECREASE_SELECTOR_SUFFIX);
    }

    public CalendarYearInspector year() {
        return new CalendarYearInspector(id() + CalendarRenderer.YEAR_SELECTOR_SUFFIX);
    }

    public ElementInspector yearIncrease() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.YEAR_INCREASE_SELECTOR_SUFFIX);
    }

    public ElementInspector yearDecrease() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.YEAR_DECREASE_SELECTOR_SUFFIX);
    }

    public ElementInspector none() {
        return new ElementByLocatorInspector(id() + CalendarRenderer.NONE_SELECTOR_SUFFIX);
    }

    public void selectCalendarCell(int row, int col) {
        subElement("tbody[1]/tr[" + (row + 1) + "]/td[" + (col) + "]/div").mouseUp();
    }

    public void selectMonthFromPopupList(int monthIndex) {
        month().mouseDown();
        month().drop().assertVisible(true);
        month().index(monthIndex).mouseUp();
        month().drop().assertVisible(false);
    }

    public void selectYearFromPopupList(int yearIndex) {
        year().mouseDown();
        year().drop().assertVisible(true);
        year().index(yearIndex).mouseUp();
        year().drop().assertVisible(false);
    }
}
