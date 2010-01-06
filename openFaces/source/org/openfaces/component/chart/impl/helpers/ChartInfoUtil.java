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
package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.impl.GridPointInfoImpl;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.PieSectorInfoImpl;
import org.openfaces.component.chart.impl.SeriesInfoImpl;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartInfoUtil {

    public static GridPointInfo getGridPointInfo(ChartEntity entity, Chart chart) {
        if (entity instanceof CategoryItemEntity)
            return getGridPointInfo((CategoryItemEntity) entity, chart);

        if (entity instanceof XYItemEntity)
            return getGridPointInfo((XYItemEntity) entity, chart);

        return null;
    }

    public static PieSectorInfo getPieSectorInfo(ChartEntity entity) {
        if (entity instanceof PieSectionEntity) {
            PieSectionEntity pieEntity = (PieSectionEntity) entity;
            return getPieSectorInfo(pieEntity.getDataset(), pieEntity.getSectionKey(), pieEntity.getPieIndex());
        }

        return null;
    }

    public static PieSectorInfo getPieSectorInfo(PieDataset pieDataset, Comparable comparable, int dsIndex) {
        double total = 0;
        for (int i = 0; i < pieDataset.getKeys().size(); i++) {
            Object key = pieDataset.getKeys().get(i);
            Object value = pieDataset.getValue((Comparable) key);
            if (value != null) {
                double dValue = ((Number) value).doubleValue();
                if (dValue > 0)
                    total = total + dValue;
            }
        }
        int index = pieDataset.getIndex(comparable);

        Object value = pieDataset.getValue(index);
        double dValue = 0;
        if (value != null) {
            dValue = ((Number) value).doubleValue();
        }

        PieSectorInfo sector = new PieSectorInfoImpl();
        sector.setKey(comparable);
        sector.setValue(value);
        sector.setSeriesTotal(total);
        sector.setIndex(index);
        sector.setSeries(new SeriesInfoImpl());

        sector.getSeries().setIndex(dsIndex);

        double p = (dValue / total);
        DecimalFormat nf1 = new DecimalFormat("#.00%");
        String proportionalPercent = nf1.format(p);

        sector.setProportionalValue(proportionalPercent);
        return sector;
    }

    private static GridPointInfo getGridPointInfo(CategoryItemEntity en, Chart chart) {
        ChartModel model = chart.getModel();
        if (model == null)
            return null;

        GridPointInfo info = new GridPointInfoImpl();
        info.setSeries(new SeriesInfoImpl());
        info.getSeries().setIndex(en.getSeries());

        ModelInfo modelInfo = new ModelInfo(model);

        if (!modelInfo.isDataEmpty()) {
            CategoryDataset ds = ModelConverter.toCategoryDataset(modelInfo);
            Comparable seriesKey = ds.getRowKey(en.getSeries());
            Comparable tupleKey = ds.getColumnKey(en.getCategoryIndex());
            Object tupleValue = ds.getValue(seriesKey, tupleKey);
            info.setKey(tupleKey);
            info.setValue(tupleValue);
            info.getSeries().setKey(seriesKey);
        }

        return info;
    }

    private static GridPointInfo getGridPointInfo(XYItemEntity en, Chart chart) {
        ChartModel model = chart.getModel();
        if (model == null)
            return null;

        GridPointInfo info = new GridPointInfoImpl();
        info.setSeries(new SeriesInfoImpl());
        info.getSeries().setIndex(en.getSeriesIndex());

        ModelInfo modelInfo = new ModelInfo(model);
        if (!modelInfo.isDataEmpty()) {
            if (modelInfo.getModelType() == ModelType.Date) {
                TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(modelInfo);
                Comparable seriesKey = ds.getSeriesKey(en.getSeriesIndex());
                Number tupleKey = ds.getX(en.getSeriesIndex(), en.getItem());
                if (tupleKey instanceof Long) {
                    info.setKey(new Date(tupleKey.longValue()));
                } else {
                    info.setKey(tupleKey);
                }

                Object tupleValue = ds.getY(en.getSeriesIndex(), en.getItem());

                info.setValue(tupleValue);
                info.getSeries().setKey(seriesKey);
            } else {
                XYDataset ds = ModelConverter.toXYSeriesCollection(modelInfo);
                Comparable seriesKey = ds.getSeriesKey(en.getSeriesIndex());
                Number tupleKey = ds.getX(en.getSeriesIndex(), en.getItem());
                info.setKey(tupleKey);

                Object tupleValue = ds.getY(en.getSeriesIndex(), en.getItem());

                info.setValue(tupleValue);
                info.getSeries().setKey(seriesKey);
            }
        }

        return info;
    }


}
