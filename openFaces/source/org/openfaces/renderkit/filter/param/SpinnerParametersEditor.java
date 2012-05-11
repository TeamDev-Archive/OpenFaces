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

public class SpinnerParametersEditor extends ParametersEditor implements Serializable {

    private static final String SPINNER_ID_SUFFIX = "spinner";

    public SpinnerParametersEditor() {
    }

    public SpinnerParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        super(filterProperty, operation);
    }

    private Spinner getSpinner(UIComponent container) {
        return (Spinner) Components.getChildBySuffix(container, SPINNER_ID_SUFFIX);
    }

    private Spinner createSpinner(FacesContext context, UIComponent container) {
        clearContainer(container);
        Spinner spinner = (Spinner) Components.createChildComponent(context, container, Spinner.COMPONENT_TYPE, SPINNER_ID_SUFFIX);
        spinner.setStep(filterProperty.getStep());
        spinner.setMinValue(filterProperty.getMinValue());
        spinner.setMaxValue(filterProperty.getMaxValue());
        //spinner.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        //spinner.setStyle("width: 70px;");
        return spinner;
    }

    private void initSpinner(Spinner spinner) {
        spinner.setValue(criterion.getArg1());
    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        Spinner spinner = getSpinner(container);
        if (spinner == null) {
            spinner = createSpinner(context, container);
        }
        initSpinner(spinner);
    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        Spinner spinner = getSpinner(container);
        if (spinner == null) {
            return;
        }
        Number param = (Number) spinner.getValue();
        criterion.setArg1(param);
    }
}