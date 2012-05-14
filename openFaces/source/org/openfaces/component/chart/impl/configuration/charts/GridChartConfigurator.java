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

package org.openfaces.component.chart.impl.configuration.charts;

import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleInsets;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.DomainMarkers;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.Marker;
import org.openfaces.component.chart.MarkerLayer;
import org.openfaces.component.chart.MarkersContainer;
import org.openfaces.component.chart.RangeMarkers;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public abstract class GridChartConfigurator extends AbstractChartConfigurator {

    protected GridChartConfigurator(ChartModel model) {
        super(model);
    }

    protected void initMarkers(Chart chart, CategoryPlot categoryPlot) {
        final GridChartView chartView = (GridChartView) chart.getChartView();
        final List<Marker> domainMarkers = collectMarkers(chartView, DomainMarkers.class);
        final List<Marker> rangeMarkers = collectMarkers(chartView, RangeMarkers.class);

        for (Marker marker : domainMarkers) {
            org.jfree.chart.plot.Marker domainMarker = initCategoryMarker(marker);

            if (domainMarker != null) {
                categoryPlot.addDomainMarker((CategoryMarker) domainMarker, getMarkerLayer(marker));
            }
        }

        for (Marker marker : rangeMarkers) {
            org.jfree.chart.plot.Marker rangeMarker = initMarker(marker);

            if (rangeMarker != null) {
                categoryPlot.addRangeMarker(rangeMarker, getMarkerLayer(marker));
            }
        }
    }

    protected void initMarkers(Chart chart, XYPlot xyPlot) {
        final GridChartView chartView = (GridChartView) chart.getChartView();
        final List<Marker> domainMarkers = collectMarkers(chartView, DomainMarkers.class);
        final List<Marker> rangeMarkers = collectMarkers(chartView, RangeMarkers.class);

        for (Marker marker : domainMarkers) {
            org.jfree.chart.plot.Marker domainMarker = initMarker(marker);

            if (domainMarker != null) {
                xyPlot.addDomainMarker(domainMarker, getMarkerLayer(marker));
            }
        }

        for (Marker marker : rangeMarkers) {
            org.jfree.chart.plot.Marker rangeMarker = initMarker(marker);

            if (rangeMarker != null) {
                xyPlot.addRangeMarker(rangeMarker, getMarkerLayer(marker));
            }
        }
    }

    private org.jfree.chart.plot.Marker initCategoryMarker(Marker marker) {
        if (marker.getValue() != null) {
            org.jfree.chart.plot.Marker domainMarker = new CategoryMarker(marker.getValue());
            initializeChartMarker(marker, domainMarker);

            ((CategoryMarker) domainMarker).setDrawAsLine(marker.getDrawAsLine());

            return domainMarker;
        }

        return null;
    }

    private org.jfree.chart.plot.Marker initMarker(Marker marker) {
        if (marker.isIntervalMarker()) {
            org.jfree.chart.plot.Marker chartMarker = new IntervalMarker(marker.getStartValue(), marker.getEndValue());
            initializeChartMarker(marker, chartMarker);

            return chartMarker;
        } else if (marker.isValueMarker()) {
            final Double value = (marker.getValue() instanceof Double)
                    ? (Double) marker.getValue()
                    : Double.parseDouble((String) marker.getValue());
            org.jfree.chart.plot.Marker chartMarker = new ValueMarker(value);
            initializeChartMarker(marker, chartMarker);

            return chartMarker;
        }

        return null;
    }

    private void initializeChartMarker(Marker marker, org.jfree.chart.plot.Marker chartMarker) {
        chartMarker.setLabel(marker.getLabel());

        if (marker.getLineStyle() != null) {
            chartMarker.setPaint(marker.getLineStyle().getColor());
            chartMarker.setStroke(marker.getLineStyle().getStroke());
        }

        chartMarker.setAlpha(marker.getAlpha());

        if (marker.getOutlineStyle() != null) {
            chartMarker.setOutlinePaint(marker.getOutlineStyle().getColor());
            chartMarker.setOutlineStroke(marker.getOutlineStyle().getStroke());
        }

        StyleObjectModel markerStyleModel = marker.getStyleObjectModel();
        chartMarker.setLabelPaint(markerStyleModel.getColor());
        chartMarker.setLabelFont(CSSUtil.getFont(markerStyleModel));

        if (marker.getLabelOffset() != null) {
            final StyleObjectModel offsetStyleModel = CSSUtil.getChartMarkerLabelOffsetModel(marker.getLabelOffset());

            final int top = offsetStyleModel.getMargin(0);
            final int left = offsetStyleModel.getMargin(1);
            final int bottom = offsetStyleModel.getMargin(2);
            final int right = offsetStyleModel.getMargin(3);

            final RectangleInsets offsetInsets = new RectangleInsets(top, left, bottom, right);

            chartMarker.setLabelOffset(offsetInsets);
        }

        chartMarker.setLabelAnchor(marker.getLabelAnchor().getAnchor());
        chartMarker.setLabelTextAnchor(marker.getLabelTextAnchor().getAnchor());
        chartMarker.setLabelOffsetType(marker.getLabelOffsetType().getOffsetType());
    }

    private Layer getMarkerLayer(Marker marker) {
        if (marker.getLayer() != null && marker.getLayer().equals(MarkerLayer.FOREGROUND)) {
            return Layer.FOREGROUND;
        } else {
            return Layer.BACKGROUND;
        }
    }

    private List<Marker> collectMarkers(GridChartView view, Class markersTypeClass) {
        Collection<UIComponent> children = view.getChildren();
        List<Marker> items = new ArrayList<Marker>();
        for (UIComponent child : children) {
            Collection<Marker> tmpCollection = getMarkersFromComponent(child, markersTypeClass);
            if (tmpCollection != null) {
                items.addAll(tmpCollection);
            }
        }
        return items;
    }

    private Collection<Marker> getMarkersFromComponent(UIComponent component, Class markersTypeClass) {
        if (component instanceof MarkersContainer
                && component.getClass().isAssignableFrom(markersTypeClass)) {
            return ((MarkersContainer) component).getMarkers();
        }

        return null;
    }
}
