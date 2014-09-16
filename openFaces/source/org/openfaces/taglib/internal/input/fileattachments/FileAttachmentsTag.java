/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.input.fileattachments;

import org.openfaces.component.input.fileattachments.FileAttachments;
import org.openfaces.event.FileAttachedEvent;
import org.openfaces.event.FileDownloadEvent;
import org.openfaces.event.FileRemovedEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FileAttachmentsTag extends AbstractComponentTag {

    public String getComponentType() {
        return FileAttachments.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.FileAttachmentsRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setObjectProperty(component, "value");
        setMethodExpressionProperty(facesContext, component, "fileDownloadListener",
                new Class[]{FileDownloadEvent.class}, void.class);
        setMethodExpressionProperty(facesContext, component, "fileRemovedListener",
                new Class[]{FileRemovedEvent.class}, void.class);
        setMethodExpressionProperty(facesContext, component, "fileAttachedListener",
                new Class[]{FileAttachedEvent.class}, void.class);
        setStringProperty(component, "var", false, true);
    }
}
