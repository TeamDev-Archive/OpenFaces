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

import org.jfree.chart.entity.ChartEntity;
import org.openfaces.component.chart.impl.configuration.charts.ChartConfigurator;
import org.openfaces.component.chart.impl.configuration.charts.PieChartConfigurator;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieChartView extends ChartView {
    private List<PieSectorProperties> sectors = new ArrayList<PieSectorProperties>();

    private PieSectorInfo selectedSector;
    private PieSectorInfo sector;

    private Boolean labelsVisible;

    private Boolean showShadow;
    private Double shadowXOffset = 4.0;
    private Double shadowYOffset = 4.0;
    private Color shadowColor = Color.GRAY;

    public PieSectorInfo getSelectedSector() {
        return selectedSector;
    }

    @Override
    public String getFamily() {
        return "org.openfaces.PieChartView";
    }

    public void setSelectedSector(PieSectorInfo selectedSector) {
        this.selectedSector = selectedSector;
    }

    public boolean isLabelsVisible() {
        return ValueBindings.get(this, "labelsVisible", labelsVisible, false);
    }

    public void setLabelsVisible(boolean labelsVisible) {
        this.labelsVisible = labelsVisible;
    }

    public List<PieSectorProperties> getSectors() {
        return sectors;
    }

    public PieSectorInfo getSector() {
        return sector;
    }

    public void setSector(PieSectorInfo sector) {
        this.sector = sector;
    }

    public Boolean isShowShadow() {
        return ValueBindings.get(this, "showShadow", showShadow, false);
    }

    public void setShowShadow(Boolean showShadow) {
        this.showShadow = showShadow;
    }

    public double getShadowXOffset() {
        return ValueBindings.get(this, "shadowXOffset", shadowXOffset, 4.0);
    }

    public void setShadowXOffset(double shadowXOffset) {
        this.shadowXOffset = shadowXOffset;
    }

    public double getShadowYOffset() {
        return ValueBindings.get(this, "shadowYOffset", shadowYOffset, 4.0);
    }

    public void setShadowYOffset(double shadowYOffset) {
        this.shadowYOffset = shadowYOffset;
    }

    public Color getShadowColor() {
        return ValueBindings.get(this, "shadowColor", shadowColor, Color.class);
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void decodeAction(String fieldValue) {
        Chart chart = getChart();
        chart.make();

        int index = Integer.parseInt(fieldValue);
        ChartEntity entity = chart.getRenderHints().getRenderingInfo().getEntityCollection().getEntity(index);
        PieSectorInfo info = ChartInfoUtil.getPieSectorInfo(entity);

        if (chart.getChartSelection() != null) {
            chart.getChartSelection().setItem(info);
        }

        selectedSector = info;

        this.queueEvent(new PieSectorEvent(this, info));
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                labelsVisible,
                saveAttachedState(context, sectors),
                showShadow,
                shadowXOffset,
                shadowYOffset,
                saveAttachedState(context, shadowColor)
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        labelsVisible = (Boolean) state[i++];
        sectors = (List<PieSectorProperties>) restoreAttachedState(facesContext, state[i++]);
        showShadow = (Boolean) state[i++];
        shadowXOffset = (Double) state[i++];
        shadowYOffset = (Double) state[i++];
        shadowColor = (Color) restoreAttachedState(facesContext, state[i++]);
    }

    public String getHint() {
        return null;
    }

    @Override
    public ChartConfigurator getConfigurator() {
        final Chart chart = getChart();
        return new PieChartConfigurator(chart.getModel());
    }
}
