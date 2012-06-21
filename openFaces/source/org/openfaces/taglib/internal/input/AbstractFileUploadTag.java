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

import org.openfaces.event.FileUploadedEvent;
import org.openfaces.event.UploadCompletionEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public abstract class AbstractFileUploadTag extends AbstractComponentTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

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

        setStringProperty(component, "stopButtonText");

        setStringProperty(component, "dropTargetStyle");
        setStringProperty(component, "dropTargetClass");
        setStringProperty(component, "dropTargetDragoverStyle");
        setStringProperty(component, "dropTargetDragoverClass");

        setStringProperty(component, "dropTargetText");

        setStringProperty(component, "fileNameStyle");
        setStringProperty(component, "fileNameClass");

        setStringProperty(component, "statusClass");
        setStringProperty(component, "statusStyle");
        setStringProperty(component, "notUploadedStatusText");
        setStringProperty(component, "uploadedStatusText");
        setStringProperty(component, "inProgressStatusText");
        setStringProperty(component, "stoppedStatusText");
        setStringProperty(component, "stoppingStatusText");
        setStringProperty(component, "fileSizeLimitErrorText");
        setStringProperty(component, "unexpectedErrorText");

        setStringProperty(component, "acceptedFileTypes");

        setBooleanProperty(component, "disabled");

        setStringProperty(component, "progressBarStyle");
        setStringProperty(component, "progressBarClass");
        setIntProperty(component, "tabindex");
        setMethodExpressionProperty(facesContext, component, "fileUploadedListener",
                new Class[]{FileUploadedEvent.class}, void.class);

        setMethodExpressionProperty(facesContext, component, "completionListener",
                new Class[]{UploadCompletionEvent.class}, void.class);
        setStringProperty(component, "onstart");
        setStringProperty(component, "onend");
        setStringProperty(component, "onfilestart");
        setStringProperty(component, "onfileinprogress");
        setStringProperty(component, "onfileend");
        setStringProperty(component, "onwrongfiletype");
        setStringProperty(component, "ondirectorydropped");

        setIntProperty(component, "fileSizeLimit");
        setLiteralCollectionProperty(component, "render");
        setStringProperty(component, "externalDropTarget");
        setStringProperty(component, "acceptedMimeTypes");
        setStringProperty(component, "directoryDroppedText");
        setStringProperty(component, "wrongFileTypeText");
        setObjectProperty(component, "externalBrowseButton");
        setBooleanProperty(component, "showInPopup");
        setObjectProperty(component, "closeButtonText");
    }
}
