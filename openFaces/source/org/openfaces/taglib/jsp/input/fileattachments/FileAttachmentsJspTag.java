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
package org.openfaces.taglib.jsp.input.fileattachments;

import org.openfaces.taglib.internal.input.fileattachments.FileAttachmentsTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

public class FileAttachmentsJspTag extends AbstractComponentJspTag {

    public FileAttachmentsJspTag() {
        super(new FileAttachmentsTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setFileDownloadListener(MethodExpression fileDownloadListener) {
        getDelegate().setPropertyValue("fileDownloadListener", fileDownloadListener);
    }

    public void setFileRemovedListener(MethodExpression fileRemovedListener) {
        getDelegate().setPropertyValue("fileRemovedListener", fileRemovedListener);
    }

    public void setFileAttachedListener(MethodExpression fileAttachedListener) {
        getDelegate().setPropertyValue("fileAttachedListener", fileAttachedListener);
    }

    public void setVar(ValueExpression var) {
        getDelegate().setPropertyValue("var", var);
    }
}
