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
package org.openfaces.component.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.util.Collection;

import static org.openfaces.component.input.DefaultMasks.*;

public class MaskEdit extends OUIInputText {
    public static final String COMPONENT_TYPE = "org.openfaces.MaskEdit";
    public static final String COMPONENT_FAMILY = "org.openfaces.MaskEdit";
    private String mask;
    private String blank;
    private String dictionary;
    private DefaultMasks defaultMask;
    private boolean blankVisible;
    private Collection<MaskDynamicConstructor> maskDynamicConstructors;
    private Collection<MaskSymbolConstructor> maskSymbolConstructors;

    public Collection getSymbolConstructors() {
        return ValueBindings.get(this, "maskSymbolConstructors", maskSymbolConstructors, Collection.class);
    }

    public void setSymbolConstructors(Collection<MaskSymbolConstructor> maskSymbolConstructors) {
        this.maskSymbolConstructors = maskSymbolConstructors;
    }

    public Collection getDynamicConstructors() {
        return ValueBindings.get(this, "maskDynamicConstructors", maskDynamicConstructors, Collection.class);
    }

    public void setDynamicConstructors(Collection<MaskDynamicConstructor> maskDynamicConstructors) {
        this.maskDynamicConstructors = maskDynamicConstructors;
    }

    public boolean isBlankVisible() {
        return ValueBindings.get(this, "blankVisible", blankVisible, true);
    }

    public void setBlankVisible(boolean blankVisible) {
        this.blankVisible = blankVisible;
    }

    public DefaultMasks getDefaultMask() {
        return defaultMask;
    }

    public void setDefaultMask(DefaultMasks defaultMask) {
        this.defaultMask = defaultMask;
    }

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

    public MaskEdit() {
        setRendererType("org.openfaces.MaskEditRenderer");
    }


    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                mask,
                blank,
                dictionary,
                defaultMask,
                blankVisible,
                maskDynamicConstructors,
                maskSymbolConstructors,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        mask = (String) values[i++];
        blank = (String) values[i++];
        dictionary = (String) values[i++];
        defaultMask = (DefaultMasks) values[i++];
        blankVisible = (Boolean) values[i++];
        maskDynamicConstructors = (Collection<MaskDynamicConstructor>) values[i++];
        maskSymbolConstructors = (Collection<MaskSymbolConstructor>) values[i++];
    }

}