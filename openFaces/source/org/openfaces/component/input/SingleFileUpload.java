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


public final class SingleFileUpload extends AbstractFileUpload {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleFileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleFileUpload";

    private SingleFileUploadLayoutMode layoutMode;
    private Boolean backToFirstScreen;
    private SingleFileUploadBtnBehavior whatToDoWithUploadOnUploading;
    private Boolean showStopButtonNearProgress;

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
                backToFirstScreen,
                whatToDoWithUploadOnUploading,
                showStopButtonNearProgress
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        layoutMode = (SingleFileUploadLayoutMode) values[i++];
        backToFirstScreen = (Boolean) values[i++];
        whatToDoWithUploadOnUploading = (SingleFileUploadBtnBehavior) values[i++];
        showStopButtonNearProgress = (Boolean) values[i++];
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

    public Boolean getBackToFirstScreen() {
        return ValueBindings.get(this, "backToFirstScreen", backToFirstScreen, Boolean.class);
    }

    public void setBackToFirstScreen(Boolean backToFirstScreen) {
        this.backToFirstScreen = backToFirstScreen;
    }

    public SingleFileUploadBtnBehavior getWhatToDoWithUploadOnUploading() {
        return ValueBindings.get(this, "whatToDoWithUploadOnUploading", whatToDoWithUploadOnUploading, SingleFileUploadBtnBehavior.class);
    }

    public void setWhatToDoWithUploadOnUploading(SingleFileUploadBtnBehavior whatToDoWithUploadOnUploading) {
        this.whatToDoWithUploadOnUploading = whatToDoWithUploadOnUploading;
    }

    public Boolean getShowStopButtonNearProgress() {
        return ValueBindings.get(this, "showStopButtonNearProgress", showStopButtonNearProgress,Boolean.class);
    }

    public void setShowStopButtonNearProgress(Boolean showStopButtonNearProgress) {
        this.showStopButtonNearProgress = showStopButtonNearProgress;
    }
}
