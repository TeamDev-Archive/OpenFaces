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
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class InputTextParametersEditor extends ParametersEditor implements Serializable {

    private static final String INPUT_TEXT_ID_SUFFIX = "input";

    public InputTextParametersEditor() {
    }

    public InputTextParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        super(filterProperty, operation);
    }

    private HtmlInputText getInputText(UIComponent container) {
        return (HtmlInputText) Components.getChildBySuffix(container, INPUT_TEXT_ID_SUFFIX);
    }

    private HtmlInputText createInputText(FacesContext context, UIComponent container) {
        HtmlInputText inputText = (HtmlInputText) Components.createChildComponent(context, container, HtmlInputText.COMPONENT_TYPE, INPUT_TEXT_ID_SUFFIX);
        inputText.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        inputText.setStyle("width: 145px;");
        inputText.setMaxlength(250);
        return inputText;
    }

    private void initInputText(HtmlInputText inputText) {
        inputText.setValue(criterion.getArg1());
    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        clearContainer(container);
        HtmlInputText inputText = getInputText(container);
        if (inputText == null) {
            inputText = createInputText(context, container);
        }
        initInputText(inputText);
    }

    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        HtmlInputText inputText = getInputText(container);
        if (inputText == null) {
            return;
        }
        criterion.setArg1(inputText.getValue());
        criterion.setCaseSensitive(filterProperty.isCaseSensitive());
    }
}
