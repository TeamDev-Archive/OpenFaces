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
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;
import org.openfaces.component.chart.impl.plots.MultiplePiePlotAdapter;
import org.openfaces.component.chart.impl.plots.PiePlotAdapter;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieChartView extends ChartView {
    private List<PieSectorProperties> sectors = new ArrayList<PieSectorProperties>();

    private PieSectorInfo selectedSector;
    private PieSectorInfo sector;

    private boolean labelsVisible;

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
        return labelsVisible;
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

    public void decodeAction(String fieldValue) {
        renderAsImageFile();

        Chart chart = getChart();
        int index = Integer.parseInt(fieldValue);
        ChartEntity entity = chart.getRenderHints().getRenderingInfo().getEntityCollection().getEntity(index);
        PieSectorInfo info = ChartInfoUtil.getPieSectorInfo(entity);

        selectedSector = info;

        this.queueEvent(new PieSectorEvent(this, info));
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                labelsVisible,
                saveAttachedState(context, sectors)
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
    }

    public String getHint() {
        return null;
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (info.isDataEmpty()) {
            return new PiePlotAdapter(null, chart, this);
        }
        if (info.getNonEmptySeriesList().length < 2) {
            PieDataset ds = ModelConverter.toPieDataset(model);
            return new PiePlotAdapter(ds, chart, this);
        }
        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        return new MultiplePiePlotAdapter(ds, TableOrder.BY_ROW, chart, this);
    }

}
