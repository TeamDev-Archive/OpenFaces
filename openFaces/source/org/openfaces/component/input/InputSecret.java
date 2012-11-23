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
package org.openfaces.component.input;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.component.OUIInputText;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Andre Shapovalov
 */
public class InputSecret extends OUIInputText {
    public static final String COMPONENT_TYPE = "org.openfaces.InputSecret";
    public static final String COMPONENT_FAMILY = "org.openfaces.InputSecret";

    private Integer maxlength;
    private Integer size;
    private Integer interval;
    private Integer duration;
    private String replacement;

    public InputSecret() {
        setRendererType("org.openfaces.InputSecretRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),
                maxlength,
                size,
                interval,
                duration,
                replacement
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        maxlength = (Integer) values[i++];
        size = (Integer) values[i++];
        interval = (Integer) values[i++];
        duration = (Integer) values[i++];
        replacement = (String) values[i++];

    }

    public int getSize() {
        return ValueBindings.get(this, "size", size, Integer.MIN_VALUE);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxlength() {
        return ValueBindings.get(this, "maxLength", maxlength, Integer.MIN_VALUE);
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public int getInterval() {
        return ValueBindings.get(this, "interval", interval, 0);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDuration() {
        return ValueBindings.get(this, "duration", duration, 0);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReplacement() {
        return ValueBindings.get(this, "replacement", replacement, "%u25CF");
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
