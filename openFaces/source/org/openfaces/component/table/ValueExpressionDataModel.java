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
package org.openfaces.component.table;

import org.openfaces.util.DataUtil;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

/**
 * @author Dmitry Pikhulya
 */
public class ValueExpressionDataModel extends DataModel {
    private ValueExpression valueExpression;
    private DataModel dataModel;

    public ValueExpressionDataModel(ValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    public void readData() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Object value = valueExpression.getValue(elContext);
        dataModel = DataUtil.objectAsDataModel(value);
    }

    public boolean isRowAvailable() {
        return dataModel.isRowAvailable();
    }

    public int getRowCount() {
        return dataModel.getRowCount();
    }

    public Object getRowData() {
        return dataModel.getRowData();
    }

    public int getRowIndex() {
        return dataModel.getRowIndex();
    }

    public void setRowIndex(int rowIndex) {
        dataModel.setRowIndex(rowIndex);
    }

    public Object getWrappedData() {
        return dataModel.getWrappedData();
    }

    public void setWrappedData(Object data) {
        dataModel.setWrappedData(data);
    }
}
