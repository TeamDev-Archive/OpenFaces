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
package org.openfaces.component.output;

import org.openfaces.component.OUIOutput;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 *
 * The HintLabel component is used to display a single-line text that may not fit in the
 * allotted space, but when the user places mouse pointer over the truncated text, the
 * full text is displayed in a tool-tip.
 * 
 * @author Andrew Palval
 */
public class HintLabel extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.HintLabel";
    public static final String COMPONENT_FAMILY = "org.openfaces.HintLabel";

    private Boolean escape;
    private String title;
    private String hintStyle;
    private String hintClass;
    private String hint;
    private Integer hintTimeout;

    public HintLabel() {
        setRendererType("org.openfaces.HintLabelRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isEscape() {
        return ValueBindings.get(this, "escape", escape, true);
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public String getHintStyle() {
        return ValueBindings.get(this, "hintStyle", hintStyle);
    }

    public void setHintStyle(String hintStyle) {
        this.hintStyle = hintStyle;
    }

    public String getHintClass() {
        return ValueBindings.get(this, "hintClass", hintClass);
    }

    public void setHintClass(String hintClass) {
        this.hintClass = hintClass;
    }

    public int getHintTimeout() {
        return ValueBindings.get(this, "hintTimeout", hintTimeout, 100);
    }

    public void setHintTimeout(int hintTimeout) {
        this.hintTimeout = hintTimeout;
    }

    public String getHint() {
        return ValueBindings.get(this, "hint", hint);
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                hintStyle,
                hintClass,
                hintTimeout,
                hint,
                title,
                escape
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        hintStyle = (String) values[i++];
        hintClass = (String) values[i++];
        hintTimeout = (Integer) values[i++];
        hint = (String) values[i++];
        title = (String) values[i++];
        escape = (Boolean) values[i++];
    }
}
