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

package org.openfaces.component.filter;

import org.openfaces.component.FilterableComponent;
import org.openfaces.component.OUIData;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class FilterableComponentPropertyLocatorFactory implements PropertyLocatorFactory {

    private FilterableComponent component;

    public FilterableComponentPropertyLocatorFactory(FilterableComponent component) {
        this.component = component;
    }

    public PropertyLocator create(Object expression) {
        return new FilterableComponentPropertyLocator(expression, component);
    }

    /**
     * @author Dmitry Pikhulya
     */
    public static class FilterableComponentPropertyLocator extends PropertyLocator implements Serializable {

        protected transient FilterableComponent component;
        protected String componentId;

        public FilterableComponentPropertyLocator(Object expression, FilterableComponent component) {
            super(expression);
            if (expression == null)
                throw new IllegalArgumentException("expression can't be null");
            if (expression instanceof ValueExpression) {
                if (component == null)
                    throw new IllegalArgumentException("component can't be null when expression is ValueExpression");
            } else if (!(expression instanceof String))
                throw new IllegalArgumentException("expression can be either ValueExpression or String, but it is: " + expression.getClass().getName());

            Integer prevUiDataIndex = null;
            if (component instanceof OUIData) {
                UIData uiData = (UIData) component;
                if (uiData.getRowIndex() != -1) {
                    prevUiDataIndex = uiData.getRowIndex();
                    uiData.setRowIndex(-1);
                }
            }
            this.componentId = ((UIComponent) component).getClientId(FacesContext.getCurrentInstance());
            if (prevUiDataIndex != null)
                ((OUIData) component).setRowIndex(prevUiDataIndex);

        }

        private FilterableComponent getComponent() {
            if (component != null) return component;
            if (componentId == null) return null;
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot = context.getViewRoot();
            if (Environment.isGateInPortal(context)) {
                String viewRootIdPart = "_viewRoot:";
                componentId = componentId.substring(componentId.indexOf(viewRootIdPart) + viewRootIdPart.length(), componentId.length());
            }
            component = (FilterableComponent) Components.findComponent(viewRoot, componentId);
            if (component == null)
                throw new IllegalStateException("Couldn't find filtered component by id: " + componentId);
            return component;
        }

        public Object getPropertyValue(Object obj) {
            FilterableComponent component = getComponent();
            FacesContext context = FacesContext.getCurrentInstance();
            return component.getFilteredValueByData(context, obj, expression);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PropertyLocator that = (PropertyLocator) o;

            if (expression != null ? !expressionToComparableString(expression).equals(expressionToComparableString(that.expression)) : that.expression != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = expression != null ? expressionToComparableString(expression).hashCode() : 0;
            return result;
        }

        private String expressionToComparableString(Object expression) {
            // address the case when com.sun.facelets.el.TagValueExpression instance reports inequality with itself
            if (expression instanceof ValueExpression)
                return ((ValueExpression) expression).getExpressionString();
            return expression.toString();
        }


    }

}
