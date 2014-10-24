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

public class CommandLink extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.CommandLink";
    public static final String COMPONENT_FAMILY = "org.openfaces.CommandLink";

    private String accesskey;
    private String tabindex;
    private String lang;
    private String title;
    private String dir;
    private String charset;
    private String coords;
    private String hreflang;
    private String rel;
    private String rev;
    private String shape;
    private String target;
    private String type;

    public CommandLink() {
        setRendererType("org.openfaces.CommandLinkRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                accesskey,
                tabindex,
                lang,
                title,
                dir,
                charset,
                coords,
                hreflang,
                rel,
                rev,
                shape,
                target,
                type
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        accesskey = (String) state[i++];
        tabindex = (String) state[i++];
        lang = (String) state[i++];
        title = (String) state[i++];
        dir = (String) state[i++];
        charset = (String) state[i++];
        coords = (String) state[i++];
        hreflang = (String) state[i++];
        rel = (String) state[i++];
        rev = (String) state[i++];
        shape = (String) state[i++];
        target = (String) state[i++];
        type = (String) state[i++];
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

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getCharset() {
        return ValueBindings.get(this, "charset", charset);
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCoords() {
        return ValueBindings.get(this, "coords", coords);
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getHreflang() {
        return ValueBindings.get(this, "hreflang", hreflang);
    }

    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    public String getRel() {
        return ValueBindings.get(this, "rel", rel);
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRev() {
        return ValueBindings.get(this, "rev", rev);
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getShape() {
        return ValueBindings.get(this, "shape", shape);
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getTarget() {
        return ValueBindings.get(this, "target", target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return ValueBindings.get(this, "type", type);
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getActionTriggerParam() {
        return getClientId(getFacesContext()) + "::clicked";
    }
}
