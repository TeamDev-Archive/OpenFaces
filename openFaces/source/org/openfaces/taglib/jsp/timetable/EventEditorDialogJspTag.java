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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventEditorDialogTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialogJspTag extends AbstractComponentJspTag {

    public EventEditorDialogJspTag() {
        super(new EventEditorDialogTag());
    }

    public void setNameLabel(ValueExpression value) {
        getDelegate().setPropertyValue("nameLabel", value);
    }

    public void setResourceLabel(ValueExpression value) {
        getDelegate().setPropertyValue("resourceLabel", value);
    }

    public void setSTartLabel(ValueExpression value) {
        getDelegate().setPropertyValue("startLabel", value);
    }

    public void setEndLabel(ValueExpression value) {
        getDelegate().setPropertyValue("endLabel", value);
    }

    public void setColorLabel(ValueExpression value) {
        getDelegate().setPropertyValue("colorLabel", value);
    }

    public void setDescriptionLabel(ValueExpression value) {
        getDelegate().setPropertyValue("descriptionLabel", value);
    }

    public void setOkButtonText(ValueExpression value) {
        getDelegate().setPropertyValue("okButtonText", value);
    }

    public void setCancelButtonText(ValueExpression value) {
        getDelegate().setPropertyValue("cancelButtonText", value);
    }

    public void setDeleteButtonText(ValueExpression value) {
        getDelegate().setPropertyValue("deleteButtonText", value);
    }


}
