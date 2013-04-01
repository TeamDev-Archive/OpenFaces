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
package org.openfaces.component.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.LinkedList;

public class MaskEdit extends InputText {
    public static final String COMPONENT_TYPE = "org.openfaces.MaskEdit";
    public static final String COMPONENT_FAMILY = "org.openfaces.MaskEdit";


    /*inputId, mask, blank, maskSymbolArray, rolloverClass, focusedClass, dictionary*/
    private String mask;
    private String blank;
    private String dictionary;
    private String maskSymbolArray;


    public String getMask() {
        return ValueBindings.get(this, "mask", mask, "");
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getBlank() {
        return ValueBindings.get(this, "blank", blank, "");
    }

    public void setBlank(String blank) {
        this.blank = blank;
    }

    public String getDictionary() {
        return ValueBindings.get(this, "dictionary", dictionary, "absdefghijklmnopqrstuwwxyz");
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getMaskSymbolArray() {
        return ValueBindings.get(this, "maskSymbolArray", maskSymbolArray,"_");
    }

    public void setMaskSymbolArray(String maskSymbolArray) {
        this.maskSymbolArray = maskSymbolArray;
    }


    public MaskEdit() {
        setRendererType("org.openfaces.MaskEditRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

}
