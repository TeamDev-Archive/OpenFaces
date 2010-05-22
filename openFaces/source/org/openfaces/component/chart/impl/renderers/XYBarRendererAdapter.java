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
package org.openfaces.component.chart.impl.renderers;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.openfaces.component.chart.BarChartView;

import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class XYBarRendererAdapter extends XYBarRenderer implements CustomizedRenderer  {
    private ItemsRenderer delegate;

    /**
     * The shadow paint.
     */
    private transient Paint shadowPaint;


    public XYBarRendererAdapter(BarChartView chartView) {
        delegate = new ItemsRenderer();
        ChartRendering.setupSeriesColors(chartView, this);
    }

    /**
     * Returns the shadow paint.
     *
     * @return The shadow paint.
     * @see #setShadowPaint(java.awt.Paint)
     * @since 1.0.11
     */
    public Paint getShadowPaint() {
        return this.shadowPaint;
    }

    /**
     * Sets the shadow paint and sends a {@link org.jfree.chart.event.RendererChangeEvent} to all
     * registered listeners.
     *
     * @param paint the paint (<code>null</code> not permitted).
     * @see #getShadowPaint()
     * @since 1.0.11
     */
    public void setShadowPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.shadowPaint = paint;
        fireChangeEvent();
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
}
