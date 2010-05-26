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
package org.openfaces.component.chart;

import org.openfaces.component.OUICommand;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;
import java.io.IOException;

public class ChartSelection extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.ChartSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.ChartSelection";
    private static final LineStyle DEFAULT_SELECTION_LINE_STYLE = new LineStyle(new Color(0, 0, 255), new BasicStroke(5.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

    private LineStyle lineStyle;
    private Paint fillPaint;
    private ItemInfo selectedItem;
    private String onchange;

    public ChartSelection() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);

        String onchange = getOnchange();
        Script automaticChangeHandler = null;
        Iterable<String> render = getRender();
        Iterable<String> execute = getExecute();

        if (render != null || (execute != null && execute.iterator().hasNext())) {
            AjaxInitializer initializer = new AjaxInitializer();
            automaticChangeHandler = new ScriptBuilder().functionCall("O$._ajaxReload",
                    initializer.getRenderArray(context, this, render),
                    initializer.getAjaxParams(context, this)).semicolon().append("return false;");
        }

        onchange = Rendering.joinScripts(onchange,
                automaticChangeHandler != null ? automaticChangeHandler.toString() : null);

        final Chart chart = (Chart) getParent();
        ScriptBuilder buf = new ScriptBuilder().initScript(context, chart, "O$.Chart._initSelection", onchange);

        Rendering.renderInitScript(context, buf,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, "chart/chart.js"));
        AjaxUtil.renderJSLinks(context);
    }


    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (selectedItem != null && ValueBindings.set(this, "selectedItem", selectedItem))
            selectedItem = null;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, lineStyle),
                saveAttachedState(context, fillPaint),
                saveAttachedState(context, selectedItem),
                onchange
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        lineStyle = (LineStyle) restoreAttachedState(context, state[i++]);
        fillPaint = (Paint) restoreAttachedState(context, state[i++]);
        selectedItem = (ItemInfo) restoreAttachedState(context, state[i++]);
        onchange = (String) state[i++];
    }

    public LineStyle getLineStyle() {
        return ValueBindings.get(this, "lineStyle", lineStyle, DEFAULT_SELECTION_LINE_STYLE, LineStyle.class);
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Paint getFillPaint() {
        return ValueBindings.get(this, "fillPaint", fillPaint, Paint.class);
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }

    public ItemInfo getSelectedItem() {
        return ValueBindings.get(this, "selectedItem", selectedItem, ItemInfo.class);
    }

    public void setSelectedItem(ItemInfo selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getOnchange() {
        return ValueBindings.get(this, "onchange", onchange, String.class);
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
}
