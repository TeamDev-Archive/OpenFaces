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
package org.openfaces.component.timetable;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class CustomEventEditor extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.CustomEventEditor";
    public static final String COMPONENT_FAMILY = "org.openfaces.CustomEventEditor";

    private String oncreate;
    private String onedit;

    public CustomEventEditor() {
        setRendererType("org.openfaces.CustomEventEditorRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getOncreate() {
        return ValueBindings.get(this, "oncreate", oncreate);
    }

    public void setOncreate(String oncreate) {
        this.oncreate = oncreate;
    }

    public String getOnedit() {
        return ValueBindings.get(this, "onedit", onedit);
    }

    public void setOnedit(String onedit) {
        this.onedit = onedit;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                oncreate,
                onedit
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArr = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArr[i++]);
        oncreate = (String) stateArr[i++];
        onedit = (String) stateArr[i++];
    }
}
