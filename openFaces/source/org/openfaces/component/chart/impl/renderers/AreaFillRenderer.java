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

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.openfaces.component.chart.LineAreaFill;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Eugene Goncharov
 */
public interface AreaFillRenderer {

    public Paint getBackgroundPaint();

    public void setBackgroundPaint(Paint paint);

    public LineAreaFill getLineAreaFill();

    public void setLineAreaFill(LineAreaFill areaFill);

    /**
     * Returns true if Area is being plotted by the renderer.
     *
     * @return <code>true</code> if Area is being plotted by the renderer.
     */
    public boolean isDrawFilledArea();

    public void setDrawFilledArea(boolean drawFilledArea);

    public void completePass(Graphics2D g2, CategoryItemRendererState state,
                             Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
                             ValueAxis rangeAxis, CategoryDataset dataSet, int row, int pass);
}
