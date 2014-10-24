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
package org.openfaces.component.table;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class Summaries extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Summaries";
    public static final String COMPONENT_FAMILY = "org.openfaces.Summaries";

    private static final String ATTR_PATTERN = "pattern";

    private Boolean footerVisible;
    private Boolean inGroupFootersVisible;
    private Boolean functionEditable;

    public Summaries() {

    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                footerVisible,
                inGroupFootersVisible,
                functionEditable
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        footerVisible = (Boolean) state[i++];
        inGroupFootersVisible = (Boolean) state[i++];
        functionEditable = (Boolean) state[i++];

    }

    public boolean getFooterVisible() {
        return ValueBindings.get(this, "footerVisible", footerVisible, true);
    }

    public void setFooterVisible(boolean footerVisible) {
        this.footerVisible = footerVisible;
    }

    public boolean getInGroupFootersVisible() {
        return ValueBindings.get(this, "inGroupFootersVisible", inGroupFootersVisible, true);
    }

    public void setInGroupFootersVisible(boolean inGroupFootersVisible) {
        this.inGroupFootersVisible = inGroupFootersVisible;
    }

    public ValueExpression getPattern() {
        return getValueExpression(ATTR_PATTERN);
    }

    public void setPattern(ValueExpression valueExpression) {
        setValueExpression(ATTR_PATTERN, valueExpression);
    }

    public boolean getFunctionEditable() {
        return ValueBindings.get(this, "functionEditable", functionEditable, true);
    }

    public void setFunctionEditable(boolean functionEditable) {
        this.functionEditable = functionEditable;
    }
}
