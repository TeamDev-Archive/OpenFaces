/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datatable;

import org.openfaces.util.Faces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StylingBean implements Serializable {

    private static final List<String> PAYMENTS_FILTER_VALUES = Arrays.asList("< 0", "0 \u2013 100", "100 \u2013 300", "300 \u2013 700", "> 700");

    private List<DeptItem> deptList = new ArrayList<DeptItem>();
    private List<FeatureItem> featuresList = new ArrayList<FeatureItem>();
    private List<PaymentItem> paymentList = new ArrayList<PaymentItem>();
    private FeatureItem selectedFeature = new FeatureItem("File sharing", true, true, true, false);

    public StylingBean() {
        deptList.add(new DeptItem("Thomas Dept", "56.8", "57.0", "57.1", "57.2", "228.1"));
        deptList.add(new DeptItem("John Dept", "54.0", "54.5", "22.0", "52.3", "182.8"));
        deptList.add(new DeptItem("Bob Dept", "55.0", "58.0", "59.0", "56.5", "228.5"));

        featuresList.add(new FeatureItem("Basic file operations", true, true, true, true));
        featuresList.add(new FeatureItem("File advanced search", true, true, true, false));
        featuresList.add(new FeatureItem("File sharing", true, true, true, false));
        featuresList.add(new FeatureItem("File editing", true, true, true, false));
        featuresList.add(new FeatureItem("Support rich text", true, false, false, false));
        featuresList.add(new FeatureItem("Autosave", true, true, false, false));
        featuresList.add(new FeatureItem("Support skins", true, true, false, false));

        paymentList.add(new PaymentItem("First Quarter", 100.00f, 150.00f, -100.00f, 120.00f));
        paymentList.add(new PaymentItem("Second Quarter", 200.00f, 250.00f, -350.00f, -150.00f));
        paymentList.add(new PaymentItem("Third Quarter", 50.00f, 700.00f, 450.00f, 2220.00f));
        paymentList.add(new PaymentItem("Fourth Quarter", -50.00f, 360.00f, -200.00f, 350.00f));
        paymentList.add(new PaymentItem("Total", 300.00f, 1460.00f, -200.00f, 2540.00f));
    }

    public List<DeptItem> getDeptList() {
        return deptList;
    }

    public List<FeatureItem> getFeaturesList() {
        return featuresList;
    }

    public List<PaymentItem> getPaymentList() {
        return paymentList;
    }

    public String getPaymentRangeDept1() {
        return calculatePayment(1);
    }

    public String getPaymentRangeDept2() {
        return calculatePayment(2);
    }

    public String getPaymentRangeDept3() {
        return calculatePayment(3);
    }

    public String getPaymentRangeDept4() {
        return calculatePayment(4);
    }

    private String calculatePayment(int deptIndex) {
        PaymentItem paymentItem = Faces.var("payments", PaymentItem.class);
        float payment = 0;
        if (deptIndex == 1) {
            payment = paymentItem.getDept1();
        } else if (deptIndex == 2) {
            payment = paymentItem.getDept2();
        } else if (deptIndex == 3) {
            payment = paymentItem.getDept3();
        } else if (deptIndex == 4) {
            payment = paymentItem.getDept4();
        }
        if (payment <= 0.00f)
            return "< 0";
        if (payment <= 100.00f)
            return "0 \u2013 100";
        if (payment <= 300)
            return "100 \u2013 300";
        if (payment <= 700)
            return "300 \u2013 700";
        return "> 700";

    }

    public List<String> getPaymentsFilterValues() {
        return PAYMENTS_FILTER_VALUES;
    }

    public FeatureItem getSelectedFeature() {
        return selectedFeature;
    }

    public void setSelectedFeature(FeatureItem selectedFeature) {
        this.selectedFeature = selectedFeature;
    }
}
