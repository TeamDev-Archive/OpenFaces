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
package org.openfaces.component.calendar;

import javax.faces.component.UIComponentBase;

/**
 * @author Kharchenko
 */
public abstract class AbstractDateRange extends UIComponentBase {

    protected AbstractDateRange() {
    }

    public abstract void setDayStyle(String style);

    public abstract String getDayStyle();

    public abstract void setRolloverDayStyle(String rolloverStyle);

    public abstract String getRolloverDayStyle();

    public abstract void setDayClass(String styleClass);

    public abstract String getDayClass();

    public abstract void setRolloverDayClass(String styleClass);

    public abstract String getRolloverDayClass();

    public abstract void setSelectedDayStyle(String style);

    public abstract String getSelectedDayStyle();

    public abstract void setRolloverSelectedDayStyle(String style);

    public abstract String getRolloverSelectedDayStyle();

    public abstract void setSelectedDayClass(String styleClass);

    public abstract String getSelectedDayClass();

    public abstract void setRolloverSelectedDayClass(String styleClass);

    public abstract String getRolloverSelectedDayClass();

}
