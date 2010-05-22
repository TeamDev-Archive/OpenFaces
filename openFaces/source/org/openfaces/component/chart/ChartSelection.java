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

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;

public class ChartSelection extends javax.faces.component.UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.ChartSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.ChartSelection";
    private static final LineStyle DEFAULT_SELECTION_LINE_STYLE = new LineStyle(new Color(0, 0, 255), new BasicStroke(5.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

    private LineStyle lineStyle;
    private Paint fillPaint;

    public ChartSelection() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, lineStyle),
                saveAttachedState(context, fillPaint)

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        lineStyle = (LineStyle) restoreAttachedState(context, state[i++]);
        fillPaint = (Paint) restoreAttachedState(context, state[i++]);
    }

    public LineStyle getLineStyle() {
        return ValueBindings.get(this, "lineStyle", lineStyle, DEFAULT_SELECTION_LINE_STYLE, LineStyle.class);
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Paint getFillPaint() {
        return ValueBindings.get(this, "fillPaint", fillPaint, Paint.class);
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }
}
