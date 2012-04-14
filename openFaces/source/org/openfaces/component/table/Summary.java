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
package org.openfaces.component.table;

import org.openfaces.component.OUIOutput;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class Summary extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.Summary";
    public static final String COMPONENT_FAMILY = "org.openfaces.Summary";

    private DataTable table;

    public Summary() {
        setRendererType("org.openfaces.SummaryRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    public ValueExpression getBy() {
        return getValueExpression("by");
    }

    public void setBy(ValueExpression by) {
        setValueExpression("by", by);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public DataTable getTable() {
        if (table == null) {
            table = Components.getParentWithClass(this, DataTable.class);
            if (table == null) throw new FacesException("The <o:summary> tag can only be used inside of " +
                    "<o:dataTable> component");
        }
        return table;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ValueExpression getByExpression() {
        ValueExpression ve = getBy();
        if (ve != null) return ve;
        BaseColumn parentColumn = Components.getParentWithClass(this, BaseColumn.class);
        if (parentColumn == null) {
            DataTable table = getTable();
            if (table.getRowIndex() != -1) {
                // reset row index just to ensure a suffix-less table id in an exception message
                table.setRowIndex(-1);
            }
            throw new FacesException("Could not detect the summary calculation expression for " +
                    "<o:summary> component in the table " + table.getClientId(getFacesContext()) + ". " +
                    "Either the \"by\" attribute has to be specified or <o:summary> should be placed into a column's " +
                    "facet to derive the expression from that column automatically");
        }
        ve = parentColumn.getColumnValueExpression();
        if (ve == null) {
            DataTable table = getTable();
            if (table.getRowIndex() != -1) {
                // reset row index just to ensure a suffix-less table id in an exception message
                table.setRowIndex(-1);
            }
            throw new FacesException("Could not detect the summary calculation expression for " +
                    "<o:summary> component in the table " + table.getClientId(getFacesContext()) + ". " +
                    "Neither the \"by\" attribute is specified, nor the value for the parent column could be " +
                    "detected (the column doesn't have the \"value\" attribute).");
        }
        return ve;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Object getByValue(ELContext elContext) {
        Object value = getByExpression().getValue(elContext);
        return value;
    }

    private Object accumulatedValue;

    public Object getAccumulatedValue() {
        return accumulatedValue;
    }

    public void setAccumulatedValue(Object accumulatedValue) {
        this.accumulatedValue = accumulatedValue;
    }

    public void encodeAfterCalculation(FacesContext context) throws IOException {
        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, this, "O$.Summary._init", getAccumulatedValue());
        Rendering.renderInitScript(context, sb, TableUtil.getTableUtilJsURL(context));
    }
}
