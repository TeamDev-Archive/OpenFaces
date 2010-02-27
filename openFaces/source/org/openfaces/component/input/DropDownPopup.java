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
package org.openfaces.component.input;

import org.openfaces.component.AbstractPopup;
import org.openfaces.component.ajax.Ajax;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.input.DropDownFieldRenderer;
import org.openfaces.renderkit.input.DropDownFieldTableStyles;
import org.openfaces.renderkit.table.TableFooter;
import org.openfaces.renderkit.table.TableHeader;
import org.openfaces.renderkit.table.TableStructure;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 *
 * @author Kharchenko
 */
public class DropDownPopup extends AbstractPopup {
    public static final String COMPONENT_TYPE = "org.openfaces.DropDownPopup";

    public static final String INNER_TABLE_SUFFIX = "::innerTable";
    public static final String ITEM_PREFIX = "::popupItem";

    private Collection<UISelectItem> dropDownList;
    private ChildData childData;

    public DropDownPopup() {
    }

    public List getVisibleColumns() {
        ChildData childData = getChildData();
        return childData.getColumns();
    }

    public ChildData getChildData() {
        if (childData != null)
            return childData;
        DropDownFieldBase dropDownField = (DropDownFieldBase) getParent();
        List<UIComponent> dropDownChildren = dropDownField.getChildren();
        List<UIComponent> childComponents = new ArrayList<UIComponent>(dropDownChildren.size());
        for (UIComponent component : dropDownChildren) {
            if (!isDropDownAuxiliaryComponent(component) && !(component instanceof BaseColumn) && !(component instanceof Ajax))
                childComponents.add(component);
        }

        DropDownFieldTableStyles tableStyles = new DropDownFieldTableStyles(dropDownField, childComponents);
        TableStructure tableStructure = new TableStructure(dropDownField, tableStyles);

        childData = new ChildData(tableStructure, childComponents);
        return childData;
    }


    public void resetChildData() {
        childData = null;
    }

    public static class ChildData {
        TableStructure tableStructure;
        private List<UIComponent> childComponents;

        public ChildData(TableStructure tableStructure, List<UIComponent> children) {
            this.tableStructure = tableStructure;
            childComponents = children;
        }

        public TableStructure getTableStructure() {
            return tableStructure;
        }

        public List<BaseColumn> getColumns() {
            return getTableStructure().getColumns();
        }

        public List<UIComponent> getChildComponents() {
            return childComponents;
        }

        public boolean isHeaderSpecified() {
            return getTableStructure().getHeader().isContentSpecified();
        }

        public boolean isFooterSpecified() {
            return getTableStructure().getFooter().isContentSpecified();
        }
    }

    protected void encodeContent(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        DropDownFieldBase dropDownField = (DropDownFieldBase) getParent();

        ChildData childData = getChildData();

        TableUtil.writeColumnTags(context, dropDownField, childData.getColumns());

        TableStructure tableStructure = childData.getTableStructure();
        TableHeader tableHeader = tableStructure.getHeader();
        if (tableHeader.isContentSpecified()) {
            tableHeader.render(context, null);
        }

        writer.startElement("tbody", this);
        if (dropDownList != null)
            renderRows(context, dropDownField, childData, dropDownList);
        writer.endElement("tbody");

        TableFooter tableFooter = tableStructure.getFooter();
        if (tableFooter.isContentSpecified()) {
            tableFooter.render(context, null);
        }
    }

    public void renderRows(
            FacesContext context,
            DropDownFieldBase dropDownField,
            ChildData childData,
            Collection<UISelectItem> dropDownItems) throws IOException {
        List<BaseColumn> columns = childData.getColumns();
        List<UIComponent> childComponents = childData.getChildComponents();
        int colCount = columns.size();

        ResponseWriter writer = context.getResponseWriter();

        String var = dropDownField.getVar();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String itemIdPrefix = getClientId(context) + ITEM_PREFIX;
        int index = 0;
        for (UISelectItem item : dropDownItems) {
            Object originalValue = item.getAttributes().get(DropDownFieldRenderer.ORIGINAL_VALUE_ATTR);
            String convertedValue = (String) item.getAttributes().get(DropDownFieldRenderer.DISPLAYED_VALUE_ATTR);

            writer.startElement("tr", this);
            writer.writeAttribute("id", itemIdPrefix + index++, null);

            Object oldVarValue = null;
            if (var != null)
                oldVarValue = requestMap.put(var, originalValue);

            List<UIComponent> children = item.getChildren();
            if (children != null && children.size() > 0) {
                writer.startElement("td", this);
                if (colCount > 1)
                    writer.writeAttribute("colspan", String.valueOf(colCount), null);
                Rendering.renderChildren(context, item);
                writer.endElement("td");
            } else {
                boolean autoGeneratedColumnFound = false;
                for (BaseColumn column : columns) {
                    boolean autoGeneratedColumn = column.getParent() == null;
                    if (autoGeneratedColumn) {
                        autoGeneratedColumnFound = true;
                        continue;
                    }
                    writer.startElement("td", this);
                    Rendering.renderChildren(context, column);
                    writer.endElement("td");
                }
                if (autoGeneratedColumnFound) {
                    if (childComponents.size() > 0) {
                        writer.startElement("td", this);
                        Rendering.renderComponents(context, childComponents);
                        writer.endElement("td");
                    } else {
                        writer.startElement("td", this);
                        if (colCount > 1)
                            writer.writeAttribute("colspan", String.valueOf(colCount), null);
                        writer.writeText(convertedValue, null);
                        writer.endElement("td");
                    }
                }
            }

            if (var != null)
                requestMap.put(var, oldVarValue);

            writer.endElement("tr");
        }
    }

    private boolean isDropDownAuxiliaryComponent(UIComponent component) {
        return (component instanceof DropDownItem || component instanceof DropDownItems);
    }

    public void setDropDownList(Collection<UISelectItem> dropDownList) {
        this.dropDownList = dropDownList;
    }

    @Override
    public void encodeOpeningTags(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", this);
        String popupId = getClientId(context);
        writer.writeAttribute("id", popupId, "id");
        writer.writeAttribute("class", "o_dropdownfield_list", null);

        writer.startElement("table", this);
        writer.writeAttribute("id", popupId + INNER_TABLE_SUFFIX, "id");
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("width", "100%", null);
    }

    @Override
    protected void encodeClosingTags(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("table");
        writer.endElement("div");
    }

    @Override
    protected boolean getUseDisplayNoneByDefault() {
        return Environment.isExplorer() || Environment.isMozilla();
    }

}
