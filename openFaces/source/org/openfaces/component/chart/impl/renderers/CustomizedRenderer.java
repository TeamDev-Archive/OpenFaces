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

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public interface CustomizedRenderer {

    public Paint getItemOutlinePaint(int row, int column);

    public Stroke getItemOutlineStroke(int row, int column);

    public Paint getItemPaint(int row, int column);

    public void setItemOutlinePaint(int row, int column, Paint paint);

    public void setItemOutlineStroke(int row, int column, Stroke stroke);

    public void setItemPaint(int row, int column, Paint paint);
}
