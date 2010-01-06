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
package org.openfaces.ajax.plugins;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxPluginIncludes {
    private List<String> jsIncludes = new ArrayList<String>();
    private List<String> cssIncludes = new ArrayList<String>();
    private List<String> scripts = new ArrayList<String>();

    public List<String> getJsIncludes() {
        // TODO [sanders] (Apr 1, 2009, 6:44 AM): return unmodifiable?
        return jsIncludes;
    }

    public void setJsIncludes(List<String> jsIncludes) {
        // TODO [sanders] (Apr 1, 2009, 6:44 AM): clear and copy?
        this.jsIncludes = jsIncludes;
    }

    public List<String> getCssIncludes() {
        // TODO [sanders] (Apr 1, 2009, 6:44 AM): return unmodifiable?
        return cssIncludes;
    }

    public void setCssIncludes(List<String> cssIncludes) {
        // TODO [sanders] (Apr 1, 2009, 6:44 AM): clear and copy?
        this.cssIncludes = cssIncludes;
    }

    public List<String> getScripts() {
        // TODO [sanders] (Apr 1, 2009, 6:44 AM): view?
        return scripts;
    }

    public void setScripts(List<String> scripts) {
        // TODO [sanders] (Apr 1, 2009, 6:45 AM): clear and copy?
        this.scripts = scripts;
    }
}
