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

package org.openfaces.testapp.testing;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OpenFacesValidatorsBean {
    private String equal1;
    private String equal2;
    private String equal3;
    private String equal4;
    private String urlValidator;
    private String emailValidator;
    private String reValidator;
    private String customValidator1;
    private String customValidator2;
    private String customValidator3;
    private Date calendarValue;
    private List<SelectItem> tlsList = new ArrayList<SelectItem>();

    public OpenFacesValidatorsBean() {
        tlsList.add(new SelectItem("1", "Item1", "description1", true));
        tlsList.add(new SelectItem("2", "Item2", "description2"));
        tlsList.add(new SelectItem("3", "Item3", "description3", true));
        tlsList.add(new SelectItem("4", "Item4", "description4"));
        tlsList.add(new SelectItem("5", "Item5", "description5"));
    }

    public String getEqual3() {
        return equal3;
    }

    public void setEqual3(String equal3) {
        this.equal3 = equal3;
    }

    public String getEqual4() {
        return equal4;
    }

    public void setEqual4(String equal4) {
        this.equal4 = equal4;
    }

    public List<SelectItem> getTLSList() {
        return tlsList;
    }

    public void setTLSList(List<SelectItem> TLSList) {
        tlsList = TLSList;
    }


    public String getEqual1() {
        return equal1;
    }

    public void setEqual1(String equal1) {
        this.equal1 = equal1;
    }

    public String getEqual2() {
        return equal2;
    }

    public void setEqual2(String equal2) {
        this.equal2 = equal2;
    }

    public String getUrlValidator() {
        return urlValidator;
    }

    public void setUrlValidator(String urlValidator) {
        this.urlValidator = urlValidator;
    }

    public String getEmailValidator() {
        return emailValidator;
    }

    public void setEmailValidator(String emailValidator) {
        this.emailValidator = emailValidator;
    }

    public String getReValidator() {
        return reValidator;
    }

    public void setReValidator(String reValidator) {
        this.reValidator = reValidator;
    }

    public String getCustomValidator1() {
        return customValidator1;
    }

    public void setCustomValidator1(String customValidator1) {
        this.customValidator1 = customValidator1;
    }

    public String getCustomValidator2() {
        return customValidator2;
    }

    public void setCustomValidator2(String customValidator2) {
        this.customValidator2 = customValidator2;
    }

    public String getCustomValidator3() {
        return customValidator3;
    }

    public void setCustomValidator3(String customValidator3) {
        this.customValidator3 = customValidator3;
    }

    public Date getCalendarValue() {
        return calendarValue;
    }

    public void setCalendarValue(Date calendarValue) {
        this.calendarValue = calendarValue;
    }

    public boolean customValidatorTest(FacesContext context, UIComponent component, Object value) {
        return "10".equals(value.toString());
    }
}
