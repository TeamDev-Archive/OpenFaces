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

import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.LineRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRendererAdapter;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartView extends GridChartView {
    private boolean shapesVisible = true;
    private List<LineProperties> linePropertiesList = new ArrayList<LineProperties>();

    public boolean isShapesVisible() {
        return shapesVisible;
    }

    public void setShapesVisible(boolean shapesVisible) {
        this.shapesVisible = shapesVisible;
    }

    public List<LineProperties> getLinePropertiesList() {
        return linePropertiesList;
    }

    public void addLinePropertiesList(LineProperties lineProperties) {
        if (lineProperties == null)
            return;

        if (linePropertiesList == null)
            linePropertiesList = new ArrayList<LineProperties>();

        linePropertiesList.add(lineProperties);
    }

    @Override
    public String getFamily() {
        return "org.openfaces.LineChartView";
    }

    public String getHint() {
        return null;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                shapesVisible,
                saveAttachedState(context, linePropertiesList)
        };

    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        shapesVisible = (Boolean) state[i++];
        linePropertiesList = (List<LineProperties>) restoreAttachedState(facesContext, state[i++]);
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);
            return new GridXYPlotAdapter(ds, new XYLineRendererAdapter(this, ds), chart, this);
        }
        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(info);
            XYLineAndShapeRenderer renderer = new XYLineRendererAdapter(this, ds);
            return new GridDatePlotAdapter(ds, renderer, chart, this);
        }
        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        LineRendererAdapter renderer = new LineRendererAdapter(this, ds);
        return new GridCategoryPlotAdapter(ds, renderer, chart, this);
    }


}
