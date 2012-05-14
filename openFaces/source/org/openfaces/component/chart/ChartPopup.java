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
package org.openfaces.component.chart;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.util.ValueBindings;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;

@ResourceDependencies({
        @ResourceDependency(name = "jsf.js", library = "javax.faces"),
        @ResourceDependency(name = "default.css", library = "openfaces")
})
public class ChartPopup extends PopupLayer {
    public static final String COMPONENT_TYPE = "org.openfaces.ChartPopup";
    public static final String COMPONENT_FAMILY = "org.openfaces.ChartPopup";
    private LoadingMode loadingMode;

    public ChartPopup() {
        setRendererType("org.openfaces.ChartPopupRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getClientId(FacesContext context) {
        return super.getClientId(context);
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, loadingMode)
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        loadingMode = (LoadingMode) restoreAttachedState(context, state[i++]);
    }

    public LoadingMode getLoadingMode() {
        return ValueBindings.get(this, "loadingMode", loadingMode, LoadingMode.CLIENT, LoadingMode.class);
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }
}
