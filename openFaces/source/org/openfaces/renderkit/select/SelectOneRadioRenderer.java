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
import org.openfaces.component.select.SelectOneRadio;
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
public class SelectOneRadioRenderer extends SelectManyInputRenderer {

    protected void renderWithImages(FacesContext facesContext, ResponseWriter writer, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, boolean isLineLayout) throws IOException {
        SelectOneRadio selectOneRadio = (SelectOneRadio) selectManyInputBase;
        String clientId = selectOneRadio.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", selectOneRadio);
        }

        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", selectOneRadio);
            }
            writer.startElement("td", selectOneRadio);

            String id = getIdIndexed(clientId, i);

            String imageId = id + IMAGE_SUFFIX;

            writer.startElement(TAG_NAME, selectManyInputBase);
            writeAttribute(writer, "id", imageId);
            writeAttribute(writer, "type", "image");
            writeAttribute(writer, "src", getCurrentImageUrl(facesContext, selectOneRadio, selectItem));
            writeCommonAttributes(writer, selectOneRadio, selectItem);
            writer.endElement(TAG_NAME);

            writer.startElement(TAG_NAME, selectOneRadio);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "type", "radio");

            writeAttribute(writer, "value", getFormattedValue(selectOneRadio, selectItem.getValue()));
            writeAttribute(writer, "style", "display: none;");

            if (selectOneRadio.isDisabled() || selectItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectOneRadio, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectOneRadio);
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

    protected void renderWithHtmlElements(FacesContext facesContext, ResponseWriter writer, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, boolean isLineLayout) throws IOException {
        SelectOneRadio selectOneRadio = (SelectOneRadio) selectManyInputBase;
        String clientId = selectOneRadio.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", selectOneRadio);
        }

        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", selectOneRadio);
            }
            writer.startElement("td", selectOneRadio);

            String id = getIdIndexed(clientId, i);
            writer.startElement(TAG_NAME, selectOneRadio);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "type", "radio");
            writeAttribute(writer, "value", getFormattedValue(selectOneRadio, selectItem.getValue()));
            writeCommonAttributes(writer, selectOneRadio, selectItem);
            if (selectOneRadio.isDisabled() || selectItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectOneRadio, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writeAttribute(writer, "onchange", selectOneRadio.getOnchange());
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectOneRadio);
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
        SelectOneRadio selectOneRadio = (SelectOneRadio) component;

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        String clientId = selectOneRadio.getClientId(context);

        if (requestMap.containsKey(clientId)) {
            String requestValue = requestMap.get(clientId);
            selectOneRadio.setSubmittedValue(requestValue);
        }
    }

    protected void renderInitScript(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase,
                                    JSONObject imagesObj, JSONObject stylesObj, int selectItemCount, AnonymousFunction onchangeFunction)
            throws IOException {
        SelectOneRadio selectOneRadio = (SelectOneRadio) selectManyInputBase;
        Script initScript = new ScriptBuilder().initScript(facesContext, selectOneRadio, "O$.Radio._init",
                imagesObj,
                stylesObj,
                selectItemCount,
                selectOneRadio.isDisabled(),
                selectOneRadio.isReadonly(),
                onchangeFunction
        );

        Rendering.renderInitScript(facesContext, initScript,
                Resources.getUtilJsURL(facesContext),
                Resources.getInternalURL(facesContext, SelectOneRadioRenderer.class, "radio.js"));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isValueEquals(SelectOneRadio selectOneRadio, SelectItem selectItem) {
        return selectOneRadio.getValue() != null &&
                selectOneRadio.getValue().equals(selectItem.getValue());
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return Rendering.convertFromString(context, component, (String) submittedValue);
    }

}
