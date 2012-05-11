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
package org.openfaces.taglib.jsp.output;

import javax.el.ValueExpression;

public class ProgressBarJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public ProgressBarJspTag() {
        super(new org.openfaces.taglib.internal.output.ProgressBarTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setProgressStyle(ValueExpression progressStyle) {
        getDelegate().setPropertyValue("progressStyle", progressStyle);
    }

    public void setProgressClass(ValueExpression progressClass) {
        getDelegate().setPropertyValue("progressClass", progressClass);
    }

    public void setNotProcessedStyle(ValueExpression notProcessedStyle) {
        getDelegate().setPropertyValue("notProcessedStyle", notProcessedStyle);
    }

    public void setNotProcessedClass(ValueExpression notProcessedClass) {
        getDelegate().setPropertyValue("notProcessedClass", notProcessedClass);
    }
    public void setLabelStyle(ValueExpression labelStyle) {
        getDelegate().setPropertyValue("labelStyle", labelStyle);
    }
    public void setLabelClass(ValueExpression labelClass) {
        getDelegate().setPropertyValue("labelClass", labelClass);
    }
    public void setLabelFormat(ValueExpression labelFormat) {
        getDelegate().setPropertyValue("labelFormat", labelFormat);
    }

    public void setLabelAlignment(ValueExpression labelAlignment) {
        getDelegate().setPropertyValue("labelAlignment", labelAlignment);
    }

    public void setProcessedImg(ValueExpression processedImg) {
        getDelegate().setPropertyValue("processedImg", processedImg);
    }
    public void setNotProcessedImg(ValueExpression notProcessedImg) {
        getDelegate().setPropertyValue("notProcessedImg", notProcessedImg);
    }
}
