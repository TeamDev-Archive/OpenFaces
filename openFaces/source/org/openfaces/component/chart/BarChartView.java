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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.BarRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.BarRendererAdapter;
import org.openfaces.component.chart.impl.renderers.GradientXYBarPainterAdapter;
import org.openfaces.component.chart.impl.renderers.StandardXYBarPainterAdapter;
import org.openfaces.component.chart.impl.renderers.XYBarRendererAdapter;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.Iterator;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BarChartView extends GridChartView {
    private boolean showGradient;
    private Double g1WhitePosition = 0.1;
    private Double g2FullIntensityPosition = 0.2;
    private Double g3LightIntensityPosition = 0.8;

    private boolean showShadow;
    private Double shadowXOffset = 4.0;
    private Double shadowYOffset = 4.0;
    private Color shadowColor = Color.GRAY;

    @Override
    public String getFamily() {
        return "org.openfaces.BarChartView";
    }

    public String getHint() {
        return null;
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (isShowGradient()) {
            validateGradientParameters();
        }

        if (isShowShadow()) {
            validateShadowOffsetParameters();
        }

        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);
            XYBarRenderer renderer = new XYBarRendererAdapter(this);
            configureRendererPresentation((XYBarRendererAdapter) renderer, ds.getSeriesCount());

            return new GridXYPlotAdapter(ds, renderer, chart, this);
        }
        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(info);
            XYBarRenderer renderer = new XYBarRendererAdapter(this);
            configureRendererPresentation((XYBarRendererAdapter) renderer, ds.getSeriesCount());

            return new GridDatePlotAdapter(ds, renderer, chart, this);
        }

        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        BarRenderer renderer = isEnable3D()
                ? new BarRenderer3DAdapter(this)
                : new BarRendererAdapter(this);
        configureRendererPresentation(renderer, ds.getRowCount());
        return new GridCategoryPlotAdapter(ds, renderer, chart, this);
    }

    private void configureRendererPresentation(BarRenderer renderer, int seriesCount) {
        if (isShowGradient()) {
            renderer.setBarPainter(new GradientBarPainter(getG1WhitePosition(), getG2FullIntensityPosition(), getG3LightIntensityPosition()));
        } else {
            renderer.setBarPainter(new StandardBarPainter());
        }

        renderer.setShadowVisible(isShowShadow());
        renderer.setShadowXOffset(getShadowXOffset());
        renderer.setShadowYOffset(getShadowYOffset());

        renderer.setShadowPaint(getShadowColor());

        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawBarOutline(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
            final Iterator outlinesIterator = getOutlines().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (outlinesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) outlinesIterator.next();
                    renderer.setSeriesOutlinePaint(seriesIndex, lineStyle.getColor());
                    renderer.setSeriesOutlineStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }

        if (isEnable3D()) {
            ((BarRenderer3DAdapter) renderer).setWallPaint(getWallColor());
        }
    }

    private void configureRendererPresentation(XYBarRendererAdapter renderer, int seriesCount) {
        if (isShowGradient()) {
            renderer.setBarPainter(new GradientXYBarPainterAdapter(getG1WhitePosition(), getG2FullIntensityPosition(), getG3LightIntensityPosition()));
        } else {
            renderer.setBarPainter(new StandardXYBarPainterAdapter());
        }

        renderer.setShadowVisible(isShowShadow());
        renderer.setShadowXOffset(getShadowXOffset());
        renderer.setShadowYOffset(getShadowYOffset());

        renderer.setShadowPaint(getShadowColor());

        final boolean outlinesSpecified = getOutlines() != null && !getOutlines().isEmpty();

        if (getDefaultOutlineStyle() != null || outlinesSpecified) {
            renderer.setDrawBarOutline(true);
        }

        if (getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(getDefaultOutlineStyle().getStroke());
        } else if (outlinesSpecified) {
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

    private void validateGradientParameters() {
        g1WhitePosition = checkBoundaryValues(g1WhitePosition);
        g2FullIntensityPosition = checkBoundaryValues(g2FullIntensityPosition);
        g3LightIntensityPosition = checkBoundaryValues(g3LightIntensityPosition);

        if (g1WhitePosition > g2FullIntensityPosition
                || g1WhitePosition > g3LightIntensityPosition
                || g2FullIntensityPosition > g3LightIntensityPosition) {
            throw new IllegalArgumentException("Gradient parameters are incorrect.");
        }
    }

    private void validateShadowOffsetParameters() {
        if (shadowXOffset < 0) {
            shadowXOffset = 0.0;
        }

        if (shadowYOffset < 0) {
            shadowYOffset = 0.0;
        }
    }

    private double checkBoundaryValues(double position) {
        double correctPosition = position;

        if (position < 0) {
            correctPosition = 0;
        } else if (position > 1) {
            correctPosition = 1;
        }

        return correctPosition;
    }

    public Boolean isShowGradient() {
        return ValueBindings.get(this, "showGradient", showGradient, false);
    }

    public void setShowGradient(Boolean showGradient) {
        this.showGradient = showGradient;
    }

    public double getG1WhitePosition() {
        return ValueBindings.get(this, "g1WhitePosition", g1WhitePosition, 0.1);
    }

    public void setG1WhitePosition(double g1WhitePosition) {
        this.g1WhitePosition = g1WhitePosition;
    }

    public double getG2FullIntensityPosition() {
        return ValueBindings.get(this, "g2FullIntensityPosition", g2FullIntensityPosition, 0.2);
    }

    public void setG2FullIntensityPosition(double g2FullIntensityPosition) {
        this.g2FullIntensityPosition = g2FullIntensityPosition;
    }

    public double getG3LightIntensityPosition() {
        return ValueBindings.get(this, "g3LightIntensityPosition", g3LightIntensityPosition, 0.8);
    }

    public void setG3LightIntensityPosition(double g3LightIntensityPosition) {
        this.g3LightIntensityPosition = g3LightIntensityPosition;
    }

    public Boolean isShowShadow() {
        return ValueBindings.get(this, "showShadow", showShadow, false);
    }

    public void setShowShadow(Boolean showShadow) {
        this.showShadow = showShadow;
    }

    public double getShadowXOffset() {
        return ValueBindings.get(this, "shadowXOffset", shadowXOffset, 4.0);
    }

    public void setShadowXOffset(double shadowXOffset) {
        this.shadowXOffset = shadowXOffset;
    }

    public double getShadowYOffset() {
        return ValueBindings.get(this, "shadowYOffset", shadowYOffset, 4.0);
    }

    public void setShadowYOffset(double shadowYOffset) {
        this.shadowYOffset = shadowYOffset;
    }

    public Color getShadowColor() {
        return ValueBindings.get(this, "shadowColor", shadowColor, Color.class);
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);

        return new Object[]{superState,
                showGradient,
                g1WhitePosition,
                g2FullIntensityPosition,
                g3LightIntensityPosition,
                showShadow,
                shadowXOffset,
                shadowYOffset,
                saveAttachedState(context, shadowColor),
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        showGradient = (Boolean) state[i++];
        g1WhitePosition = (Double) state[i++];
        g2FullIntensityPosition = (Double) state[i++];
        g3LightIntensityPosition = (Double) state[i++];
        showShadow = (Boolean) state[i++];
        shadowXOffset = (Double) state[i++];
        shadowYOffset = (Double) state[i++];
        shadowColor = (Color) restoreAttachedState(facesContext, state[i++]);
    }


}