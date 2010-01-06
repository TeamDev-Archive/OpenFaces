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
package org.openfaces.taglib.facelets;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import org.openfaces.taglib.internal.AbstractTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public abstract class FaceletsExpressionCreator implements AbstractTag.ExpressionCreator {
    protected FaceletContext faceletContext;
    private static final Class[] EMPTY_ARRAY = new Class[0];

    public FaceletsExpressionCreator(FaceletContext faceletContext) {
        this.faceletContext = faceletContext;
    }

    public ValueExpression createValueExpression(FacesContext facesContext, String attributeName, String attributeValue, Class type) {
        TagAttribute attribute = getAttribute(attributeName);
        ValueExpression valueExpression = (type != null)
                ? attribute.getValueExpression(faceletContext, type)
                : attribute.getValueExpression(faceletContext, Object.class);
        return valueExpression;
    }

    public boolean isValueReference(String propertyName, String value) {
        TagAttribute attribute = getAttribute(propertyName);
        return !attribute.isLiteral();
    }

    public MethodExpression createMethodExpression(
            FacesContext facesContext,
            String attributeName,
            String actionDeclaration,
            Class returnType,
            Class[] paramTypes
    ) {
        TagAttribute attribute = getAttribute(attributeName);
        if (paramTypes == null)
            paramTypes = EMPTY_ARRAY;
        MethodExpression methodExpression = attribute.getMethodExpression(faceletContext, returnType, paramTypes);
        return methodExpression;
    }

    protected abstract TagAttribute getAttribute(String attributeName);
}
