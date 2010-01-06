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
package org.openfaces.component.chart.impl.plots;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicPropertiesUtils {

    //put transmitted <value> in request scope with transmitted <name>, getValue for transmitted <binding>
    public static Object getDynamicValue(String name, Object value, ValueExpression expression) {
        if (expression == null)
            return null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

        Object oldAttributeValue = requestMap.put(name, value);
        Object result = expression.getValue(facesContext.getELContext());
        requestMap.put(name, oldAttributeValue);

        return result;
    }
}
