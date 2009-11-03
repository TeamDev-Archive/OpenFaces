/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.selectoneradio;

import org.openfaces.component.select.SelectItem;

import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Marshalenko
 */
public class RadioTest {

    private String radioGroup1;
    private String radioGroup2;
    private String radioGroup3;
    private String radioGroup4;
    private String radioGroup5;
    private String radioGroup6;
    
    private int valueChangeListener1Counter = 0;
    private int valueChangeListener2Counter = 0;
    public boolean testValueChangeListener1;
    public boolean testValueChangeListener2;

    private String radioGroup7;
    private String selectOneMenu7;
    private List<SelectItem> selectOneRadioItemsGroup7;
    private List<javax.faces.model.SelectItem> selectItemsGroup7;

    private String radioGroup8;
    private String selectOneMenu8;
    private List<SelectItem> selectOneRadioItemsGroup8;
    private List<javax.faces.model.SelectItem> selectItemsGroup8;

    public String getRadioGroup1() {
        return radioGroup1;
    }

    public void setRadioGroup1(String radioGroup1) {
        this.radioGroup1 = radioGroup1;
    }

    public String getRadioGroup2() {
        return radioGroup2;
    }

    public void setRadioGroup2(String radioGroup2) {
        this.radioGroup2 = radioGroup2;
    }

    public String getRadioGroup3() {
        return radioGroup3;
    }

    public void setRadioGroup3(String radioGroup3) {
        this.radioGroup3 = radioGroup3;
    }

    public String getRadioGroup4() {
        return radioGroup4;
    }

    public void setRadioGroup4(String radioGroup4) {
        this.radioGroup4 = radioGroup4;
    }

    public String getRadioGroup5() {
        return radioGroup5;
    }

    public void setRadioGroup5(String radioGroup5) {
        this.radioGroup5 = radioGroup5;
    }

    public String getRadioGroup6() {
        return radioGroup6;
    }

    public void setRadioGroup6(String radioGroup6) {
        this.radioGroup6 = radioGroup6;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void valueChangedAttribute1(ValueChangeEvent event) {
        valueChangeListener1Counter++;
    }

    public int getValueChangeListener1Counter() {
        return valueChangeListener1Counter;
    }

    public void setValueChangeListener1Counter(int valueChangeListener1Counter) {
        this.valueChangeListener1Counter = valueChangeListener1Counter;
    }

    public String getValueChangeListener1Flag() {
        return String.valueOf(valueChangeListener1Counter);
    }

    public void valueChangedAttribute2(ValueChangeEvent event) {
        valueChangeListener2Counter++;
    }

    public int getValueChangeListener2Counter() {
        return valueChangeListener2Counter;
    }

    public void setValueChangeListener2Counter(int valueChangeListener2Counter) {
        this.valueChangeListener2Counter = valueChangeListener2Counter;
    }

    public String getValueChangeListener2Flag() {
        return String.valueOf(valueChangeListener2Counter);
    }

    public boolean isTestValueChangeListener1() {
        return testValueChangeListener1;
    }

    public void setTestValueChangeListener1(boolean testValueChangeListener1) {
        this.testValueChangeListener1 = testValueChangeListener1;
    }

    public boolean isTestValueChangeListener2() {
        return testValueChangeListener2;
    }

    public void setTestValueChangeListener2(boolean testValueChangeListener2) {
        this.testValueChangeListener2 = testValueChangeListener2;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getRadioGroup7() {
        return radioGroup7;
    }

    public void setRadioGroup7(String radioGroup7) {
        this.radioGroup7 = radioGroup7;
    }

    public String getSelectOneMenu7() {
        if (selectOneMenu7 == null) {
            selectOneMenu7 = "1";
        }
        return selectOneMenu7;
    }

    public void setSelectOneMenu7(String selectOneMenu7) {
        this.selectOneMenu7 = selectOneMenu7;
    }

    public List<javax.faces.model.SelectItem> getSelectItemsGroup7() {
        selectItemsGroup7 = new ArrayList<javax.faces.model.SelectItem>();
        for (int i = 1; i < 6; i++) {
            selectItemsGroup7.add(new javax.faces.model.SelectItem(String.valueOf(i), "Radio " + i));
        }
        return selectItemsGroup7;
    }

    public void setSelectItemsGroup7(List<javax.faces.model.SelectItem> selectItemsGroup7) {
        this.selectItemsGroup7 = selectItemsGroup7;
    }

    public List<SelectItem> getSelectOneRadioItemsGroup7() {
        if (this.selectOneRadioItemsGroup7 == null) {
            this.selectOneRadioItemsGroup7 = new ArrayList<SelectItem>();
            for (int i = 1; i < 6; i++) {
                SelectItem selectItem = new SelectItem();
                selectItem.setItemValue(String.valueOf(i));
                selectItem.setItemLabel("Radio " + i);
                this.selectOneRadioItemsGroup7.add(selectItem);
            }
        }
        return this.selectOneRadioItemsGroup7;
    }

    public void reloadSelectOneRadio7() {
        int selectedForDisable = Integer.parseInt(selectOneMenu7);
        this.selectOneRadioItemsGroup7 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setItemValue(String.valueOf(i));
            selectItem.setItemLabel("Radio " + i);
            if (i == selectedForDisable) selectItem.setItemDisabled(true);
            this.selectOneRadioItemsGroup7.add(selectItem);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getRadioGroup8() {
        return radioGroup8;
    }

    public void setRadioGroup8(String radioGroup8) {
        this.radioGroup8 = radioGroup8;
    }

    public String getSelectOneMenu8() {
        if (selectOneMenu8 == null) {
            selectOneMenu8 = "1";
        }
        return selectOneMenu8;
    }

    public void setSelectOneMenu8(String selectOneMenu8) {
        this.selectOneMenu8 = selectOneMenu8;
    }

    public List<javax.faces.model.SelectItem> getSelectItemsGroup8() {
        selectItemsGroup8 = new ArrayList<javax.faces.model.SelectItem>();
        for (int i = 1; i < 6; i++) {
            selectItemsGroup8.add(new javax.faces.model.SelectItem(String.valueOf(i), "Radio " + i));
        }
        return selectItemsGroup8;
    }

    public void setSelectItemsGroup8(List<javax.faces.model.SelectItem> selectItemsGroup8) {
        this.selectItemsGroup8 = selectItemsGroup8;
    }

    public List<SelectItem> getSelectOneRadioItemsGroup8() {
        if (this.selectOneRadioItemsGroup8 == null) {
            this.selectOneRadioItemsGroup8 = new ArrayList<SelectItem>();
            for (int i = 1; i < 6; i++) {
                SelectItem selectItem = new SelectItem();
                selectItem.setItemValue(String.valueOf(i));
                selectItem.setItemLabel("Radio " + i);
                this.selectOneRadioItemsGroup8.add(selectItem);
            }
        }
        return this.selectOneRadioItemsGroup8;
    }

    public void reloadSelectOneRadio8() {
        int selectedForDisable = Integer.parseInt(selectOneMenu8);
        this.selectOneRadioItemsGroup8 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setItemValue(String.valueOf(i));
            selectItem.setItemLabel("Radio " + i);
            if (i == selectedForDisable) selectItem.setItemDisabled(true);
            this.selectOneRadioItemsGroup8.add(selectItem);
        }
    }
}
