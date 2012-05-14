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
package org.openfaces.component.chart;

import org.openfaces.component.chart.impl.configuration.charts.ChartConfigurator;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.Collection;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class ChartView extends UICommand implements StyledComponent, HasLabels {
    private Boolean enable3D;
    private Color wallColor;

    private String style;
    private String url;
    private String tooltip;
    private ChartLabels labels;
    private Object colors;
    private Float foregroundAlpha;
    private String onmouseover;
    private String onmouseout;
    private String onclick;

    private Paint backgroundPaint;
    private Paint titlePaint;
    protected LineStyle defaultOutlineStyle;
    protected Collection<LineStyle> outlines;

    protected ChartView() {
        setRendererType(null);
    }

    public boolean isEnable3D() {
        return ValueBindings.get(this, "enable3D", enable3D, false);
    }

    public void setEnable3D(boolean enable3D) {
        this.enable3D = enable3D;
    }

    public Color getWallColor() {
        return ValueBindings.get(this, "wallColor", wallColor, Color.GRAY, Color.class);
    }

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
    }

    public String getOnmouseover() {
        return onmouseover;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmouseout() {
        return onmouseout;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicOnclick() {
        ValueExpression ve = getValueExpression("onclick");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicOnMouseOver() {
        ValueExpression ve = getValueExpression("onmouseover");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicOnMouseOut() { // todo: review whether these public getDynamic... methods should really be public
        ValueExpression ve = getValueExpression("onmouseout");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicTooltip() {
        ValueExpression ve = getValueExpression("tooltip");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setDynamicTooltip(ChartViewValueExpression dynamicTooltip) {
        setValueExpression("tooltip", dynamicTooltip);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicUrl() {
        ValueExpression ve = getValueExpression("url");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setDynamicUrl(ChartViewValueExpression dynamicUrl) {
        setValueExpression("url", dynamicUrl);
    }

    public Float getForegroundAlpha() {
        return ValueBindings.get(this, "foregroundAlpha", foregroundAlpha, Float.class);
    }

    public void setForegroundAlpha(Float foregroundAlpha) {
        this.foregroundAlpha = foregroundAlpha;
    }

    public Chart getChart() {
        return (Chart) getParent();
    }

    public Object getColors() {
        return ValueBindings.get(this, "colors", colors, Object.class);
    }

    public void setColors(Object colors) {
        this.colors = colors;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ChartLabels getLabels() {
        return labels;
    }

    public void setLabels(ChartLabels labels) {
        this.labels = labels;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setTextStyle(String style) {
        this.style = style;
    }

    public void setStyle(String style) {
        setTextStyle(style);
    }

    public String getStyle() {
        return getTextStyle();
    }

    public ChartPopup getChartPopup() {
        return Components.findChildWithClass(this, ChartPopup.class, "<o:chartPopup>");
    }

    public Paint getBackgroundPaint() {
        return ValueBindings.get(this, "backgroundPaint", backgroundPaint, Paint.class);
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
    }

    public Paint getTitlePaint() {
        return ValueBindings.get(this, "titlePaint", titlePaint, Paint.class);
    }

    public void setTitlePaint(Paint titlePaint) {
        this.titlePaint = titlePaint;
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        return new StyledComponent[]{
                Chart.DEFAULT_CHART_STYLE,
                getChart(),
                this
        };
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                style,
                url,
                tooltip,
                saveAttachedState(context, colors),
                foregroundAlpha,
                onmouseout,
                onmouseover,
                onclick,
                enable3D,
                saveAttachedState(context, wallColor),
                saveAttachedState(context, backgroundPaint),
                saveAttachedState(context, titlePaint),
                saveAttachedState(context, defaultOutlineStyle),
                saveAttachedState(context, outlines)
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        url = (String) state[i++];
        tooltip = (String) state[i++];
        colors = (Object) restoreAttachedState(facesContext, state[i++]);
        foregroundAlpha = (Float) state[i++];
        onmouseout = (String) state[i++];
        onmouseover = (String) state[i++];
        onclick = (String) state[i++];
        enable3D = (Boolean) state[i++];
        wallColor = (Color) restoreAttachedState(facesContext, state[i++]);
        backgroundPaint = (Paint) restoreAttachedState(facesContext, state[i++]);
        titlePaint = (Paint) restoreAttachedState(facesContext, state[i++]);
        defaultOutlineStyle = (LineStyle) restoreAttachedState(facesContext, state[i++]);
        outlines = (Collection<LineStyle>) restoreAttachedState(facesContext, state[i++]);
    }

    public abstract ChartConfigurator getConfigurator();

    public abstract void decodeAction(String fieldValue);

    public LineStyle getDefaultOutlineStyle() {
        return ValueBindings.get(this, "defaultOutlineStyle", defaultOutlineStyle, LineStyle.class);
    }

    public void setDefaultOutlineStyle(LineStyle defaultOutlineStyle) {
        this.defaultOutlineStyle = defaultOutlineStyle;
    }

    public Collection<LineStyle> getOutlines() {
        return ValueBindings.get(this, "outlines", outlines, Collection.class);
    }

    public void setOutlines(Collection outlines) {
        this.outlines = outlines;
    }
}
