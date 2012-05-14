/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.filter;

import org.openfaces.component.input.InputText;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class InputTextFilter extends TextSearchFilter {
    public static final String COMPONENT_TYPE = "org.openfaces.InputTextFilter";
    public static final String COMPONENT_FAMILY = "org.openfaces.InputTextFilter";

    private String dir;
    private String lang;
    private String alt;
    private String autocomplete;

    public InputTextFilter() {
        setRendererType("org.openfaces.InputTextFilterRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    protected String getInputComponentType() {
        return InputText.COMPONENT_TYPE;
    }

    public String getAutocomplete() {
        return ValueBindings.get(this, "autocomplete", autocomplete);
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getAlt() {
        return ValueBindings.get(this, "alt", alt);
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                dir,
                lang,
                alt,
                autocomplete
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        dir = (String) values[i++];
        lang = (String) values[i++];
        alt = (String) values[i++];
        autocomplete = (String) values[i++];
    }


}
