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
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.renderkit.filter.FilterRow;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

public abstract class ParametersEditor {

    protected ExpressionFilterCriterion criterion = new ExpressionFilterCriterion();
    protected FilterProperty filterProperty;

    public ParametersEditor() {
    }

    protected ParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        this.filterProperty = filterProperty;
        criterion.setPropertyLocator(filterProperty.getPropertyLocator());
        criterion.setCondition(operation);
    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container){
        criterion.setInverse(filterRow.isInverse());
    }

    public abstract void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container);

    protected void clearContainer(UIComponent container) {
        List<UIComponent> children = container.getChildren();
        children.clear();
    }

    public ExpressionFilterCriterion getCriterion() {
        return criterion;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.criterion.getParameters().clear();
        this.criterion.getParameters().putAll(parameters);
    }

    public static enum ParameterEditorType {
        DROP_DOWN_PARAMETERS_EDITOR,
        DATE_CHOOSER_PARAMETERS_EDITOR,
        TWO_DATE_CHOOSER_PARAMETERS_EDITOR,
        SPINNER_PARAMETRS_EDITOR,
        TWO_SPINNER_PARAMETRS_EDITOR,
        INPUT_TEXT_PARAMETRS_EDITOR
    }

    public static ParameterEditorType getParameterEditorType(FilterProperty filterProperty, FilterCondition operation) {
        if (operation == null)
            operation = FilterCondition.EQUALS;
        switch (operation) {
            case LESS_OR_EQUAL:
            case GREATER_OR_EQUAL:
            case GREATER:
            case LESS:
                switch (filterProperty.getType()) {
                    case DATE:
                        return ParameterEditorType.DATE_CHOOSER_PARAMETERS_EDITOR;
                    case NUMBER:
                        return ParameterEditorType.SPINNER_PARAMETRS_EDITOR;
                    default:
                        return ParameterEditorType.INPUT_TEXT_PARAMETRS_EDITOR;
                }
            case BETWEEN:
                switch (filterProperty.getType()) {
                    case DATE:
                        return ParameterEditorType.TWO_DATE_CHOOSER_PARAMETERS_EDITOR;
                    case NUMBER:
                        return ParameterEditorType.TWO_SPINNER_PARAMETRS_EDITOR;
                    default:
                        throw new UnsupportedOperationException();
                }
            case EQUALS: {

                switch (filterProperty.getType()) {
                    case DATE:
                        return ParameterEditorType.DATE_CHOOSER_PARAMETERS_EDITOR;
                    case NUMBER:
                        return ParameterEditorType.SPINNER_PARAMETRS_EDITOR;
                    case SELECT:
                        return ParameterEditorType.DROP_DOWN_PARAMETERS_EDITOR;
                    default:
                        if (filterProperty.getDataProvider() != null) {
                            return ParameterEditorType.DROP_DOWN_PARAMETERS_EDITOR;
                        } else {
                            return ParameterEditorType.INPUT_TEXT_PARAMETRS_EDITOR;
                        }
                }
            }
            case CONTAINS:
            case BEGINS_WITH:
            case ENDS_WITH:
            default:
                return ParameterEditorType.INPUT_TEXT_PARAMETRS_EDITOR;

        }
    }

    public static ParametersEditor getInstance(ParameterEditorType type, FilterProperty filterProperty,
                                               FilterCondition operation, Map<String, Object> parameters) {

        ParametersEditor result;
        switch (type) {
            case DROP_DOWN_PARAMETERS_EDITOR:
                result = new DropDownParametersEditor(filterProperty, operation);
                break;
            case DATE_CHOOSER_PARAMETERS_EDITOR:
                result = new DateChooserParametersEditor(filterProperty, operation);
                break;
            case TWO_DATE_CHOOSER_PARAMETERS_EDITOR:
                result = new TwoDateChooserParametersEditor(filterProperty, operation);
                break;
            case SPINNER_PARAMETRS_EDITOR:
                result = new SpinnerParametersEditor(filterProperty, operation);
                break;
            case TWO_SPINNER_PARAMETRS_EDITOR:
                result = new TwoSpinnerParametersEditor(filterProperty, operation);
                break;
            case INPUT_TEXT_PARAMETRS_EDITOR:
                result = new InputTextParametersEditor(filterProperty, operation);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (parameters != null)
            result.setParameters(parameters);
        return result;
    }
}
