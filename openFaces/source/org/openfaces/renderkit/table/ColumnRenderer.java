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

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.SyntheticColumn;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnRenderer extends RendererBase {
    public static final String ATTR_CUSTOM_CELL = "_customCell";

    private static final String DETECTED_CONVERTER_ATTR = "_detectedConverter";
    private static final String CONVERTER_NOT_SPECIFIED = "notSpecified";

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        Column column = (Column) component;
        Cell customCell = (Cell) column.getAttributes().get(ATTR_CUSTOM_CELL);

        UIComponent cellContentsContainer = customCell != null ? customCell : column;

        Runnable exitContext =  (cellContentsContainer instanceof ContextDependentComponent)
                ? ((ContextDependentComponent) cellContentsContainer).enterComponentContext() : null;
        try {
            List<UIComponent> components = cellContentsContainer instanceof SyntheticColumn
                ? ((SyntheticColumn) cellContentsContainer).getChildrenForProcessing()
                : cellContentsContainer.getChildren();
            if (components.size() > 0)
                renderWithInplaceComponents(context, column, components);
            else
                renderWithColumnValue(context, column);
        } finally {
            if (exitContext != null) exitContext.run();
        }
    }

    private void renderWithInplaceComponents(
            FacesContext context,
            Column column,
            List<UIComponent> components) throws IOException {

        boolean childrenEmpty = true;
        for (UIComponent child : components) {
            child.encodeAll(context);
            if (!TableStructure.isComponentEmpty(child)) {
                childrenEmpty = false;
            }
        }

        TableStructure tableStructure = TableStructure.getCurrentInstance(column.getTable());
        if (childrenEmpty && tableStructure.isEmptyCellsTreatmentRequired())
            Rendering.writeNonBreakableSpace(context.getResponseWriter());
    }

    private void renderWithColumnValue(FacesContext context, Column column) throws IOException {
        // columnValue var should already be set when the column's rendering is initiated
        Object columnValue = context.getExternalContext().getRequestMap().get(Column.COLUMN_VALUE_VAR);

        Converter converter = getColumnConverter(context, column);
        String columnText = converter != null
                ? converter.getAsString(context, column, columnValue)
                : columnValue != null ? columnValue.toString() : "";
        ResponseWriter responseWriter = context.getResponseWriter();
        if (!Rendering.isNullOrEmpty(columnText)) {
            responseWriter.writeText(columnText, null);
        } else {
            TableStructure tableStructure = TableStructure.getCurrentInstance(column.getTable());
            if (tableStructure.isEmptyCellsTreatmentRequired())
                Rendering.writeNonBreakableSpace(responseWriter);
        }
    }

    private Converter getColumnConverter(FacesContext context, Column column) {
        Object detectedConverterAttrValue = column.getAttributes().get(DETECTED_CONVERTER_ATTR);
        if (detectedConverterAttrValue != null) {
            if (detectedConverterAttrValue.equals(CONVERTER_NOT_SPECIFIED))
                return null;
            else
                return (Converter) detectedConverterAttrValue;
        }

        Converter converter = column.getConverter();
        if (converter == null) {
            ValueExpression valueExpression = column.getColumnValueExpression();
            if (valueExpression != null) {
                Class valueType = valueExpression.getType(context.getELContext());
                converter = Rendering.getConverterForType(context, valueType);
            }
        }
        column.getAttributes().put(DETECTED_CONVERTER_ATTR, converter != null ? converter : CONVERTER_NOT_SPECIFIED);
        return converter;
    }

}
