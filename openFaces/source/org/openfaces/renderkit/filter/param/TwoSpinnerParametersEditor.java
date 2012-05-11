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

package org.openfaces.renderkit.filter.param;

import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.input.Spinner;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class TwoSpinnerParametersEditor extends ParametersEditor implements Serializable {
    private static final String SPINNER_BEFORE_ID_SUFFIX = "spinnerBefore";
    private static final String SPINNER_AFTER_ID_SUFFIX = "spinnerAfter";

    public TwoSpinnerParametersEditor() {
    }

    public TwoSpinnerParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        super(filterProperty, operation);
    }

    private Spinner getSpinnerBefore(UIComponent container) {
        return (Spinner) Components.getChildBySuffix(container, SPINNER_BEFORE_ID_SUFFIX);
    }

    private Spinner createSpinnerBefore(FacesContext context, UIComponent container) {
        clearContainer(container);
        Spinner spinner = (Spinner) Components.createChildComponent(context, container, Spinner.COMPONENT_TYPE, SPINNER_BEFORE_ID_SUFFIX);
        spinner.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        spinner.setStyle("width: 70px;");
        return spinner;
    }

    private void initSpinnerBefore(Spinner spinner) {
        spinner.setValue(criterion.getArg1());
    }


    private Spinner getSpinnerAfter(UIComponent container) {
        return (Spinner) Components.getChildBySuffix(container, SPINNER_AFTER_ID_SUFFIX);
    }

    private Spinner createSpinnerAfter(FacesContext context, UIComponent container) {
        Spinner spinner = (Spinner) Components.createChildComponent(context, container, Spinner.COMPONENT_TYPE, SPINNER_AFTER_ID_SUFFIX);
        spinner.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        spinner.setStyle("width: 70px;");
        return spinner;
    }

    private void initSpinnerAfter(Spinner spinner) {
        spinner.setValue(criterion.getArg2());
    }


    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        Spinner spinnerBefore = getSpinnerBefore(container);
        if (spinnerBefore == null) {
            spinnerBefore = createSpinnerBefore(context, container);
        }
        initSpinnerBefore(spinnerBefore);

        Spinner spinnerAfter = getSpinnerAfter(container);
        if (spinnerAfter == null) {
            spinnerAfter = createSpinnerAfter(context, container);
        }
        initSpinnerAfter(spinnerAfter);

    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        Spinner spinnerBefore = getSpinnerBefore(container);
        if (spinnerBefore == null) {
            return;
        }
        criterion.setArg1(spinnerBefore.getValue());

        Spinner spinnerAfter = getSpinnerAfter(container);
        if (spinnerAfter == null) {
            return;
        }
        criterion.setArg2(spinnerAfter.getValue());
    }

}