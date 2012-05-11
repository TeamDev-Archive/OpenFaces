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
package org.openfaces.component.timetable;

import org.openfaces.component.input.DateChooser;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.window.Window;
import org.openfaces.renderkit.CompoundComponentRenderer;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialog extends Window {
    public static final String COMPONENT_TYPE = "org.openfaces.EventEditorDialog";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventEditorDialog";

    private static final String FACET_NAME_FIELD = "nameField";
    private static final String FACET_RESOURCE_FIELD = "resourceField";
    private static final String FACET_START_DATE_FIELD = "startDateField";
    private static final String FACET_END_DATE_FIELD = "endDateField";
    private static final String FACET_START_TIME_FIELD = "startTimeField";
    private static final String FACET_END_TIME_FIELD = "endTimeField";
    private static final String FACET_DESCRIPTION_AREA = "descriptionArea";

    private String createEventCaption;
    private String editEventCaption;

    private Boolean centered;

    private String nameLabel;
    private String resourceLabel;
    private String startLabel;
    private String endLabel;
    private String colorLabel;
    private String descriptionLabel;
    private String okButtonText;
    private String cancelButtonText;
    private String deleteButtonText;

    private String labelStyle;
    private String labelClass;
    private String okButtonStyle;
    private String okButtonClass;
    private String cancelButtonStyle;
    private String cancelButtonClass;
    private String deleteButtonStyle;
    private String deleteButtonClass;


    public EventEditorDialog() {
        setRendererType("org.openfaces.EventEditorDialogRenderer");

        setModal(true);
        setDraggable(true);
    }



    @Override
    protected String getDefaultMinWidth() {
        return "350px";
    }

    @Override
    protected String getDefaultMinHeight() {
        return "350px";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                centered,
                createEventCaption,
                editEventCaption,
                nameLabel,
                resourceLabel,
                startLabel,
                endLabel,
                colorLabel,
                descriptionLabel,
                okButtonText,
                cancelButtonText,
                deleteButtonText,
                labelStyle,
                labelClass,
                okButtonStyle,
                okButtonClass,
                cancelButtonStyle,
                cancelButtonClass,
                deleteButtonStyle,
                deleteButtonClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        centered = (Boolean) state[i++];
        createEventCaption = (String) state[i++];
        editEventCaption = (String) state[i++];
        nameLabel = (String) state[i++];
        resourceLabel = (String) state[i++];
        startLabel = (String) state[i++];
        endLabel = (String) state[i++];
        colorLabel = (String) state[i++];
        descriptionLabel = (String) state[i++];
        okButtonText = (String) state[i++];
        cancelButtonText = (String) state[i++];
        deleteButtonText = (String) state[i++];
        labelStyle = (String) state[i++];
        labelClass = (String) state[i++];
        okButtonStyle = (String) state[i++];
        okButtonClass = (String) state[i++];
        cancelButtonStyle = (String) state[i++];
        cancelButtonClass = (String) state[i++];
        deleteButtonStyle = (String) state[i++];
        deleteButtonClass = (String) state[i++];
    }

    public boolean isCentered() {
        return ValueBindings.get(this, "centered", centered, true);
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public String getCreateEventCaption() {
        return ValueBindings.get(this, "createEventCaption", createEventCaption, "Create Event");
    }

    public void setCreateEventCaption(String createEventCaption) {
        this.createEventCaption = createEventCaption;
    }

    public String getEditEventCaption() {
        return ValueBindings.get(this, "editEventCaption", editEventCaption, "Edit Event");
    }

    public void setEditEventCaption(String editEventCaption) {
        this.editEventCaption = editEventCaption;
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

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        String subComponentsCreatedKey = "_subComponentsCreated";
        if (!getAttributes().containsKey(subComponentsCreatedKey)) {
            getAttributes().put(subComponentsCreatedKey, true);
            createSubComponents(context);
        }
        super.encodeBegin(context);
    }

    private void createSubComponents(FacesContext context) {
        ((CompoundComponentRenderer) getRenderer(context)).createSubComponents(context, this);
    }

    public String getLabelStyle() {
        return ValueBindings.get(this, "labelStyle", labelStyle);
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getLabelClass() {
        return ValueBindings.get(this, "labelClass", labelClass);
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getOkButtonStyle() {
        return ValueBindings.get(this, "okButtonStyle", okButtonStyle);
    }

    public void setOkButtonStyle(String okButtonStyle) {
        this.okButtonStyle = okButtonStyle;
    }

    public String getOkButtonClass() {
        return ValueBindings.get(this, "okButtonClass", okButtonClass);
    }

    public void setOkButtonClass(String okButtonClass) {
        this.okButtonClass = okButtonClass;
    }

    public String getCancelButtonStyle() {
        return ValueBindings.get(this, "cancelButtonStyle", cancelButtonStyle);
    }

    public void setCancelButtonStyle(String cancelButtonStyle) {
        this.cancelButtonStyle = cancelButtonStyle;
    }

    public String getCancelButtonClass() {
        return ValueBindings.get(this, "cancelButtonClass", cancelButtonClass);
    }

    public void setCancelButtonClass(String cancelButtonClass) {
        this.cancelButtonClass = cancelButtonClass;
    }

    public String getDeleteButtonStyle() {
        return ValueBindings.get(this, "deleteButtonStyle", deleteButtonStyle);
    }

    public void setDeleteButtonStyle(String deleteButtonStyle) {
        this.deleteButtonStyle = deleteButtonStyle;
    }

    public String getDeleteButtonClass() {
        return ValueBindings.get(this, "deleteButtonClass", deleteButtonClass);
    }

    public void setDeleteButtonClass(String deleteButtonClass) {
        this.deleteButtonClass = deleteButtonClass;
    }

    public UIInput getNameField() {
        return (UIInput) getFacet(FACET_NAME_FIELD);
    }

    public void setNameField(UIInput nameField) {
        getFacets().put(FACET_NAME_FIELD, nameField);
    }

    public DropDownField getResourceField() {
        return (DropDownField) getFacet(FACET_RESOURCE_FIELD);
    }

    public void setResourceField(DropDownField resourceField) {
        getFacets().put(FACET_RESOURCE_FIELD, resourceField);
    }

    public DateChooser getStartDateField() {
        return (DateChooser) getFacet(FACET_START_DATE_FIELD);
    }

    public void setStartDateField(DateChooser startDateField) {
        getFacets().put(FACET_START_DATE_FIELD, startDateField);
    }

    public DateChooser getEndDateField() {
        return (DateChooser) getFacet(FACET_END_DATE_FIELD);
    }

    public void setEndDateField(DateChooser endDateField) {
        getFacets().put(FACET_END_DATE_FIELD, endDateField);
    }

    public UIInput getStartTimeField() {
        return (UIInput) getFacet(FACET_START_TIME_FIELD);
    }

    public void setStartTimeField(UIInput startTimeField) {
        getFacets().put(FACET_START_TIME_FIELD, startTimeField);
    }

    public UIInput getEndTimeField() {
        return (UIInput) getFacet(FACET_END_TIME_FIELD);
    }

    public void setEndTimeField(UIInput endTimeField) {
        getFacets().put(FACET_END_TIME_FIELD, endTimeField);
    }

    public UIInput getDescriptionArea() {
        return (UIInput) getFacet(FACET_DESCRIPTION_AREA);
    }

    public void setDescriptionArea(UIInput descriptionArea) {
        getFacets().put(FACET_DESCRIPTION_AREA, descriptionArea);
    }


}
