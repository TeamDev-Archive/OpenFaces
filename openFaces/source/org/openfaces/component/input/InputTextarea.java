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
package org.openfaces.component.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Alexander Golubev
 */
public class InputTextarea extends OUIInputText {
    public static final String COMPONENT_TYPE = "org.openfaces.InputTextarea";
    public static final String COMPONENT_FAMILY = "org.openfaces.InputTextarea";

    private Integer rows;
    private Integer cols;
    private String dir;
    private String lang;
    private String onselect;
    private Boolean readonly;
    private Boolean autoGrowing;

    public InputTextarea() {
        setRendererType("org.openfaces.InputTextareaRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setAutoGrowing(boolean autoGrowing) {
        this.autoGrowing = autoGrowing;
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public String getOnselect() {
        return ValueBindings.get(this, "onselect", onselect);
    }

    public int getRows() {
        return ValueBindings.get(this, "rows", rows, Integer.MIN_VALUE);
    }

    public int getCols() {
        return ValueBindings.get(this, "cols", cols, Integer.MIN_VALUE);
    }

    public boolean isReadonly() {
        return ValueBindings.get(this, "readonly", readonly, false);
    }

    public boolean isAutoGrowing() {
        return ValueBindings.get(this, "autoGrowing", autoGrowing, false);
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                rows,
                cols,
                dir,
                lang,
                onselect,
                readonly
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        rows = (Integer) values[i++];
        cols = (Integer) values[i++];
        dir = (String) values[i++];
        lang = (String) values[i++];
        onselect = (String) values[i++];
        readonly = (Boolean) values[i++];
    }

}
