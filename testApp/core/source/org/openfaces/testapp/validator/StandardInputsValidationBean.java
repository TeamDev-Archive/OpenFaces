/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class StandardInputsValidationBean {
    private String inputSecretValue;
    private String inputTextValue;
    private String inputTextAreaValue;
    private boolean checkBoxValue;
    private List selectManyCheckboxValues = new ArrayList();
    private List selectManyListboxValues = new ArrayList();
    private List selectManyMenuValues = new ArrayList();
    private String selectOneListboxValue;
    private String selectOneMenuValue;
    private String selectOneRadioValue;


    public List getSelectManyListboxValues() {
        return selectManyListboxValues;
    }

    public void setSelectManyListboxValues(List selectManyListboxValues) {
        this.selectManyListboxValues = selectManyListboxValues;
    }

    public List getSelectManyMenuValues() {
        return selectManyMenuValues;
    }

    public void setSelectManyMenuValues(List selectManyMenuValues) {
        this.selectManyMenuValues = selectManyMenuValues;
    }

    public String getSelectOneListboxValue() {
        return selectOneListboxValue;
    }

    public void setSelectOneListboxValue(String selectOneListboxValue) {
        this.selectOneListboxValue = selectOneListboxValue;
    }

    public String getSelectOneMenuValue() {
        return selectOneMenuValue;
    }

    public void setSelectOneMenuValue(String selectOneMenuValue) {
        this.selectOneMenuValue = selectOneMenuValue;
    }

    public String getSelectOneRadioValue() {
        return selectOneRadioValue;
    }

    public void setSelectOneRadioValue(String selectOneRadioValue) {
        this.selectOneRadioValue = selectOneRadioValue;
    }

    public List getSelectManyCheckboxValues() {
        return selectManyCheckboxValues;
    }

    public void setSelectManyCheckboxValues(List selectManyCheckboxValues) {
        this.selectManyCheckboxValues = selectManyCheckboxValues;
    }

    public boolean isCheckBoxValue() {
        return checkBoxValue;
    }

    public void setCheckBoxValue(boolean checkBoxValue) {
        this.checkBoxValue = checkBoxValue;
    }

    public String getInputSecretValue() {
        return inputSecretValue;
    }

    public void setInputSecretValue(String inputSecretValue) {
        this.inputSecretValue = inputSecretValue;
    }

    public String getInputTextValue() {
        return inputTextValue;
    }

    public void setInputTextValue(String inputTextValue) {
        this.inputTextValue = inputTextValue;
    }

    public String getInputTextAreaValue() {
        return inputTextAreaValue;
    }

    public void setInputTextAreaValue(String inputTextAreaValue) {
        this.inputTextAreaValue = inputTextAreaValue;
    }

    public void customCheckBoxValidator(FacesContext context, UIComponent component, Object object) {
        if ((Boolean) object) {
            return;
        }
        context.addMessage(component.getClientId(context), new FacesMessage("custom validator error", "this checkBox should be checked"));
    }

    public void customValidator(FacesContext context, UIComponent component, Object object) {
        if ("item 1".equals(object)) {
            return;
        }
        context.addMessage(component.getClientId(context), new FacesMessage("custom validator error", "Should be 'dogs'"));
    }
}
