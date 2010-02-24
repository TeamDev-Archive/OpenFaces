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

package org.openfaces.renderkit.filter.param;

import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.input.DateChooser;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class DateChooserParametersEditor extends ParametersEditor implements Serializable {

    private static final String DATE_CHOOSER_ID_SUFFIX = "dateChooser";

    public DateChooserParametersEditor() {
    }

    public DateChooserParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        super(filterProperty, operation);
    }

    private DateChooser getDateChooser(UIComponent container) {
        return (DateChooser) Components.getChildBySuffix(container, DATE_CHOOSER_ID_SUFFIX);
    }

    private DateChooser createDateChooser(FacesContext context, UIComponent container) {
        clearContainer(container);
        DateChooser dateChooser = (DateChooser) Components.createChildComponent(context, container, DateChooser.COMPONENT_TYPE, DATE_CHOOSER_ID_SUFFIX);
        dateChooser.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        dateChooser.setStyle("width: 90px !important;");
        return dateChooser;
    }

    private void initDateChooser(DateChooser dateChooser) {
        dateChooser.setValue(criterion.getArg1());
        dateChooser.setTimeZone(filterProperty.getTimeZone());
        dateChooser.setPattern(filterProperty.getPattern());        
    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        DateChooser dateChooser = getDateChooser(container);
        if (dateChooser == null) {
            dateChooser = createDateChooser(context, container);
        }
        initDateChooser(dateChooser);
    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        DateChooser dateChooser = getDateChooser(container);
        if (dateChooser == null) {
            return;
        }
        criterion.setArg1(dateChooser.getValue());
        criterion.getParameters().put("timeZone", filterProperty.getTimeZone());
    }

}
