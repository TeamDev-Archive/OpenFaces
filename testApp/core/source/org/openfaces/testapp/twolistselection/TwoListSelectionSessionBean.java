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
package org.openfaces.testapp.twolistselection;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class TwoListSelectionSessionBean {

    private List<SelectItem> testCollection = new ArrayList<SelectItem>();
    private List testSelectedCollection = new ArrayList();
    private List testSelectedCollection2 = new ArrayList();
    private List testSelectedCollection3 = new ArrayList();

    private boolean asAttributeVCL;
    public static boolean testValueChangeListener;

    public TwoListSelectionSessionBean() {
        testCollection.add(new SelectItem("item_Value_1", "item_Label_1"));
        testCollection.add(new SelectItem("item_Value_2", "item_Label_2"));
        testCollection.add(new SelectItem("item_Value_3", "item_Label_3"));
        testCollection.add(new SelectItem("item_Value_4", "item_Label_4", "", true));
        testCollection.add(new SelectItem("item_Value_5", "item_Label_5"));
        testCollection.add(new SelectItem("item_Value_6", "item_Label_6", "", true));
        testCollection.add(new SelectItem("item_Value_7", "item_Label_7"));

        testSelectedCollection.add(testCollection.get(2).getValue());
        testSelectedCollection.add(testCollection.get(4).getValue());

        testSelectedCollection2.add("item_Value_3");
        testSelectedCollection2.add("item_Value_5");

        testSelectedCollection3.add("itemV_3");
        testSelectedCollection3.add("itemV_5");
    }

    public List<SelectItem> getTestCollection() {
        return testCollection;
    }

    public void setTestCollection(List<SelectItem> testCollection) {
        this.testCollection = testCollection;
    }

    public List getTestSelectedCollection() {
        return testSelectedCollection;
    }

    public void setTestSelectedCollection(List testSelectedCollection) {
        this.testSelectedCollection = testSelectedCollection;
    }

    public String getSelectedValueAsString() {
        StringBuilder result = new StringBuilder();
        if (testSelectedCollection.size() > 0) {
            for (Object selectedItem : testSelectedCollection) {
                result.append((String) selectedItem);
                result.append(" ");
            }
        } else {
            result.append("none");
        }
        return result.toString();
    }

    public String getSelectedValueAsString2() {
        StringBuilder result = new StringBuilder();
        if (testSelectedCollection2.size() > 0) {
            for (Object selectedItem : testSelectedCollection2) {
                result.append((String) selectedItem);
                result.append(" ");
            }
        } else {
            result.append("none");
        }
        return result.toString();
    }

    public List getTestSelectedCollection2() {
        return testSelectedCollection2;
    }

    public void setTestSelectedCollection2(List testSelectedCollection2) {
        this.testSelectedCollection2 = testSelectedCollection2;
    }

    public List getTestSelectedCollection3() {
        return testSelectedCollection3;
    }

    public void setTestSelectedCollection3(List testSelectedCollection3) {
        this.testSelectedCollection3 = testSelectedCollection3;
    }

    public String getSelectedValueAsString3() {
        StringBuilder result = new StringBuilder();
        if (testSelectedCollection3.size() > 0) {
            for (Object selectedItem : testSelectedCollection3) {
                result.append((String) selectedItem);
                result.append(" ");
            }
        } else {
            result.append("none");
        }
        return result.toString();
    }

    public void VCLAttribute(ValueChangeEvent event) {
        asAttributeVCL = !asAttributeVCL;
    }


    public boolean isAsAttributeVCL() {
        return asAttributeVCL;
    }

    public void setAsAttributeVCL(boolean asAttributeVCL) {
        this.asAttributeVCL = asAttributeVCL;
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

}