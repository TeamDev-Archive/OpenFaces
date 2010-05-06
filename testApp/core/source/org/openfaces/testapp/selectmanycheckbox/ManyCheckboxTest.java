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

package org.openfaces.testapp.selectmanycheckbox;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Marshalenko
 */
public class ManyCheckboxTest {

    private List<String> checkboxGroup1;
    private List<String> checkboxGroup2;
    private List<String> checkboxGroup3;
    private List<String> checkboxGroup4;

    private int valueChangeListener1Counter = 0;
    private int valueChangeListener2Counter = 0;
    public boolean testValueChangeListener1;
    public boolean testValueChangeListener2;

    private List<String> checkboxGroup7;
    private String selectOneMenu7;
    private List<SelectItem> selectManyCheckboxItemsGroup7;
    private List<javax.faces.model.SelectItem> selectItemsGroup7;

    private List<String> checkboxGroup8;
    private String selectOneMenu8;
    private List<SelectItem> selectManyCheckboxItemsGroup8;
    private List<javax.faces.model.SelectItem> selectItemsGroup8;

    public List<String> getCheckboxGroup1() {
        return checkboxGroup1;
    }

    public void setCheckboxGroup1(List<String> checkboxGroup1) {
        this.checkboxGroup1 = checkboxGroup1;
    }

    public List<String> getCheckboxGroup2() {
        return checkboxGroup2;
    }

    public void setCheckboxGroup2(List<String> checkboxGroup2) {
        this.checkboxGroup2 = checkboxGroup2;
    }

    public List<String> getCheckboxGroup3() {
        return checkboxGroup3;
    }

    public void setCheckboxGroup3(List<String> checkboxGroup3) {
        this.checkboxGroup3 = checkboxGroup3;
    }

    public List<String> getCheckboxGroup4() {
        return checkboxGroup4;
    }

    public void setCheckboxGroup4(List<String> checkboxGroup4) {
        this.checkboxGroup4 = checkboxGroup4;
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

    public List<String> getCheckboxGroup7() {
        return checkboxGroup7;
    }

    public void setCheckboxGroup7(List<String> checkboxGroup7) {
        this.checkboxGroup7 = checkboxGroup7;
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
            selectItemsGroup7.add(new javax.faces.model.SelectItem(String.valueOf(i), "Chk " + i));
        }
        return selectItemsGroup7;
    }

    public void setSelectItemsGroup7(List<javax.faces.model.SelectItem> selectItemsGroup7) {
        this.selectItemsGroup7 = selectItemsGroup7;
    }

    public List<SelectItem> getSelectManyCheckboxItemsGroup7() {
        if (this.selectManyCheckboxItemsGroup7 == null) {
            this.selectManyCheckboxItemsGroup7 = new ArrayList<SelectItem>();
            for (int i = 1; i < 6; i++) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(String.valueOf(i));
                selectItem.setLabel("Chk " + i);
                this.selectManyCheckboxItemsGroup7.add(selectItem);
            }
        }
        return this.selectManyCheckboxItemsGroup7;
    }

    public void reloadSelectManyCheckbox7() {
        reloadSelectManyCheckbox7(null);
    }

    public void reloadSelectManyCheckbox7(ActionEvent event) {
        int selectedForDisable = Integer.parseInt(selectOneMenu7);
        this.selectManyCheckboxItemsGroup7 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(String.valueOf(i));
            selectItem.setLabel("Chk " + i);
            if (i == selectedForDisable) selectItem.setDisabled(true);
            this.selectManyCheckboxItemsGroup7.add(selectItem);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<String> getCheckboxGroup8() {
        return checkboxGroup8;
    }

    public void setCheckboxGroup8(List<String> checkboxGroup8) {
        this.checkboxGroup8 = checkboxGroup8;
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
            selectItemsGroup8.add(new javax.faces.model.SelectItem(String.valueOf(i), "Chk " + i));
        }
        return selectItemsGroup8;
    }

    public void setSelectItemsGroup8(List<javax.faces.model.SelectItem> selectItemsGroup8) {
        this.selectItemsGroup8 = selectItemsGroup8;
    }

    public List<SelectItem> getSelectManyCheckboxItemsGroup8() {
        if (this.selectManyCheckboxItemsGroup8 == null) {
            this.selectManyCheckboxItemsGroup8 = new ArrayList<SelectItem>();
            for (int i = 1; i < 6; i++) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(String.valueOf(i));
                selectItem.setLabel("Chk " + i);
                this.selectManyCheckboxItemsGroup8.add(selectItem);
            }
        }
        return this.selectManyCheckboxItemsGroup8;
    }

    public void reloadSelectManyCheckbox8() {
        reloadSelectManyCheckbox8(null);
    }

    public void reloadSelectManyCheckbox8(ActionEvent event) {
        int selectedForDisable = Integer.parseInt(selectOneMenu8);
        this.selectManyCheckboxItemsGroup8 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(String.valueOf(i));
            selectItem.setLabel("Chk " + i);
            if (i == selectedForDisable) selectItem.setDisabled(true);
            this.selectManyCheckboxItemsGroup8.add(selectItem);
        }
    }
}
