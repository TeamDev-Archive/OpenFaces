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

package org.openfaces.renderkit.select;

import org.openfaces.component.select.OUISelectManyInputBase;
import org.openfaces.component.select.SelectManyCheckbox;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.openfaces.renderkit.select.SelectManyInputImageManager.getCurrentImageUrl;

/**
 * @author Oleg Marshalenko
 */
public class SelectManyCheckboxRenderer extends SelectManyInputRenderer {

    protected void renderWithHtmlElements(FacesContext facesContext,
                                          ResponseWriter writer,
                                          OUISelectManyInputBase selectManyInputBase,
                                          List<SelectItem> selectItems,
                                          boolean isLineLayout) throws IOException {
        renderComponent(facesContext, writer,
                (SelectManyCheckbox) selectManyInputBase,
                selectItems, isLineLayout, false);
    }

    protected void renderWithImages(FacesContext facesContext,
                                    ResponseWriter writer,
                                    OUISelectManyInputBase selectManyInputBase,
                                    List<SelectItem> selectItems,
                                    boolean isLineLayout) throws IOException {
        renderComponent(facesContext, writer,
                (SelectManyCheckbox) selectManyInputBase,
                selectItems, isLineLayout, true);
    }

    private void renderComponent(FacesContext facesContext,
                                 ResponseWriter writer,
                                 SelectManyCheckbox selectManyInputBase,
                                 List<SelectItem> selectItems,
                                 boolean isLineLayout,
                                 boolean isCheckboxWithImages) throws IOException {
        SelectManyCheckbox selectManyCheckbox = (SelectManyCheckbox) selectManyInputBase;
        String clientId = selectManyCheckbox.getClientId(facesContext);

        List<List<SelectItem>> itemsDistribution =
                calculateItemsDistribution(selectItems, isLineLayout, selectManyCheckbox);

        int itemsNumber = 0;
        for (int currentRow = 0; currentRow < itemsDistribution.size(); currentRow++) {
            writer.startElement("tr", selectManyCheckbox);
            for (int currentColumn = 0; currentColumn < itemsDistribution.get(0).size(); currentColumn++) {
                writer.startElement("td", selectManyCheckbox);
                if (currentColumn >= itemsDistribution.get(currentRow).size()) {
                    writer.endElement("td");
                    continue;
                }
                String id = getIdIndexed(clientId, itemsNumber++);
                if (isCheckboxWithImages) {
                    renderOneCheckboxWithImage(facesContext, writer, selectManyCheckbox,
                            clientId, currentRow, currentColumn, id, itemsDistribution);
                } else {
                    renderOneCheckboxWithHtml(writer, selectManyCheckbox,
                            clientId, currentRow, currentColumn, id, itemsDistribution);
                }
                writer.endElement("td");
            }
            writer.endElement("tr");
        }
    }

    private void renderOneCheckboxWithImage(FacesContext facesContext,
                                            ResponseWriter writer,
                                            SelectManyCheckbox selectManyCheckbox,
                                            String clientId,
                                            int currentRow, int currentColumn,
                                            String id,
                                            List<List<SelectItem>> itemsDistribution) throws IOException {
        SelectItem selectItem = itemsDistribution.get(currentRow).get(currentColumn);

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
    }

    private void renderOneCheckboxWithHtml(ResponseWriter writer,
                                           SelectManyCheckbox selectManyCheckbox,
                                           String clientId,
                                           int currentRow,
                                           int currentColumn, String id,
                                           List<List<SelectItem>> itemsDistribution) throws IOException {
        SelectItem selectItem = itemsDistribution.get(currentRow).get(currentColumn);
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
    }

    private List<List<SelectItem>> calculateItemsDistribution(List<SelectItem> selectItems, boolean isLineLayout, SelectManyCheckbox selectManyCheckbox) {
        Integer columns = selectManyCheckbox.getColumns();
        Integer rows = selectManyCheckbox.getRows();
        if (rows == null && columns == null) {
            if (isLineLayout) {
                rows = 1;
            } else {
                columns = 1;
            }
        }
        if (rows != null && columns != null) {
            throw new FacesException("The 'rows' and 'columns' attributes cannot be set together.");
        }
        List<List<SelectItem>> itemsDistribution = new ArrayList<List<SelectItem>>();
        if (columns != null) {
            itemsDistribution = distributeItemsIntoColumns(selectItems, isLineLayout, columns);
        } else if (rows != null) {
            itemsDistribution = distributeItemsIntoRows(selectItems, isLineLayout, rows);
        }
        return itemsDistribution;
    }

    private List<List<SelectItem>> distributeItemsIntoColumns(List<SelectItem> items, boolean isLineLayout, int columns) {
        List<List<SelectItem>> table = new ArrayList<List<SelectItem>>();

        if (isLineLayout) {
            int currentColumn = 0;
            int currentRow = 0;
            table.add(new ArrayList<SelectItem>());

            for (SelectItem item : items) {
                if (currentColumn == columns) {
                    currentColumn = 0;
                    currentRow++;
                    table.add(currentRow, new ArrayList<SelectItem>());
                }
                table.get(currentRow).add(currentColumn++, item);
            }
        } else {
            int rows = items.size() / columns;
            if (items.size() % columns != 0) {
                rows += 1;
            }

            for (int i = 0; i < rows; i++) {
                table.add(i, new ArrayList<SelectItem>());
            }
            int currentRow = 0;
            int currentColumn = 0;
            for (SelectItem item : items) {
                if (currentRow == rows) {
                    currentRow = 0;
                    currentColumn++;
                }
                table.get(currentRow++).add(currentColumn, item);
            }
        }
        return table;
    }

    private List<List<SelectItem>> distributeItemsIntoRows(List<SelectItem> items, boolean isLineLayout, int rows) {
        List<List<SelectItem>> table = new ArrayList<List<SelectItem>>();

        if (isLineLayout) {
            int columns = items.size() / rows;
            if (items.size() % rows != 0) {
                columns += 1;
            }
            int currentColumn = 0;
            int currentRow = 0;
            table.add(new ArrayList<SelectItem>());
            for (SelectItem item : items) {
                if (currentColumn == columns) {
                    currentColumn = 0;
                    currentRow++;
                    table.add(currentRow, new ArrayList<SelectItem>());
                }
                table.get(currentRow).add(currentColumn++, item);
            }
        } else {
            for (int i = 0; i < rows; i++) {
                table.add(new ArrayList<SelectItem>());
            }
            int currentRow = 0;
            for (SelectItem item : items) {
                if (currentRow == rows) currentRow = 0;
                table.get(currentRow++).add(item);
            }
        }
        return table;
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
                Resources.utilJsURL(facesContext),
                Resources.internalURL(facesContext, "select/manycheckbox.js"));
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
