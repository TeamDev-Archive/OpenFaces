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

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Sergey Pensov
 */
public class MaskEditBean {


    private String value;
    private String mask = "######";
    private String blank = "DD/MM/YY";
    private String maskSymbol = "DMY";
    private DefaultMasks defaultMasks;

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

    public String getMaskSymbol() {
        return maskSymbol;
    }

    public void setMaskSymbol(String maskSymbol) {
        this.maskSymbol = maskSymbol;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setBlank(String blank) {
        this.blank = blank;
    }

    public String getMask() {

        return mask;
    }

    public String getBlank() {
        return blank;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        System.out.println(value);
        this.value = value;
    }

    public String coolSubmit() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams = context.getExternalContext().getRequestParameterMap();
        System.out.println("Login:" + this.getValue() + " blank:" + this.getBlank() + " test:" + this.getSubmitTest());
        return "";
    }

}
