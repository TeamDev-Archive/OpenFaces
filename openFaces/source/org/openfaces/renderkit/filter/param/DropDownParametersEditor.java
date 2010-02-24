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
import org.openfaces.component.filter.FilterType;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class DropDownParametersEditor extends ParametersEditor implements Serializable {

    private static final String DROP_SOWN_ID_SUFFIX = "dropDown";
    private static final String DROP_DOWN_ITEMS_ID_SUFFIX = "items";    

    public DropDownParametersEditor() {
    }

    public DropDownParametersEditor(FilterProperty filterProperty, FilterCondition operation) {
        super(filterProperty, operation);
    }

    private DropDownField getDropDown(UIComponent container) {
        return (DropDownField) Components.getChildBySuffix(container, DROP_SOWN_ID_SUFFIX);
    }


    private DropDownItems getDropDownItems(DropDownField dropDown) {
        return (DropDownItems) Components.getChildBySuffix(dropDown, DROP_DOWN_ITEMS_ID_SUFFIX);
    }

    private DropDownField createDropDown(FacesContext context, UIComponent container) {
        clearContainer(container);
        DropDownField dropDown = (DropDownField) Components.createChildComponent(context, container, DropDownField.COMPONENT_TYPE, DROP_SOWN_ID_SUFFIX);
        dropDown.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        dropDown.setStyle("width: 145px;");
        dropDown.setMaxlength(250);
        
        if (filterProperty.getType() == FilterType.SELECT) {
            dropDown.setCustomValueAllowed(false);
        }
        DropDownItems dropDownItems = (DropDownItems) Components.createChildComponent(context, dropDown, DropDownItems.COMPONENT_TYPE, DROP_DOWN_ITEMS_ID_SUFFIX);
        return dropDown;
    }

    private void initDropDown(DropDownField dropDown) {
        if (criterion != null) {
            dropDown.setValue(criterion.getArg1());
        }
        DropDownItems items = getDropDownItems(dropDown);
        items.setValue(filterProperty.getDataProvider());
        dropDown.setConverter(filterProperty.getConverter());

    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        DropDownField dropDown = getDropDown(container);
        if (dropDown == null) {
            dropDown = createDropDown(context, container);
        }
        initDropDown(dropDown);
    }


    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        DropDownField dropDown = getDropDown(container);
        if (dropDown == null) {
            return;
        }
        criterion.setArg1(dropDown.getValue());

        if (filterProperty.getType() == FilterType.TEXT) {
            criterion.setCaseSensitive(filterProperty.isCaseSensitive());
        }
    }

    @Override
    public ExpressionFilterCriterion getCriterion() {
        if (filterProperty.getType() == FilterType.SELECT && criterion.getArg1() == null) {
                return null;            
        }
        return super.getCriterion();
    }
}