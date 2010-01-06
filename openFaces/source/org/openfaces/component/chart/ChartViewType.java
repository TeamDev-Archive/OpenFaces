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

/**
 * @author Ekaterina Shliakhovetskaya
 */
public enum ChartViewType {
    PIE("pie"),
    BAR("bar"),
    LINE("line");

    private String name;

    ChartViewType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public ChartView createChartView() {
        if (PIE.name.equals(name))
            return new PieChartView();
        else if (BAR.name.equals(name))
            return new BarChartView();
        else if (LINE.name.equals(name))
            return new LineChartView();
        else
            throw new IllegalStateException("Unknown ChartViewType enumeration value: " + name);
    }

    public boolean isViewOfThisType(ChartView view) {
        if (PIE.name.equals(name))
            return view instanceof PieChartView;
        else if (BAR.name.equals(name))
            return view instanceof BarChartView;
        else if (LINE.name.equals(name))
            return view instanceof LineChartView;
        else
            throw new IllegalStateException("Unknown ChartViewType enumeration value: " + name);
    }
}
