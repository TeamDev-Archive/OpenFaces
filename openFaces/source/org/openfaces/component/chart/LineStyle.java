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

package org.openfaces.component.chart;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Eugene Goncharov
 */
public class LineStyle implements Serializable {
    private Color color;
    private Stroke stroke;

    public LineStyle() {
    }

    public LineStyle(Color color, Stroke stroke) {
        this.color = color;
        this.stroke = stroke;
    }

    public LineStyle(Stroke stroke) {
        this.stroke = stroke;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineStyle)) return false;

        LineStyle lineStyle = (LineStyle) o;

        if (color != null ? !color.equals(lineStyle.color) : lineStyle.color != null) return false;
        if (stroke != null ? !stroke.equals(lineStyle.stroke) : lineStyle.stroke != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (stroke != null ? stroke.hashCode() : 0);
        return result;
    }
}
