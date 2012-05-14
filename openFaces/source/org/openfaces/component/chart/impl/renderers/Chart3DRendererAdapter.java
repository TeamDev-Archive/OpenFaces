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

import org.jfree.chart.Effect3D;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public interface Chart3DRendererAdapter extends Effect3D {

    /**
     * Returns the paint used to highlight the left and bottom wall in the plot
     * background.
     *
     * @return The paint.
     */
    public Paint getWallPaint();

    /**
     * Sets the paint used to hightlight the left and bottom walls in the plot
     * background and sends a {@link org.jfree.chart.event.RendererChangeEvent} to all registered
     * listeners.
     *
     * @param paint the paint.
     */
    public void setWallPaint(Paint paint);
}
