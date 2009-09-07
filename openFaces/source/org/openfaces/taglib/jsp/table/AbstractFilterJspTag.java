/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class AbstractFilterJspTag extends AbstractComponentJspTag {
    public AbstractFilterJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setExpression(ValueExpression expression) {
        getDelegate().setPropertyValue("expression", expression);
    }

    public void setCriterion(ValueExpression criterion) {
        getDelegate().setPropertyValue("criterion", criterion);
    }

    public void setOptions(ValueExpression options) {
        getDelegate().setPropertyValue("options", options);
    }

    public void setAllRecordsText(ValueExpression allRecordsText) {
        getDelegate().setPropertyValue("allRecordsText", allRecordsText);
    }

    public void setEmptyRecordsText(ValueExpression emptyRecordsText) {
        getDelegate().setPropertyValue("emptyRecordsText", emptyRecordsText);
    }

    public void setNonEmptyRecordsText(ValueExpression nonEmptyRecordsText) {
        getDelegate().setPropertyValue("nonEmptyRecordsText", nonEmptyRecordsText);
    }

    public void setPromptText(ValueExpression promptText) {
        getDelegate().setPropertyValue("promptText", promptText);
    }

    public void setPromptTextStyle(ValueExpression promptTextStyle) {
        getDelegate().setPropertyValue("promptTextStyle", promptTextStyle);
    }

    public void setPromptTextClass(ValueExpression promptTextClass) {
        getDelegate().setPropertyValue("promptTextClass", promptTextClass);
    }
}
