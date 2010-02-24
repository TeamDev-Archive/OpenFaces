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

import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.AbstractTableRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class CheckboxColumn extends BaseColumn {
    public static final String COMPONENT_TYPE = "org.openfaces.CheckboxColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.CheckboxColumn";

    private static final String DEFAULT_CLASS = "o_checkbox_column";
    private static final String ROW_INDEXES_ONLY_FOR_TABLES_MESSAGE = "CheckboxColumn's rowIndexes property cannot be used for CheckboxColumn embedded into TreeTable. Row indexes are only available within DataTable.";
    private static final String NODE_PATH_FOR_TREETABLE_MESSAGE = "CheckboxColumn's nodePaths property cannot be used for CheckboxColumn embedded into DataTable. Node paths are only available within TreeTable.";

    private Boolean sortable;
    private AbstractTableSelection selectedRows;
    private Map<String, ValueExpression> deferredValueExpressions;

    public CheckboxColumn() {
        setRendererType("org.openfaces.CheckboxColumnRenderer");
        getAttributes().put("defaultStyle", DEFAULT_CLASS);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                saveAttachedState(context, selectedRows),
                saveExpressionMap(context, deferredValueExpressions),
                sortable};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        super.restoreState(context, state[0]);
        selectedRows = (AbstractTableSelection) restoreAttachedState(context, state[1]);
        deferredValueExpressions = restoreExpressionMap(context, state[2]);
        sortable = (Boolean) state[3];
        assignDataModel();
    }


    private Map<String, ValueExpression> restoreExpressionMap(FacesContext context, Object obj) {
        if (obj == null)
            return null;
        Map<String, ValueExpression> result = new HashMap<String, ValueExpression>();
        Set<Map.Entry<String, ValueExpression>> entries = ((Map<String, ValueExpression>) obj).entrySet();
        for (Map.Entry<String, ValueExpression> entry : entries) {
            result.put(entry.getKey(), (ValueExpression) restoreAttachedState(context, entry.getValue()));
        }
        return result;

    }

    private Object saveExpressionMap(FacesContext context, Map<String, ValueExpression> bindingMap) {
        if (bindingMap == null)
            return null;
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, ValueExpression> entry : bindingMap.entrySet()) {
            result.put(entry.getKey(), saveAttachedState(context, entry.getValue()));
        }
        return result;
    }

    public boolean getSortable() {
        return ValueBindings.get(this, "sortable", sortable, false);
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }


    void assignDataModel() {
        AbstractTable table = getTable();
        if (table == null)
            throw new IllegalStateException("Check-box column must be inserted into a DataTable or TreeTable component");
        initSelectedRowsIfNeeded(table);
        selectedRows.setTable(table);
        selectedRows.setModel((TableDataModel) table.getUiDataValue());
    }

    private void initSelectedRowsIfNeeded(AbstractTable table) {
        if (selectedRows == null) {
            if (table instanceof DataTable)
                selectedRows = new MultipleRowSelection();
            else if (table instanceof TreeTable)
                selectedRows = new MultipleNodeSelection();
            else
                throw new IllegalStateException("Unknown AbstractTable implementation: " + table.getClass().getName());

            if (deferredValueExpressions != null) {
                Set<Map.Entry<String, ValueExpression>> bindings = deferredValueExpressions.entrySet();
                for (Map.Entry<String, ValueExpression> bindingEntry : bindings) {
                    String name = bindingEntry.getKey();
                    ValueExpression expression = bindingEntry.getValue();
                    String adaptedPropertyName = adaptPropertyName(name, selectedRows);
                    selectedRows.setValueExpression(adaptedPropertyName, expression);
                }
                deferredValueExpressions = null;
            }

        }
    }

    public List<Integer> encodeSelectionIntoIndexes() {
        if (selectedRows == null || selectedRows.getModel() == null)
            assignDataModel();
        return selectedRows.encodeSelectionIntoIndexes();
    }

    public void decodeSelectionFromIndexes(List<Integer> indexes) {
        selectedRows.decodeSelectionFromIndexes(indexes);
    }

    public List<Integer> getRowIndexes() {
        if (selectedRows instanceof MultipleRowSelection)
            return ((MultipleRowSelection) selectedRows).getRowIndexes();
        else if (selectedRows instanceof MultipleNodeSelection)
            throw new UnsupportedOperationException(ROW_INDEXES_ONLY_FOR_TABLES_MESSAGE);
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));

    }

    public void setRowIndexes(List<Integer> indexes) {
        if (selectedRows instanceof MultipleRowSelection)
            ((MultipleRowSelection) selectedRows).setRowIndexes(indexes);
        else if (selectedRows instanceof MultipleNodeSelection)
            throw new UnsupportedOperationException(ROW_INDEXES_ONLY_FOR_TABLES_MESSAGE);
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }

    public List<Object> getRowDatas() {
        if (selectedRows instanceof MultipleRowSelection)
            return ((MultipleRowSelection) selectedRows).getRowDatas();
        else if (selectedRows instanceof MultipleNodeSelection)
            return ((MultipleNodeSelection) selectedRows).getNodeDatas();
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }

    public void setRowDatas(List<Object> rowDatas) {
        if (selectedRows instanceof MultipleRowSelection)
            ((MultipleRowSelection) selectedRows).setRowDatas(rowDatas);
        else if (selectedRows instanceof MultipleNodeSelection)
            ((MultipleNodeSelection) selectedRows).setNodeDatas(rowDatas);
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }

    public List<TreePath> getNodePaths() {
        if (selectedRows instanceof MultipleRowSelection)
            throw new UnsupportedOperationException(NODE_PATH_FOR_TREETABLE_MESSAGE);
        else if (selectedRows instanceof MultipleNodeSelection)
            return ((MultipleNodeSelection) selectedRows).getNodePaths();
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }

    public void setNodePaths(List<TreePath> nodePaths) {
        if (selectedRows instanceof MultipleRowSelection)
            throw new UnsupportedOperationException(NODE_PATH_FOR_TREETABLE_MESSAGE);
        else if (selectedRows instanceof MultipleNodeSelection)
            ((MultipleNodeSelection) selectedRows).setNodePaths(nodePaths);
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (selectedRows == null || selectedRows.getModel() == null)
            assignDataModel();
        selectedRows.beforeEncode(); // todo: encodeBegin is invoked for each row, and selectedRows.beforeEncode() reads the selection binding, so this call should be moved to a place where it will be invoked just one per table rendering
        super.encodeBegin(context);
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        selectedRows.processUpdates(context);
        selectedRows.beforeInvokeApplication();
    }

    @Override
    public void setValueExpression(String name, ValueExpression expression) {
        if (MultipleRowSelection.ROW_INDEXES_PROPERTY.equals(name) ||
                MultipleRowSelection.ROW_DATAS_PROPERTY.equals(name) ||
                "nodePaths".equals(name)) {
            if (selectedRows != null) {
                String adaptedPropertyName = adaptPropertyName(name, selectedRows);
                selectedRows.setValueExpression(adaptedPropertyName, expression);
            } else {
                if (deferredValueExpressions == null)
                    deferredValueExpressions = new HashMap<String, ValueExpression>();
                deferredValueExpressions.put(name, expression);
            }
        } else
            super.setValueExpression(name, expression);
    }

    private String adaptPropertyName(String name, AbstractTableSelection selection) {
        if (selection instanceof MultipleRowSelection) {
            if (name.equals(MultipleRowSelection.ROW_INDEXES_PROPERTY))
                return name;
            if (name.equals(MultipleRowSelection.ROW_DATAS_PROPERTY))
                return name;
            if (name.equals("nodePaths"))
                throw new IllegalStateException(NODE_PATH_FOR_TREETABLE_MESSAGE);

            throw new IllegalStateException("Unknown property: " + name);
        } else if (selection instanceof MultipleNodeSelection) {
            if (name.equals(MultipleRowSelection.ROW_INDEXES_PROPERTY))
                throw new UnsupportedOperationException(ROW_INDEXES_ONLY_FOR_TABLES_MESSAGE);
            if (name.equals(MultipleRowSelection.ROW_DATAS_PROPERTY))
                return "nodeDatas";
            if (name.equals("nodePaths"))
                return name;

            throw new IllegalStateException("Unknown property: " + name);
        } else
            throw new IllegalStateException("selection is " + (selection != null ? selection.getClass().getName() : "null"));
    }

    @Override
    public ValueExpression getValueExpression(String name) {
        if (MultipleRowSelection.ROW_INDEXES_PROPERTY.equals(name) ||
                MultipleRowSelection.ROW_DATAS_PROPERTY.equals(name) ||
                "nodePaths".equals(name)) {
            if (selectedRows == null)
                return deferredValueExpressions.get(name);
            else
                return selectedRows.getValueExpression(name);
        } else
            return super.getValueExpression(name);
    }

    void rememberByKeys() {
        selectedRows.rememberByKeys();
    }


    public void encodeOnAjaxNodeFolding(FacesContext context) throws IOException {
        if (selectedRows instanceof MultipleNodeSelection) {
            List<Integer> selectedRowIndexes = selectedRows.encodeSelectionIntoIndexes();
            ScriptBuilder buf = new ScriptBuilder().functionCall("O$.Table._setCheckboxColIndexes",
                    this, selectedRowIndexes).semicolon();
            Rendering.renderInitScript(context, buf,
                    Resources.getUtilJsURL(context),
                    TableUtil.getTableUtilJsURL(context),
                    AbstractTableRenderer.getTableJsURL(context)
            );

        } else if (!(selectedRows instanceof MultipleRowSelection)) {
            throw new IllegalStateException("mySelectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
        }
    }

    public List getSelectedRowKeys() {
        if (selectedRows instanceof MultipleRowSelection)
            return ((MultipleRowSelection) selectedRows).getSelectedRowKeys();
        else if (selectedRows instanceof MultipleNodeSelection)
            return ((MultipleNodeSelection) selectedRows).getSelectedNodeKeyPaths();
        else
            throw new IllegalStateException("selectedRows is " + (selectedRows != null ? selectedRows.getClass().getName() : "null"));
    }
}
