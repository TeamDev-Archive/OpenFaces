/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.chart;

import java.io.Serializable;

public class MonthIncome implements Serializable {
    private String month;
    private String quarter;
    private Integer firstProduct;
    private Integer secondProduct;

    public MonthIncome(String month, String quarter) {
        this(month, quarter, 0, 0);
    }

    public MonthIncome(String month, String quarter, Integer firstProduct, Integer secondProduct) {
        this.month = month;
        this.quarter = quarter;
        this.firstProduct = firstProduct;
        this.secondProduct = secondProduct;
    }

    public Integer getFirstProduct() {
        return firstProduct;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public void setFirstProduct(Integer firstProduct) {
        this.firstProduct = firstProduct;
    }

    public void setSecondProduct(Integer secondProduct) {
        this.secondProduct = secondProduct;
    }

    public Integer getSecondProduct() {
        return secondProduct;
    }

    public String getMonth() {
        return month;
    }

}