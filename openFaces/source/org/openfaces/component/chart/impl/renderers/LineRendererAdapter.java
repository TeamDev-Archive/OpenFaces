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
package org.openfaces.component.chart.impl.renderers;

import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.configuration.ConfigurableRenderer;
import org.openfaces.component.chart.impl.configuration.RendererConfigurator;

import java.awt.*;
import java.util.Collection;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineRendererAdapter extends LineAndShapeRenderer implements CustomizedRenderer, ConfigurableRenderer {
    private ItemsRenderer delegate;
    private ConfigurableRendererBase configurationDelegate;

    public LineRendererAdapter() {
        delegate = new ItemsRenderer();
        configurationDelegate = new ConfigurableRendererBase();
    }


    @Override
    public Paint getItemOutlinePaint(int row, int column) {
        final Paint itemOutlinePaint = delegate.getItemOutlinePaint(row, column);
        if (itemOutlinePaint != null) {
            return itemOutlinePaint;
        }

        return super.getItemOutlinePaint(row, column);
    }

    @Override
    public Stroke getItemOutlineStroke(int row, int column) {
        final Stroke outlineStroke = delegate.getItemOutlineStroke(row, column);
        if (outlineStroke != null) {
            return outlineStroke;
        }

        return super.getItemOutlineStroke(row, column);
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        final Paint itemPaint = delegate.getItemPaint(row, column);
        if (itemPaint != null) {
            return itemPaint;
        }

        return super.getItemPaint(row, column);
    }

    public void setItemOutlinePaint(int row, int column, Paint paint) {
        delegate.setItemOutlinePaint(row, column, paint);
    }

    public void setItemOutlineStroke(int row, int column, Stroke stroke) {
        delegate.setItemOutlineStroke(row, column, stroke);
    }

    public void setItemPaint(int row, int column, Paint paint) {
        delegate.setItemPaint(row, column, paint);
    }

    public void addConfigurator(RendererConfigurator configurator) {
        configurationDelegate.addConfigurator(configurator);
    }

    public Collection<RendererConfigurator> getConfigurators() {
        return configurationDelegate.getConfigurators();
    }

    public void configure(ChartView chartView) {
        configurationDelegate.configure(chartView, this);
    }
}
