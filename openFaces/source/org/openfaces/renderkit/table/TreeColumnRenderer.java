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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.ExpansionToggle;
import org.openfaces.component.table.Cell;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreeTable;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class TreeColumnRenderer extends RendererBase {
    public static final String ATTR_CUSTOM_CELL = "_customCell";

    private static final String DEFAULT_INDENT_CLASS = "o_treetable_indent";
    private static final String DEFAULT_EXPANSION_TOGGLE_CELL_CLASS = "o_treetable_expansion_toggle_cell";

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        TreeColumn treeColumn = ((TreeColumn) component);
        Cell customCell = (Cell) treeColumn.getAttributes().get(ATTR_CUSTOM_CELL);
        if (!treeColumn.getShowAsTree()) {
            if (customCell != null)
                Rendering.renderChildren(context, customCell);
            else
                Rendering.renderChildren(context, component);
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        TreeTable treeTable = getTreeTable(component);
        if (treeTable == null)
            throw new IllegalStateException("TreeColumn must be embedded into a TreeTable component");
        int level = treeTable.getNodeLevel();

        String indentStyle = null;
        String levelIndent = treeColumn.getLevelIndent();
        if (!Rendering.isNullOrEmpty(levelIndent)) {
            indentStyle = "width: " + levelIndent;
        }
        String indentClass = Styles.getCSSClass(context, treeTable, indentStyle, DEFAULT_INDENT_CLASS);

        writer.startElement("table", component);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("class", "o_cellWrapper", null);
        writer.startElement("tr", component);
        writer.startElement("td", component);
        for (int i = 0; i < level; i++) {
            writer.writeAttribute("class", indentClass, null);
            if (Environment.isOpera() && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.startElement("div", component);
            writer.writeAttribute("class", indentClass, null);
            if ((Environment.isOpera() || Environment.isMozilla()) && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.endElement("div");

            writer.endElement("td");
            writer.startElement("td", component);
        }

        if (Environment.isOpera() && indentStyle != null)
            writer.writeAttribute("style", indentStyle, null);

        boolean nodeHasChildren = treeTable.getNodeHasChildren();
        if (nodeHasChildren) {
            String expansionToggleCellClass = Styles.getCSSClass(
                    context, treeTable, treeColumn.getExpansionToggleCellStyle(),
                    DEFAULT_EXPANSION_TOGGLE_CELL_CLASS, treeColumn.getExpansionToggleCellClass());
            expansionToggleCellClass = Styles.mergeClassNames(expansionToggleCellClass, indentClass);
            writer.writeAttribute("class", Styles.mergeClassNames(expansionToggleCellClass, indentClass), null);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.startElement("div", component);
        if ((Environment.isOpera() || Environment.isMozilla()) && indentStyle != null)
            writer.writeAttribute("style", indentStyle, null);

        if (nodeHasChildren) {
            Components.generateIdIfNotSpecified(component);
            ExpansionToggle expansionToggle = treeColumn.getExpansionToggle();
            expansionToggle.encodeAll(context);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.endElement("div");
        writer.endElement("td");
        writer.startElement("td", component);

        if (customCell != null)
            Rendering.renderChildren(context, customCell);
        else
            Rendering.renderChildren(context, component);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private TreeTable getTreeTable(UIComponent column) {
        UIComponent parent = column.getParent();
        while (parent instanceof ColumnGroup)
            parent = parent.getParent();
        return (TreeTable) parent;
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
