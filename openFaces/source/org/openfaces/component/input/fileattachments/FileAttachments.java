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

import org.openfaces.component.OUIObjectIteratorBase;
import org.openfaces.event.FileAttachedListener;
import org.openfaces.event.FileDownloadListener;
import org.openfaces.event.FileRemovedListener;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/*todo: fileUpload component as a facet is necessary now - create fileUpload component by default - see compound component
* todo: indexes for children of this component is not correct - try to use Iterator setObjectId and look on method resetChildrenIds()
* todo: when page is  reloading, abstractAttachmentAction doesn't save state fileAttachments and fileAttachment in AbstractFileAttachmentAction
* todo: make the size to display in more pleasant form  - like in fileupload.
* todo: */
public final class FileAttachments extends OUIObjectIteratorBase {
    public static final String COMPONENT_TYPE = "org.openfaces.FileAttachments";
    public static final String COMPONENT_FAMILY = "org.openfaces.FileAttachments";
    private List<FileAttachment> value;
    private MethodExpression fileDownloadListener;
    private MethodExpression fileRemovedListener;
    private MethodExpression fileAttachedListener;
    private String var;
    private String objectId;
    private String immutableClientId;

    public FileAttachments() {
        setRendererType("org.openfaces.FileAttachmentsRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                value,
                saveAttachedState(context, fileDownloadListener),
                saveAttachedState(context, fileRemovedListener),
                saveAttachedState(context, fileAttachedListener),
                var
        };
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        value = (List<FileAttachment>) values[i++];
        fileDownloadListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        fileRemovedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        fileAttachedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        var = (String) values[i++];
    }

    @SuppressWarnings(value = "unchecked")
    public List<FileAttachment> getValue() {
        return ValueBindings.get(this, "value", value, Collections.emptyList(), false, List.class);
    }

    public void setValue(List<FileAttachment> value) {
        this.value = value;
    }

    public MethodExpression getFileDownloadListener() {
        return fileDownloadListener;
    }

    public void setFileDownloadListener(MethodExpression fileDownloadListener) {
        this.fileDownloadListener = fileDownloadListener;
    }

    public void addFileDownloadListener(FileDownloadListener fileDownloadListener) {
        addFacesListener(fileDownloadListener);
    }

    public FileDownloadListener[] getFileDownloadListeners() {
        return (FileDownloadListener[]) getFacesListeners(FileDownloadListener.class);
    }

    public void removeFileDownloadListener(FileDownloadListener fileDownloadListener) {
        removeFacesListener(fileDownloadListener);
    }

    public MethodExpression getFileRemovedListener() {
        return fileRemovedListener;
    }

    public void setFileRemovedListener(MethodExpression fileRemovedListener) {
        this.fileRemovedListener = fileRemovedListener;
    }

    public void addFileRemovedListener(FileRemovedListener fileRemovedListener) {
        addFacesListener(fileRemovedListener);
    }

    public FileRemovedListener[] getFileRemovedListeners() {
        return (FileRemovedListener[]) getFacesListeners(FileRemovedListener.class);
    }

    public void removeFileRemovedListener(FileRemovedListener fileRemovedListener) {
        removeFacesListener(fileRemovedListener);
    }

    public MethodExpression getFileAttachedListener() {
        return fileAttachedListener;
    }

    public void setFileAttachedListener(MethodExpression fileAttachedListener) {
        this.fileAttachedListener = fileAttachedListener;
    }

    public void addFileAttachedListener(FileAttachedListener fileAttachedListener) {
        addFacesListener(fileAttachedListener);
    }

    public FileAttachedListener[] getFileAttachedListeners() {
        return (FileAttachedListener[]) getFacesListeners(FileAttachedListener.class);
    }

    public void removeFileAttachedListener(FileAttachedListener fileAttachedListener) {
        removeFacesListener(fileAttachedListener);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }


    public String getImmutableClientId() {
        return immutableClientId;
    }

    public void setImmutableClientId(String immutableClientId) {
        this.immutableClientId = immutableClientId;
    }
}
