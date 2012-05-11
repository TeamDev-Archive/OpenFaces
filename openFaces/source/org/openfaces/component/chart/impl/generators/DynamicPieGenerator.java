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
package org.openfaces.component.chart.impl.generators;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;
import org.openfaces.component.chart.ChartViewValueExpression;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.PieSectorProperties;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;

import java.io.Serializable;
import java.text.AttributedString;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicPieGenerator implements PieToolTipGenerator, PieURLGenerator, PieSectionLabelGenerator, Serializable {
    private static final String SECTOR = "sector";

    private transient final PieChartView pieView;
    private transient final ChartViewValueExpression valueExpression;

    public DynamicPieGenerator(PieChartView pieView, ChartViewValueExpression valueExpression) {
        this.pieView = pieView;
        this.valueExpression = valueExpression;
    }

    public String generateMouseAction(PieDataset pieDataset, Comparable comparable) {
        return getValue(pieDataset, comparable, 0);
    }

    public String generateToolTip(PieDataset pieDataset, Comparable comparable) {
        return getValue(pieDataset, comparable, 0);
    }

    public String generateURL(PieDataset pieDataset, Comparable comparable, int i) {
        return getValue(pieDataset, comparable, i);
    }

    public String generateSectionLabel(PieDataset pieDataset, Comparable comparable) {

        return getValue(pieDataset, comparable, 0);
    }

    public AttributedString generateAttributedSectionLabel(PieDataset pieDataset, Comparable comparable) {
        return null;
    }

    public boolean getConditionValue(PieSectorProperties sector, int index, PieDataset pieDataset, Comparable comparable) {
        PieSectorInfo info = ChartInfoUtil.getPieSectorInfo(pieDataset, comparable, index);

        ChartViewValueExpression dynamicCondition = sector.getDynamicCondition();
        if (dynamicCondition == null)
            return true;

        pieView.setSector(info);
        boolean result = (Boolean) sector.getDynamicCondition().getHint(SECTOR, info);
        pieView.setSector(null);

        return result;
    }

    private String getValue(PieDataset pieDataset, Comparable comparable, int index) {
        PieSectorInfo info = ChartInfoUtil.getPieSectorInfo(pieDataset, comparable, index);

        if (valueExpression == null)
            return null;

        pieView.setSector(info);
        String result = valueExpression.getHint(SECTOR, info).toString();
        pieView.setSector(null);

        return result;

    }


}
