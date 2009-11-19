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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.ExpansionToggle;
import org.openfaces.component.table.TableCell;
import org.openfaces.component.table.TableColumnGroup;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreeTable;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.StyleUtil;

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
        TableCell customCell = (TableCell) treeColumn.getAttributes().get(ATTR_CUSTOM_CELL);
        if (!treeColumn.getShowAsTree()) {
            if (customCell != null)
                RenderingUtil.renderChildren(context, customCell);
            else
                RenderingUtil.renderChildren(context, component);
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        TreeTable treeTable = getTreeTable(component);
        if (treeTable == null)
            throw new IllegalStateException("TreeColumn must be embedded into a TreeTable component");
        int level = treeTable.getNodeLevel();

        String indentStyle = null;
        String levelIndent = treeColumn.getLevelIndent();
        if (!RenderingUtil.isNullOrEmpty(levelIndent)) {
            indentStyle = "width: " + levelIndent;
        }
        String indentClass = StyleUtil.getCSSClass(context, treeTable, indentStyle, DEFAULT_INDENT_CLASS);

        writer.startElement("table", component);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("class", "o_cellWrapper", null);
        writer.startElement("tr", component);
        writer.startElement("td", component);
        for (int i = 0; i < level; i++) {
            writer.writeAttribute("class", indentClass, null);
            if (EnvironmentUtil.isOpera() && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.startElement("div", component);
            writer.writeAttribute("class", indentClass, null);
            if ((EnvironmentUtil.isOpera() || EnvironmentUtil.isMozilla()) && indentStyle != null)
                writer.writeAttribute("style", indentStyle, null);
            writer.endElement("div");

            writer.endElement("td");
            writer.startElement("td", component);
        }

        if (EnvironmentUtil.isOpera() && indentStyle != null)
            writer.writeAttribute("style", indentStyle, null);

        boolean nodeHasChildren = treeTable.getNodeHasChildren();
        if (nodeHasChildren) {
            String expansionToggleCellClass = StyleUtil.getCSSClass(
                    context, treeTable, treeColumn.getExpansionToggleCellStyle(),
                    DEFAULT_EXPANSION_TOGGLE_CELL_CLASS, treeColumn.getExpansionToggleCellClass());
            expansionToggleCellClass = StyleUtil.mergeClassNames(expansionToggleCellClass, indentClass);
            writer.writeAttribute("class", StyleUtil.mergeClassNames(expansionToggleCellClass, indentClass), null);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.startElement("div", component);
        if ((EnvironmentUtil.isOpera() || EnvironmentUtil.isMozilla()) && indentStyle != null)
            writer.writeAttribute("style", indentStyle, null);

        if (nodeHasChildren) {
            ComponentUtil.generateIdIfNotSpecified(component);
            ExpansionToggle expansionToggle = treeColumn.getExpansionToggle();
            expansionToggle.encodeAll(context);
        } else
            writer.writeAttribute("class", indentClass, null);

        writer.endElement("div");
        writer.endElement("td");
        writer.startElement("td", component);

        if (customCell != null)
            RenderingUtil.renderChildren(context, customCell);
        else
            RenderingUtil.renderChildren(context, component);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private TreeTable getTreeTable(UIComponent column) {
        UIComponent parent = column.getParent();
        while (parent instanceof TableColumnGroup)
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
