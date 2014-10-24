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

import org.openfaces.taglib.internal.input.SingleFileUploadTag;

import javax.el.ValueExpression;

public class SingleFileUploadJspTag extends AbstractFileUploadJspTag{

    public SingleFileUploadJspTag() {
        super(new SingleFileUploadTag());
    }
    public void setLayoutMode(ValueExpression layoutMode) {
        getDelegate().setPropertyValue("layoutMode", layoutMode);
    }
    public void setShowInfoAfterUpload(ValueExpression showInfoAfterUpload) {
        getDelegate().setPropertyValue("showInfoAfterUpload", showInfoAfterUpload);
    }
    public void setBrowseButtonDuringUpload(ValueExpression browseButtonDuringUpload) {
        getDelegate().setPropertyValue("browseButtonDuringUpload", browseButtonDuringUpload);
    }
    public void setStopButtonNearProgress(ValueExpression stopButtonNearProgress) {
        getDelegate().setPropertyValue("stopButtonNearProgress", stopButtonNearProgress);
    }
    public void setFileInfoAreaStyle(ValueExpression fileInfoAreaStyle) {
        getDelegate().setPropertyValue("fileInfoAreaStyle", fileInfoAreaStyle);
    }
    public void setFileInfoAreaClass(ValueExpression fileInfoAreaClass) {
        getDelegate().setPropertyValue("fileInfoAreaClass", fileInfoAreaClass);
    }
}
