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

import org.openfaces.component.input.FileUploadMode;
import org.openfaces.component.input.MultipleFileUpload;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class MultipleFileUploadTag extends AbstractFileUploadTag {

    public String getComponentType() {
        return MultipleFileUpload.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.MultipleFileUploadRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "minQuantity");
        setIntProperty(component, "maxQuantity");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");

        setStringProperty(component, "uploadButtonText");
        setStringProperty(component, "removeAllButtonText");
        setStringProperty(component, "stopAllButtonText");
        setStringProperty(component, "removeButtonText");
        setStringProperty(component, "clearButtonText");

        setStringProperty(component, "allInfosStyle");
        setStringProperty(component, "allInfosClass");

        setBooleanProperty(component, "duplicateAllowed");

        setBooleanProperty(component, "autoUpload");
        setBooleanProperty(component, "multiple");
        setEnumerationProperty(component, "uploadMode", FileUploadMode.class);
        setStringProperty(component, "fileInfoRowStyle");
        setStringProperty(component, "fileInfoRowClass");
    }
}
