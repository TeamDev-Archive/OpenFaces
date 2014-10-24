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
package org.openfaces.component.input;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;


public final class SingleFileUpload extends AbstractFileUpload {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleFileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleFileUpload";

    private SingleFileUploadLayoutMode layoutMode;
    private Boolean showInfoAfterUpload;
    private SingleFileUploadBtnBehavior browseButtonDuringUpload;
    private Boolean stopButtonNearProgress;
    
    private String fileInfoAreaStyle;
    private String fileInfoAreaClass;

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
                layoutMode,
                showInfoAfterUpload,
                browseButtonDuringUpload,
                stopButtonNearProgress,
                fileInfoAreaStyle,
                fileInfoAreaClass,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        layoutMode = (SingleFileUploadLayoutMode) values[i++];
        showInfoAfterUpload = (Boolean) values[i++];
        browseButtonDuringUpload = (SingleFileUploadBtnBehavior) values[i++];
        stopButtonNearProgress = (Boolean) values[i++];
        fileInfoAreaStyle = (String) values[i++];
        fileInfoAreaClass = (String) values[i++];
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

    public Boolean getShowInfoAfterUpload() {
        return ValueBindings.get(this, "showInfoAfterUpload", showInfoAfterUpload, Boolean.class);
    }

    public void setShowInfoAfterUpload(Boolean showInfoAfterUpload) {
        this.showInfoAfterUpload = showInfoAfterUpload;
    }

    public SingleFileUploadBtnBehavior getBrowseButtonDuringUpload() {
        return ValueBindings.get(this, "browseButtonDuringUpload", browseButtonDuringUpload, SingleFileUploadBtnBehavior.class);
    }

    public void setBrowseButtonDuringUpload(SingleFileUploadBtnBehavior browseButtonDuringUpload) {
        this.browseButtonDuringUpload = browseButtonDuringUpload;
    }

    public Boolean getStopButtonNearProgress() {
        return ValueBindings.get(this, "stopButtonNearProgress", stopButtonNearProgress,Boolean.class);
    }

    public void setStopButtonNearProgress(Boolean stopButtonNearProgress) {
        this.stopButtonNearProgress = stopButtonNearProgress;
    }

    public String getFileInfoAreaStyle() {
        return ValueBindings.get(this, "fileInfoAreaStyle", fileInfoAreaStyle);
    }

    public void setFileInfoAreaStyle(String fileInfoAreaStyle) {
        this.fileInfoAreaStyle = fileInfoAreaStyle;
    }

    public String getFileInfoAreaClass() {
        return ValueBindings.get(this, "fileInfoAreaClass", fileInfoAreaClass);
    }

    public void setFileInfoAreaClass(String fileInfoAreaClass) {
        this.fileInfoAreaClass = fileInfoAreaClass;
    }
}
