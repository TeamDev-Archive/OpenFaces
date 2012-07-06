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
package org.openfaces.component.command;

import org.openfaces.component.OUICommand;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * This component is under construction. API is subject to change. Please avoid using this component in a production
 * environment.
 *
 * @author Dmitry Pikhulya
 */
public class CommandButton extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.CommandButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.CommandButton";

    private String type;
    private String accesskey;
    private String tabindex;
    private String lang;
    private String title;
    private String alt;
    private String dir;
    private String image;

    public CommandButton() {
        setRendererType("org.openfaces.CommandButtonRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                type,
                accesskey,
                tabindex,
                lang,
                title,
                alt,
                dir,
                image
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        type = (String) state[i++];
        accesskey = (String) state[i++];
        tabindex = (String) state[i++];
        lang = (String) state[i++];
        title = (String) state[i++];
        alt = (String) state[i++];
        dir = (String) state[i++];
        image = (String) state[i++];
    }

    public String getType() {
        return ValueBindings.get(this, "type", type, "submit");
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return ValueBindings.get(this, "title", title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlt() {
        return ValueBindings.get(this, "alt", alt);
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getImage() {
        return ValueBindings.get(this, "image", image);
    }

    public void setImage(String image) {
        this.image = image;
    }
}