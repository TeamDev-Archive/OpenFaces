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

import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andrii.loboda
 */
public abstract class AbstractCellSelection extends AbstractTableSelection {

    private MethodExpression cellSelectable;
    private MethodExpression selectableCells;
    private JSONArray cellsSelectableInJSON;
    private String cursorStyle;
    private String cursorClass;

    protected AbstractCellSelection() {
    }

    protected AbstractCellSelection(TableDataModel model) {
        super(model);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                cellSelectable,
                selectableCells,
                cursorStyle,
                cursorClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        cellSelectable = (MethodExpression) stateArray[i++];
        selectableCells = (MethodExpression) stateArray[i++];
        cursorStyle = (String) stateArray[i++];
        cursorClass = (String) stateArray[i++];
    }

    @Override
    protected List<?> convertFieldValue(String fieldValue) {
        try {
            JSONArray array = new JSONArray(fieldValue);
            List<JSONArray> indexes = new ArrayList<JSONArray>();
            for (int i = 0; i < array.length(); i++) {
                indexes.add((JSONArray) array.get(i));

            }
            return indexes;
        } catch (JSONException e) {
            throw new IllegalStateException("Wrong fieldValue for selecting cell", e);
        }
    }

    @Override
    protected String getSelectableItems() {
        return "cells";
    }

    protected final JSONArray transformToJSON(CellId cellId) throws JSONException {
        return new JSONArray(new Object[]{getRowIndexByRowData(cellId.getRowData()), cellId.getColumnId()});
    }

    public MethodExpression getCellSelectable() {
        return ValueBindings.get(this, "cellSelectable", cellSelectable, MethodExpression.class);
    }

    public void setCellSelectable(MethodExpression cellSelectable) {
        this.cellSelectable = cellSelectable;
    }

    public MethodExpression getSelectableCells() {
        return ValueBindings.get(this, "selectableCells", selectableCells, MethodExpression.class);
    }

    public void setSelectableCells(MethodExpression selectableCells) {
        this.selectableCells = selectableCells;
    }

    public void saveSelectableCells(JSONArray array) {
        cellsSelectableInJSON = array;
    }

    @Override
    public JSONArray getSelectablesCells() {
        return cellsSelectableInJSON;
    }

    public String getCursorStyle() {
        return ValueBindings.get(this, "cursorStyle", cursorStyle);
    }

    public void setCursorStyle(String cursorStyle) {
        this.cursorStyle = cursorStyle;
    }

    public String getCursorClass() {
        return ValueBindings.get(this, "cursorClass", cursorClass);
    }

    public void setCursorClass(String cursorClass) {
        this.cursorClass = cursorClass;
    }

    @Override
    public void registerSelectionStyle(FacesContext context) {
        super.registerSelectionStyle(context);
        AbstractTable table = getTable();
        String selectionCls = Styles.getCSSClass_dontCascade(
                context, table, null, StyleGroup.selectedStyleGroup(), null, DefaultStyles.getDefaultCursorSelectionStyle());
        selectionCls += " " + Styles.getCSSClass_dontCascade(
                context, table, getCursorStyle(), StyleGroup.selectedStyleGroup(), getCursorClass(), null);
        getAttributes().put(ATTR_SELECTION_CURSOR_CLS, selectionCls);
    }
}
