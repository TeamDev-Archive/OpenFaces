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

package org.openfaces.component.select;

import org.openfaces.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.lang.reflect.Array;
import java.util.List;

/**
 * @author Oleg Marshalenko
 */
public class SelectManyCheckbox extends OUISelectManyInputBase {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectManyCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectManyCheckbox";

    public SelectManyCheckbox() {
        setRendererType("org.openfaces.SelectManyCheckboxRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    protected void validateValue(FacesContext context, Object value) {
        super.validateValue(context, value);
        if (!isValid() || (value == null)) {
            return;
        }

        boolean isList = (value instanceof List);
        int length = isList ? ((List) value).size() : Array.getLength(value);

        if (isRequired() && length == 0) {
            FacesMessage message = MessageUtil.getMessage(context,
                    FacesMessage.SEVERITY_ERROR, REQUIRED_MESSAGE_ID, new Object[]{this.getId()});
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }

}
