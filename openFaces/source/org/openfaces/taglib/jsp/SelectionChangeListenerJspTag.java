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

package org.openfaces.taglib.jsp;

import org.openfaces.component.select.TabSelectionHolder;
import org.openfaces.event.SelectionChangeListener;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Andrew Palval
 */
public class SelectionChangeListenerJspTag extends TagSupport {
    private ValueExpression type;

    public SelectionChangeListenerJspTag() {
    }

    public void setType(ValueExpression type) {
        this.type = type;
    }

    public int doStartTag() throws JspException {
        if (type == null) {
            throw new JspException("type attribute not set");
        }

        UIComponentClassicTagBase componentTag = UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (componentTag == null)
            throw new JspException("SelectionChangeListener has no UIComponentTag parent.");

        if (componentTag.getCreated()) {
            UIComponent component = componentTag.getComponentInstance();
            if (!(component instanceof TabSelectionHolder))
                throw new JspException("Component " + component.getClass().toString() + " does not support" +
                        " SelectionChange event.");
            FacesContext context = FacesContext.getCurrentInstance();
            String className = (String) type.getValue(context.getELContext());
            try {
                SelectionChangeListener changeListener = (SelectionChangeListener) Class.forName(className).newInstance();
                ((TabSelectionHolder) component).addSelectionListener(changeListener);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return Tag.SKIP_BODY;
    }
}
