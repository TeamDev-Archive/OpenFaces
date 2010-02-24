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

package org.openfaces.renderkit.filter;

import org.openfaces.component.command.CommandButton;
import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.component.input.SuggestionMode;
import org.openfaces.renderkit.filter.param.ParametersEditor;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ValueExpressionImpl;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Natalia Zolochevska
 */
public class FilterRow implements Serializable {
    public static final String ROW_ID_SUFFIX = "row";
    public static final String ADD_BUTTON_CONTAINER_SUFFIX = "add";
    public static final String DELETE_BUTTON_CONTAINER_SUFFIX = "delete";
    public static final String BUTTON_SUFFIX = "button";
    public static final String CHECKBOX_SUFFIX = "checkbox";
    public static final String LABEL_SUFFIX = "label";
    public static final String INVERSE_CHECKBOX_CONTAINER_SUFFIX = "inverse";
    public static final String PROPERTY_SELECTOR_ID_SUFFIX = "propertySelector";
    public static final String OPERATION_SELECTOR_ID_SUFFIX = "operationSelector";
    public static final String PARAMETERS_EDITOR_ID_SUFFIX = "parametersEditor";
    public static final String DROP_DOWN_ID_SUFFIX = "dropDown";
    public static final String DROP_DOWN_ITEMS_ID_SUFFIX = "items";

    public static final String DEFAULT_ROW_CLASS = "o_filter_row";
    public static final String DEFAULT_ROW_ITEM_CLASS = "o_filter_row_item";
    public static final String DEFAULT_ROW_ITEM_CHECKBOX_CLASS = "o_filter_row_item o_filter_row_item_checkbox";
    public static final String DEFAULT_ROW_ITEM_INPUT_CLASS = "o_filter_row_item o_filter_row_item_input";
    public static final String DEFAULT_PROPERTY_CLASS = "o_filter_property";
    public static final String DEFAULT_OPERATION_CLASS = "o_filter_operation";
    public static final String DEFAULT_PARAMETER_CLASS = "o_filter_parameter";
    public static final String DEFAULT_ADD_BUTTON_CLASS = "o_filter_add_button";
    public static final String DEFAULT_DELETE_BUTTON_CLASS = "o_filter_delete_button";
    public static final String DEFAULT_INVERSE_CHECKBOX_CLASS = "o_filter_inverse_checkbox";
    public static final String DEFAULT_INVERSE_LABEL_CLASS = "o_filter_inverse_label";

    public static final String INVERSE_LABEL = "not";

    private final String rowIdSuffix;
    private final int index;

    private boolean inverse;
    private FilterProperty property;
    private FilterCondition operation;
    private ParametersEditor parametersEditor;
    private ParametersEditor.ParameterEditorType parametersEditorType;

    private boolean lastRow;


    public FilterRow(int index) {
        this.index = index;
        rowIdSuffix = ROW_ID_SUFFIX + Rendering.SERVER_ID_SUFFIX_SEPARATOR + index;
    }

    public int getIndex() {
        return index;
    }

    public void setLastRow(boolean lastRow) {
        this.lastRow = lastRow;
    }

