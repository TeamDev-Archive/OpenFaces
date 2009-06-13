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
package org.openfaces.renderkit.timetable;

import org.openfaces.component.input.DateChooser;
import org.openfaces.component.timetable.DayTable;
import org.openfaces.component.timetable.EventEditorDialog;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.renderkit.TableRenderer;
import org.openfaces.renderkit.CompoundComponentRenderer;
import org.openfaces.renderkit.window.WindowRenderer;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.HTML;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.FunctionCallScript;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialogRenderer extends WindowRenderer implements CompoundComponentRenderer {
    public void createSubComponents(FacesContext context, UIComponent component) {
        EventEditorDialog dialog = (EventEditorDialog) component;
        HtmlOutputText captionText = ComponentUtil.createOutputText(context, "");
        captionText.setId(dialog.getId() + RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + "caption");
        dialog.setCaption(captionText);
    }

    @Override
    protected String getDefaultClassName() {
        return StyleUtil.mergeClassNames(super.getDefaultClassName(), "o_eventEditorDialog");
    }

    @Override
    protected String getDefaultModalLayerClass() {
        return StyleUtil.mergeClassNames(super.getDefaultModalLayerClass(), "o_eventEditor_modalLayer");
    }

    @Override
    protected String getDefaultContentClass() {
        return StyleUtil.mergeClassNames(super.getDefaultContentClass(), "o_eventEditorDialogContent");
    }

    @Override
    protected void encodeCustomContent(FacesContext context, PopupLayer popupLayer) throws IOException {
        final EventEditorDialog dialog = (EventEditorDialog) popupLayer;
        final DayTable dayTable = (DayTable) dialog.getParent();
        final UIComponent[][] components = new UIComponent[][]{
                {
                        ComponentUtil.composeHtmlOutputText(context, popupLayer, "nameLabel", dialog.getNameLabel()),
                        getNameField(context, popupLayer)
                },
                {
                        ComponentUtil.composeHtmlOutputText(context, popupLayer, "startLabel", dialog.getStartLabel()),
                        createDateTimeFields(context, popupLayer, "start")
                },
                {
                        ComponentUtil.composeHtmlOutputText(context, popupLayer, "endLabel", dialog.getEndLabel()),
                        createDateTimeFields(context, popupLayer, "end")
                },
                {
                        ComponentUtil.composeHtmlOutputText(context, popupLayer, "descriptionLabel", dialog.getDescriptionLabel()),
                },
                {
                        getDescriptionField(context, popupLayer)
                },
                {
                        null
                }
        };

        new TableRenderer() {

            @Override
            protected void writeTableAttributes(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
                super.writeTableAttributes(context, writer, component);
                writer.writeAttribute("class", "o_fullWidthAndHeight", null);
            }

            @Override
            protected void writeRowAttributes(ResponseWriter writer, int rowIndex) throws IOException {
                if (rowIndex == 4) {
                    writer.writeAttribute("style", "height: 100%;", null);
                }
            }

            @Override
            protected void writeCellAttributes(ResponseWriter writer, int rowIndex, int cellIndex) throws IOException {
                super.writeCellAttributes(writer, rowIndex, cellIndex);
                if (rowIndex < 3 && cellIndex == 0)
                    writer.writeAttribute("style", "width: 0", null);
                if (rowIndex == 4)
                    writer.writeAttribute("style", "vertical-align: top;", null);
            }

            @Override
            protected void encodeCellContents(FacesContext context, ResponseWriter writer, UIComponent component, int rowIndex, int colIndex) throws IOException {
                super.encodeCellContents(context, writer, component, rowIndex, colIndex);
                if (rowIndex == components.length - 1) {
                    writer.startElement("div", component);
                    writer.writeAttribute("class", "o_eventEditor_buttonsArea", null);

                    HtmlCommandButton deleteButton = ComponentUtil.createButtonFacet(context, dialog, "deleteButton", dialog.getDeleteButtonText());
                    deleteButton.setStyle("float: left");
                    deleteButton.encodeAll(context);

                    HtmlCommandButton okButton = ComponentUtil.createButtonFacet(context, dialog, "okButton", dialog.getOkButtonText());
                    okButton.encodeAll(context);
                    HtmlCommandButton cancelButton = ComponentUtil.createButtonFacet(context, dialog, "cancelButton", dialog.getCancelButtonText());
                    writer.write(HTML.NBSP_ENTITY);
                    cancelButton.encodeAll(context);
                    writer.endElement("div");

                    RenderingUtil.renderInitScript(context, new ScriptBuilder().functionCall("O$._initEventEditorDialog",
                            dayTable,
                            dialog,
                            dialog.getNewEventCaption(),
                            dialog.getEditEventCaption()).semicolon());
                }
            }

        }.render(popupLayer, components);
    }

    @Override
    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);
        RenderingUtil.renderInitScript(context, new ScriptBuilder().functionCall("O$.fixInputsWidthStrict", 
                new FunctionCallScript("O$", component)));

    }

    private UIComponent getDescriptionField(FacesContext context, PopupLayer popupLayer) {
        HtmlInputTextarea descriptionField = (HtmlInputTextarea) RenderingUtil.getOrCreateFacet(context, popupLayer,
                HtmlInputTextarea.COMPONENT_TYPE, "descriptionField", HtmlInputTextarea.class);
        descriptionField.setStyleClass("o_fullWidthAndHeight");
        descriptionField.setStyle("resize: none");
        return descriptionField;
    }

    private UIComponent getNameField(FacesContext context, PopupLayer popupLayer) {
        HtmlInputText nameField = (HtmlInputText) RenderingUtil.getOrCreateFacet(context, popupLayer, HtmlInputText.COMPONENT_TYPE, "nameField", HtmlInputText.class);
        nameField.setStyleClass("o_fullWidth");
        return nameField;
    }

    private UIComponent createDateTimeFields(FacesContext context, PopupLayer popupLayer, String idPrefix) {
        HtmlPanelGrid container = (HtmlPanelGrid) RenderingUtil.getOrCreateFacet(context, popupLayer,
                HtmlPanelGrid.COMPONENT_TYPE, idPrefix + "Fields", HtmlPanelGrid.class);
        container.setCellpadding("0");
        container.setCellspacing("0");
        container.setBorder(0);
        container.setColumns(3);
        List<UIComponent> children = container.getChildren();
        children.clear();

        children.add(RenderingUtil.getOrCreateFacet(context, popupLayer, DateChooser.COMPONENT_TYPE, idPrefix + "DateField", DateChooser.class));
        children.add(ComponentUtil.createOutputText(context, HTML.NBSP_ENTITY, false));
        HtmlInputText timeField = (HtmlInputText) RenderingUtil.getOrCreateFacet(context, popupLayer, HtmlInputText.COMPONENT_TYPE, idPrefix + "TimeField", HtmlInputText.class);
        timeField.setStyle("width: 50px");

        children.add(timeField);
        return container;
    }

}
