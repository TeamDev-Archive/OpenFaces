/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class ValidationBean {

    private String required;
    private List<SelectItem> tlsList = new ArrayList<SelectItem>();
    private float validDR;
    private String equal1;
    private String equal2;
    private String urlValidator;
    private String emailValidator;
    private String reValidator;
    private String customValidator1;


    public ValidationBean() {
        for (int i = 0; i < 10; i++) {
            tlsList.add(new SelectItem("value" + i, "Label" + i));
        }
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public List<SelectItem> getTLSList() {
        return tlsList;
    }

    public void setTLSList(List<SelectItem> TLSList) {
        tlsList = TLSList;
    }

    public float getValidDR() {
        return validDR;
    }

    public void setValidDR(float validDR) {
        this.validDR = validDR;
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

    public boolean customValidatorTest(FacesContext context, UIComponent component, Object value) {
        return "10".equals(value.toString());
    }
}
