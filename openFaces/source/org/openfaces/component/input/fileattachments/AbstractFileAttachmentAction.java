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

package org.openfaces.component.input.fileattachments;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIInputBase;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author andrii.loboda
 */
public abstract class AbstractFileAttachmentAction extends OUIInputBase implements OUIClientAction {

    private String event;
    private String _for;
    private Boolean standalone;
    private FileAttachments fileAttachmentsComponent;
    private FileAttachment fileAttachment;

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                event,
                _for,
                standalone,
                fileAttachmentsComponent,
                fileAttachment
        };
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        event = (String) values[i++];
        _for = (String) values[i++];
        standalone = (Boolean) values[i++];
        fileAttachmentsComponent = (FileAttachments) values[i++];
        fileAttachment = (FileAttachment) values[i++];
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", event, "click");
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String aFor) {
        _for = aFor;
    }

    public boolean isStandalone() {
        return ValueBindings.get(this, "standalone", standalone, false);
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    /*This method is not designed for public use*/
    public FileAttachments getFileAttachmentsComponent() {
        return fileAttachmentsComponent;
    }

    /*This method is not designed for public use*/
    public void setFileAttachmentsComponent(FileAttachments fileAttachmentsComponent) {
        this.fileAttachmentsComponent = fileAttachmentsComponent;
    }

    /*This method is not designed for public use*/
    public FileAttachment getFileAttachment() {
        return fileAttachment;
    }

    /*This method is not designed for public use*/
    public void setFileAttachment(FileAttachment fileAttachment) {
        this.fileAttachment = fileAttachment;
    }
}
