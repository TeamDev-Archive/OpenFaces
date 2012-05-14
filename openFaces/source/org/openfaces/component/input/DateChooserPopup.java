/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.input;

import org.openfaces.component.SimplePopup;
import org.openfaces.component.calendar.Calendar;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.

 * @author Kharchenko
 */
public class DateChooserPopup extends SimplePopup {
    public static final String COMPONENT_TYPE = "org.openfaces.DateChooserPopup";

    public static final String CALENDAR_SUFFIX = Rendering.SERVER_ID_SUFFIX_SEPARATOR + "calendar";

    private Calendar calendar;

    public DateChooserPopup() {
    }

    @Override
    protected void encodeOpeningTags(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("style", "position: absolute; visibility: hidden;", null);
    }

    protected void encodeContent(FacesContext context) throws IOException {
        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof Calendar && child.getClientId(context).indexOf(CALENDAR_SUFFIX) > -1) {
                children.remove(child);
                break;
            }
        }
        children.add(calendar);

        calendar.setId(getId() + CALENDAR_SUFFIX);
        Rendering.renderChildren(context, this);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), calendar};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] vals = (Object[]) state;
        int i = 0;
        super.restoreState(context, vals[i++]);
        calendar = (Calendar) vals[i++];
    }
}
