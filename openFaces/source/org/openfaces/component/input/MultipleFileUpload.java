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


public final class MultipleFileUpload extends AbstractFileUpload {
    public static final String COMPONENT_TYPE = "org.openfaces.MultipleFileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.MultipleFileUpload";

    private int minQuantity;
    private int maxQuantity;

    private String headerStyle;
    private String headerClass;

    private String uploadButtonText;
    private String removeAllButtonText;
    private String stopAllButtonText;
    private String removeButtonText;

    private String clearButtonText;

    private String allInfosStyle;
    private String allInfosClass;

    private boolean duplicateAllowed;
    private boolean autoUpload;

    private boolean multiple;

    private FileUploadMode uploadMode;

    private String fileInfoRowStyle;
    private String fileInfoRowClass;

    public MultipleFileUpload() {
        setRendererType("org.openfaces.MultipleFileUploadRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                minQuantity,
                maxQuantity,
                headerStyle,
                headerClass,
                uploadButtonText,
                removeAllButtonText,
                stopAllButtonText,
                removeButtonText,
                clearButtonText,
                allInfosStyle,
                allInfosClass,
                duplicateAllowed,
                multiple,
                uploadMode,
                fileInfoRowStyle,
                fileInfoRowClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        minQuantity = (Integer) values[i++];
        maxQuantity = (Integer) values[i++];
        headerStyle = (String) values[i++];
        headerClass = (String) values[i++];
        uploadButtonText = (String) values[i++];
        removeAllButtonText = (String) values[i++];
        stopAllButtonText = (String) values[i++];
        removeButtonText = (String) values[i++];
        clearButtonText = (String) values[i++];
        allInfosStyle = (String) values[i++];
        allInfosClass = (String) values[i++];
        duplicateAllowed = (Boolean) values[i++];
        multiple = (Boolean) values[i++];
        uploadMode = (FileUploadMode) values[i++];
        fileInfoRowStyle = (String) values[i++];
        fileInfoRowClass = (String) values[i++];
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getAllInfosStyle() {
        return ValueBindings.get(this, "allInfosStyle", allInfosStyle);
    }

    public void setAllInfosStyle(String allInfosStyle) {
        this.allInfosStyle = allInfosStyle;
    }

    public String getAllInfosClass() {
        return ValueBindings.get(this, "allInfosClass", allInfosClass);
    }

    public void setAllInfosClass(String allInfosClass) {
        this.allInfosClass = allInfosClass;
    }

    public int getMinQuantity() {
        return ValueBindings.get(this, "minQuantity", minQuantity, 1);
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return ValueBindings.get(this, "maxQuantity", maxQuantity, 0);
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isDuplicateAllowed() {
        return ValueBindings.get(this, "duplicateAllowed", duplicateAllowed, true);
    }

    public void setDuplicateAllowed(boolean duplicateAllowed) {
        this.duplicateAllowed = duplicateAllowed;
    }

    public boolean isAutoUpload() {
        return ValueBindings.get(this, "autoUpload", autoUpload, false);
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public String getUploadButtonText() {
        return ValueBindings.get(this, "uploadButtonText", uploadButtonText, "Upload");
    }

    public void setUploadButtonText(String uploadButtonText) {
        this.uploadButtonText = uploadButtonText;
    }

    public String getRemoveAllButtonText() {
        return ValueBindings.get(this, "removeAllButtonText", removeAllButtonText, "Remove all");
    }

    public void setRemoveAllButtonText(String removeAllButtonText) {
        this.removeAllButtonText = removeAllButtonText;
    }

    public String getStopAllButtonText() {
        return ValueBindings.get(this, "stopAllButtonText", stopAllButtonText, "Stop all");
    }

    public void setStopAllButtonText(String stopAllButtonText) {
        this.stopAllButtonText = stopAllButtonText;
    }

    public String getRemoveButtonText() {
        return ValueBindings.get(this, "removeButtonText", removeButtonText, "Remove");
    }

    public void setRemoveButtonText(String removeButtonText) {
        this.removeButtonText = removeButtonText;
    }

    public String getClearButtonText() {
        return ValueBindings.get(this, "clearButtonText", clearButtonText, "Clear");
    }

    public void setClearButtonText(String clearButtonText) {
        this.clearButtonText = clearButtonText;
    }

    public boolean isMultiple() {
        return true;
    }

    protected void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public FileUploadMode getUploadMode() {
        return ValueBindings.get(this, "uploadMode", uploadMode, FileUploadMode.PARALLEL, FileUploadMode.class);
    }

    public void setUploadMode(FileUploadMode uploadMode) {
        this.uploadMode = uploadMode;
    }

    @Override
    public String getDropTargetText() {
        return ValueBindings.get(this, "dropTargetText", dropTargetText);
    }

    @Override
    public String getBrowseButtonText() {
        return ValueBindings.get(this, "browseButtonText", browseButtonText);
    }

    public String getFileInfoRowStyle() {
        return ValueBindings.get(this, "fileInfoRowStyle", fileInfoRowStyle);
    }

    public void setFileInfoRowStyle(String fileInfoRowStyle) {
        this.fileInfoRowStyle = fileInfoRowStyle;
    }

    public String getFileInfoRowClass() {
        return ValueBindings.get(this, "fileInfoRowClass", fileInfoRowClass);
    }

    public void setFileInfoRowClass(String fileInfoRowClass) {
        this.fileInfoRowClass = fileInfoRowClass;
    }

}
