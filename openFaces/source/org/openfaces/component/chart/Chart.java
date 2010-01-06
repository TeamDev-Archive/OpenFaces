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

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;
import org.openfaces.component.chart.impl.JfcRenderHints;
import org.openfaces.renderkit.chart.ChartDefaultStyle;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.taglib.internal.chart.ChartTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * The Chart component represents various datasets in a graphical form, for example as a pie,
 * line, or bar charts. The component is based on the JFreeChart engine and exposes a friendly
 * API with JSF-specific features. Styles can be customized for every chart element (image,
 * legend, title, etc.).
 * 
 * @author Ekaterina Shliakhovetskaya
 */
public class Chart extends OUIComponentBase implements StyledComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.Chart";
    public static final String COMPONENT_FAMILY = "org.openfaces.Chart";
    public static final StyledComponent DEFAULT_CHART_STYLE = new ChartDefaultStyle();

    private Integer width;
    private Integer height;

    private ChartViewType view;
    private ChartModel model;
    private ChartLegend legend;
    private boolean legendVisible = true;
    private String textStyle;
    private ChartTitle title; //todo(question): maybe better reduce API to just using <chartTitle> tag?
    private JfcRenderHints renderHints = new JfcRenderHints();

    private byte[] imageBytes;

    private ChartNoDataMessage noDataMessage;

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
        return legendVisible;
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
            children.add(view);
            return view;
        }

        if (!viewType.isViewOfThisType(view)) {
            children.remove(view);
            view = viewType.createChartView();
            children.add(view);
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
                saveAttachedState(facesContext, renderHints)
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
    }

}
