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
package org.openfaces.component;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class OUIInputText extends OUIInputBase {
    // todo: add "lang" attribute as it is a standard attribute for JSF input components
    // todo: add "dir" attribute as it is a standard attribute for JSF input components
    // todo: add "onselect" attribute as it is a standard attribute for JSF input components
    private String promptText;
    private String promptTextStyle;
    private String promptTextClass;
    private String accesskey;
    private String tabindex;
    private String title;

    public String getPromptText() {
        return ValueBindings.get(this, "promptText", promptText);
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptTextStyle() {
        return ValueBindings.get(this, "promptTextStyle", promptTextStyle);
    }

    public void setPromptTextStyle(String promptTextStyle) {
        this.promptTextStyle = promptTextStyle;
    }

    public String getPromptTextClass() {
        return ValueBindings.get(this, "promptTextClass", promptTextClass);
    }

    public void setPromptTextClass(String promptTextClass) {
        this.promptTextClass = promptTextClass;
    }

    public String getTitle() {
        return ValueBindings.get(this, "title", title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccesskey() {
        return ValueBindings.get(this, "accesskey", accesskey);
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getTabindex() {
        return ValueBindings.get(this, "tabindex", tabindex);
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                promptText,
                promptTextStyle,
                promptTextClass,


                accesskey,
                tabindex,
                title
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        promptText = (String) values[i++];
        promptTextStyle = (String) values[i++];
        promptTextClass = (String) values[i++];

        accesskey = (String) values[i++];
        tabindex = (String) values[i++];
        title = (String) values[i++];
    }
}
