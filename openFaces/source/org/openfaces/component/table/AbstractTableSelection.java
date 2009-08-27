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
package org.openfaces.component.table;

import org.openfaces.util.ComponentUtil;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.table.DataTableRenderer;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableSelection extends UICommand {
    private static final String SESSION_KEY_SELECTION_EVENTS_PROCESSED = "OF:tableSelectionEventsProcessed";

    private TableDataModel model;
    private boolean selectionChanged;
    private String style;
    private String styleClass;
    private Boolean enabled;
    private Boolean mouseSupport;
    private Boolean keyboardSupport;
    private String onchange;

    protected AbstractTableSelection() {
        setRendererType(null);
    }

    protected AbstractTableSelection(TableDataModel model) {
        this.model = model;
    }

    public abstract boolean isMultipleSelectionAllowed();

    public abstract void rememberByKeys();

    protected abstract void readSelectionFromBinding();

    protected abstract void writeSelectionToBinding();

    /**
     * @return List of Index instances
     */
    protected abstract List<Integer> encodeSelectionIntoIndexes();

    protected abstract void decodeSelectionFromIndexes(List<Integer> indexes);

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, enabled, mouseSupport, keyboardSupport, style, styleClass, onchange};
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        enabled = (Boolean) stateArray[i++];
        mouseSupport = (Boolean) stateArray[i++];
        keyboardSupport = (Boolean) stateArray[i++];
        style = (String) stateArray[i++];
        styleClass = (String) stateArray[i++];
        onchange = (String) stateArray[i++];
    }

    public String getOnchange() {
        return ValueBindings.get(this, "onchange", onchange);
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public boolean isEnabled() {
        return ValueBindings.get(this, "enabled", enabled, true);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isMouseSupport() {
        return ValueBindings.get(this, "mouseSupport", mouseSupport, true);
    }

    public void setMouseSupport(boolean mouseSupport) {
        this.mouseSupport = mouseSupport;
    }

    public boolean isKeyboardSupport() {
        return ValueBindings.get(this, "keyboardSupport", keyboardSupport, true);
    }

    public void setKeyboardSupport(boolean keyboardSupport) {
        this.keyboardSupport = keyboardSupport;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void beforeInvokeApplication() {
        setSelectionChanged(false);
    }

    public void beforeEncode() {
        if (!isSelectionChanged())
            readSelectionFromBinding();
    }

    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        writeSelectionToBinding();
    }

    public TableDataModel getModel() {
        return model;
    }

    public void setModel(TableDataModel model) {
        this.model = model;
    }

    protected boolean isSelectionChanged() {
        return selectionChanged;
    }

    protected void setSelectionChanged(boolean selectionChanged) {
        this.selectionChanged = selectionChanged;
    }

    private AbstractTable table;

    public AbstractTable getTable() {
        if (table == null) {
            table = (AbstractTable) getParent();
        }
        return table;
    }

    public void setTable(AbstractTable table) {
        this.table = table;
    }

    /**
     * This method must be invoked before rendering this selection component.
     * <p/>
     * Registering selection before the rendering procedure is required for all AbstractTableRenderer to be able to
     * render selection in one <style> tag with other table styles, which is required to work around IE bug during Ajax,
     * where styles are inserted in the wrong order during replaceChild if they are in different <style> tags.
     *
     * @param context current FacesContext
     */
    public void registerSelectionStyle(FacesContext context) {
        AbstractTable table = getTable();
        String selectionCls = StyleUtil.getCSSClass_dontCascade(
                context, table, getStyle(), StyleGroup.selectedStyleGroup(), getStyleClass(), DefaultStyles.getDefaultSelectionStyle());
        getAttributes().put("_selectionCls_", selectionCls);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);

        AbstractTable table = getTable();
        ResponseWriter writer = context.getResponseWriter();
        RenderingUtil.renderHiddenField(writer, getSelectionFieldName(context, table), null);

        boolean postEvent = getActionExpression() != null;

        ScriptBuilder buf = new ScriptBuilder().initScript(context, table, "O$.Table._initSelection",
                isEnabled(),
                "rows",
                isMultipleSelectionAllowed(),
                encodeSelectionIntoIndexes(),
                getAttributes().get("_selectionCls_"),
                getOnchange(),
                postEvent ? getSelectionEventsProcessed() + 1 : null,
                getSelectionColumnIndexes(table),
                isMouseSupport(),
                isKeyboardSupport());

        RenderingUtil.renderInitScript(context, buf, new String[]{
                ResourceUtil.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                ResourceUtil.getInternalResourceURL(context, DataTableRenderer.class, "table.js")
        });

        if (postEvent)
            RenderingUtil.renderHiddenField(writer, getSelectionEventFieldName(context, table), null);
    }

    private List<Integer> getSelectionColumnIndexes(AbstractTable table) {
        List<Integer> result = new ArrayList<Integer>();
        List<BaseColumn> columns = table.getColumnsForRendering();
        for (int i = 0, colIndex = 0, colCount = columns.size(); i < colCount; i++) {
            BaseColumn column = columns.get(i);
            if (column instanceof SelectionColumn) {
                result.add(colIndex);
            }
            colIndex++;
        }
        return result;
    }

    private String getSelectionEventFieldName(FacesContext facesContext, UIComponent table) {
        return table.getClientId(facesContext) + "::selectionEvent";
    }

    private int getSelectionEventsProcessed() {
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String eventCounterStr = (String) sessionMap.get(SESSION_KEY_SELECTION_EVENTS_PROCESSED);
        int eventCounter = (eventCounterStr != null) ? Integer.parseInt(eventCounterStr) : 0;
        return eventCounter;
    }

    private void setSelectionEventsProcessed(int value) {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put(SESSION_KEY_SELECTION_EVENTS_PROCESSED, String.valueOf(value));
    }

    private String getSelectionFieldName(FacesContext facesContext, UIComponent table) {
        return table.getClientId(facesContext) + "::selection";
    }

    public void decode(FacesContext context) {
        super.decode(context);
        AbstractTable table = getTable();
        if (!isEnabled())
            return;
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String selectionFieldName = getSelectionFieldName(context, table);
        String selectionFieldValue = requestParameterMap.get(selectionFieldName);
        if (selectionFieldValue == null || selectionFieldValue.length() == 0)
            return;
        if (!(selectionFieldValue.startsWith("[") && selectionFieldValue.endsWith("]")))
            throw new IllegalStateException("AbstractTableSelection.decodeSelection: illegal selectionField value: " + selectionFieldValue);
        selectionFieldValue = selectionFieldValue.substring(1, selectionFieldValue.length() - 1);
        String[] items = selectionFieldValue.split(",");
        int itemCount = items.length;
        if (itemCount == 1 && items[0].equals(""))
            itemCount = 0;
        List<Integer> indexes = new ArrayList<Integer>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            String item = items[i];
            indexes.add(Integer.valueOf(item));
        }
        decodeSelectionFromIndexes(indexes);

        String eventNoStr = requestParameterMap.get(getSelectionEventFieldName(context, table));
        if (eventNoStr != null && eventNoStr.length() > 0) {
            int eventNo = Integer.parseInt(eventNoStr);
            int eventsProcessed = getSelectionEventsProcessed();
            if (eventNo > eventsProcessed) {
                setSelectionEventsProcessed(eventNo);
                FacesEvent event = new ActionEvent(this);
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                queueEvent(event);
            }
        }
    }

    protected List objectToList(Object value, String attributeName) {
        if (value == null)
            return null;
        if (value instanceof List)
            return (List) value;
        if (value instanceof Collection)
            return new ArrayList<Object>((Collection) value);
        Class valueClass = value.getClass();
        if (valueClass.isArray()) {
            Object[] array = ComponentUtil.anyArrayToObjectArray(value);
            return Arrays.asList(array);
        }
        throw new RuntimeException("Value of invalid type was provided by the '" + attributeName + "' attribute binding: " + valueClass + "; expected either a collection or an array");
    }
}
