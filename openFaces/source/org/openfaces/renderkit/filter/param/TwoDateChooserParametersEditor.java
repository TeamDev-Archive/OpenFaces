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

package org.openfaces.renderkit.filter.param;

import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.filter.OperationType;
import org.openfaces.component.input.DateChooser;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TwoDateChooserParametersEditor extends ParametersEditor implements Serializable {

    private static final String DATE_CHOOSER_BEFORE_ID_SUFFIX = "dateChooserBefore";
    private static final String DATE_CHOOSER_AFTER_ID_SUFFIX = "dateChooserAfter";

    public TwoDateChooserParametersEditor() {
    }

    public TwoDateChooserParametersEditor(FilterProperty filterProperty, OperationType operation) {
        super(filterProperty, operation);
    }

    private DateChooser getDateChooserBefore(FacesContext context, UIComponent container) {
        return (DateChooser) ComponentUtil.getChildBySuffix(container, DATE_CHOOSER_BEFORE_ID_SUFFIX);
    }

    private DateChooser createDateChooserBefore(FacesContext context, UIComponent container) {
        clearContainer(container);
        DateChooser dateChooser = (DateChooser) ComponentUtil.createChildComponent(context, container, DateChooser.COMPONENT_TYPE, DATE_CHOOSER_BEFORE_ID_SUFFIX);
        dateChooser.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        dateChooser.setStyle("width: 90px !important;");
        return dateChooser;
    }

    private void initDateChooserBefore(FacesContext context, DateChooser dateChooser) {
        dateChooser.setValue(criterion.getParameter());
    }

    private DateChooser getDateChooserAfter(FacesContext context, UIComponent container) {
        return (DateChooser) ComponentUtil.getChildBySuffix(container, DATE_CHOOSER_AFTER_ID_SUFFIX);
    }

    private DateChooser createDateChooserAfter(FacesContext context, UIComponent container) {
        DateChooser dateChooser = (DateChooser) ComponentUtil.createChildComponent(context, container, DateChooser.COMPONENT_TYPE, DATE_CHOOSER_AFTER_ID_SUFFIX);
        dateChooser.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        dateChooser.setStyle("width: 90px !important;");
        return dateChooser;
    }

    private void initDateChooserAfter(FacesContext context, DateChooser dateChooser) {
        dateChooser.setValue(criterion.getParameter(1));
    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        DateChooser dateChooserBefore = getDateChooserBefore(context, container);
        if (dateChooserBefore == null) {
            dateChooserBefore = createDateChooserBefore(context, container);
        }
        initDateChooserBefore(context, dateChooserBefore);

        DateChooser dateChooserAfter = getDateChooserAfter(context, container);
        if (dateChooserAfter == null) {
            dateChooserAfter = createDateChooserAfter(context, container);
        }
        initDateChooserAfter(context, dateChooserAfter);
    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        DateChooser dateChooserBefore = getDateChooserBefore(context, container);
        if (dateChooserBefore == null) {
            return;
        }
        Date param1 = (Date) dateChooserBefore.getValue();
        DateChooser dateChooserAfter = getDateChooserAfter(context, container);
        if (dateChooserAfter == null) {
            return;
        }
        Date param2 = (Date) dateChooserAfter.getValue();
        TimeZone param3 = filterProperty.getTimeZone();
        List<Object> parameters = new ArrayList<Object>(2);
        parameters.add(param1);
        parameters.add(param2);
        parameters.add(param3);
        criterion.setParameters(parameters);
    }

}