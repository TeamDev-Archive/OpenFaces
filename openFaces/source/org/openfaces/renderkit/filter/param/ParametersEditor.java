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
import org.openfaces.component.filter.criterion.NamedPropertyLocator;
import org.openfaces.component.filter.criterion.PropertyFilterCriterion;
import org.openfaces.renderkit.filter.FilterRow;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public abstract class ParametersEditor {

    protected PropertyFilterCriterion criterion = new PropertyFilterCriterion();
    protected FilterProperty filterProperty;

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container){
        criterion.setInverse(filterRow.isInverse());
    }

    public abstract void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container);

    protected void clearContainer(UIComponent container) {
        List<UIComponent> children = container.getChildren();
        children.clear();
    }

    public PropertyFilterCriterion getCriterion() {
        return criterion;
    }

    public void setParameters(PropertyFilterCriterion criterion) {
        this.criterion.getParameters().clear();
        this.criterion.getParameters().putAll(criterion.getParameters());
    }

    public ParametersEditor() {
    }


    protected ParametersEditor(FilterProperty filterProperty, OperationType operation) {
        this.filterProperty = filterProperty;
        criterion.setPropertyLocator(new NamedPropertyLocator(filterProperty.getName()));
        criterion.setOperation(operation);
    }

    public static enum ParameterEditorType {
        DROP_DOWN_PARAMETERS_EDITOR,
        DATE_CHOOSER_PARAMETERS_EDITOR,
        TWO_DATE_CHOOSER_PARAMETERS_EDITOR,
        SPINNER_PARAMETRS_EDITOR,
        TWO_SPINNER_PARAMETRS_EDITOR,
        INPUT_TEXT_PARAMETRS_EDITOR
    }

    public static ParameterEditorType getParameterEditorType(FilterProperty filterProperty, OperationType operation) {
        switch (operation) {
            case EXACT:
                return ParameterEditorType.DROP_DOWN_PARAMETERS_EDITOR;
            case EQ:
            case LE:
            case GE:
            case GT:
            case LT:
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
                if (filterProperty.getDataProvider() != null) {
                    return ParameterEditorType.DROP_DOWN_PARAMETERS_EDITOR;
                } else {
                    return ParameterEditorType.INPUT_TEXT_PARAMETRS_EDITOR;
                }
            }
            case CONTAINS:
            case BEGINS:
            case ENDS:
            default:
                return ParameterEditorType.INPUT_TEXT_PARAMETRS_EDITOR;

        }
    }

    public static ParametersEditor getInstance(ParameterEditorType type, FilterProperty filterProperty, OperationType operation) {
        switch (type) {
            case DROP_DOWN_PARAMETERS_EDITOR:
                return new DropDownParametersEditor(filterProperty, operation);
            case DATE_CHOOSER_PARAMETERS_EDITOR:
                return new DateChooserParametersEditor(filterProperty, operation);
            case TWO_DATE_CHOOSER_PARAMETERS_EDITOR:
                return new TwoDateChooserParametersEditor(filterProperty, operation);
            case SPINNER_PARAMETRS_EDITOR:
                return new SpinnerParametersEditor(filterProperty, operation);
            case TWO_SPINNER_PARAMETRS_EDITOR:
                return new TwoSpinnerParametersEditor(filterProperty, operation);
            case INPUT_TEXT_PARAMETRS_EDITOR:
                return new InputTextParametersEditor(filterProperty, operation);
            default:
                throw new UnsupportedOperationException();
        }

    }
}
