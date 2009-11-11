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

package org.openfaces.renderkit.select;

import org.openfaces.component.select.SelectItem;
import org.openfaces.component.select.SelectItems;
import org.openfaces.component.select.SelectOneRadio;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import static org.openfaces.renderkit.select.SelectOneRadioImageManager.*;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marshalenko
 */
public class SelectOneRadioRenderer extends RendererBase {

    protected static final String TAG_NAME = "input";

    protected static final String IMAGE_SUFFIX = "::image";
    protected static final String LABEL_SUFFIX = "::label";

    protected static final String SELECTED_STATE = "on";
    protected static final String UNSELECTED_STATE = "off";

    protected static final String PLAIN_EFFECT = "plain";
    protected static final String ROLLOVER_EFFECT = "rollover";
    protected static final String PRESSED_EFFECT = "pressed";
    protected static final String DISABLED_EFFECT = "disabled";

    protected static final String STYLE_CLASS_KEY = "styleClass";
    protected static final String ENABLED_CLASS_KEY = "enabledClass";
    protected static final String DISABLED_CLASS_KEY = "disabledClass";
    protected static final String ROLLOVER_CLASS_KEY = "rolloverClass";
    protected static final String FOCUSED_CLASS_KEY = "focusedClass";

    protected static final String ROLLOVER_ITEM_CLASS_KEY = "rolloverItemClass";
    protected static final String PRESSED_ITEM_CLASS_KEY = "pressedItemClass";
    protected static final String FOCUSED_ITEM_CLASS_KEY = "focusedItemClass";
    protected static final String SELECTED_ITEM_CLASS_KEY = "selectedItemClass";

