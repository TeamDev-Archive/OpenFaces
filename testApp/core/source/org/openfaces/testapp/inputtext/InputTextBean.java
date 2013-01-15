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

package org.openfaces.testapp.inputtext;

import javax.faces.event.ValueChangeEvent;
import java.util.logging.Logger;

/**
 * @author Vladimir Kurganov
 */
public class InputTextBean {

    Logger logger = Logger.getLogger(InputTextBean.class.getName());
    
    public boolean testValueChangeListener;
    private String value;
    private String value2;
    private String a4jValue = "ajax4jsf";
    private String promptText = "some prompt text";
    private String a4jPromptText = "ajax4jsf prompt text";

    private int valueChangeListenerCounter = 0;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public void valueChangedAttribute(ValueChangeEvent event) {
        valueChangeListenerCounter++;
    }

    public int getValueChangeListenerCounter() {
        return valueChangeListenerCounter;
    }

    public void setValueChangeListenerCounter(int valueChangeListenerCounter) {
        this.valueChangeListenerCounter = valueChangeListenerCounter;
    }

    public String getValueChangeListenerFlag() {
        return String.valueOf(valueChangeListenerCounter);
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

    public String getA4jValue() {
        logger.info("getA4jValue");
        return a4jValue;
    }

    public String getA4jPromptText() {
        logger.info("getA4jPromptText");
        return a4jPromptText;
    }

    public void setTestValueChangeListener(boolean testValueChangeListener) {
        this.testValueChangeListener = testValueChangeListener;
    }

    public void setA4jValue(String a4jValue) {
    }

    public void setA4jPromptText(String a4jPromptText) {
    }
}