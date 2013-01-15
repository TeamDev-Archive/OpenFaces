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

package org.openfaces.testapp.testing;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.GregorianCalendar;


public class ValidBean {
    private String required;
    private String validDR;
    private String validLR;
    private String validL;
    private Integer intConv;
    private Double doubleConv;
    private Byte byteConv;
    private Short shortConv;
    private Float floatConv;
    private Date date = new GregorianCalendar(12, 12, 2006).getTime();
    private Date date2 = new GregorianCalendar(12, 12, 2006).getTime();
    private Number number = 598.88;

    public void action() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("My Global Message!!!"));
    }


    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getValidDR() {
        return validDR;
    }

    public void setValidDR(String validDR) {
        this.validDR = validDR;
    }

    public String getValidLR() {
        return validLR;
    }

    public void setValidLR(String validLR) {
        this.validLR = validLR;
    }

    public String getValidL() {
        return validL;
    }

    public void setValidL(String validL) {
        this.validL = validL;
    }

    public Integer getIntConv() {
        return intConv;
    }

    public void setIntConv(Integer intConv) {
        this.intConv = intConv;
    }

    public Double getDoubleConv() {
        return doubleConv;
    }

    public void setDoubleConv(Double doubleConv) {
        this.doubleConv = doubleConv;
    }

    public Byte getByteConv() {
        return byteConv;
    }

    public void setByteConv(Byte byteConv) {
        this.byteConv = byteConv;
    }

    public Short getShortConv() {
        return shortConv;
    }

    public void setShortConv(Short shortConv) {
        this.shortConv = shortConv;
    }

    public Float getFloatConv() {
        return floatConv;
    }

    public void setFloatConv(Float floatConv) {
        this.floatConv = floatConv;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }
}
