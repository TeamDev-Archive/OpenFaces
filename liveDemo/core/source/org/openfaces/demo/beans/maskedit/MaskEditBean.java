/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
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

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Sergey Pensov
 */
public class MaskEditBean {


    private String value1;
    private String mask1 = "####   ####   ####   ####";
    private String blank1 = "----   ----   ----   ----";
    private String value2;
    private String mask2 = "##/##";
    private String blank2 = "--/--";
    private String value3;
    private String mask3 = "###";
    private String blank3 = "---";
    private String message = "";

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

    public String getMessage() {
        return createMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String createMessage() {

        if (isFieldNotFilled(value1) || isFieldNotFilled(value2) || isFieldNotFilled(value3)) {
            return "<p>PLease, fill in all fields.";
        } else if (isInvalidDate(value2)){
            return "<p>PLease, check \'Valid Through\' field.";
        }
        return "<p>Credit Card: "
                + "</p><p>Credit Card Number: " + "XXXX XXXX XXXX " + value1.substring(value1.length() - 4)
                + "</p><p>Valid Through: " + value2
                + "</p><p>CVV2/CVC2: " + value3
                + "</p>";
    }

    private boolean isFieldNotFilled(String fieldValue) {
        return  (fieldValue == null || fieldValue.isEmpty() || fieldValue.contains("-"));
    }

    private boolean isInvalidDate(String date) {
        int month = Integer.parseInt(date.substring(0,2));
        int year = Integer.parseInt(date.substring(3, 5));
        boolean invalidDate = false;
        if (month < 1 || month > 12){
            invalidDate = true;
        } else if (year < 0 || year >99){
            invalidDate = true;
        }
        return invalidDate;
    }

}