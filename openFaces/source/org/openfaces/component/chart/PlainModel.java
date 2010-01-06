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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PlainModel implements ChartModel {
    private List<Series> series;

    public List<Series> getSeriesList() {
        return series;
    }

    public void setSeriesList(List<Series> series) {
        this.series = series;
    }

    public void addSeries(Series series) {
        if (series == null)
            return;

        if (this.series == null)
            this.series = new ArrayList<Series>();

        this.series.add(series);
    }

    public Series[] getSeries() {
        if (series == null)
            return null;

        return series.toArray(new Series[series.size()]);
    }
}
