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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.ExpansionToggle;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreeTable;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class TreeColumnRenderer extends ColumnRenderer {
    private static final String DEFAULT_INDENT_CLASS = "o_treetable_indent";
    private static final String DEFAULT_EXPANSION_TOGGLE_CELL_CLASS = "o_treetable_expansion_toggle_cell";

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        TreeColumn treeColumn = ((TreeColumn) component);
        ResponseWriter writer = context.getResponseWriter();
        AbstractTable table = getTable(treeColumn);
        if (!treeColumn.getShowAsTree()) {
            super.encodeChildren(context, component);
            return;
        }

        int level = table.getNodeLevel();

        String indentStyle = null;
        String levelIndent = treeColumn.getLevelIndent();
        if (!Rendering.isNullOrEmpty(levelIndent)) {
            indentStyle = "width: " + levelIndent;
        }
        String indentClass = Styles.getCSSClass(context, table, indentStyle, DEFAULT_INDENT_CLASS);

        writer.startElement("table", treeColumn);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("class", "o_cellWrapper", null);
        writer.startElement("tr", treeColumn);
        writer.startElement("td", treeColumn);
        for (int i = 0; i < level; i++) {
            writer.writeAttribute("class", indentClass, null);
            if (Environment.isOpera() && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.startElement("div", treeColumn);
            writer.writeAttribute("class", indentClass, null);
            if ((Environment.isOpera() || Environment.isMozilla()) && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.endElement("div");

            writer.endElement("td");
            writer.startElement("td", treeColumn);
        }

        boolean showTreeStructure = false;
        String treeStructureStyle = showTreeStructure
                ? "background: url('" + Resources.internalURL(context, "table/treeStructureSolid.png") +
                "') no-repeat left center;"
                : null;

        if (Environment.isOpera() && indentStyle != null)
            writer.writeAttribute("style", indentStyle + (treeStructureStyle != null ? treeStructureStyle : ""), null);
        else if (treeStructureStyle != null) {
            writer.writeAttribute("style", treeStructureStyle, null);
        }


        boolean nodeHasChildren = table.getNodeHasChildren();
        if (nodeHasChildren) {
            String expansionToggleCellClass = Styles.getCSSClass(
                    context, table, treeColumn.getExpansionToggleCellStyle(),
                    DEFAULT_EXPANSION_TOGGLE_CELL_CLASS, treeColumn.getExpansionToggleCellClass());
            expansionToggleCellClass = Styles.mergeClassNames(expansionToggleCellClass, indentClass);
            writer.writeAttribute("class", Styles.mergeClassNames(expansionToggleCellClass, indentClass), null);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.startElement("div", treeColumn);
        if ((Environment.isOpera() || Environment.isMozilla()) && indentStyle != null)
            writer.writeAttribute("style", indentStyle, null);

        if (nodeHasChildren) {
            Components.generateIdIfNotSpecified(treeColumn);
            ExpansionToggle expansionToggle = treeColumn.getExpansionToggle();
            expansionToggle.encodeAll(context);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.endElement("div");
        writer.endElement("td");
        writer.startElement("td", treeColumn);

        super.encodeChildren(context, component);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private AbstractTable getTable(TreeColumn column) {
        DataTable groupedDataTable = DataTable.getGroupedDataTable(column);
        if (groupedDataTable != null) {
            // implicitly generated TreeColumn for a grouped DataTable
            return groupedDataTable;
        }

        UIComponent parent = column.getParent();
        while (parent instanceof ColumnGroup)
            parent = parent.getParent();
        if (!(parent instanceof TreeTable))
            throw new IllegalStateException("<o:treeColumn> must be embedded into <o:treeTable> component, but it was placed into a component with class: " + parent.getClass().getName());
        return (AbstractTable) parent;
    }


    static void renderImgTag(FacesContext context, String url, String className) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("img", null);

        writer.writeAttribute("src", url, null);
        if (className != null)
            writer.writeAttribute("class", className, null);
        writer.endElement("img");
    }


}
