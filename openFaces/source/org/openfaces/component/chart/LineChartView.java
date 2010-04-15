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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.LineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.LineRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRendererAdapter;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartView extends GridChartView {
    private boolean shapesVisible = true;
    private List<LineProperties> linePropertiesList = new ArrayList<LineProperties>();

    private Paint defaultFillColor;
    private LineStyle defaultLineStyle;
    private Collection strokes;
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

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                shapesVisible,
                saveAttachedState(context, linePropertiesList)
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
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);
            XYLineAndShapeRenderer renderer = isEnable3D()
                    ? new XYLineRenderer3DAdapter(this, ds)
                    : new XYLineRendererAdapter(this, ds);
            configureRenderer(renderer);
            return new GridXYPlotAdapter(ds, renderer, chart, this);
        }
        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(info);
            XYLineAndShapeRenderer renderer = isEnable3D()
                    ? new XYLineRenderer3DAdapter(this, ds)
                    : new XYLineRendererAdapter(this, ds);
            configureRenderer(renderer);
            return new GridDatePlotAdapter(ds, renderer, chart, this);
        }
        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        LineAndShapeRenderer renderer = isEnable3D()
                ? new LineRenderer3DAdapter(this, ds)
                : new LineRendererAdapter(this, ds);
        configureRenderer(renderer);
        return new GridCategoryPlotAdapter(ds, renderer, chart, this);
    }

    private void configureRenderer(XYLineAndShapeRenderer renderer) {
        if (isEnable3D()) {
            ((XYLineRenderer3DAdapter) renderer).setWallPaint(getWallColor());
        }
        
        final boolean fillPaintsSpecified = getFillPaints() != null && !getFillPaints().isEmpty();
        final boolean strokesSpecified = getStrokes() != null && !getStrokes().isEmpty();
        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        renderer.setBaseShapesVisible(true);

        if (getDefaultFillColor() != null || fillPaintsSpecified) {
            renderer.setUseFillPaint(true);
        }

        if (getDefaultFillColor() != null && !fillPaintsSpecified) {
            renderer.setBaseFillPaint(getDefaultFillColor());
        } else if (fillPaintsSpecified) {
            renderer.setSeriesFillPaint(0, Color.RED);
            renderer.setSeriesFillPaint(1, Color.GREEN);
            renderer.setSeriesFillPaint(2, Color.BLUE);
        }

        if (getDefaultLineStyle() != null && !strokesSpecified) {
            renderer.setBaseStroke(getDefaultLineStyle().getStroke());
        } else if (strokesSpecified) {
            renderer.setSeriesStroke(0, new BasicStroke(3F));
            renderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{
                    6F, 6F
            }, 0.0F));
        }

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawOutlines(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
            renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
            renderer.setSeriesOutlineStroke(1, new BasicStroke(3.0F));
            renderer.setSeriesOutlineStroke(2, new BasicStroke(6.0F));
        }
    }

    private void configureRenderer(LineAndShapeRenderer renderer) {
        if (isEnable3D()) {
            ((LineRenderer3DAdapter) renderer).setWallPaint(getWallColor());
        }

        final boolean fillPaintsSpecified = getFillPaints() != null && !getFillPaints().isEmpty();
        final boolean strokesSpecified = getStrokes() != null && !getStrokes().isEmpty();
        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        renderer.setBaseShapesVisible(true);

        if (getDefaultFillColor() != null || fillPaintsSpecified) {
            renderer.setUseFillPaint(true);
        }

        if (getDefaultFillColor() != null && !fillPaintsSpecified) {
            renderer.setBaseFillPaint(getDefaultFillColor());
        } else if (fillPaintsSpecified) {
            renderer.setSeriesFillPaint(0, Color.RED);
            renderer.setSeriesFillPaint(1, Color.GREEN);
            renderer.setSeriesFillPaint(2, Color.BLUE);
        }

        if (getDefaultLineStyle() != null && !strokesSpecified) {
            renderer.setBaseStroke(getDefaultLineStyle().getStroke());
        } else if (strokesSpecified) {
            renderer.setSeriesStroke(0, new BasicStroke(3F));
            renderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 1, 1.0F, new float[]{
                    6F, 6F
            }, 0.0F));
        }

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawOutlines(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
            renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
            renderer.setSeriesOutlineStroke(1, new BasicStroke(3.0F));
            renderer.setSeriesOutlineStroke(2, new BasicStroke(6.0F));
        }
    }


    public Paint getDefaultFillColor() {
        return ValueBindings.get(this, "defaultFillColor", defaultFillColor, null);
    }

    public void setDefaultFillColor(Paint defaultFillColor) {
        this.defaultFillColor = defaultFillColor;
    }

    public LineStyle getDefaultLineStyle() {
        return ValueBindings.get(this, "defaultLineStyle", defaultLineStyle, null);
    }

    public void setDefaultLineStyle(LineStyle defaultLineStyle) {
        this.defaultLineStyle = defaultLineStyle;
    }


    public Collection getStrokes() {
        return ValueBindings.get(this, "strokes", strokes, null);
    }

    public void setStrokes(Collection strokes) {
        this.strokes = strokes;
    }

    public Collection getFillPaints() {
        return ValueBindings.get(this, "fillPaints", fillPaints, null);
    }

    public void setFillPaints(Collection fillPaints) {
        this.fillPaints = fillPaints;
    }
}