    public boolean isInverse() {
        return inverse;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    private UIComponent createRowContainer(FacesContext context, CompositeFilter compositeFilter) {
        HtmlPanelGroup rowContainer = (HtmlPanelGroup) Components.createChildComponent(
                context, compositeFilter, HtmlPanelGroup.COMPONENT_TYPE, rowIdSuffix);
        rowContainer.setLayout("block");
        rowContainer.setStyleClass(DEFAULT_ROW_CLASS);

        createDeleteButton(context, rowContainer, compositeFilter);
        createAddButton(context, rowContainer, compositeFilter);
        return rowContainer;
    }

    private UIComponent getRowContainer(CompositeFilter compositeFilter) {
        return Components.getChildBySuffix(compositeFilter, rowIdSuffix);
    }


    private HtmlSelectBooleanCheckbox createInverseCheckBox(FacesContext context, UIComponent rowContainer) {
        HtmlPanelGroup inverseCheckBoxContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, INVERSE_CHECKBOX_CONTAINER_SUFFIX, 1);

        inverseCheckBoxContainer.setStyleClass(DEFAULT_ROW_ITEM_CHECKBOX_CLASS);
        HtmlSelectBooleanCheckbox inverseCheckBox = (HtmlSelectBooleanCheckbox) Components.createChildComponent(context, inverseCheckBoxContainer, HtmlSelectBooleanCheckbox.COMPONENT_TYPE, CHECKBOX_SUFFIX);
        //TODO: add onclick that will send ajax request to update component state        
        inverseCheckBox.setStyleClass(DEFAULT_INVERSE_CHECKBOX_CLASS);
        inverseCheckBox.setTitle(INVERSE_LABEL);

        HtmlOutputText inverseLabel = (HtmlOutputText) Components.createChildComponent(context, inverseCheckBoxContainer, HtmlOutputText.COMPONENT_TYPE, LABEL_SUFFIX);
        inverseLabel.setStyleClass(DEFAULT_INVERSE_LABEL_CLASS);
        inverseLabel.setValue(INVERSE_LABEL);
        return inverseCheckBox;
    }

    private void initInverseCheckBox(HtmlSelectBooleanCheckbox inverseCheckBox) {
        inverseCheckBox.setValue(inverse);
    }

    private HtmlSelectBooleanCheckbox getInverseCheckBox(HtmlPanelGroup inverseCheckBoxContainer) {
        return (HtmlSelectBooleanCheckbox) Components.getChildBySuffix(inverseCheckBoxContainer, CHECKBOX_SUFFIX);
    }

    private HtmlPanelGroup getInverseCheckBoxContainer(UIComponent rowContainer) {
        return (HtmlPanelGroup) Components.getChildBySuffix(rowContainer, INVERSE_CHECKBOX_CONTAINER_SUFFIX);
    }

