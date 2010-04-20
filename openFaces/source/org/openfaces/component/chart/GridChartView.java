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

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleInsets;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class GridChartView extends ChartView {
    private Orientation orientation = Orientation.VERTICAL;
    private GridPointInfo point;
    private ChartDomain showAxes;

    private List<ChartGridLines> gridLines = new ArrayList<ChartGridLines>();

    private LineStyle defaultOutlineStyle;
    private Collection outlines;

    private BarChartLabelPosition defaultLabelsPosition;
    private BarChartLabelPosition positiveLabelsPosition;
    private BarChartLabelPosition negativeLabelsPosition;

    private String keyAxisLabel;
    private String valueAxisLabel;
    private List<ChartAxis> axes = new ArrayList<ChartAxis>();

    private boolean labelsVisible = false;
    private Double labelsOffset;

    public GridPointInfo getPoint() {
        return point;
    }

    public void setPoint(GridPointInfo point) {
        this.point = point;
    }

    public Orientation getOrientation() {
        return ValueBindings.get(this, "orientation", orientation, Orientation.class);
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public ChartAxis getBaseAxis() {
        if (axes == null || axes.size() == 0)
            return null;

        ChartAxis baseAxis = null;

        for (ChartAxis axis : axes) {
            ChartDomain axisDomain = axis.getDomain();
            if (axisDomain == null || axisDomain.equals(ChartDomain.BOTH)) {
                baseAxis = axis;
                break;
            }
        }

        return baseAxis;
    }

    public ChartGridLines getBaseGrid() {
        if (gridLines == null || gridLines.size() == 0)
            return null;

        ChartGridLines baseGridLines = null;

        for (ChartGridLines lines : gridLines) {
            if (lines.getDomain().equals(ChartDomain.BOTH)) {
                baseGridLines = lines;
                break;
            }
        }

        return baseGridLines;
    }

    public void setBaseAxis(ChartAxis baseAxis) {
        if (baseAxis == null)
            throw new IllegalArgumentException("'null' as not allowed for base axis");

        baseAxis.setDomain(ChartDomain.BOTH);

        if (axes == null) axes = new ArrayList<ChartAxis>();

        axes.add(baseAxis);
    }

    public ChartAxis getKeyAxis() {
        if (axes == null || axes.size() == 0)
            return null;

        ChartAxis keyAxis = null;

        for (ChartAxis axis : axes) {
            ChartDomain axisDomain = axis.getDomain();
            if (axisDomain != null && axisDomain.equals(ChartDomain.KEY)) {
                keyAxis = axis;
                break;
            }
        }

        return keyAxis;
    }

    public ChartGridLines getKeyGrid() {
        if (gridLines == null || gridLines.size() == 0)
            return null;

        ChartGridLines keyGridLines = null;

        for (ChartGridLines lines : gridLines) {
            if (lines.getDomain().equals(ChartDomain.KEY)) {
                keyGridLines = lines;
                break;
            }
        }

        return keyGridLines;
    }

    public ChartGridLines getValueGrid() {
        if (gridLines == null || gridLines.size() == 0)
            return null;

        ChartGridLines valueGridLines = null;

        for (ChartGridLines lines : gridLines) {
            if (lines.getDomain().equals(ChartDomain.VALUE)) {
                valueGridLines = lines;
                break;
            }
        }

        return valueGridLines;
    }

    public ChartAxis getValueAxis() {
        if (axes == null || axes.size() == 0)
            return null;

        ChartAxis valueAxis = null;

        for (ChartAxis axis : axes) {
            ChartDomain axisDomain = axis.getDomain();
            if (axisDomain != null && axisDomain.equals(ChartDomain.VALUE)) {
                valueAxis = axis;
                break;
            }
        }

        return valueAxis;
    }

    public List<ChartAxis> getAxes() {
        return axes;
    }

    public boolean isLabelsVisible() {
        return labelsVisible;
    }

    public void setLabelsVisible(boolean labelsVisible) {
        this.labelsVisible = labelsVisible;
    }

    public List<ChartGridLines> getGridLines() {
        return gridLines;
    }

    public LineStyle getDefaultOutlineStyle() {
        return ValueBindings.get(this, "defaultOutlineStyle", defaultOutlineStyle, LineStyle.class);
    }

    public void setDefaultOutlineStyle(LineStyle defaultOutlineStyle) {
        this.defaultOutlineStyle = defaultOutlineStyle;
    }

    public Collection getOutlines() {
        return ValueBindings.get(this, "outlines", outlines, Collection.class);
    }

    public void setOutlines(Collection outlines) {
        this.outlines = outlines;
    }

    public ChartDomain getShowAxes() {
        return ValueBindings.get(this, "showAxes", showAxes, ChartDomain.class);
    }

    public void setShowAxes(ChartDomain showAxes) {
        this.showAxes = showAxes;
    }

    @Override
    public String getFamily() {
        return "org.openfaces.GridChartView";
    }

    public String getKeyAxisLabel() {
        return keyAxisLabel;
    }

    public void setKeyAxisLabel(String keyAxisLabel) {
        this.keyAxisLabel = keyAxisLabel;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }

    public BarChartLabelPosition getDefaultLabelsPosition() {
        return ValueBindings.get(this, "defaultLabelsPosition", defaultLabelsPosition, BarChartLabelPosition.class);
    }

    public void setDefaultLabelsPosition(BarChartLabelPosition defaultLabelsPosition) {
        this.defaultLabelsPosition = defaultLabelsPosition;
    }

    public BarChartLabelPosition getPositiveLabelsPosition() {
        return ValueBindings.get(this, "positiveLabelsPosition", positiveLabelsPosition, BarChartLabelPosition.class);
    }

    public void setPositiveLabelsPosition(BarChartLabelPosition positiveLabelsPosition) {
        this.positiveLabelsPosition = positiveLabelsPosition;
    }

    public BarChartLabelPosition getNegativeLabelsPosition() {
        return ValueBindings.get(this, "negativeLabelsPosition", negativeLabelsPosition, BarChartLabelPosition.class);
    }

    public void setNegativeLabelsPosition(BarChartLabelPosition negativeLabelsPosition) {
        this.negativeLabelsPosition = negativeLabelsPosition;
    }

    public double getLabelsOffset() {
        return ValueBindings.get(this, "labelsOffset", labelsOffset, 10.0);
    }

    public void setLabelsOffset(double labelsOffset) {
        this.labelsOffset = labelsOffset;
    }

    protected void initMarkers(CategoryPlot categoryPlot) {
        final List<Marker> markers = collectMarkers(this);

        for (Marker marker : markers) {
            processMarker(categoryPlot, marker);
        }
    }

    protected void initMarkers(XYPlot xyPlot) {
        final List<Marker> markers = collectMarkers(this);

        for (Marker marker : markers) {
            processMarker(xyPlot, marker);
        }
    }

    private void processMarker(CategoryPlot categoryPlot, Marker marker) {
        org.jfree.chart.plot.Marker domainMarker = new CategoryMarker(marker.getValue());
        initializeDomainMarker(marker, domainMarker);

        ((CategoryMarker) domainMarker).setDrawAsLine(marker.getDrawAsLine());

        categoryPlot.addDomainMarker((CategoryMarker) domainMarker, getMarkerLayer(marker));

    }

    private void processMarker(XYPlot xyPlot, Marker marker) {
        if (marker.isIntervalMarker()) {
            org.jfree.chart.plot.Marker domainMarker = new IntervalMarker(marker.getStartValue(), marker.getEndValue());
            initializeDomainMarker(marker, domainMarker);

            xyPlot.addDomainMarker(domainMarker, getMarkerLayer(marker));
        } else if (marker.isValueMarker()) {
            org.jfree.chart.plot.Marker domainMarker = new ValueMarker((Double) marker.getValue());
            initializeDomainMarker(marker, domainMarker);

            xyPlot.addDomainMarker(domainMarker, getMarkerLayer(marker));
        }
    }

    private void initializeDomainMarker(Marker marker, org.jfree.chart.plot.Marker domainMarker) {
        domainMarker.setLabel(marker.getLabel());

        if (marker.getLineStyle() != null) {
            domainMarker.setPaint(marker.getLineStyle().getColor());
            domainMarker.setStroke(marker.getLineStyle().getStroke());
        }

        domainMarker.setAlpha(marker.getAlpha());

        if (marker.getOutlineStyle() != null) {
            domainMarker.setOutlinePaint(marker.getOutlineStyle().getColor());
            domainMarker.setOutlineStroke(marker.getOutlineStyle().getStroke());
        }

        StyleObjectModel markerStyleModel = marker.getStyleObjectModel();
        domainMarker.setLabelPaint(markerStyleModel.getColor());
        domainMarker.setLabelFont(CSSUtil.getFont(markerStyleModel));

        if (marker.getLabelOffset() != null) {
            final StyleObjectModel offsetStyleModel = CSSUtil.getChartMarkerLabelOffsetModel(marker.getLabelOffset());

            final int top = offsetStyleModel.getMargin(0);
            final int left = offsetStyleModel.getMargin(1);
            final int bottom = offsetStyleModel.getMargin(2);
            final int right = offsetStyleModel.getMargin(3);

            final RectangleInsets offsetInsets = new RectangleInsets(top, left, bottom, right);

            domainMarker.setLabelOffset(offsetInsets);
        }

        domainMarker.setLabelAnchor(marker.getLabelAnchor().getAnchor());
        domainMarker.setLabelTextAnchor(marker.getLabelTextAnchor().getAnchor());
        domainMarker.setLabelOffsetType(marker.getLabelOffsetType().getOffsetType());
    }

    private Layer getMarkerLayer(Marker marker) {
        if (marker.getLayer() != null && marker.getLayer().equals(MarkerLayer.FOREGROUND)) {
            return Layer.FOREGROUND;
        } else {
            return Layer.BACKGROUND;
        }
    }

    private List<Marker> collectMarkers(GridChartView view) {
        Collection<UIComponent> children = view.getChildren();
        List<Marker> items = new ArrayList<Marker>();
        for (UIComponent child : children) {
            Collection<Marker> tmpCollection = getMarkersFromComponent(child);
            if (tmpCollection != null) {
                items.addAll(tmpCollection);
            }
        }
        return items;
    }

    private Collection<Marker> getMarkersFromComponent(UIComponent component) {
        if (component instanceof Marker) {
            return Collections.singletonList((Marker) component);
        }

        return new ArrayList<Marker>();
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, labelsVisible,
                saveAttachedState(context, gridLines),
                saveAttachedState(context, showAxes),
                keyAxisLabel,
                valueAxisLabel,
                saveAttachedState(context, axes),
                saveAttachedState(context, orientation),
                saveAttachedState(context, defaultOutlineStyle),
                saveAttachedState(context, outlines),
                saveAttachedState(context, defaultLabelsPosition),
                saveAttachedState(context, positiveLabelsPosition),
                saveAttachedState(context, negativeLabelsPosition),
                labelsOffset
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        labelsVisible = (Boolean) state[i++];
        gridLines = (List<ChartGridLines>) restoreAttachedState(facesContext, state[i++]);
        showAxes = (ChartDomain) restoreAttachedState(facesContext, state[i++]);
        keyAxisLabel = (String) state[i++];
        valueAxisLabel = (String) state[i++];
        axes = (ArrayList<ChartAxis>) restoreAttachedState(facesContext, state[i++]);
        orientation = (Orientation) restoreAttachedState(facesContext, state[i++]);
        defaultOutlineStyle = (LineStyle) restoreAttachedState(facesContext, state[i++]);
        outlines = (Collection<LineStyle>) restoreAttachedState(facesContext, state[i++]);
        defaultLabelsPosition = (BarChartLabelPosition) restoreAttachedState(facesContext, state[i++]);
        positiveLabelsPosition = (BarChartLabelPosition) restoreAttachedState(facesContext, state[i++]);
        negativeLabelsPosition = (BarChartLabelPosition) restoreAttachedState(facesContext, state[i++]);
        labelsOffset = (Double) state[i++];
    }

    public void decodeAction(String fieldValue) {
        renderAsImageFile();

        Chart chart = getChart();
        int index = Integer.parseInt(fieldValue);
        ChartEntity entity = chart.getRenderHints().getRenderingInfo().getEntityCollection().getEntity(index);
        GridPointInfo info = ChartInfoUtil.getGridPointInfo(entity, chart);

        point = info;

        queueEvent(new GridPointEvent(this, info));
    }
}