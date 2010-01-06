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
package org.openfaces.taglib.internal;

import javax.el.Expression;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Kaplin
 */
public abstract class AbstractTag {
    private Map<String, String> properties = new HashMap<String, String>();

    private ExpressionCreator expressionCreator;

    public ExpressionCreator getExpressionCreator() {
        return expressionCreator;
    }

    public void setExpressionCreator(ExpressionCreator expressionCreator) {
        this.expressionCreator = expressionCreator;
    }

    public void cleanUp() {
        properties.clear();
    }

    public String getPropertyValue(String key) {
        return properties.get(key);
    }

    public void setPropertyValue(String key, String value) {
        properties.put(key, value);
    }

    public void setPropertyValue(String key, Expression value) {
        properties.put(key, value.getExpressionString());
    }

    protected final ValueExpression createValueExpression(FacesContext facesContext, String attributeName, String attributeValue, Class type) {
        return expressionCreator.createValueExpression(facesContext, attributeName, attributeValue, type);
    }

    protected final ValueExpression createValueExpression(FacesContext facesContext, String attributeName, String attributeValue) {
        return expressionCreator.createValueExpression(facesContext, attributeName, attributeValue, null);
    }


    protected MethodExpression createMethodExpression(
            FacesContext facesContext,
            String attributeName,
            String actionDeclaration,
            Class returnType,
            Class[] paramTypes
    ) {
        return expressionCreator.createMethodExpression(facesContext, attributeName, actionDeclaration, returnType, paramTypes);
    }

    public interface ExpressionCreator {
        ValueExpression createValueExpression(FacesContext facesContext, String attributeName, String attributeValue, Class type);

        MethodExpression createMethodExpression(FacesContext facesContext, String attributeName, String actionDeclaration,
                                                Class returnType, Class[] paramTypes);

        public boolean isValueReference(String propertyName, String value);
    }
}
