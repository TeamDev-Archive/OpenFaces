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
import org.openfaces.util.ValueBindings;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class GridChartView extends ChartView {
    private Orientation orientation = Orientation.VERTICAL;
    private GridPointInfo point;
    private ChartDomain showAxes;

    private List<ChartGridLines> gridLines = new ArrayList<ChartGridLines>();

    private String keyAxisLabel;
    private String valueAxisLabel;
    private List<ChartAxis> axes = new ArrayList<ChartAxis>();

    private boolean labelsVisible = false;

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

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, labelsVisible,
                saveAttachedState(context, gridLines),
                saveAttachedState(context, showAxes),
                keyAxisLabel,
                valueAxisLabel,
                saveAttachedState(context, axes),
                saveAttachedState(context, orientation)
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