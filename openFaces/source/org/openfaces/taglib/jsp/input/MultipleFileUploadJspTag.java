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
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.MultipleFileUploadTag;

import javax.el.ValueExpression;

public class MultipleFileUploadJspTag extends AbstractFileUploadJspTag {

    public MultipleFileUploadJspTag() {
        super(new MultipleFileUploadTag());
    }

    public void setMaxQuantity(ValueExpression maxQuantity) {
        getDelegate().setPropertyValue("maxQuantity", maxQuantity);
    }

    public void setMinQuantity(ValueExpression minQuantity) {
        getDelegate().setPropertyValue("minQuantity", minQuantity);
    }

    public void setHeaderStyle(ValueExpression headerStyle) {
        getDelegate().setPropertyValue("headerStyle", headerStyle);
    }

    public void setHeaderClass(ValueExpression headerClass) {
        getDelegate().setPropertyValue("headerClass", headerClass);
    }

    public void setUploadButtonText(ValueExpression uploadButtonText) {
        getDelegate().setPropertyValue("uploadButtonText", uploadButtonText);
    }

    public void setRemoveAllButtonText(ValueExpression removeAllButtonText) {
        getDelegate().setPropertyValue("removeAllButtonText", removeAllButtonText);
    }

    public void setStopAllButtonText(ValueExpression stopAllButtonText) {
        getDelegate().setPropertyValue("stopAllButtonText", stopAllButtonText);
    }


    public void setRemoveButtonText(ValueExpression removeButtonText) {
        getDelegate().setPropertyValue("removeButtonText", removeButtonText);
    }

    public void setClearButtonText(ValueExpression clearButtonText) {
        getDelegate().setPropertyValue("clearButtonText", clearButtonText);
    }


    public void setAllInfosStyle(ValueExpression allInfosStyle) {
        getDelegate().setPropertyValue("allInfosStyle", allInfosStyle);
    }


    public void setAllInfosClass(ValueExpression allInfosClass) {
        getDelegate().setPropertyValue("allInfosClass", allInfosClass);
    }

    public void setDuplicateAllowed(ValueExpression duplicateAllowed) {
        getDelegate().setPropertyValue("duplicateAllowed", duplicateAllowed);
    }

    public void setAutoUpload(ValueExpression autoUpload) {
        getDelegate().setPropertyValue("autoUpload", autoUpload);
    }

    public void setMultiple(ValueExpression multiple) {
        getDelegate().setPropertyValue("multiple", multiple);
    }

    public void setUploadMode(ValueExpression uploadMode) {
        getDelegate().setPropertyValue("uploadMode", uploadMode);
    }
    public void setFileInfoRowStyle(ValueExpression fileInfoRowStyle) {
        getDelegate().setPropertyValue("fileInfoRowStyle", fileInfoRowStyle);
    }
    public void setFileInfoRowClass(ValueExpression fileInfoRowClass) {
        getDelegate().setPropertyValue("fileInfoRowClass", fileInfoRowClass);
    }
}
