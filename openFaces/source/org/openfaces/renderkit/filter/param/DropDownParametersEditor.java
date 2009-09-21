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
import org.openfaces.component.filter.FilterType;
import org.openfaces.component.filter.OperationType;
import org.openfaces.component.filter.criterion.PropertyFilterCriterion;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DropDownParametersEditor extends ParametersEditor implements Serializable {

    private static final String DROP_SOWN_ID_SUFFIX = "dropDown";
    private static final String DROP_DOWN_ITEMS_ID_SUFFIX = "items";    

    public DropDownParametersEditor() {
    }

    public DropDownParametersEditor(FilterProperty filterProperty, OperationType operation) {
        super(filterProperty, operation);
    }

    private DropDownField getDropDown(FacesContext context, UIComponent container) {
        return (DropDownField) ComponentUtil.getChildBySuffix(container, DROP_SOWN_ID_SUFFIX);
    }


    private DropDownItems getDropDownItems(FacesContext context, DropDownField dropDown) {
        return (DropDownItems) ComponentUtil.getChildBySuffix(dropDown, DROP_DOWN_ITEMS_ID_SUFFIX);
    }

    private DropDownField createDropDown(FacesContext context, UIComponent container) {
        clearContainer(container);
        DropDownField dropDown = (DropDownField) ComponentUtil.createChildComponent(context, container, DropDownField.COMPONENT_TYPE, DROP_SOWN_ID_SUFFIX);
        dropDown.setStyleClass(FilterRow.DEFAULT_PARAMETER_CLASS);
        dropDown.setStyle("width: 145px;");
        dropDown.setMaxlength(250);
        
        if (filterProperty.getType() == FilterType.SELECT) {
            dropDown.setCustomValueAllowed(false);
        }
        DropDownItems dropDownItems = (DropDownItems) ComponentUtil.createChildComponent(context, dropDown, DropDownItems.COMPONENT_TYPE, DROP_DOWN_ITEMS_ID_SUFFIX);
        return dropDown;
    }

    private void initDropDown(FacesContext context, DropDownField dropDown) {
        if (criterion != null) {
            dropDown.setValue(criterion.getParameter());
        }
        DropDownItems items = getDropDownItems(context, dropDown);
        items.setValue(filterProperty.getDataProvider());
        dropDown.setConverter(filterProperty.getConverter());

    }

    public void prepare(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        super.prepare(context, compositeFilter, filterRow, container);
        DropDownField dropDown = getDropDown(context, container);
        if (dropDown == null) {
            dropDown = createDropDown(context, container);
        }
        initDropDown(context, dropDown);
    }


    public void update(FacesContext context, CompositeFilter compositeFilter, FilterRow filterRow, UIComponent container) {
        DropDownField dropDown = getDropDown(context, container);
        if (dropDown == null) {
            return;
        }
        Object param1 = dropDown.getValue();

        List<Object> parameters = new ArrayList<Object>(2);
        parameters.add(param1);
        if (filterProperty.getType() == FilterType.TEXT) {
            boolean param2 = filterProperty.isCaseSensitive();
            parameters.add(param2);
        }
        criterion.setParameters(parameters);
    }

    @Override
    public PropertyFilterCriterion getCriterion() {
        if (filterProperty.getType() == FilterType.SELECT && criterion.getParameter()==null) {
                return null;            
        }
        return super.getCriterion();    //To change body of overridden methods use File | Settings | File Templates.
    }
}