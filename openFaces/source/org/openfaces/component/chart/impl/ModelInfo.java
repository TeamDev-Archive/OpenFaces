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
package org.openfaces.component.chart.impl;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utility class which collects the information about data presented in the chart model
 *
 * @author Ekaterina Shliakhovetskaya
 */
public class ModelInfo implements Serializable {
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 8199508075695198993L;

    private boolean dataEmpty;
    private ModelType modelType = ModelType.Category;
    private List<Series> nonEmptySeriesList;

    public ModelInfo(ChartModel model) {
        if (model == null) {
            dataEmpty = true;
            return;
        }

        Series[] modelSeries = model.getSeries();
        if (modelSeries == null || modelSeries.length == 0) {
            dataEmpty = true;
            return;
        }

        nonEmptySeriesList = new ArrayList<Series>();

        for (Series s : modelSeries) {
            if (s == null)
                continue;
            Tuple[] tuples = s.getTuples();
            if (tuples != null && tuples.length > 0)
                nonEmptySeriesList.add(s);
        }

        if (nonEmptySeriesList.size() == 0) {
            dataEmpty = true;
            return;
        }

        Series firstSeries = nonEmptySeriesList.get(0);
        Tuple[] tuples = firstSeries.getTuples();
        Tuple firstTuple = tuples[0];

        Object key = firstTuple.getKey();
        if (key instanceof Number) {
            modelType = ModelType.Number;
        } else if (key instanceof Date) {
            modelType = ModelType.Date;
        } else if (key instanceof Calendar) {
            modelType = ModelType.Date;
        }
    }

    public boolean isDataEmpty() {
        return dataEmpty;
    }

    public Series[] getNonEmptySeriesList() {
        if (nonEmptySeriesList == null)
            return null;

        return nonEmptySeriesList.toArray(new Series[nonEmptySeriesList.size()]);
    }

    public ModelType getModelType() {
        return modelType;
    }

}
