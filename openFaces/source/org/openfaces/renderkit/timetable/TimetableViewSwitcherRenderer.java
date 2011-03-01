/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.Timetable;
import org.openfaces.component.timetable.TimetableViewSwitcher;
import org.openfaces.renderkit.select.TabSetRenderer;
import org.openfaces.util.Components;
import org.openfaces.util.RawScript;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimetableViewSwitcherRenderer extends TabSetRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        TimetableViewSwitcher switcher = (TimetableViewSwitcher) component;
        Timetable timetable = switcher.getTimetable();
        UISelectItems selectItems = Components.getChildWithClass(switcher, UISelectItems.class, "items");
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem("Month"));
        items.add(new SelectItem("Week"));
        items.add(new SelectItem("Day"));
        selectItems.setValue(items);
        switcher.setOnchange(
                new ScriptBuilder().O$(timetable).functionCall("setView",
                        new RawScript("['month','week','day'][O$('" + switcher.getClientId(context) + "').getSelectedIndex()]")
                ).toString()
        );

        super.encodeBegin(context, component);

    }
}
