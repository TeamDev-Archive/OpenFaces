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
package org.openfaces.component.chart;

import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;

public class Marker extends javax.faces.component.UIComponentBase implements StyledComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.Marker";
    public static final String COMPONENT_FAMILY = "org.openfaces.Marker";
    private static final LineStyle DEFAULT_LINE_STYLE = new LineStyle(new Color(212, 212, 212), new BasicStroke(1.5f));
    private static final String DEFAULT_TEXT_STYLE = "color: black;";

    private Double startValue;
    private Double endValue;
    private Comparable value;
    private Boolean drawAsLine;

    private String textStyle;
    private Float alpha;
    private String label;
    private LineStyle lineStyle;
    private LineStyle outlineStyle;
    private MarkerLayer layer;

    private MarkerLabelAnchor labelAnchor;
    private MarkerLabelTextAnchor labelTextAnchor;
    private MarkerLabelOffsetType labelOffsetType;
    private String labelOffset;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    public Double getStartValue() {
        return ValueBindings.get(this, "startValue", startValue, Double.class);
    }

    public void setStartValue(Double startValue) {
        this.startValue = startValue;
    }

    public Double getEndValue() {
        return ValueBindings.get(this, "endValue", endValue, Double.class);
    }

    public void setEndValue(Double endValue) {
        this.endValue = endValue;
    }

    public Comparable getValue() {
        return ValueBindings.get(this, "value", value, Comparable.class);
    }

    public void setValue(Comparable value) {
        this.value = value;
    }

    public boolean getDrawAsLine() {
        return ValueBindings.get(this, "drawAsLine", drawAsLine, false);
    }

    public void setDrawAsLine(boolean drawAsLine) {
        this.drawAsLine = drawAsLine;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "testStyle", textStyle, DEFAULT_TEXT_STYLE, String.class);
    }

    public void setTextStyle(String style) {
        textStyle = style;
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        return new StyledComponent[]{
                Chart.DEFAULT_CHART_STYLE,
                this
        };
    }

    public String getHint() {
        return null;
    }

    public LineStyle getLineStyle() {
        return ValueBindings.get(this, "lineStyle", lineStyle, DEFAULT_LINE_STYLE, LineStyle.class);
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public LineStyle getOutlineStyle() {
        return ValueBindings.get(this, "outlineStyle", outlineStyle, LineStyle.class);
    }

    public void setOutlineStyle(LineStyle outlineStyle) {
        this.outlineStyle = outlineStyle;
    }

    public float getAlpha() {
        return ValueBindings.get(this, "alpha", alpha, 0.6F, Float.class);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getLabel() {
        return ValueBindings.get(this, "label", label, String.class);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isValueMarker() {
        final Comparable value = this.getValue();

        return isValidDouble(value);
    }

    public boolean isIntervalMarker() {
        return this.getStartValue() != null && this.getEndValue() != null;
    }

    private boolean isValidDouble(Comparable value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Double) {
            return true;
        }

        if (!(value instanceof String)) {
            return false;
        }

        boolean valueIsValidDouble = true;
        try {
            Double.parseDouble((String) value);
        } catch (NumberFormatException e) {
            valueIsValidDouble = false;
        }

        return valueIsValidDouble;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                startValue,
                endValue,
                saveAttachedState(context, value),
                drawAsLine,
                textStyle,
                alpha,
                label,
                saveAttachedState(context, lineStyle),
                saveAttachedState(context, outlineStyle),
                saveAttachedState(context, layer),
                saveAttachedState(context, labelAnchor),
                saveAttachedState(context, labelTextAnchor),
                saveAttachedState(context, labelOffsetType),
                labelOffset
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

        startValue = (Double) state[i++];
        endValue = (Double) state[i++];
        value = (Comparable) restoreAttachedState(context, state[i++]);
        drawAsLine = (Boolean) state[i++];
        textStyle = (String) state[i++];
        alpha = (Float) state[i++];
        label = (String) state[i++];
        lineStyle = (LineStyle) restoreAttachedState(context, state[i++]);
        outlineStyle = (LineStyle) restoreAttachedState(context, state[i++]);
        layer = (MarkerLayer) restoreAttachedState(context, state[i++]);
        labelAnchor = (MarkerLabelAnchor) restoreAttachedState(context, state[i++]);
        labelTextAnchor = (MarkerLabelTextAnchor) restoreAttachedState(context, state[i++]);
        labelOffsetType = (MarkerLabelOffsetType) restoreAttachedState(context, state[i++]);
        labelOffset = (String) state[i++];
    }

    public MarkerLayer getLayer() {
        return ValueBindings.get(this, "layer", layer, null);
    }

    public void setLayer(MarkerLayer layer) {
        this.layer = layer;
    }

    public MarkerLabelAnchor getLabelAnchor() {
        return ValueBindings.get(this, "labelAnchor", labelAnchor, MarkerLabelAnchor.TOP_LEFT, MarkerLabelAnchor.class);
    }

    public void setLabelAnchor(MarkerLabelAnchor labelAnchor) {
        this.labelAnchor = labelAnchor;
    }

    public MarkerLabelTextAnchor getLabelTextAnchor() {
        return ValueBindings.get(this, "labelTextAnchor", labelTextAnchor, MarkerLabelTextAnchor.TOP_LEFT, MarkerLabelTextAnchor.class);
    }

    public void setLabelTextAnchor(MarkerLabelTextAnchor labelTextAnchor) {
        this.labelTextAnchor = labelTextAnchor;
    }

    public MarkerLabelOffsetType getLabelOffsetType() {
        return ValueBindings.get(this, "labelOffsetType", labelOffsetType, MarkerLabelOffsetType.CONTRACT, MarkerLabelOffsetType.class);
    }

    public void setLabelOffsetType(MarkerLabelOffsetType labelOffsetType) {
        this.labelOffsetType = labelOffsetType;
    }

    public String getLabelOffset() {
        return ValueBindings.get(this, "labelOffset", labelOffset, String.class);
    }

    public void setLabelOffset(String labelOffset) {
        this.labelOffset = labelOffset;
    }
}