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

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.openfaces.component.OUIData;
import org.openfaces.component.OUIObjectIteratorBase;
import org.openfaces.component.chart.impl.JfcRenderHints;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.helpers.JFreeChartAdapter;
import org.openfaces.renderkit.chart.ChartDefaultStyle;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.taglib.internal.chart.ChartTag;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Chart component represents various datasets in a graphical form, for example as a pie,
 * line, or bar charts. The component is based on the JFreeChart engine and exposes a friendly
 * API with JSF-specific features. Styles can be customized for every chart element (image,
 * legend, title, etc.).
 *
 * @author Ekaterina Shliakhovetskaya
 */
public class Chart extends OUIObjectIteratorBase implements StyledComponent, NamingContainer {
    public static final String COMPONENT_TYPE = "org.openfaces.Chart";
    public static final String COMPONENT_FAMILY = "org.openfaces.Chart";
    public static final StyledComponent DEFAULT_CHART_STYLE = new ChartDefaultStyle();

    private Integer width;
    private Integer height;

    private ChartViewType view;
    private ChartModel model;
    private ChartLegend legend;
    private Boolean legendVisible;
    private String textStyle;
    private ChartTitle title; //todo(question): maybe better reduce API to just using <chartTitle> tag?
    private JfcRenderHints renderHints = new JfcRenderHints();

    private byte[] imageBytes;

    private ChartNoDataMessage noDataMessage;
    private TimePeriod timePeriodPrecision;

    private Object initialDescendantComponentState = null;
    private Map<String, Object> descendantComponentState = new HashMap<String, Object>();
    private int entityIndex = -1;
    private int selectedIndex = -1;

