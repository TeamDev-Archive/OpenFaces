/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.timetable;

import org.openfaces.component.select.TabSetItem;
import org.openfaces.component.timetable.Timetable;
import org.openfaces.component.timetable.TimetableViewSwitcher;
import org.openfaces.renderkit.select.TabSetRenderer;
import org.openfaces.util.Components;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TimetableViewSwitcherRenderer extends TabSetRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        TimetableViewSwitcher switcher = (TimetableViewSwitcher) component;
        Timetable timetable = switcher.getTimetable();
        List<UIComponent> children = switcher.getChildren();
        children.clear();
        children.add(createTabSetItem("Month", switcher, "monthItem"));
        children.add(createTabSetItem("Week", switcher, "weekItem"));
        children.add(createTabSetItem("Day", switcher, "dayItem"));
        switcher.setOnchange(
                new ScriptBuilder().O$(timetable).dot().functionCall("setViewType",
                        new RawScript("['month','week','day'][O$('" + switcher.getClientId(context) + "').getSelectedIndex()]")
                ).toString()
        );

        super.encodeBegin(context, component);

        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, switcher,
                "O$.TimetableViewSwitcher._init", timetable,

                Resources.utilJsURL(context),
                TimetableRenderer.getTimetableJsURL(context)));

    }

    private TabSetItem createTabSetItem(String text, UIComponent idGenerationBase, String idSuffix) {
        FacesContext context = FacesContext.getCurrentInstance();
        TabSetItem tabSetItem = Components.createComponent(context,
                TabSetItem.COMPONENT_TYPE, TabSetItem.class, idGenerationBase, idSuffix);
        HtmlOutputText outputText = Components.createOutputText(context, text);
        outputText.setId(Components.generateIdWithSuffix(tabSetItem, "text"));
        tabSetItem.getChildren().add(outputText);
        return tabSetItem;
    }
}
