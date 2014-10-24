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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupingBox;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.openfaces.util.Rendering.writeStandardEvents;

public class GroupingBoxRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        final ResponseWriter writer = context.getResponseWriter();
        final GroupingBox groupingBox = (GroupingBox) component;
        final UIComponent parent = Components.getFacetOwner(groupingBox);
        if (!(parent instanceof DataTable))
            throw new IllegalStateException("<o:groupingBox> can only be placed into a facet of <o:dataTable> " +
                    "component. Though the following parent component has been encountered: " +
                    parent.getClass().getName());

        final DataTable table = (DataTable) parent;
        final String boxClassName = Styles.getCSSClass(context, component, groupingBox.getStyle(), "o_groupingBox", groupingBox.getStyleClass());
        final String headerClassName = Styles.getCSSClass(context, component, groupingBox.getHeaderStyle(), "o_groupingBox_header", groupingBox.getHeaderClass());
        final String promptClassName = Styles.getCSSClass(context, component, groupingBox.getPromptTextStyle(), "o_groupingBox_promptText", groupingBox.getPromptTextClass());
        final String connectorStyle =  groupingBox.getConnectorStyle();
        writer.startElement("table", component);
        writeStandardEvents(writer, groupingBox);
        writer.writeAttribute("class", boxClassName, null);
        writeIdAttribute(context, component);

        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);

        writer.startElement("tr", component);
        writer.startElement("td", component);
        writer.writeAttribute("style", "position: relative", null);
        writer.startElement("span", component);
        writer.writeAttribute("class", promptClassName, null);
        writer.append(groupingBox.getPromptText());
        writer.endElement("span");

        writer.endElement("td");
        writer.endElement("tr");

        writer.endElement("table");
        Rendering.renderInitScript(context, new ScriptBuilder()
                .initScript(context, component, "O$.Table._initRowGroupingBox",
                        table,
                        connectorStyle, headerClassName,
                        groupingBox.getHeaderHorizOffset(), groupingBox.getHeaderVertOffset())
                .semicolon());
        Styles.renderStyleClasses(context, component);
    }
}