    public Chart() {
        setRendererType(ChartTag.RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public ChartViewType getView() {
        return ValueBindings.get(this, "view", view, ChartViewType.class);
    }

    public void setView(ChartViewType view) {
        this.view = view;
    }

    public JfcRenderHints getRenderHints() {
        return renderHints;
    }

    public void setRenderHints(JfcRenderHints renderHints) {
        this.renderHints = renderHints;
    }

    public ChartNoDataMessage getNoDataMessage() {
        return noDataMessage;
    }

    public void setNoDataMessage(ChartNoDataMessage noDataMessage) {
        this.noDataMessage = noDataMessage;
    }

    public int getHeight() {
        return ValueBindings.get(this, "height", height, 400);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return ValueBindings.get(this, "width", width, 400);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ChartLegend getLegend() {
        return legend;
    }

    public void setLegend(ChartLegend legend) {
        this.legend = legend;
    }

    public boolean isLegendVisible() {
        return ValueBindings.get(this, "legendVisible", legendVisible, true);
    }

    public void setLegendVisible(boolean legendVisible) {
        this.legendVisible = legendVisible;
    }

    public ChartTitle getTitle() {
        return title;
    }

    public void setTitle(ChartTitle title) {
        this.title = title;
    }

    public ChartView getChartView() {
        List<UIComponent> children = getChildren();
        ChartView view = null;
        for (UIComponent child : children) {
            if (child instanceof ChartView) {
                if (view != null)
                    throw new RuntimeException("There should be only one view child under this component: " + getId());
                view = (ChartView) child;
            }
        }
        ChartViewType viewType = getView();
        if (viewType == null)
            return view;

        if (view == null) {
            view = viewType.createChartView();
            Components.addChild(this, view);
            children.add(view);
            return view;
        }

        if (!viewType.isViewOfThisType(view)) {
            Components.removeChild(this, view);
            view = viewType.createChartView();
            Components.addChild(this, view);
        }
        return view;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "testStyle", textStyle);
    }

    public void setTextStyle(String style) {
        textStyle = style;
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        StyledComponent[] chain = new StyledComponent[2];
        chain[0] = DEFAULT_CHART_STYLE;
        chain[1] = this;
        return chain;
    }

    public String getHint() {
        return null;
    }

    public ChartModel getModel() {
        return ValueBindings.get(this, "model", model, ChartModel.class);
    }

    public void setModel(ChartModel model) {
        this.model = model;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public ChartMenu getChartMenu() {
        return Components.findChildWithClass(this, ChartMenu.class, "<o:chartMenu>");
    }

    public ChartSelection getChartSelection() {
        return Components.findChildWithClass(this, ChartSelection.class, "<o:chartSelection>");
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        setEntityIndex(-1);
        initialDescendantComponentState = null;
        descendantComponentState.clear();
        super.encodeBegin(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
        setEntityIndex(-1);
    }

    @Override
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);
        if (getEntityIndex() != -1) {
            setEntityIndex(-1);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        super.processValidators(context);
        if (getEntityIndex() != -1) {
            setEntityIndex(-1);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (getEntityIndex() != -1) {
            setEntityIndex(-1);
        }
    }

    public Integer getEntityIndex() {
        return entityIndex;
    }

    public void setEntityIndex(int index) {
        if (this.entityIndex == index) {
            return;
        }

        if (index < -1) {
            throw new IllegalArgumentException("index is less than -1");
        }

        FacesContext facesContext = getFacesContext();
        if (this.entityIndex == -1) {
            if (initialDescendantComponentState == null) {
                initialDescendantComponentState = OUIData.saveDescendantComponentStates(getChildren().iterator(), false);
            }
        } else {
            descendantComponentState.put(getClientId(facesContext), OUIData.saveDescendantComponentStates(getChildren().iterator(), false));
        }

        this.entityIndex = index;

        if (index == -1) {
            OUIData.restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
        } else {
            Object rowState = descendantComponentState.get(getClientId(facesContext));
            if (rowState == null) {
                OUIData.restoreDescendantComponentStates(getChildren().iterator(), initialDescendantComponentState, false);
            } else {
                OUIData.restoreDescendantComponentStates(getChildren().iterator(), rowState, false);
            }
        }
    }

    public void setObjectId(String objectId) {
        super.setObjectId(objectId);
        if (objectId != null) {
            setEntityIndex(Integer.valueOf(objectId));
            setId(getId());
        } else
            setEntityIndex(-1);
    }

    public String getObjectId() {
        if (getEntityIndex() != -1)
            return getEntityIndex().toString();
        else
            return null;
    }

    private JFreeChart getJFreeChart() {
        ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
        renderHints.setRenderingInfo(chartRenderingInfo);

        ChartModel model = getModel();
        ModelInfo info = new ModelInfo(model);
        renderHints.setModelInfo(info);

        Plot plot = PlotFactory.createPlot(this, info);

        return new JFreeChartAdapter(plot, this);
    }

    public BufferedImage make() {
        JFreeChart jFreeChart = getJFreeChart();
        ChartRenderingInfo chartRenderingInfo = renderHints.getRenderingInfo();

        return jFreeChart.createBufferedImage(getWidth(), getHeight(), chartRenderingInfo);
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);

        return new Object[]{
                superState,
                view,
                height,
                width,
                model,
                legendVisible,
                textStyle,
                imageBytes,
                saveAttachedState(facesContext, renderHints),
                saveAttachedState(facesContext, timePeriodPrecision),
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        view = (ChartViewType) state[i++];
        height = (Integer) state[i++];
        width = (Integer) state[i++];
        model = (ChartModel) state[i++];
        legendVisible = (Boolean) state[i++];
        textStyle = (String) state[i++];
        imageBytes = (byte[]) state[i++];
        renderHints = (JfcRenderHints) restoreAttachedState(facesContext, state[i++]);
        timePeriodPrecision = (TimePeriod) restoreAttachedState(facesContext, state[i++]);
    }

    public TimePeriod getTimePeriodPrecision() {
        return ValueBindings.get(this, "timePeriodPrecision", timePeriodPrecision, TimePeriod.DAY, TimePeriod.class);
    }

    public void setTimePeriodPrecision(TimePeriod timePeriodPrecision) {
        this.timePeriodPrecision = timePeriodPrecision;
    }
}
