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
package org.openfaces.component.util;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class KeepVisible extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.KeepVisible";
    public static final String COMPONENT_FAMILY = "org.openfaces.KeepVisible";
    private UIComponent forElement;
    private String topMargin;
    private String bottomMargin;
    private String _for;

    public KeepVisible() {
        setRendererType("org.openfaces.KeepVisibleRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                topMargin,
                bottomMargin,
                _for
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        topMargin = (String) state[i++];
        bottomMargin = (String) state[i++];
        _for = (String) state[i];

    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ScriptBuilder scriptBuilder = new ScriptBuilder();
        scriptBuilder.initScript(context, this, "O$.KeepVisible._init",
                (getForElement() != null) ? getForElement() : getFor(),
                getTopMargin(),
                getBottomMargin());

        Rendering.renderInitScript(context, scriptBuilder,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "util/keepVisible.js"));
    }

    public String getTopMargin() {
        return ValueBindings.get(this, "topMargin", topMargin);
    }

    public void setTopMargin(String topMargin) {
        this.topMargin = topMargin;
    }

    public String getBottomMargin() {
        return ValueBindings.get(this, "bottomMargin", bottomMargin);
    }

    public void setBottomMargin(String bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    private UIComponent getForElement() {
        if (forElement == null) {
            String aFor = getFor();
            if (aFor != null) {
                UIComponent referredComponent = Components.referenceIdToComponent(this, aFor);
                forElement = referredComponent;
            } else {
                forElement = null;
            }
        }
        return forElement;
    }
}
