/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.maskedit;

import org.openfaces.component.input.DefaultMasks;
import org.openfaces.component.input.MaskDynamicConstructor;
import org.openfaces.component.input.MaskSymbolConstructor;

import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Sergey Pensov
 */
public class MaskEditBean {


    private String value1;
    private String mask1 = "///I/#.I.#.#.I///";
    private String blank1 = "/// /_. ._._. ///";
    private String value2;
    private String mask2 = "+3(###) ### ### ##";
    private String blank2 = "+3(   )           ";
    private String value3;
    private String mask3 = "I.I.I";
    private String blank3 = " . . ";

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getMask1() {
        return mask1;
    }

    public void setMask1(String mask1) {
        this.mask1 = mask1;
    }

    public String getBlank1() {
        return blank1;
    }

    public void setBlank1(String blank1) {
        this.blank1 = blank1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getMask2() {
        return mask2;
    }

    public void setMask2(String mask2) {
        this.mask2 = mask2;
    }

    public String getBlank2() {
        return blank2;
    }

    public void setBlank2(String blank2) {
        this.blank2 = blank2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getMask3() {
        return mask3;
    }

    public void setMask3(String mask3) {
        this.mask3 = mask3;
    }

    public String getBlank3() {
        return blank3;
    }

    public void setBlank3(String blank3) {
        this.blank3 = blank3;
    }

    private DefaultMasks defaultMasks;
    private boolean blankVisible;
    private Collection<MaskDynamicConstructor> dynamicConstructors = new LinkedList<MaskDynamicConstructor>();
    private Collection<MaskSymbolConstructor> symbolConstructors = new LinkedList<MaskSymbolConstructor>();

    public MaskEditBean() {
        dynamicConstructors.add(new MaskDynamicConstructor('I', '#', 1, 4, '_'));
        symbolConstructors.add(new MaskSymbolConstructor('X', "asd123"));
    }

    public Collection<MaskDynamicConstructor> getDynamicConstructors() {
        return dynamicConstructors;
    }

    public void setDynamicConstructors(Collection<MaskDynamicConstructor> dynamicConstructors) {
        this.dynamicConstructors = dynamicConstructors;
    }

    public Collection<MaskSymbolConstructor> getSymbolConstructors() {
        return symbolConstructors;
    }

    public void setSymbolConstructors(Collection<MaskSymbolConstructor> symbolConstructors) {
        this.symbolConstructors = symbolConstructors;
    }

    public boolean isBlankVisible() {
        return blankVisible;
    }

    public void setBlankVisible(boolean blankVisible) {
        this.blankVisible = blankVisible;
    }

    public DefaultMasks getDefaultMasks() {
        return defaultMasks;
    }

    public void setDefaultMasks(DefaultMasks defaultMasks) {
        this.defaultMasks = defaultMasks;
    }

    public String getSubmitTest() {
        return submitTest;
    }

    public void setSubmitTest(String submitTest) {
        this.submitTest = submitTest;
    }

    private String submitTest;


}
