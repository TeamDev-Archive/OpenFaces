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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.SingleFileUpload;
import org.openfaces.component.input.SingleFileUploadBtnBehavior;
import org.openfaces.component.input.SingleFileUploadLayoutMode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class SingleFileUploadTag extends AbstractFileUploadTag {

    public String getComponentType() {
        return SingleFileUpload.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.SingleFileUploadRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setEnumerationProperty(component, "layoutMode", SingleFileUploadLayoutMode.class);
        setBooleanProperty(component, "showInfoAfterUpload");
        setEnumerationProperty(component, "browseButtonDuringUpload", SingleFileUploadBtnBehavior.class);
        setBooleanProperty(component, "stopButtonNearProgress");

        setStringProperty(component, "fileInfoAreaStyle");
        setStringProperty(component, "fileInfoAreaClass");
    }
}
