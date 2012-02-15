/*
 * OpenFaces - JSF Component Library 2.0
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

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;


public class SingleFileUpload extends AbstractFileUpload {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleFileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleFileUpload";

    private SingleFileUploadLayoutMode layoutMode;

    public SingleFileUpload() {
        setRendererType("org.openfaces.SingleFileUploadRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                layoutMode
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        layoutMode = (SingleFileUploadLayoutMode) values[i++];
    }

    @Override
    public String getDropTargetText() {
        return ValueBindings.get(this, "dropTargetText", dropTargetText);
    }

    @Override
    public String getBrowseButtonText() {
        return ValueBindings.get(this, "browseButtonText", browseButtonText);
    }

    public SingleFileUploadLayoutMode getLayoutMode() {
        return ValueBindings.get(this, "layoutMode", layoutMode, SingleFileUploadLayoutMode.FULL, SingleFileUploadLayoutMode.class);
    }

    public void setLayoutMode(SingleFileUploadLayoutMode layoutMode) {
        this.layoutMode = layoutMode;
    }
}
