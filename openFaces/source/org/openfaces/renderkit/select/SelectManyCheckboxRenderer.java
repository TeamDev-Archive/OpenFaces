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

package org.openfaces.renderkit.select;

import org.openfaces.component.select.OUISelectManyInputBase;
import org.openfaces.component.select.SelectManyCheckbox;
import org.openfaces.org.json.JSONObject;
import static org.openfaces.renderkit.select.SelectManyInputImageManager.getCurrentImageUrl;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marshalenko
 */
public class SelectManyCheckboxRenderer extends SelectManyInputRenderer {

    protected void renderWithImages(FacesContext facesContext,
                                    ResponseWriter writer,
                                    OUISelectManyInputBase selectManyInputBase,
                                    List<SelectItem> selectItems,
                                    boolean isLineLayout
    ) throws IOException {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) selectManyInputBase;
        String clientId = selectManyCheckbox.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", selectManyCheckbox);
        }

        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", selectManyCheckbox);
            }
            writer.startElement("td", selectManyCheckbox);

            String id = getIdIndexed(clientId, i);

            String imageId = id + IMAGE_SUFFIX;

            writer.startElement(TAG_NAME, selectManyCheckbox);
            writeAttribute(writer, "id", imageId);
            writeAttribute(writer, "type", "image");
            writeAttribute(writer, "src", getCurrentImageUrl(facesContext, selectManyCheckbox, selectItem));
            writeCommonAttributes(writer, selectManyCheckbox, selectItem);
            writer.endElement(TAG_NAME);

            writer.startElement(TAG_NAME, selectManyCheckbox);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "type", "checkbox");
            writeAttribute(writer, "value", getFormattedValue(selectManyCheckbox, selectItem.getValue()));
            writeAttribute(writer, "style", "display: none;");

            if (selectManyCheckbox.isDisabled() || selectItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectManyCheckbox, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectManyCheckbox);
            String labelId = id + LABEL_SUFFIX;
            writeAttribute(writer, "id", labelId);
            writeAttribute(writer, "for", imageId);
            writeLabelText(writer, selectItem);
            writer.endElement("label");
            writer.endElement("td");

            if (!isLineLayout) {
                writer.endElement("tr");
            }
        }
    }

    protected void renderWithHtmlElements(FacesContext facesContext,
                                          ResponseWriter writer,
                                          OUISelectManyInputBase selectManyInputBase,
                                          List<SelectItem> selectItems,
                                          boolean isLineLayout
    ) throws IOException {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) selectManyInputBase;
        String clientId = selectManyCheckbox.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", selectManyCheckbox);
        }

        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", selectManyCheckbox);
            }
            writer.startElement("td", selectManyCheckbox);

            String id = getIdIndexed(clientId, i);
            writer.startElement(TAG_NAME, selectManyCheckbox);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "type", "checkbox");
            writeAttribute(writer, "value", getFormattedValue(selectManyCheckbox, selectItem.getValue()));
            writeCommonAttributes(writer, selectManyCheckbox, selectItem);
            if (selectManyCheckbox.isDisabled() || selectItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectManyCheckbox, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writeAttribute(writer, "onchange", selectManyCheckbox.getOnchange());
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectManyCheckbox);
            String labelId = id + LABEL_SUFFIX;
            writeAttribute(writer, "id", labelId);
            writeAttribute(writer, "for", id);
            writeLabelText(writer, selectItem);
            writer.endElement("label");
            writer.endElement("td");

            if (!isLineLayout) {
                writer.endElement("tr");
            }
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) component;

        Map<String, String[]> requestMap = context.getExternalContext().getRequestParameterValuesMap();

        String clientId = selectManyCheckbox.getClientId(context);

        if (requestMap.containsKey(clientId)) {
            String[] requestValues = requestMap.get(clientId);
            selectManyCheckbox.setSubmittedValue(requestValues);
        } else {
            selectManyCheckbox.setSubmittedValue(new String[0]);
        }
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) component;
        return Rendering.getConvertedUISelectManyValue(context, selectManyCheckbox, submittedValue);
    }


    protected void renderInitScript(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase,
                                    JSONObject imagesObj, JSONObject stylesObj,
                                    int selectItemCount, AnonymousFunction onchangeFunction)
            throws IOException {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) selectManyInputBase;
        Script initScript = new ScriptBuilder().initScript(facesContext, selectManyCheckbox, "O$.ManyCheckbox._init",
                imagesObj,
                stylesObj,
                selectItemCount,
                selectManyCheckbox.isDisabled(),
                selectManyCheckbox.isReadonly(),
                onchangeFunction
        );

        Rendering.renderInitScript(facesContext, initScript,
                Resources.getUtilJsURL(facesContext),
                Resources.getInternalURL(facesContext, SelectOneRadioRenderer.class, "manycheckbox.js"));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isValueEquals(SelectManyCheckbox selectManyCheckbox, SelectItem selectItem) {
        List values = (List) selectManyCheckbox.getSubmittedValue();
        if (values == null) {
            values = (List) selectManyCheckbox.getValue();
        }
        return values != null && values.contains(selectItem.getValue());
    }
}
