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
package org.openfaces.component.timetable;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class RectangleAlignment implements Serializable {
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private double horizontalDistance;
    private double verticalDistance;
    private ElementRectangleKind borderRectangleKind;

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public double getHorizontalDistance() {
        return horizontalDistance;
    }

    public void setHorizontalDistance(double horizontalDistance) {
        this.horizontalDistance = horizontalDistance;
    }

    public double getVerticalDistance() {
        return verticalDistance;
    }

    public void setVerticalDistance(double verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public ElementRectangleKind getBorderRectangleKind() {
        return borderRectangleKind;
    }

    public void setBorderRectangleKind(ElementRectangleKind borderRectangleKind) {
        this.borderRectangleKind = borderRectangleKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangleAlignment that = (RectangleAlignment) o;

        if (Double.compare(that.horizontalDistance, horizontalDistance) != 0) return false;
        if (Double.compare(that.verticalDistance, verticalDistance) != 0) return false;
        if (borderRectangleKind != null ? !borderRectangleKind.equals(that.borderRectangleKind) : that.borderRectangleKind != null)
            return false;
        if (horizontalAlignment != null ? !horizontalAlignment.equals(that.horizontalAlignment) : that.horizontalAlignment != null)
            return false;
        if (verticalAlignment != null ? !verticalAlignment.equals(that.verticalAlignment) : that.verticalAlignment != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (horizontalAlignment != null ? horizontalAlignment.hashCode() : 0);
        result = 31 * result + (verticalAlignment != null ? verticalAlignment.hashCode() : 0);
        temp = horizontalDistance != +0.0d ? Double.doubleToLongBits(horizontalDistance) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = verticalDistance != +0.0d ? Double.doubleToLongBits(verticalDistance) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (borderRectangleKind != null ? borderRectangleKind.hashCode() : 0);
        return result;
    }
}