    protected static final String DEFAULT_IMAGE_CLASS = "o_radio_image";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectOneRadio selectOneRadio = (SelectOneRadio) component;
        ComponentUtil.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
        if (!component.isRendered()) return;
        renderSelectOneRadioComponent(context, selectOneRadio);
    }

    private void renderSelectOneRadioComponent(FacesContext facesContext, SelectOneRadio selectOneRadio) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = selectOneRadio.getClientId(facesContext);

        writer.startElement("table", selectOneRadio);
        writeAttribute(writer, "id", clientId);

        if (selectOneRadio.getBorder() != null && selectOneRadio.getBorder().trim().length() > 0) {
            writeAttribute(writer, "border", selectOneRadio.getBorder() + "px");
        }

        if (!SelectOneRadio.LAYOUT_LINE_DIRECTION.equals(selectOneRadio.getLayout()) &&
            !SelectOneRadio.LAYOUT_PAGE_DIRECTION.equals(selectOneRadio.getLayout())) {
            throw new RuntimeException("Attribute 'layout' should be value one of the: 'lineDirection' or 'pageDirection'.");
        }
        boolean isLineLayout = SelectOneRadio.LAYOUT_LINE_DIRECTION.equals(selectOneRadio.getLayout());

        List<SelectItem> selectItems = collectSelectItems(selectOneRadio);
        JSONObject imagesObj = null;
        if (isRenderedWithImage(selectOneRadio)) {
            imagesObj = new JSONObject();
            try {
                JSONObject selectedImagesObj = new JSONObject();
                selectedImagesObj.put(PLAIN_EFFECT, getSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(ROLLOVER_EFFECT, getRolloverSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(PRESSED_EFFECT, getPressedSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(DISABLED_EFFECT, getDisabledSelectedImageUrl(facesContext, selectOneRadio));
                imagesObj.put(SELECTED_STATE, selectedImagesObj);

                JSONObject unselectedImagesObj = new JSONObject();
                unselectedImagesObj.put(PLAIN_EFFECT, getUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(ROLLOVER_EFFECT, getRolloverUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(PRESSED_EFFECT, getPressedUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(DISABLED_EFFECT, getDisabledUnselectedImageUrl(facesContext, selectOneRadio));
                imagesObj.put(UNSELECTED_STATE, unselectedImagesObj);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            renderWithImage(facesContext, writer, selectOneRadio, selectItems, isLineLayout);
        } else {
            renderWithHtmlRadioButton(facesContext, writer, selectOneRadio, selectItems, isLineLayout);
        }
        if (isLineLayout) {
            writer.startElement("td", selectOneRadio);
        } else {
            writer.startElement("tr", selectOneRadio);
            writer.startElement("td", selectOneRadio);
        }
        addStyleClassesAndJS(facesContext, selectOneRadio, selectItems, imagesObj);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void renderWithImage(FacesContext facesContext, ResponseWriter writer, SelectOneRadio selectOneRadio, List<SelectItem> selectItems, boolean isLineLayout) throws IOException {
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

            writer.startElement(TAG_NAME, selectItem);
            writeAttribute(writer, "id", imageId);
            writeAttribute(writer, "type", "image");
            writeAttribute(writer, "src", getCurrentImageUrl(facesContext, selectOneRadio, selectItem));
            writeCommonAttributes(writer, selectOneRadio, selectItem);
            writer.endElement(TAG_NAME);

            writer.startElement(TAG_NAME, selectOneRadio);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "type", "radio");
            writeAttribute(writer, "value", selectItem.getItemValue().toString());
            writeAttribute(writer, "style", "display: none;");

            if (selectOneRadio.isDisabled() || selectItem.isItemDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectOneRadio, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectItem);
            String labelId = id + LABEL_SUFFIX;
            writeAttribute(writer, "id", labelId);
            writeAttribute(writer, "for", imageId);
            writer.writeText(selectItem.getItemLabel(), selectItem.getItemLabel());
            writer.endElement("label");
            writer.endElement("td");

            if (!isLineLayout) {
                writer.endElement("tr");
            }
        }
    }

    protected void renderWithHtmlRadioButton(FacesContext facesContext, ResponseWriter writer, SelectOneRadio selectOneRadio, List<SelectItem> selectItems, boolean isLineLayout) throws IOException {
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
            writeAttribute(writer, "value", selectItem.getItemValue().toString());
            writeCommonAttributes(writer, selectOneRadio, selectItem);
            if (selectOneRadio.isDisabled() || selectItem.isItemDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }
            if (isValueEquals(selectOneRadio, selectItem)) {
                writeAttribute(writer, "checked", "checked");
            }
            writeAttribute(writer, "onchange", selectOneRadio.getOnchange());
            writer.endElement(TAG_NAME);

            writer.startElement("label", selectItem);
            String labelId = id + LABEL_SUFFIX;
            writeAttribute(writer, "id", labelId);
            writeAttribute(writer, "for", id);
            writer.writeText(selectItem.getItemLabel(), selectItem.getItemLabel());
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

    private boolean isValueEquals(SelectOneRadio selectOneRadio, SelectItem selectItem) {
        return selectOneRadio.getValue() != null &&
               selectOneRadio.getValue().equals(selectItem.getItemValue());
    }

    private String getIdIndexed(String clientId, int index) {
        return clientId + ":" + index;
    }

    private List<SelectItem> collectSelectItems(SelectOneRadio selectOneRadio) {
        Collection<UIComponent> children = selectOneRadio.getChildren();
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (UIComponent child : children) {
            Collection<SelectItem> tmpCollection = getItemsFromComponent(child);
            if (tmpCollection != null) {
                items.addAll(tmpCollection);
            }
        }
        return items;
    }

    private Collection<SelectItem> getItemsFromComponent(UIComponent component) {
        if (component instanceof SelectItem) {
            return Collections.singletonList((SelectItem) component);
        }

        if (!(component instanceof SelectItems))
            return null;

        Object itemsValue = ((SelectItems) component).getValue();
        if (itemsValue == null)
            return null;

        if (itemsValue.getClass().isArray())
            itemsValue = Arrays.asList((Object[]) itemsValue);
        if (!(itemsValue instanceof Collection))
            throw new IllegalArgumentException("The 'value' attribute of <o:selectItems> tag should contain either " +
                    "an array or a Collection, but the following type was encountered: " + itemsValue.getClass().getName());

        Collection itemCollection = (Collection) itemsValue;
        Collection<SelectItem> items = new ArrayList<SelectItem>(itemCollection.size());
        for (Object collectionItem : itemCollection) {
            if (collectionItem instanceof SelectItem)
                items.add((SelectItem) collectionItem);
            else
                items.add(new SelectItem(collectionItem));
        }
        return items;
    }

    protected boolean isRenderedWithImage(SelectOneRadio selectOneRadio) {
        return hasImages(selectOneRadio);
    }

    protected void writeCommonAttributes(ResponseWriter writer, SelectOneRadio selectOneRadio, SelectItem selectItem) throws IOException {
        writeAttribute(writer, "title", selectOneRadio.getTitle());
        writeAttribute(writer, "accesskey", selectOneRadio.getAccesskey());
        writeAttribute(writer, "dir", selectOneRadio.getDir());
        writeAttribute(writer, "lang", selectOneRadio.getLang());
        writeAttribute(writer, "onselect", selectOneRadio.getOnselect());
        writeAttribute(writer, "tabindex", selectOneRadio.getTabindex());

        if (!selectOneRadio.isDisabled() && !selectOneRadio.isReadonly() && !selectItem.isItemDisabled()) {
            RenderingUtil.writeStandardEvents(writer, selectOneRadio);
        }
    }

    private void addStyleClassesAndJS(FacesContext facesContext, SelectOneRadio selectOneRadio, List<SelectItem> selectItems, JSONObject imagesObj) throws IOException {
        String styleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getStyle(), StyleGroup.regularStyleGroup(), selectOneRadio.getStyleClass(), null);
        String enabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getEnabledStyle(), StyleGroup.regularStyleGroup(1), selectOneRadio.getEnabledClass(), null);
        String disabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getDisabledStyle(), StyleGroup.regularStyleGroup(2), selectOneRadio.getDisabledClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getFocusedStyle(), StyleGroup.regularStyleGroup(3), selectOneRadio.getFocusedClass(), null);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getRolloverStyle(), StyleGroup.regularStyleGroup(4), selectOneRadio.getRolloverClass(), null);

        String selectedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getSelectedItemStyle(), StyleGroup.regularStyleGroup(5), selectOneRadio.getSelectedItemClass(), null);
        String focusedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getFocusedItemStyle(), StyleGroup.regularStyleGroup(6), selectOneRadio.getFocusedItemClass(), null);
        String rolloverItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getRolloverItemStyle(), StyleGroup.regularStyleGroup(7), selectOneRadio.getRolloverItemClass(), null);
        String pressedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getPressedItemStyle(), StyleGroup.regularStyleGroup(8), selectOneRadio.getPressedItemClass(), null);

        StyleUtil.renderStyleClasses(facesContext, selectOneRadio);

        JSONObject stylesObj = new JSONObject();
        try {
            stylesObj.put(STYLE_CLASS_KEY, styleClass);
            stylesObj.put(ENABLED_CLASS_KEY, enabledStyleClass);
            stylesObj.put(DISABLED_CLASS_KEY, disabledStyleClass);
            stylesObj.put(ROLLOVER_CLASS_KEY, rolloverStyleClass);
            stylesObj.put(FOCUSED_CLASS_KEY, focusedStyleClass);

            stylesObj.put(ROLLOVER_ITEM_CLASS_KEY, rolloverItemStyleClass);
            stylesObj.put(FOCUSED_ITEM_CLASS_KEY, focusedItemStyleClass);
            stylesObj.put(SELECTED_ITEM_CLASS_KEY, selectedItemStyleClass);
            stylesObj.put(PRESSED_ITEM_CLASS_KEY, pressedItemStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AnonymousFunction onchangeFunction = null;
        String onchange = selectOneRadio.getOnchange();

        if (onchange != null) {
            onchangeFunction = new AnonymousFunction(onchange, "event");
        }

        renderInitScript(facesContext, selectOneRadio, imagesObj, stylesObj, selectItems.size(), onchangeFunction);
    }

    protected void renderInitScript(FacesContext facesContext, SelectOneRadio selectOneRadio,
            JSONObject imagesObj, JSONObject stylesObj, int selectItemCount, AnonymousFunction onchangeFunction)
            throws IOException {

        Script initScript = new ScriptBuilder().initScript(facesContext, selectOneRadio, "O$.Radio._init",
                imagesObj,
                stylesObj,
                selectItemCount,
                selectOneRadio.isDisabled(),
                selectOneRadio.isReadonly(),
                onchangeFunction
        );

        RenderingUtil.renderInitScript(facesContext, initScript,
                new String[] {
                ResourceUtil.getUtilJsURL(facesContext),
                ResourceUtil.getInternalResourceURL(facesContext, SelectOneRadioRenderer.class, "radio.js")
            }
        );
    }


}
