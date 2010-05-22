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

import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.AreaFillRenderer;
import org.openfaces.component.chart.impl.renderers.Chart3DRendererAdapter;
import org.openfaces.component.chart.impl.renderers.LineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.LineRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartView extends GridChartView {
    private boolean shapesVisible = true;
    private List<LineProperties> linePropertiesList = new ArrayList<LineProperties>();

    private Paint defaultFillColor;
    private LineStyle defaultLineStyle;
    private Collection lineStyles;
    private Collection fillPaints;

    public boolean isShapesVisible() {
        return shapesVisible;
    }

    public void setShapesVisible(boolean shapesVisible) {
        this.shapesVisible = shapesVisible;
    }

    public List<LineProperties> getLinePropertiesList() {
        return linePropertiesList;
    }

    public void addLinePropertiesList(LineProperties lineProperties) {
        if (lineProperties == null)
            return;

        if (linePropertiesList == null)
            linePropertiesList = new ArrayList<LineProperties>();

        linePropertiesList.add(lineProperties);
    }

    @Override
    public String getFamily() {
        return "org.openfaces.LineChartView";
    }

    public String getHint() {
        return null;
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);

            AbstractXYItemRenderer renderer = createRenderer(ds);

            final GridXYPlotAdapter xyPlot = new GridXYPlotAdapter(ds, renderer, chart, this);
            initMarkers(xyPlot);

            return xyPlot;
        }

        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(chart, info);

            AbstractXYItemRenderer renderer = createRenderer(ds);

            final GridDatePlotAdapter xyPlot = new GridDatePlotAdapter(ds, renderer, chart, this);
            initMarkers(xyPlot);

            return xyPlot;
        }

        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        LineAndShapeRenderer renderer;

        if (getLineAreaFill() != null) {
//            renderer = new LineAreaFillRenderer(this, ds);
            renderer = isEnable3D()
                    ? new LineRenderer3DAdapter(this, ds)
                    : new LineRendererAdapter(this, ds);
        } else {
            renderer = isEnable3D()
                    ? new LineRenderer3DAdapter(this, ds)
                    : new LineRendererAdapter(this, ds);
        }

        configureRenderer(renderer, ds.getRowCount());

        final GridCategoryPlotAdapter gridCategoryPlot = new GridCategoryPlotAdapter(ds, renderer, chart, this);
        initMarkers(gridCategoryPlot);

        return gridCategoryPlot;
    }

    private AbstractXYItemRenderer createRenderer(XYDataset ds) {
        AbstractXYItemRenderer renderer;

        if (getLineAreaFill() != null) {
//            renderer = new XYLineAreaFillRenderer(this, ds);
            renderer = isEnable3D()
                    ? new XYLineRenderer3DAdapter(this, ds)
                    : new XYLineRendererAdapter(this, ds);
        } else {
            renderer = isEnable3D()
                    ? new XYLineRenderer3DAdapter(this, ds)
                    : new XYLineRendererAdapter(this, ds);
        }

        configureRenderer((XYRendererAdapter) renderer, ds.getSeriesCount());

        return renderer;
    }

    private void configureRenderer(XYRendererAdapter renderer, int seriesCount) {
        if (isEnable3D() && renderer instanceof Chart3DRendererAdapter) {
            ((Chart3DRendererAdapter) renderer).setWallPaint(getWallColor());
        }

        if (renderer instanceof AreaFillRenderer) {
            ((AreaFillRenderer) renderer).setBackgroundPaint(getBackgroundPaint());
            ((AreaFillRenderer) renderer).setLineAreaFill(getLineAreaFill());
        }

        if (getChart().getChartSelection() != null && !this.shapesVisible) {
            throw new IllegalStateException("Chart selection is unsupported with disabled shapes.");
        }

        final boolean fillPaintsSpecified = getFillPaints() != null && !getFillPaints().isEmpty();
        final boolean strokesSpecified = getLineStyles() != null && !getLineStyles().isEmpty();
        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        renderer.setBaseShapesVisible(this.shapesVisible);

        if (getDefaultFillColor() != null || fillPaintsSpecified) {
            renderer.setUseFillPaint(true);
        }

        if (getDefaultFillColor() != null && !fillPaintsSpecified) {
            renderer.setBaseFillPaint(getDefaultFillColor());
        } else if (fillPaintsSpecified) {
            final Iterator fillPaintsIterator = getFillPaints().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (fillPaintsIterator.hasNext()) {
                    final Paint paint = (Paint) fillPaintsIterator.next();
                    renderer.setSeriesFillPaint(seriesIndex, paint);
                }
            }
        }

        if (getDefaultLineStyle() != null && !strokesSpecified) {
            renderer.setBaseStroke(getDefaultLineStyle().getStroke());
        } else if (strokesSpecified) {
            final Iterator strokesIterator = getLineStyles().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (strokesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) strokesIterator.next();
                    renderer.setSeriesStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawOutlines(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
            renderer.setUseOutlinePaint(true);
            final Iterator outlinesIterator = getOutlines().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (outlinesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) outlinesIterator.next();
                    renderer.setSeriesOutlinePaint(seriesIndex, lineStyle.getColor());
                    renderer.setSeriesOutlineStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }
    }

    private void configureRenderer(LineAndShapeRenderer renderer, int seriesCount) {
        if (isEnable3D() && renderer instanceof Chart3DRendererAdapter) {
            ((Chart3DRendererAdapter) renderer).setWallPaint(getWallColor());
        }

        if (renderer instanceof AreaFillRenderer) {
            ((AreaFillRenderer) renderer).setBackgroundPaint(getBackgroundPaint());
            ((AreaFillRenderer) renderer).setLineAreaFill(getLineAreaFill());
        }

        if (getChart().getChartSelection() != null && !this.shapesVisible) {
            throw new IllegalStateException("Chart selection is unsupported with disabled shapes.");
        }

        final boolean fillPaintsSpecified = getFillPaints() != null && !getFillPaints().isEmpty();
        final boolean lineStylesSpecified = getLineStyles() != null && !getLineStyles().isEmpty();
        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        renderer.setBaseShapesVisible(this.shapesVisible);

        if (getDefaultFillColor() != null || fillPaintsSpecified) {
            renderer.setUseFillPaint(true);
        }

        if (getDefaultFillColor() != null && !fillPaintsSpecified) {
            renderer.setBaseFillPaint(getDefaultFillColor());
        } else if (fillPaintsSpecified) {
            final Iterator fillPaintsIterator = getFillPaints().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (fillPaintsIterator.hasNext()) {
                    final Paint paint = (Paint) fillPaintsIterator.next();
                    renderer.setSeriesFillPaint(seriesIndex, paint);
                    renderer.setSeriesShapesFilled(seriesIndex, true);
                }
            }
        }

        if (getDefaultLineStyle() != null && !lineStylesSpecified) {
            renderer.setBaseStroke(getDefaultLineStyle().getStroke());
        } else if (lineStylesSpecified) {
            final Iterator strokesIterator = getLineStyles().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (strokesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) strokesIterator.next();
                    renderer.setSeriesStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawOutlines(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
            renderer.setUseOutlinePaint(true);
            final Iterator outlinesIterator = getOutlines().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (outlinesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) outlinesIterator.next();
                    renderer.setSeriesOutlinePaint(seriesIndex, lineStyle.getColor());
                    renderer.setSeriesOutlineStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }
    }

    public Paint getDefaultFillColor() {
        return ValueBindings.get(this, "defaultFillColor", defaultFillColor, Paint.class);
    }

    public void setDefaultFillColor(Paint defaultFillColor) {
        this.defaultFillColor = defaultFillColor;
    }

    public LineStyle getDefaultLineStyle() {
        return ValueBindings.get(this, "defaultLineStyle", defaultLineStyle, LineStyle.class);
    }

    public void setDefaultLineStyle(LineStyle defaultLineStyle) {
        this.defaultLineStyle = defaultLineStyle;
    }


    public Collection getLineStyles() {
        return ValueBindings.get(this, "lineStyles", lineStyles, Collection.class);
    }

    public void setLineStyles(Collection lineStyles) {
        this.lineStyles = lineStyles;
    }

    public Collection getFillPaints() {
        return ValueBindings.get(this, "fillPaints", fillPaints, Collection.class);
    }

    public void setFillPaints(Collection fillPaints) {
        this.fillPaints = fillPaints;
    }

    public LineAreaFill getLineAreaFill() {
        List<UIComponent> children = getChildren();
        LineAreaFill lineAreaFill = null;
        for (UIComponent child : children) {
            if (child instanceof LineAreaFill) {
                if (lineAreaFill != null)
                    throw new RuntimeException("There should be only one LineAreaFill child under this component: " + getId());
                lineAreaFill = (LineAreaFill) child;
            }
        }

        return lineAreaFill;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                shapesVisible,
                saveAttachedState(context, linePropertiesList),
                saveAttachedState(context, defaultFillColor),
                saveAttachedState(context, fillPaints),
                saveAttachedState(context, defaultLineStyle),
                saveAttachedState(context, lineStyles)
        };

    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        shapesVisible = (Boolean) state[i++];
        linePropertiesList = (List<LineProperties>) restoreAttachedState(facesContext, state[i++]);
        defaultFillColor = (Paint) restoreAttachedState(facesContext, state[i++]);
        fillPaints = (Collection<Paint>) restoreAttachedState(facesContext, state[i++]);
        defaultLineStyle = (LineStyle) restoreAttachedState(facesContext, state[i++]);
        lineStyles = (Collection<LineStyle>) restoreAttachedState(facesContext, state[i++]);
    }

}
