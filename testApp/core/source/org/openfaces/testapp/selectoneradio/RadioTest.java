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

import org.openfaces.component.input.SelectOneRadioItem;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 23, 2009
 * Time: 12:25:22 PM
 */
public class RadioTest {

    private String radioGroup1;
    private String radioGroup2;
    private String radioGroup3;
    private String radioGroup4;
    private String radioGroup5;
    private String radioGroup6;
    
    private String valueChangeListener5;

    private String radioGroup7;
    private String selectOneMenu7;
    private List<SelectOneRadioItem> selectOneRadioItemsGroup7;
    private List<SelectItem> selectItemsGroup7;

    private String radioGroup8;
    private String selectOneMenu8;
    private List<SelectOneRadioItem> selectOneRadioItemsGroup8;
    private List<SelectItem> selectItemsGroup8;    

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

    public String getValueChangeListener5() {
        return valueChangeListener5;
    }

    public void setValueChangeListener5(String valueChangeListener5) {
        this.valueChangeListener5 = valueChangeListener5;
    }

    public void valueChangeListener(ValueChangeEvent e) {
        this.valueChangeListener5 = new Date().toString();
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

    public List<SelectItem> getSelectItemsGroup7() {
        selectItemsGroup7 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            selectItemsGroup7.add(new SelectItem(String.valueOf(i), "Radio " + i));
        }
        return selectItemsGroup7;
    }

    public void setSelectItemsGroup7(List<SelectItem> selectItemsGroup7) {
        this.selectItemsGroup7 = selectItemsGroup7;
    }

    public List<SelectOneRadioItem> getSelectOneRadioItemsGroup7() {
        if (selectOneRadioItemsGroup7 == null) {
            selectOneRadioItemsGroup7 = new ArrayList<SelectOneRadioItem>();
            for (int i = 1; i < 6; i++) {
                SelectOneRadioItem selectOneRadioItem = new SelectOneRadioItem();
                selectOneRadioItem.setValue(String.valueOf(i));
                selectOneRadioItem.setItemLabel("Radio " + i);
                selectOneRadioItemsGroup7.add(selectOneRadioItem);
            }
        }
        return selectOneRadioItemsGroup7;
    }

    public void reloadSelectOneRadio7() {
        int selectedForDisable = Integer.parseInt(selectOneMenu7);
        selectOneRadioItemsGroup7 = new ArrayList<SelectOneRadioItem>();
        for (int i = 1; i < 6; i++) {
            SelectOneRadioItem selectOneRadioItem = new SelectOneRadioItem();
            selectOneRadioItem.setValue(String.valueOf(i));
            selectOneRadioItem.setItemLabel("Radio " + i);
            if (i == selectedForDisable) selectOneRadioItem.setDisabled(true);
            selectOneRadioItemsGroup7.add(selectOneRadioItem);
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

    public List<SelectItem> getSelectItemsGroup8() {
        selectItemsGroup8 = new ArrayList<SelectItem>();
        for (int i = 1; i < 6; i++) {
            selectItemsGroup8.add(new SelectItem(String.valueOf(i), "Radio " + i));
        }
        return selectItemsGroup8;
    }

    public void setSelectItemsGroup8(List<SelectItem> selectItemsGroup8) {
        this.selectItemsGroup8 = selectItemsGroup8;
    }

    public List<SelectOneRadioItem> getSelectOneRadioItemsGroup8() {
        if (selectOneRadioItemsGroup8 == null) {
            selectOneRadioItemsGroup8 = new ArrayList<SelectOneRadioItem>();
            for (int i = 1; i < 6; i++) {
                SelectOneRadioItem selectOneRadioItem = new SelectOneRadioItem();
                selectOneRadioItem.setValue(String.valueOf(i));
                selectOneRadioItem.setItemLabel("Radio " + i);
                selectOneRadioItemsGroup8.add(selectOneRadioItem);
            }
        }
        return selectOneRadioItemsGroup8;
    }

    public void reloadSelectOneRadio8() {
        int selectedForDisable = Integer.parseInt(selectOneMenu8);
        selectOneRadioItemsGroup8 = new ArrayList<SelectOneRadioItem>();
        for (int i = 1; i < 6; i++) {
            SelectOneRadioItem selectOneRadioItem = new SelectOneRadioItem();
            selectOneRadioItem.setValue(String.valueOf(i));
            selectOneRadioItem.setItemLabel("Radio " + i);
            if (i == selectedForDisable) selectOneRadioItem.setDisabled(true);
            selectOneRadioItemsGroup8.add(selectOneRadioItem);
        }
    }
}
