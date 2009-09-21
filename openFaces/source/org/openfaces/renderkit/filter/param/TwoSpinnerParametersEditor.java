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
import org.openfaces.component.input.Spinner;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TwoSpinnerParametersEditor extends ParametersEditor implements Serializable {

    private static final String SPINNER_BEFORE_ID_SUFFIX = "spinnerBefore";
    private static final String SPINNER_AFTER_ID_SUFFIX = "spinnerAfter";

    public TwoSpinnerParametersEditor() {
    }

    public TwoSpinnerParametersEditor(FilterProperty filterProperty, OperationType operation) {
        super(filterProperty, operation);
    }

    private Spinner getSpinnerBefore(FacesContext context, UIComponent container) {
        return (Spinner) ComponentUtil.getChildBySuffix(container, SPINNER_BEFORE_ID_SUFFIX);
    }

    private Spinner createSpinnerBefore(FacesContext context, UIComponent container) {
        clearContainer(container);
        Spinner spinner = (Spinner) ComponentUtil.createChildComponent(context, container, Spinner.COMPONENT_TYPE, SPINNER_BEFORE_ID_SUFFIX);
        spinner.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);        
        spinner.setStyle("width: 70px;");
        return spinner;
    }

    private void initSpinnerBefore(FacesContext context, Spinner spinner) {
        spinner.setValue(criterion.getParameter(0));
    }


    private Spinner getSpinnerAfter(FacesContext context, UIComponent container) {
        return (Spinner) ComponentUtil.getChildBySuffix(container, SPINNER_AFTER_ID_SUFFIX);
    }

    private Spinner createSpinnerAfter(FacesContext context, UIComponent container) {
        Spinner spinner = (Spinner) ComponentUtil.createChildComponent(context, container, Spinner.COMPONENT_TYPE, SPINNER_AFTER_ID_SUFFIX);
        spinner.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        spinner.setStyle("width: 70px;");
        return spinner;
    }

    private void initSpinnerAfter(FacesContext context, Spinner spinner) {
        spinner.setValue(criterion.getParameter(1));
    }


    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        Spinner spinnerBefore = getSpinnerBefore(context, container);
        if (spinnerBefore == null) {
            spinnerBefore = createSpinnerBefore(context, container);
        }
        initSpinnerBefore(context, spinnerBefore);

        Spinner spinnerAfter = getSpinnerAfter(context, container);
        if (spinnerAfter == null) {
            spinnerAfter = createSpinnerAfter(context, container);
        }
        initSpinnerAfter(context, spinnerAfter);

    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        Spinner spinnerBefore = getSpinnerBefore(context, container);
        if (spinnerBefore == null) {
            return;
        }
        Number param1 = (Number) spinnerBefore.getValue();
        Spinner spinnerAfter = getSpinnerAfter(context, container);
        if (spinnerAfter == null) {
            return;
        }
        Number param2 = (Number) spinnerAfter.getValue();
        List<Object> parameters = new ArrayList<Object>(2);
        parameters.add(param1);
        parameters.add(param2);
        criterion.setParameters(parameters);
    }

}