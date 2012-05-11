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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class ItemsRenderer implements Serializable {
    private Map<Integer, Map<Integer, Paint>> outlinePaints = new HashMap<Integer, Map<Integer, Paint>>();
    private Map<Integer, Map<Integer, Stroke>> outlineStrokes = new HashMap<Integer, Map<Integer, Stroke>>();
    private Map<Integer, Map<Integer, Paint>> itemPaints = new HashMap<Integer, Map<Integer, Paint>>();

    public ItemsRenderer() {

    }

    public Paint getItemOutlinePaint(int row, int column) {
        if (outlinePaints.containsKey(row)) {
            final Map<Integer, Paint> rowOutlinePaints = outlinePaints.get(row);

            return rowOutlinePaints.get(column);
        }

        return null;
    }

    public Stroke getItemOutlineStroke(int row, int column) {
        if (outlineStrokes.containsKey(row)) {
            final Map<Integer, Stroke> rowOutlineStroke = outlineStrokes.get(row);
            return rowOutlineStroke.get(column);
        }

        return null;
    }

    public Paint getItemPaint(int row, int column) {
        if (itemPaints.containsKey(row)) {
            final Map<Integer, Paint> rowItemPaints = itemPaints.get(row);

            return rowItemPaints.get(column);
        }

        return null;
    }


    public void setItemOutlinePaint(int row, int column, Paint paint) {
        if (!outlinePaints.containsKey(row)) {
            outlinePaints.put(row, new HashMap<Integer, Paint>());
        }

        final Map<Integer, Paint> rowOutlinePaints = outlinePaints.get(row);
        rowOutlinePaints.put(column, paint);
    }

    public void setItemOutlineStroke(int row, int column, Stroke stroke) {
        if (!outlineStrokes.containsKey(row)) {
            outlineStrokes.put(row, new HashMap<Integer, Stroke>());
        }

        final Map<Integer, Stroke> rowOutlineStroke = outlineStrokes.get(row);
        rowOutlineStroke.put(column, stroke);
    }

    public void setItemPaint(int row, int column, Paint paint) {
        if (!itemPaints.containsKey(row)) {
            itemPaints.put(row, new HashMap<Integer, Paint>());
        }

        final Map<Integer, Paint> rowPaints = itemPaints.get(row);
        rowPaints.put(column, paint);
    }
}
