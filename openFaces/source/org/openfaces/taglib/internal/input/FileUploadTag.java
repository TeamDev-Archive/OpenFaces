/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.FileUpload;
import org.openfaces.event.FileUploadedEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FileUploadTag extends AbstractComponentTag {

    public String getComponentType() {
        return FileUpload.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.FileUploadRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "minQuantity");
        setIntProperty(component, "maxQuantity");
        setObjectProperty(component, "uploadedFiles");


        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");

        setStringProperty(component, "addButtonStyle");
        setStringProperty(component, "addButtonClass");
        setStringProperty(component, "addButtonLabel");

        setStringProperty(component, "addButtonOnMouseOverStyle");
        setStringProperty(component, "addButtonOnMouseOverClass");

        setStringProperty(component, "addButtonOnMouseDownStyle");
        setStringProperty(component, "addButtonOnMouseDownClass");

        setStringProperty(component, "addButtonOnFocusStyle");
        setStringProperty(component, "addButtonOnFocusClass");

        setStringProperty(component, "addButtonDisabledStyle");
        setStringProperty(component, "addButtonDisabledClass");

        setStringProperty(component, "allInfosStyle");
        setStringProperty(component, "allInfosClass");

        setStringProperty(component, "fileInfoStyle");
        setStringProperty(component, "fileInfoClass");

        setStringProperty(component, "infoTitleClass");
        setStringProperty(component, "infoTitleStyle");

        setStringProperty(component, "infoStatusClass");
        setStringProperty(component, "infoStatusStyle");
        setStringProperty(component, "statusNotUploadedText");
        setStringProperty(component, "statusUploadedText");
        setStringProperty(component, "statusInProgressText");
        setStringProperty(component, "maxFileSizeErrorText");

        setStringProperty(component, "acceptedFileTypes");
        setBooleanProperty(component, "duplicateAllowed");
        setBooleanProperty(component, "autoUpload");
        setBooleanProperty(component, "disabled");

        setStringProperty(component, "progressBarStyle");
        setStringProperty(component, "progressBarClass");
        setIntProperty(component, "tabindex");
        setMethodExpressionProperty(facesContext, component, "fileUploadedListener",
                new Class[]{FileUploadedEvent.class}, void.class);
    }
}
