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

package org.openfaces.component.chart.impl.renderers.states;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRendererState;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eugene Goncharov
 */
public class LineFillItemRendererState extends CategoryItemRendererState {
    private Polygon areaPolygon;
    private Collection<Line2D> lines;

    public LineFillItemRendererState(PlotRenderingInfo info) {
        super(info);

        areaPolygon = new Polygon();
        lines = new ArrayList<Line2D>();
    }

    public Polygon getAreaPolygon() {
        return areaPolygon;
    }

    public void setAreaPolygon(Polygon areaPolygon) {
        this.areaPolygon = areaPolygon;
    }

    public Collection<Line2D> getLines() {
        return lines;
    }

    public void setLines(Collection<Line2D> lines) {
        this.lines = lines;
    }
}
