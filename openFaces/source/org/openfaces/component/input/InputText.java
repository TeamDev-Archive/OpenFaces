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
package org.openfaces.component.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.component.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */

public class InputText extends OUIInputText {
    public static final String COMPONENT_TYPE = "org.openfaces.InputText";
    public static final String COMPONENT_FAMILY = "org.openfaces.InputText";

    private String dir;
    private String lang;
    private String alt;
    private String onselect;
    private Boolean readonly;
    private Integer maxlength;
    private Integer size;
    //todo: add autocomplete attribute (as it was added to HtmlInputText in JSF 1.2)

    public int getSize() {
        return ValueBindings.get(this, "size", size, Integer.MIN_VALUE);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isReadonly() {
        return ValueBindings.get(this, "readonly", readonly, false);
    }

    public int getMaxlength() {
        return ValueBindings.get(this, "maxLength", maxlength, Integer.MIN_VALUE);
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public String getAlt() {
        return ValueBindings.get(this, "alt", alt);
    }

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public String getOnselect() {
        return ValueBindings.get(this, "onselect", onselect);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public InputText() {
        setRendererType("org.openfaces.InputTextRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                dir,
                lang,
                alt,
                onselect,
                readonly,
                maxlength,
                size
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        dir = (String) values[i++];
        lang = (String) values[i++];
        alt = (String) values[i++];
        onselect = (String) values[i++];
        readonly = (Boolean) values[i++];
        maxlength = (Integer) values[i++];
        size = (Integer) values[i++];
    }

}
