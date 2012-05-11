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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventEditorDialogTag;
import org.openfaces.taglib.jsp.window.WindowJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialogJspTag extends WindowJspTag {

    public EventEditorDialogJspTag() {
        super(new EventEditorDialogTag());
    }

    public void setCreateEventCaption(ValueExpression value) {
        getDelegate().setPropertyValue("createEventCaption", value);
    }

    public void setEditEventCaption(ValueExpression value) {
        getDelegate().setPropertyValue("editEventCaption", value);
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


    public void setLabelStyle(ValueExpression labelStyle) {
        getDelegate().setPropertyValue("labelStyle", labelStyle);
    }

    public void setLabelClass(ValueExpression labelClass) {
        getDelegate().setPropertyValue("labelClass", labelClass);
    }

    public void setOkButtonStyle(ValueExpression okButtonStyle) {
        getDelegate().setPropertyValue("okButtonStyle", okButtonStyle);
    }

    public void setOkButtonClass(ValueExpression okButtonClass) {
        getDelegate().setPropertyValue("okButtonClass", okButtonClass);
    }

    public void setCancelButtonStyle(ValueExpression cancelButtonStyle) {
        getDelegate().setPropertyValue("cancelButtonStyle", cancelButtonStyle);
    }

    public void setCancelButtonClass(ValueExpression cancelButtonClass) {
        getDelegate().setPropertyValue("cancelButtonClass", cancelButtonClass);
    }

    public void setDeleteButtonStyle(ValueExpression deleteButtonStyle) {
        getDelegate().setPropertyValue("deleteButtonStyle", deleteButtonStyle);
    }

    public void setDeleteButtonClass(ValueExpression deleteButtonClass) {
        getDelegate().setPropertyValue("deleteButtonClass", deleteButtonClass);
    }

    public void setCentered(ValueExpression centered) {
        getDelegate().setPropertyValue("centered", centered);
    }
}
