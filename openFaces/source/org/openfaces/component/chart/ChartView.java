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

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
import org.jfree.chart.plot.Plot;
import org.openfaces.util.Rendering;
import org.openfaces.util.ValueBindings;
import org.openfaces.component.chart.impl.JfcRenderHints;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.helpers.JFreeChartAdapter;
import org.openfaces.component.chart.impl.helpers.MapRenderUtilities;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import java.awt.image.BufferedImage;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class ChartView extends UICommand implements StyledComponent, HasLabels {
    private String style;
    private String url;
    private String tooltip;
    private ChartLabels labels;
    private String colors;
    private Float foregroundAlpha;
    private String onmouseover;
    private String onmouseout;
    private String onclick;

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

    protected ChartView() {
        setRendererType(null);
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

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
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

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        StyledComponent[] chain = new StyledComponent[3];
        chain[0] = Chart.DEFAULT_CHART_STYLE;
        chain[1] = getChart();
        chain[2] = this;
        return chain;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style,
                url,
                tooltip,
                colors,
                foregroundAlpha,
                onmouseout,
                onmouseover,
                onclick
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
        colors = (String) state[i++];
        foregroundAlpha = (Float) state[i++];
        onmouseout = (String) state[i++];
        onmouseover = (String) state[i++];
        onclick = (String) state[i++];
    }

    public byte[] renderAsImageFile() {
        Chart chart = getChart();
        JfcRenderHints renderHints = chart.getRenderHints();
        ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
        renderHints.setRenderingInfo(chartRenderingInfo);

        ChartModel model = chart.getModel();
        ModelInfo info = new ModelInfo(model);
        renderHints.setModelInfo(info);

        Plot plot = createPlot(chart, model, info);
        JFreeChart jFreeChart = new JFreeChartAdapter(plot, chart);
        int width = chart.getWidth();
        int height = chart.getHeight();
        BufferedImage image = jFreeChart.createBufferedImage(width, height, chartRenderingInfo);
        byte[] imageAsByteArray = Rendering.encodeAsPNG(image);

        String mapId = renderHints.getMapId(chart);
        if (mapId != null) {
            String map = MapRenderUtilities.getImageMapExt(chart, mapId, chartRenderingInfo,
                    new StandardToolTipTagFragmentGenerator(), new StandardURLTagFragmentGenerator());
            renderHints.setMap(map);
        }

        return imageAsByteArray;
    }

    protected abstract Plot createPlot(Chart chart, ChartModel model, ModelInfo info);

    public abstract void decodeAction(String fieldValue);

}
