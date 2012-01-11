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
import org.openfaces.event.UploadCompletionEvent;
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

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");

        setStringProperty(component, "browseButtonStyle");
        setStringProperty(component, "browseButtonClass");
        setStringProperty(component, "browseButtonText");

        setStringProperty(component, "browseButtonRolloverStyle");
        setStringProperty(component, "browseButtonRolloverClass");

        setStringProperty(component, "browseButtonPressedStyle");
        setStringProperty(component, "browseButtonPressedClass");

        setStringProperty(component, "browseButtonFocusedStyle");
        setStringProperty(component, "browseButtonFocusedClass");

        setStringProperty(component, "browseButtonDisabledStyle");
        setStringProperty(component, "browseButtonDisabledClass");

        setStringProperty(component, "allInfosStyle");
        setStringProperty(component, "allInfosClass");

        setStringProperty(component, "rowStyle");
        setStringProperty(component, "rowClass");

        setStringProperty(component, "fileNameStyle");
        setStringProperty(component, "fileNameClass");

        setStringProperty(component, "uploadStatusClass");
        setStringProperty(component, "uploadStatusStyle");
        setStringProperty(component, "notUploadedStatusText");
        setStringProperty(component, "uploadedStatusText");
        setStringProperty(component, "inProgressStatusText");
        setStringProperty(component, "stoppedStatusText");
        setStringProperty(component, "stoppingStatusText");
        setStringProperty(component, "fileSizeLimitErrorText");
        setStringProperty(component, "unexpectedErrorText");

        setStringProperty(component, "acceptedFileTypes");
        setBooleanProperty(component, "duplicateAllowed");
        setBooleanProperty(component, "autoUpload");
        setBooleanProperty(component, "disabled");

        setStringProperty(component, "progressBarStyle");
        setStringProperty(component, "progressBarClass");
        setIntProperty(component, "tabindex");
        setMethodExpressionProperty(facesContext, component, "fileUploadedListener",
                new Class[]{FileUploadedEvent.class}, void.class);
        setBooleanProperty(component, "multiple");
        setMethodExpressionProperty(facesContext, component, "uploadCompletionListener",
                new Class[]{UploadCompletionEvent.class}, void.class);
        setStringProperty(component, "onuploadstart");
        setStringProperty(component, "onuploadend");
    }
}
