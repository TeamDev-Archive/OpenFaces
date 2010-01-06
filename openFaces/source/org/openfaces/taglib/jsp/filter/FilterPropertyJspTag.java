/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.filter;

import org.openfaces.taglib.internal.filter.FilterPropertyTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Natalia Zolochevska
 */
public class FilterPropertyJspTag extends AbstractComponentJspTag {

    public FilterPropertyJspTag() {
        super(new FilterPropertyTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setName(ValueExpression name) {
        getDelegate().setPropertyValue("name", name);
    }

    public void setType(ValueExpression type) {
        getDelegate().setPropertyValue("type", type);
    }

    public void setDataProvider(ValueExpression dataProvider) {
        getDelegate().setPropertyValue("dataProvider", dataProvider);
    }

    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setMaxValue(ValueExpression maxValue) {
        getDelegate().setPropertyValue("maxValue", maxValue);
    }

    public void setMinValue(ValueExpression minValue) {
        getDelegate().setPropertyValue("minValue", minValue);
    }

    public void setStep(ValueExpression stepValue) {
        getDelegate().setPropertyValue("step", stepValue);
    }

    public void setPattern(ValueExpression pattern) {
        getDelegate().setPropertyValue("pattern", pattern);
    }

    public void setTimeZone(ValueExpression timeZone) {
        getDelegate().setPropertyValue("timeZone", timeZone);
    }

    public void setCaseSensitive(ValueExpression caseSensitive) {
        getDelegate().setPropertyValue("caseSensitive", caseSensitive);
    }


}