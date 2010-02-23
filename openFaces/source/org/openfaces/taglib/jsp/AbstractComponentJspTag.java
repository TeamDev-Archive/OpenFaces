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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.TagUtil;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.AbstractTag;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentELTag;

/**
 * @author Andrew Palval
 */
public abstract class AbstractComponentJspTag extends UIComponentELTag {

    private AbstractComponentTag delegate;

    protected AbstractComponentJspTag(AbstractComponentTag delegate) {
        setDelegate(delegate);
    }

    public final void setComponentProperties(FacesContext facesContext, UIComponent component) {
        getDelegate().setComponentProperties(facesContext, component);
    }

    @Override
    public void release() {
        super.release();
        getDelegate().cleanUp();
        getDelegate().setFacesContext(null);
    }

    public final String getComponentType() {
        return getDelegate().getComponentType();
    }

    public final String getRendererType() {
        return getDelegate().getRendererType();
    }


    public void setId(ValueExpression id) {
        if (!id.isLiteralText())
            throw new IllegalArgumentException("'id' attribute cannot be specified as value binding");
        super.setId(id.getExpressionString());
    }

    protected AbstractComponentTag getDelegate() {
        return delegate;
    }

    @Override
    protected void setProperties(UIComponent component) {
        // super.setProperties shouldn't be called for facelets support. JspTagComponentWrapper substitutes FacesContext
        // in this class, but super.setProperties uses FacesContext from its own private field which we can't influence,
        // so we just do the same job in setComponentProperties instead of invoking super.setProperties.
        FacesContext facesContext = getFacesContext();
        getDelegate().setFacesContext(facesContext);
        setComponentProperties(facesContext, component);
        TagUtil.initComponentChildren(facesContext, component);
    }

    @Override
    public void setRendered(ValueExpression rendered) {
        super.setRendered(rendered);
        getDelegate().setPropertyValue("rendered", rendered);
    }

    public void setStyle(ValueExpression style) {
        getDelegate().setPropertyValue("style", style);
    }

    public void setStyleClass(ValueExpression styleClass) {
        getDelegate().setPropertyValue("styleClass", styleClass);
    }

    public void setRolloverStyle(ValueExpression style) {
        getDelegate().setPropertyValue("rolloverStyle", style);
    }

    public void setRolloverClass(ValueExpression styleClass) {
        getDelegate().setPropertyValue("rolloverClass", styleClass);
    }

    public void setFocusedStyle(ValueExpression focusedStyle) {
        getDelegate().setPropertyValue("focusedStyle", focusedStyle);
    }

    public void setFocusedClass(ValueExpression focusedClass) {
        getDelegate().setPropertyValue("focusedClass", focusedClass);
    }

    public void setOnclick(ValueExpression onclick) {
        getDelegate().setPropertyValue("onclick", onclick);
    }

    public void setOndblclick(ValueExpression ondblclick) {
        getDelegate().setPropertyValue("ondblclick", ondblclick);
    }

    public void setOnmousedown(ValueExpression onmousedown) {
        getDelegate().setPropertyValue("onmousedown", onmousedown);
    }

    public void setOnmouseover(ValueExpression onmouseover) {
        getDelegate().setPropertyValue("onmouseover", onmouseover);
    }

    public void setOnmousemove(ValueExpression onmousemove) {
        getDelegate().setPropertyValue("onmousemove", onmousemove);
    }

    public void setOnmouseout(ValueExpression onmouseout) {
        getDelegate().setPropertyValue("onmouseout", onmouseout);
    }

    public void setOnmouseup(ValueExpression onmouseup) {
        getDelegate().setPropertyValue("onmouseup", onmouseup);
    }

    public void setOncontextmenu(ValueExpression oncontextmenu) {
        getDelegate().setPropertyValue("oncontextmenu", oncontextmenu);
    }

    public void setOnkeydown(ValueExpression onkeydown) {
        getDelegate().setPropertyValue("onkeydown", onkeydown);
    }

    public void setOnkeyup(ValueExpression onkeyup) {
        getDelegate().setPropertyValue("onkeyup", onkeyup);
    }

    public void setOnkeypress(ValueExpression onkeypress) {
        getDelegate().setPropertyValue("onkeypress", onkeypress);
    }

    public void setOnblur(ValueExpression onblur) {
        getDelegate().setPropertyValue("onblur", onblur);
    }

    public void setOnfocus(ValueExpression onfocus) {
        getDelegate().setPropertyValue("onfocus", onfocus);
    }

    public void setOnchange(ValueExpression onchange) {
        getDelegate().setPropertyValue("onchange", onchange);
    }


    public void setDelegate(AbstractComponentTag delegate) {
        this.delegate = delegate;
        this.delegate.setExpressionCreator(JSP_EXPRESSION_CREATOR);
    }

    public static final AbstractTag.ExpressionCreator JSP_EXPRESSION_CREATOR = new AbstractTag.ExpressionCreator() {
        public ValueExpression createValueExpression(FacesContext facesContext, String attributeName, String attributeValue, Class type) {
            if (type == null)
                type = Object.class;
            Application application = facesContext.getApplication();
            ELContext elContext = facesContext.getELContext();
            ExpressionFactory expressionFactory = application.getExpressionFactory();
            return expressionFactory.createValueExpression(elContext, attributeValue, type);
        }

        public MethodExpression createMethodExpression(FacesContext facesContext, String attributeName, String actionDeclaration, Class returnType, Class[] paramTypes) {
            Application application = facesContext.getApplication();
            ELContext elContext = facesContext.getELContext();
            ExpressionFactory expressionFactory = application.getExpressionFactory();
            return expressionFactory.createMethodExpression(elContext, actionDeclaration, returnType, paramTypes);
        }

        public boolean isValueReference(String propertyName, String value) {
            return AbstractComponentTag.isValueReference(value);
        }
    };

}
