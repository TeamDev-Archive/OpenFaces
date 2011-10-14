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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupingBox;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class GroupingBoxRenderer extends org.openfaces.renderkit.RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        final ResponseWriter writer = context.getResponseWriter();
        GroupingBox groupingBox = (GroupingBox) component;
        UIComponent parent = groupingBox.getParent();
        if (!(parent instanceof DataTable))
            throw new IllegalStateException("<o:groupingBox> can only be placed as a child component inside of " +
                    "a <o:dataTable> component. Though the following parent component has been encountered: " +
                    parent.getClass().getName());

        DataTable table = (DataTable) groupingBox.getParent();

        writer.startElement("div", component);
        writeIdAttribute(context, component);
        String boxClassName = Styles.getCSSClass(context, component, groupingBox.getStyle(), "o_groupingBox", groupingBox.getStyleClass());
        writer.writeAttribute("class", boxClassName, null);
        writer.startElement("span", component);
        
        writer.append(groupingBox.getPromptText());
        writer.endElement("span");
        writer.endElement("div");
        Rendering.renderInitScript(context, new ScriptBuilder()
                .initScript(context, component, "O$.Table._initRowGroupingBox", table).semicolon());
        Styles.renderStyleClasses(context, component);
    }
}
