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

import org.jfree.chart.renderer.xy.GradientXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * @author Eugene Goncharov
 */
public class GradientXYBarPainterAdapter extends GradientXYBarPainter {

    public GradientXYBarPainterAdapter() {
        super();
    }

    public GradientXYBarPainterAdapter(double g1, double g2, double g3) {
        super(g1, g2, g3);
    }

    @Override
    public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row, int column, RectangularShape bar, RectangleEdge base, boolean pegShadow) {
        // handle a special case - if the bar colour has alpha == 0, it is
        // invisible so we shouldn't draw any shadow
        Paint itemPaint = renderer.getItemPaint(row, column);
        if (itemPaint instanceof Color) {
            Color c = (Color) itemPaint;
            if (c.getAlpha() == 0) {
                return;
            }
        }

        RectangularShape shadow = createShadow(bar, renderer.getShadowXOffset(),
                renderer.getShadowYOffset(), base, pegShadow);
        if (renderer instanceof XYBarRendererAdapter) {
            g2.setPaint(((XYBarRendererAdapter) renderer).getShadowPaint());
        }
        g2.fill(shadow);
    }

    /**
     * Creates a shadow for the bar.
     *
     * @param bar       the bar shape.
     * @param xOffset   the x-offset for the shadow.
     * @param yOffset   the y-offset for the shadow.
     * @param base      the edge that is the base of the bar.
     * @param pegShadow peg the shadow to the base?
     * @return A rectangle for the shadow.
     */
    private Rectangle2D createShadow(RectangularShape bar, double xOffset,
                                     double yOffset, RectangleEdge base, boolean pegShadow) {
        double x0 = bar.getMinX();
        double x1 = bar.getMaxX();
        double y0 = bar.getMinY();
        double y1 = bar.getMaxY();
        if (base == RectangleEdge.TOP) {
            x0 += xOffset;
            x1 += xOffset;
            if (!pegShadow) {
                y0 += yOffset;
            }
            y1 += yOffset;
        } else if (base == RectangleEdge.BOTTOM) {
            x0 += xOffset;
            x1 += xOffset;
            y0 += yOffset;
            if (!pegShadow) {
                y1 += yOffset;
            }
        } else if (base == RectangleEdge.LEFT) {
            if (!pegShadow) {
                x0 += xOffset;
            }
            x1 += xOffset;
            y0 += yOffset;
            y1 += yOffset;
        } else if (base == RectangleEdge.RIGHT) {
            x0 += xOffset;
            if (!pegShadow) {
                x1 += xOffset;
            }
            y0 += yOffset;
            y1 += yOffset;
        }
        return new Rectangle2D.Double(x0, y0, (x1 - x0), (y1 - y0));
    }
}
