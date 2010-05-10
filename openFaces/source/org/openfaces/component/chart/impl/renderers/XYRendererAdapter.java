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

import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public interface XYRendererAdapter extends XYItemRenderer {

    /**
     * Returns the flag used to control whether or not the lines for a series
     * are visible.
     *
     * @param series the series index (zero-based).
     * @return The flag (possibly <code>null</code>).
     * @see #setSeriesLinesVisible(int, Boolean)
     */
    public Boolean getSeriesLinesVisible(int series);

    /**
     * Sets the 'lines visible' flag for a series and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series the series index (zero-based).
     * @param flag   the flag (<code>null</code> permitted).
     * @see #getSeriesLinesVisible(int)
     */
    public void setSeriesLinesVisible(int series, Boolean flag);

    /**
     * Sets the 'lines visible' flag for a series and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible the flag.
     * @see #getSeriesLinesVisible(int)
     */
    public void setSeriesLinesVisible(int series, boolean visible);

    /**
     * Returns the flag used to control whether or not the shapes for a series
     * are visible.
     *
     * @param series the series index (zero-based).
     * @return A boolean.
     * @see #setSeriesShapesVisible(int, Boolean)
     */
    public Boolean getSeriesShapesVisible(int series);

    /**
     * Sets the 'shapes visible' flag for a series and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible the flag.
     * @see #getSeriesShapesVisible(int)
     */
    public void setSeriesShapesVisible(int series, boolean visible);

    /**
     * Sets the 'shapes visible' flag for a series and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series the series index (zero-based).
     * @param flag   the flag.
     * @see #getSeriesShapesVisible(int)
     */
    public void setSeriesShapesVisible(int series, Boolean flag);

    /**
     * Returns the base 'shape visible' attribute.
     *
     * @return The base flag.
     * @see #setBaseShapesVisible(boolean)
     */
    public boolean getBaseShapesVisible();

    /**
     * Sets the base 'shapes visible' flag and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param flag the flag.
     * @see #getBaseShapesVisible()
     */
    public void setBaseShapesVisible(boolean flag);

    /**
     * Returns the base 'shape filled' attribute.
     *
     * @return The base flag.
     * @see #setBaseShapesFilled(boolean)
     */
    public boolean getBaseShapesFilled();

    /**
     * Sets the base 'shapes filled' flag and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param flag the flag.
     * @see #getBaseShapesFilled()
     */
    public void setBaseShapesFilled(boolean flag);

    /**
     * Returns <code>true</code> if outlines should be drawn for shapes, and
     * <code>false</code> otherwise.
     *
     * @return A boolean.
     * @see #setDrawOutlines(boolean)
     */
    public boolean getDrawOutlines();

    /**
     * Sets the flag that controls whether outlines are drawn for
     * shapes, and sends a {@link org.jfree.chart.event.RendererChangeEvent} to all registered
     * listeners.
     * <p/>
     * In some cases, shapes look better if they do NOT have an outline, but
     * this flag allows you to set your own preference.
     *
     * @param flag the flag.
     * @see #getDrawOutlines()
     */
    public void setDrawOutlines(boolean flag);

    /**
     * Returns <code>true</code> if the renderer should use the fill paint
     * setting to fill shapes, and <code>false</code> if it should just
     * use the regular paint.
     * <p/>
     * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the
     * effect of this flag.
     *
     * @return A boolean.
     * @see #setUseFillPaint(boolean)
     * @see #getUseOutlinePaint()
     */
    public boolean getUseFillPaint();

    /**
     * Sets the flag that controls whether the fill paint is used to fill
     * shapes, and sends a {@link org.jfree.chart.event.RendererChangeEvent} to all
     * registered listeners.
     *
     * @param flag the flag.
     * @see #getUseFillPaint()
     */
    public void setUseFillPaint(boolean flag);

    /**
     * Returns <code>true</code> if the renderer should use the outline paint
     * setting to draw shape outlines, and <code>false</code> if it should just
     * use the regular paint.
     *
     * @return A boolean.
     * @see #setUseOutlinePaint(boolean)
     * @see #getUseFillPaint()
     */
    public boolean getUseOutlinePaint();

    /**
     * Sets the flag that controls whether the outline paint is used to draw
     * shape outlines, and sends a {@link org.jfree.chart.event.RendererChangeEvent} to all
     * registered listeners.
     * <p/>
     * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the
     * effect of this flag.
     *
     * @param flag the flag.
     * @see #getUseOutlinePaint()
     */
    public void setUseOutlinePaint(boolean flag);

    /**
     * Returns the paint used to fill data items as they are drawn.  The
     * default implementation passes control to the
     * {@link #lookupSeriesFillPaint(int)} method - you can override this
     * method if you require different behaviour.
     *
     * @param row    the row (or series) index (zero-based).
     * @param column the column (or category) index (zero-based).
     * @return The paint (never <code>null</code>).
     */
    public Paint getItemFillPaint(int row, int column);

    /**
     * Returns the paint used to fill an item drawn by the renderer.
     *
     * @param series the series (zero-based index).
     * @return The paint (never <code>null</code>).
     * @since 1.0.6
     */
    public Paint lookupSeriesFillPaint(int series);

    /**
     * Returns the paint used to fill an item drawn by the renderer.
     *
     * @param series the series (zero-based index).
     * @return The paint (never <code>null</code>).
     * @see #setSeriesFillPaint(int, Paint)
     */
    public Paint getSeriesFillPaint(int series);

    /**
     * Sets the paint used for a series fill and sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint (<code>null</code> permitted).
     * @see #getSeriesFillPaint(int)
     */
    public void setSeriesFillPaint(int series, Paint paint);

    /**
     * Sets the paint used to fill a series and, if requested,
     * sends a {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint (<code>null</code> permitted).
     * @param notify notify listeners?
     * @see #getSeriesFillPaint(int)
     */
    public void setSeriesFillPaint(int series, Paint paint, boolean notify);

    /**
     * Returns the base fill paint.
     *
     * @return The paint (never <code>null</code>).
     * @see #setBaseFillPaint(Paint)
     */
    public Paint getBaseFillPaint();

    /**
     * Sets the base fill paint and sends a {@link org.jfree.chart.event.RendererChangeEvent} to
     * all registered listeners.
     *
     * @param paint the paint (<code>null</code> not permitted).
     * @see #getBaseFillPaint()
     */
    public void setBaseFillPaint(Paint paint);

    /**
     * Sets the base fill paint and, if requested, sends a
     * {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     * @param notify notify listeners?
     * @see #getBaseFillPaint()
     */
    public void setBaseFillPaint(Paint paint, boolean notify);
}