    private HtmlSelectBooleanCheckbox findInverseCheckBox(FacesContext context, CompositeFilter compositeFilter) {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) {
            return null;
        }
        HtmlPanelGroup inverseContainer = getInverseCheckBoxContainer(rowContainer);
        if (inverseContainer == null) {
            return null;
        }
        HtmlSelectBooleanCheckbox inverseCheckBox = getInverseCheckBox(inverseContainer);
        return inverseCheckBox;
    }


    private CommandButton createAddButton(FacesContext context, UIComponent rowContainer, CompositeFilter compositeFilter) {
        HtmlPanelGroup addButtonContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, ADD_BUTTON_CONTAINER_SUFFIX);
        addButtonContainer.setStyleClass(DEFAULT_ROW_ITEM_CLASS);
        CommandButton addButton = (CommandButton) Components.createChildComponent(context, addButtonContainer, CommandButton.COMPONENT_TYPE, BUTTON_SUFFIX);
        addButton.setType("button");
        addButton.setValue("+");
        addButton.setOnclick("O$('" + compositeFilter.getClientId(context) + "').add(); return false;");
        addButtonContainer.setValueExpression("rendered", new ValueExpressionImpl() {
            public Object getValue(ELContext elContext) {
                return lastRow;
            }
        });
        addButton.setStyleClass(DEFAULT_ADD_BUTTON_CLASS);
        return addButton;
    }

    private CommandButton createDeleteButton(FacesContext context, UIComponent rowContainer, CompositeFilter compositeFilter) {
        HtmlPanelGroup deleteButtonContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, DELETE_BUTTON_CONTAINER_SUFFIX);
        deleteButtonContainer.setStyleClass(DEFAULT_ROW_ITEM_CLASS);
        CommandButton deleteButton = (CommandButton) Components.createChildComponent(context, deleteButtonContainer, CommandButton.COMPONENT_TYPE, BUTTON_SUFFIX);
        deleteButton.setType("button");
        deleteButton.setValue("-");
        deleteButton.setOnclick("O$('" + compositeFilter.getClientId(context) + "').remove(" + index + "); return false;");
        deleteButton.setStyleClass(DEFAULT_DELETE_BUTTON_CLASS);

        return deleteButton;
    }


    private HtmlPanelGroup createPropertySelectorContainer(FacesContext context, UIComponent rowContainer) {
        HtmlPanelGroup propertySelectorContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, PROPERTY_SELECTOR_ID_SUFFIX, 0);
        propertySelectorContainer.setStyleClass(DEFAULT_ROW_ITEM_INPUT_CLASS);
        return propertySelectorContainer;
    }

    private HtmlPanelGroup getPropertySelectorContainer(UIComponent rowContainer) {
        return (HtmlPanelGroup) Components.getChildBySuffix(rowContainer, PROPERTY_SELECTOR_ID_SUFFIX);
    }

    private DropDownField createPropertySelector(FacesContext context, HtmlPanelGroup propertySelectorContainer, CompositeFilter compositeFilter) {
        DropDownField propertySelector = (DropDownField) Components.createChildComponent(context, propertySelectorContainer, DropDownField.COMPONENT_TYPE, DROP_DOWN_ID_SUFFIX);
        DropDownItems dropDownItems = (DropDownItems) Components.createChildComponent(context, propertySelector, DropDownItems.COMPONENT_TYPE, DROP_DOWN_ITEMS_ID_SUFFIX);
        List<String> properties = compositeFilter.getFilterPropertiesTitles();
        dropDownItems.setValue(properties);
        propertySelector.setOnchange("O$('" + compositeFilter.getClientId(context) + "')._propertyChange(" + index + ");");
        propertySelector.setStyleClass(DEFAULT_PROPERTY_CLASS);
        propertySelector.setCustomValueAllowed(false);
        propertySelector.setSuggestionMode(SuggestionMode.ALL);
        propertySelector.setSuggestionDelay(0);
        propertySelector.setAutoComplete(true);
        return propertySelector;
    }

    private void initPropertySelector(DropDownField propertySelector) {
        String propertyValue = (property != null) ? property.getTitle() : null;
        propertySelector.setValue(propertyValue);
    }

    private DropDownField getPropertySelector(HtmlPanelGroup propertySelectorContainer) {
        return (DropDownField) Components.getChildBySuffix(propertySelectorContainer, DROP_DOWN_ID_SUFFIX);
    }

    private DropDownField findPropertySelector(CompositeFilter compositeFilter) {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) return null;
        HtmlPanelGroup propertySelectorContainer = getPropertySelectorContainer(rowContainer);
        if (propertySelectorContainer == null) return null;
        DropDownField propertySelector = getPropertySelector(propertySelectorContainer);
        return propertySelector;
    }


    private HtmlPanelGroup createOperationSelectorContainer(FacesContext context, UIComponent rowContainer) {
        HtmlPanelGroup operationSelectorContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, OPERATION_SELECTOR_ID_SUFFIX, 2);
        operationSelectorContainer.setStyleClass(DEFAULT_ROW_ITEM_INPUT_CLASS);
        operationSelectorContainer.setValueExpression("rendered", new ValueExpressionImpl() {
            public Object getValue(ELContext elContext) {
                return property != null;
            }
        });
        return operationSelectorContainer;
    }

    private HtmlPanelGroup getOperationSelectorContainer(UIComponent rowContainer) {
        return (HtmlPanelGroup) Components.getChildBySuffix(rowContainer, OPERATION_SELECTOR_ID_SUFFIX);
    }

    private DropDownField createOperationSelector(FacesContext context, HtmlPanelGroup operationSelectorContainer, CompositeFilter compositeFilter) {
        DropDownField operationSelector = (DropDownField) Components.createChildComponent(context, operationSelectorContainer, DropDownField.COMPONENT_TYPE, DROP_DOWN_ID_SUFFIX);
        Components.createChildComponent(context, operationSelector, DropDownItems.COMPONENT_TYPE, DROP_DOWN_ITEMS_ID_SUFFIX);
        operationSelector.setOnchange("O$('" + compositeFilter.getClientId(context) + "')._operationChange(" + index + ");");
        operationSelector.setConverter(compositeFilter.getConditionConverter());
        operationSelector.setStyleClass(DEFAULT_OPERATION_CLASS);
        operationSelector.setStyle("width: 100px;");
        operationSelector.setCustomValueAllowed(false);
        operationSelector.setSuggestionDelay(0);
        operationSelector.setSuggestionMode(SuggestionMode.ALL);
        operationSelector.setAutoComplete(true);
        return operationSelector;
    }


    private void initOperationSelector(DropDownField operationSelector, CompositeFilter compositeFilter) {
        operationSelector.setValue(operation);
        DropDownItems dropDownItems = getOperationSelectorItems(operationSelector);
        EnumSet<FilterCondition> operations = compositeFilter.getOperations(property);
        dropDownItems.setValue(operations);
    }


    private DropDownField getOperationSelector(HtmlPanelGroup operationSelectorContainer) {
        return (DropDownField) Components.getChildBySuffix(operationSelectorContainer, DROP_DOWN_ID_SUFFIX);
    }

    private DropDownField findOperationSelector(CompositeFilter compositeFilter) {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) return null;
        HtmlPanelGroup operationSelectorContainer = getOperationSelectorContainer(rowContainer);
        if (operationSelectorContainer == null) return null;
        DropDownField operationSelector = getOperationSelector(operationSelectorContainer);
        return operationSelector;
    }


    private DropDownItems getOperationSelectorItems(DropDownField operationSelector) {
        return (DropDownItems) Components.getChildBySuffix(operationSelector, DROP_DOWN_ITEMS_ID_SUFFIX);
    }


    private HtmlPanelGroup createParametersEditorContainer(FacesContext context, UIComponent rowContainer) {
        HtmlPanelGroup parametersEditorContainer = (HtmlPanelGroup) Components.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, PARAMETERS_EDITOR_ID_SUFFIX, 3);
        parametersEditorContainer.setStyleClass(DEFAULT_ROW_ITEM_INPUT_CLASS);
        parametersEditorContainer.setValueExpression("rendered", new ValueExpressionImpl() {
            public Object getValue(ELContext elContext) {
                return operation != null;
            }
        });
        return parametersEditorContainer;
    }


    private UIComponent getParametersEditorContainer(UIComponent rowContainer) {
        return Components.getChildBySuffix(rowContainer, PARAMETERS_EDITOR_ID_SUFFIX);
    }


    private UIComponent findParametersEditorContainer(CompositeFilter compositeFilter) {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) {
            return null;
        }
        UIComponent parametersEditorContainer = getParametersEditorContainer(rowContainer);
        return parametersEditorContainer;
    }

    public UIComponent preparateRowComponentHierarchy(FacesContext context, CompositeFilter compositeFilter) throws IOException {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) {
            rowContainer = createRowContainer(context, compositeFilter);
        }
        HtmlPanelGroup propertySelectorContainer = getPropertySelectorContainer(rowContainer);
        if (propertySelectorContainer == null) {
            propertySelectorContainer = createPropertySelectorContainer(context, rowContainer);
        }
        DropDownField propertySelector = getPropertySelector(propertySelectorContainer);
        if (propertySelector == null) {
            propertySelector = createPropertySelector(context, propertySelectorContainer, compositeFilter);
        }
        initPropertySelector(propertySelector);

        if (property != null) {
            preparateOperationComponentHierarchy(context, rowContainer, compositeFilter);
        }
        if (operation != null) {
            preparateParametersComponentHierarchy(context, rowContainer, compositeFilter);
        }
        return rowContainer;

    }

    public HtmlPanelGroup preparateOperationComponentHierarchy(
            FacesContext context,
            UIComponent rowContainer,
            CompositeFilter compositeFilter) throws IOException {
        HtmlSelectBooleanCheckbox inverseCheckBox = findInverseCheckBox(context, compositeFilter);
        if (inverseCheckBox == null) {
            inverseCheckBox = createInverseCheckBox(context, rowContainer);
        }
        initInverseCheckBox(inverseCheckBox);
        HtmlPanelGroup operationSelectorContainer = getOperationSelectorContainer(rowContainer);
        if (operationSelectorContainer == null) {
            operationSelectorContainer = createOperationSelectorContainer(context, rowContainer);
        }
        DropDownField operationSelector = getOperationSelector(operationSelectorContainer);
        if (operationSelector == null) {
            operationSelector = createOperationSelector(context, operationSelectorContainer, compositeFilter);
        }
        initOperationSelector(operationSelector, compositeFilter);
        return operationSelectorContainer;
    }

    public UIComponent preparateParametersComponentHierarchy(
            FacesContext context, UIComponent rowContainer, CompositeFilter compositeFilter) throws IOException {
        UIComponent parametersEditorContainer = getParametersEditorContainer(rowContainer);
        if (parametersEditorContainer == null) {
            parametersEditorContainer = createParametersEditorContainer(context, rowContainer);
        }
        if (parametersEditor == null) {
            ParametersEditor.ParameterEditorType type = ParametersEditor.getParameterEditorType(property, operation);
            parametersEditor = ParametersEditor.getInstance(type, property, operation, null);
            parametersEditorType = type;
        }
        parametersEditor.prepare(context, compositeFilter, this, parametersEditorContainer);
        return parametersEditorContainer;
    }


    public void encodeRow(FacesContext context, CompositeFilter compositeFilter) throws IOException {
        UIComponent component = preparateRowComponentHierarchy(context, compositeFilter);
        component.encodeAll(context);
    }

    public void encodeOperationSelector(FacesContext context, CompositeFilter compositeFilter) throws IOException {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        UIComponent component = preparateOperationComponentHierarchy(context, rowContainer, compositeFilter);
        HtmlPanelGroup inverseCheckBoxContainer = getInverseCheckBoxContainer(rowContainer);
        inverseCheckBoxContainer.encodeAll(context);
        component.encodeAll(context);
    }

    public void encodeParametersEditor(FacesContext context, CompositeFilter compositeFilter) throws IOException {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        if (rowContainer == null) {
            return;
        }
        UIComponent component = preparateParametersComponentHierarchy(context, rowContainer, compositeFilter);
        component.encodeAll(context);
    }


    public ExpressionFilterCriterion updateRowModelFromEditors(FacesContext context, CompositeFilter compositeFilter) {
        UIComponent parametersEditorContainer = findParametersEditorContainer(compositeFilter);
        DropDownField propertySelector = findPropertySelector(compositeFilter);
        if (propertySelector == null) {
            property = null;
            operation = null;
            parametersEditor = null;
            return null;
        }
        String propertyValue = (String) propertySelector.getValue();
        FilterProperty newProperty = compositeFilter.getFilterPropertyByTitle(propertyValue);
        boolean propertyModified = property == null ? newProperty != null : newProperty != null && !newProperty.getTitle().equals(property.getTitle());
        property = newProperty;
        DropDownField operationSelector = findOperationSelector(compositeFilter);
        HtmlSelectBooleanCheckbox inverseCheckBox = findInverseCheckBox(context, compositeFilter);
        if (propertyModified || property == null || operationSelector == null || inverseCheckBox == null) {
            operation = null;
            parametersEditor = null;
            return null;
        }
        FilterCondition newOperation = (FilterCondition) operationSelector.getValue();
        inverse = (Boolean) inverseCheckBox.getValue();
        boolean operationModified = newOperation == null ? operation == null : !newOperation.equals(operation);
        operation = newOperation;
        if (operation == null || parametersEditor == null) {
            parametersEditor = null;
            return null;
        }
        parametersEditor.update(context, compositeFilter, this, parametersEditorContainer);
        ExpressionFilterCriterion result = parametersEditor.getCriterion();
        result.setInverse(inverse);
        if (operationModified) {
            parametersEditor = null;
        }
        return result;
    }

    public void updateRowModelFromCriterion(ExpressionFilterCriterion criterion, CompositeFilter compositeFilter) {
        property = compositeFilter.getFilterPropertyByPropertyLocator(criterion.getPropertyLocator());
        operation = criterion.getCondition();
        inverse = criterion.isInverse();
        parametersEditorType = ParametersEditor.getParameterEditorType(property, operation);
        parametersEditor = ParametersEditor.getInstance(parametersEditorType, property, operation, criterion.getParameters());
    }

    public void removeInlineComponents(CompositeFilter compositeFilter) {
        UIComponent rowContainer = getRowContainer(compositeFilter);
        rowContainer.getParent().getChildren().remove(rowContainer);
    }

}
