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
package org.openfaces.component.timetable;

import org.openfaces.component.window.PopupLayer;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialog extends PopupLayer {
    public static final String COMPONENT_TYPE = "org.openfaces.EventEditorDialog";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventEditorDialog";

    private String nameLabel;
    private String resourceLabel;
    private String startLabel;
    private String endLabel;
    private String colorLabel;
    private String descriptionLabel;
    private String okButtonText;
    private String cancelButtonText;
    private String deleteButtonText;

    public EventEditorDialog() {
        setRendererType("org.openfaces.EventEditorDialogRenderer");

        setModal(true);
        setDraggable(true);
        setModalLayerClass("o_eventEditor_modalLayer");
        setStyleClass("o_eventEditorDialog");
    }


    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                nameLabel,
                resourceLabel,
                startLabel,
                endLabel,
                colorLabel,
                descriptionLabel,
                okButtonText,
                cancelButtonText,
                deleteButtonText
        };
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        nameLabel = (String) state[i++];
        resourceLabel = (String) state[i++];
        startLabel = (String) state[i++];
        endLabel = (String) state[i++];
        colorLabel = (String) state[i++];
        descriptionLabel = (String) state[i++];
        okButtonText = (String) state[i++];
        cancelButtonText = (String) state[i++];
        deleteButtonText = (String) state[i++];
    }

    public String getNameLabel() {
        return ValueBindings.get(this, "nameLabel", nameLabel, "Name:");
    }

    public void setNameLabel(String value) {
        nameLabel = value;
    }

    public String getResourceLabel() {
        return ValueBindings.get(this, "resourceLabel", resourceLabel, "Resource:");
    }

    public void setResourceLabel(String value) {
        resourceLabel = value;
    }

    public String getStartLabel() {
        return ValueBindings.get(this, "startLabel", startLabel, "Start:");
    }

    public void setStartLabel(String value) {
        startLabel = value;
    }

    public String getEndLabel() {
        return ValueBindings.get(this, "endLabel", endLabel, "End:");
    }

    public void setEndLabel(String value) {
        endLabel = value;
    }

    public String getColorLabel() {
        return ValueBindings.get(this, "colorLabel", colorLabel, "Color:");
    }

    public void setColorLabel(String value) {
        colorLabel = value;
    }

    public String getDescriptionLabel() {
        return ValueBindings.get(this, "descriptionLabel", descriptionLabel, "Description:");
    }

    public void setDescriptionLabel(String value) {
        descriptionLabel = value;
    }

    public String getOkButtonText() {
        return ValueBindings.get(this, "okButtonText", okButtonText, "OK");
    }

    public void setOKButtonText(String value) {
        okButtonText = value;
    }

    public String getCancelButtonText() {
        return ValueBindings.get(this, "cancelButtonText", cancelButtonText, "Cancel");
    }

    public void setCancelButtonText(String value) {
        cancelButtonText = value;
    }

    public String getDeleteButtonText() {
        return ValueBindings.get(this, "deleteButtonText", deleteButtonText, "Delete");
    }

    public void setDeleteButtonText(String value) {
        deleteButtonText = value;
    }


}
