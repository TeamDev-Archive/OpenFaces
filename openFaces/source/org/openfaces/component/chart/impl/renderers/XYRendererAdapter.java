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

import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public interface XYRendererAdapter extends XYItemRenderer {

    public Boolean getSeriesLinesVisible(int series);

    public void setSeriesLinesVisible(int series, Boolean flag);

    public void setSeriesLinesVisible(int series, boolean visible);

    public Boolean getSeriesShapesVisible(int series);

    public void setSeriesShapesVisible(int series, boolean visible);

    public void setSeriesShapesVisible(int series, Boolean flag);

    public boolean getBaseShapesVisible();

    public void setBaseShapesVisible(boolean flag);

    public boolean getBaseShapesFilled();

    public void setBaseShapesFilled(boolean flag);

    public boolean getDrawOutlines();

    public void setDrawOutlines(boolean flag);

    public boolean getUseFillPaint();

    public void setUseFillPaint(boolean flag);

    public boolean getUseOutlinePaint();

    public void setUseOutlinePaint(boolean flag);

    public Paint getItemFillPaint(int row, int column);

    public Paint lookupSeriesFillPaint(int series);

    public Paint getSeriesFillPaint(int series);

    public void setSeriesFillPaint(int series, Paint paint);

    public void setSeriesFillPaint(int series, Paint paint, boolean notify);

    public Paint getBaseFillPaint();

    public void setBaseFillPaint(Paint paint);

    public void setBaseFillPaint(Paint paint, boolean notify);

    Boolean getSeriesShapesFilled(int series);

    void setSeriesShapesFilled(int series, boolean filled);

    void setSeriesShapesFilled(int series, Boolean filled);
}
