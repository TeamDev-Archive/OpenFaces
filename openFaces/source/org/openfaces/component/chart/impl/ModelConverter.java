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

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Tuple;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class - a set of static methods to transfer data represented in ChartModel
 * to appropriate JFreeChart Dataset
 *
 * @author Ekaterina Shliakhovetskaya
 */
public class ModelConverter {

    public static PieDataset toPieDataset(ChartModel model) {
        ModelInfo info = new ModelInfo(model);

        if (info.isDataEmpty())
            return null;

        DefaultPieDataset pieResult = new DefaultPieDataset();

        Series[] series = info.getNonEmptySeriesList();

        for (Series sery : series) {
            Tuple[] tuples = sery.getTuples();
            if (tuples == null)
                continue;

            for (Tuple tuple : tuples) {
                Object key = tuple.getKey();
                Object dsKey = key;
                if (key instanceof Calendar)
                    dsKey = ((Calendar) key).getTime();
                Object value = tuple.getValue();
                if (value != null && !(value instanceof Number))
                    throw new DataConversionException("Incorrect Value type for Key = " + key + ". Required: Number. Currently defined: " + value.getClass().getName());
                pieResult.setValue((Comparable) dsKey, (Number) value);
            }
        }

        if (pieResult.getItemCount() == 0) pieResult = null;

        return pieResult;
    }

    public static CategoryDataset toCategoryDataset(ModelInfo info) {
        if (info.isDataEmpty())
            return null;

        DefaultCategoryDataset categoryResult = new DefaultCategoryDataset();

        Series[] series = info.getNonEmptySeriesList();

        for (Series sery : series) {
            Tuple[] tuples = sery.getTuples();
            for (Tuple tuple : tuples) {
                Object key = tuple.getKey();
                Object value = tuple.getValue();
                if (value != null && !(value instanceof Number))
                    throw new DataConversionException("Incorrect Value type for Key = " + key + ". Required: Number. Currently defined: " + value.getClass().getName());
                categoryResult.addValue((Number) value, sery.getKey(), tuple.getKey());
            }
        }

        if (categoryResult.getRowCount() == 0) categoryResult = null;
        return categoryResult;
    }

    private static XYSeries toXYSeries(Series series) {
        if (series == null)
            return null;
        Tuple[] tuples = series.getTuples();
        if (tuples == null || tuples.length == 0)
            return null;

        XYSeries xySeries = new XYSeries(series.getKey());
        for (Tuple tuple : tuples) {
            Object key = tuple.getKey();
            Object value = tuple.getValue();

            if (!(key instanceof Number))
                throw new DataConversionException("Incorrect type for tuple key = " + key + ". Required: Number. Currently defined: " + key.getClass());
            if (!(value instanceof Number))
                throw new DataConversionException("Incorrect type for tuple value = " + value + ". Required: Number. Currently defined: " + (value != null ? value.getClass().getName() : "<null>"));
            xySeries.add((Number) key, (Number) value);
        }
        return xySeries;
    }

    private static TimeSeries toTimeSeries(Series series) {
        if (series == null)
            return null;

        Tuple[] tuples = series.getTuples();
        if (tuples == null)
            return null;

        TimeSeries tSeries = new TimeSeries(series.getKey().toString());

        for (Tuple tuple : tuples) {
            Object key = tuple.getKey();
            Object value = tuple.getValue();
            RegularTimePeriod period;

            if (key instanceof Date) {
                period = new Day((Date) key);
            } else if (key instanceof Calendar) {
                period = new Day(((Calendar) key).getTime());
            } else
                throw new DataConversionException("Incorrect Key type. Required: Date or Calendar. Currently defined: " + key.getClass());

            if (value != null && !(value instanceof Number))
                throw new DataConversionException("Incorrect Value type for Key = " + key + ". Required: Number. Currently defined: " + value.getClass().getName());
            else {
                TimeSeriesDataItem item = new TimeSeriesDataItem(period, (Number) value);
                tSeries.add(item);
            }
        }
        if (tSeries.getItemCount() == 0) tSeries = null;

        return tSeries;
    }

    public static XYSeriesCollection toXYSeriesCollection(ModelInfo info) {
        if (info.isDataEmpty())
            return null;

        XYSeriesCollection xyResult = new XYSeriesCollection();

        Series[] series = info.getNonEmptySeriesList();
        for (Series sery : series) {
            XYSeries xySeries = toXYSeries(sery);
            if (xySeries != null)
                xyResult.addSeries(xySeries);
        }

        return xyResult;
    }

    public static TimeSeriesCollection toTimeSeriesCollection(ModelInfo info) {
        if (info.isDataEmpty())
            return null;

        TimeSeriesCollection xyResult = new TimeSeriesCollection();

        Series[] series = info.getNonEmptySeriesList();
        for (Series sery : series) {
            TimeSeries tSeries = toTimeSeries(sery);
            if (tSeries != null)
                xyResult.addSeries(tSeries);
        }

        return xyResult;
    }

}
