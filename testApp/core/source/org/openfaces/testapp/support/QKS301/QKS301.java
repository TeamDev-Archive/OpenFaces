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

package org.openfaces.testapp.support.QKS301;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;


public class QKS301 {
    private String inputField;
    private Date date;
    private int size;
    private String sizeUnits;

    public String getSizeUnits() {
        return sizeUnits;
    }

    public void setSizeUnits(String sizeUnits) {
        this.sizeUnits = sizeUnits;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean validateSize(FacesContext context, UIComponent component, Object value) {
        return false;
    }


    public Date getFromDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -11);
        return c.getTime();
    }

    public Date getToDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 4);
        return c.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCanDeleteSelectedAreas() {
        return true;
    }

    public String getInputField() {
        return inputField;
    }

    public void setInputField(String inputField) {
        this.inputField = inputField;
    }

    public String deleteMultipleAction() {
        inputField = inputField + "new text";
        return null;
    }
}
