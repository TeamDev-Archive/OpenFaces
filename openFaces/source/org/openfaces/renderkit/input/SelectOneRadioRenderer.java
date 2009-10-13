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

package org.openfaces.renderkit.input;

import org.openfaces.renderkit.RendererBase;
import static org.openfaces.renderkit.input.SelectOneRadioImageManager.*;
import org.openfaces.util.*;
import org.openfaces.component.input.SelectOneRadio;
import org.openfaces.component.input.SelectOneRadioItem;
import org.openfaces.component.input.SelectOneRadioItems;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONArray;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.*;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 5:06:15 PM
 */
public class SelectOneRadioRenderer extends RendererBase {

    protected static final String TAG_NAME = "input";

    protected static final String TABLE_SUFFIX = "::table";
    protected static final String IMAGE_SUFFIX = "::image";

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
    protected static final String SELECTED_CLASS_KEY = "selectedClass";
    protected static final String UNSELECTED_CLASS_KEY = "unselectedClass";
    protected static final String PRESSED_CLASS_KEY = "pressedClass";

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

        writer.startElement("div", null);
        writeAttribute(writer, "id", clientId);
        writeAttribute(writer, "style", "display: table;");

        writer.startElement("table", null);
        if (selectOneRadio.getBorder() != null && selectOneRadio.getBorder().trim().length() > 0) {
            writeAttribute(writer, "border", selectOneRadio.getBorder() + "px");
        }

        if (!SelectOneRadio.LAYOUT_LINE_DIRECTION.equals(selectOneRadio.getLayout()) &&
            !SelectOneRadio.LAYOUT_PAGE_DIRECTION.equals(selectOneRadio.getLayout())) {
            throw new RuntimeException("Attribute 'layout' should be value one of the: 'lineDirection' or 'pageDirection'.");
        }
        boolean isLineLayout = SelectOneRadio.LAYOUT_LINE_DIRECTION.equals(selectOneRadio.getLayout());

        if (isRenderedWithImage(selectOneRadio)) {
            renderWithImage(facesContext, writer, selectOneRadio, isLineLayout);
        } else {
            renderWithHtmlRadioButton(facesContext, writer, selectOneRadio, isLineLayout);
        }

        writer.endElement("div");
    }

    private void renderWithImage(FacesContext facesContext, ResponseWriter writer, SelectOneRadio selectOneRadio, boolean isLineLayout) throws IOException {
        String styleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getStyle(), StyleGroup.regularStyleGroup(), selectOneRadio.getStyleClass(), null);
        String enabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getEnabledStyle(), StyleGroup.regularStyleGroup(1), selectOneRadio.getEnabledClass(), null);
        String disabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getDisabledStyle(), StyleGroup.regularStyleGroup(2), selectOneRadio.getDisabledClass(), null);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getRolloverStyle(), StyleGroup.regularStyleGroup(3), selectOneRadio.getRolloverClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getFocusedStyle(), StyleGroup.regularStyleGroup(4), selectOneRadio.getFocusedClass(), null);
        String selectedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getSelectedStyle(), StyleGroup.regularStyleGroup(5), selectOneRadio.getSelectedClass(), null);
        String unselectedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getUnselectedStyle(), StyleGroup.regularStyleGroup(6), selectOneRadio.getUnselectedClass(), null);

        //<table id="id_0">
        //  <tr>
        //    <td>
        //      <input id="id_0::image" type="image" src="image_link"/>
        //      <input id="id_0" name="id" type="radio" name="form:subscriptions" value="" style="display:none;"/>
        //      <label for="id">Label</label>
        //    </td>
        //    ...
        //  </tr>
        //</table>

        String clientId = selectOneRadio.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", null);
        }

        List<SelectOneRadioItem> selectOneRadioItems = collectSelectOneRadioItems(selectOneRadio);
        JSONArray stylesItemObjs = new JSONArray();
        for (int i = 0; i < selectOneRadioItems.size(); i++) {
            SelectOneRadioItem selectOneRadioItem = selectOneRadioItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", null);
            }
            writer.startElement("td", selectOneRadioItem);

            String id = getIdIndexed(clientId, i);

            String imageId = id + IMAGE_SUFFIX;

            writer.startElement(TAG_NAME, selectOneRadioItem);
            writeAttribute(writer, "type", "image");
            writeAttribute(writer, "id", imageId);
            writeAttribute(writer, "src", getCurrentImageUrl(facesContext, selectOneRadio, selectOneRadioItem));

            writeCommonAttributes(writer, selectOneRadio);

            writer.endElement(TAG_NAME);

            writer.startElement(TAG_NAME, selectOneRadio);
            writeAttribute(writer, "type", "radio");

            writeAttribute(writer, "name", clientId);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "value", selectOneRadioItem.getValue().toString());
            writeAttribute(writer, "style", "display: none;");

            if (selectOneRadio.isDisabled() || selectOneRadioItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }

            if (isValueEquals(selectOneRadio, selectOneRadioItem)) {
                writeAttribute(writer, "checked", "checked");
            }

            writer.endElement(TAG_NAME);

            writer.startElement("label", selectOneRadioItem);
            writeAttribute(writer, "for", imageId);
            writer.writeText(selectOneRadioItem.getItemLabel(), selectOneRadioItem.getItemLabel());
            writer.endElement("label");
            writer.endElement("td");

            String rolloverItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getRolloverItemStyle(), StyleGroup.regularStyleGroup(1), selectOneRadioItem.getRolloverItemClass(), null);
            String focusedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getFocusedItemStyle(), StyleGroup.regularStyleGroup(2), selectOneRadioItem.getFocusedItemClass(), null);
            String selectedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getSelectedItemStyle(), StyleGroup.regularStyleGroup(3), selectOneRadioItem.getSelectedItemClass(), null);
            String pressedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getPressedItemStyle(), StyleGroup.regularStyleGroup(3), selectOneRadioItem.getPressedItemClass(), null);

            StyleUtil.renderStyleClasses(facesContext, selectOneRadioItem);

            JSONObject stylesItemObj = new JSONObject();
            try {
                stylesItemObj.put(ROLLOVER_CLASS_KEY, rolloverItemStyleClass);
                stylesItemObj.put(FOCUSED_CLASS_KEY, focusedItemStyleClass);
                stylesItemObj.put(SELECTED_CLASS_KEY, selectedItemStyleClass);
                stylesItemObj.put(PRESSED_CLASS_KEY, pressedItemStyleClass);

                stylesItemObjs.put(stylesItemObj);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            if (!isLineLayout) {
                writer.endElement("tr");
            }
        }
        if (isLineLayout) {
            writer.endElement("tr");
        }

        writer.endElement("table");

        StyleUtil.renderStyleClasses(facesContext, selectOneRadio);

        JSONObject imagesObj = new JSONObject();

        try {
            {
                JSONObject selectedImagesObj = new JSONObject();
                selectedImagesObj.put(PLAIN_EFFECT, getSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(ROLLOVER_EFFECT, getRolloverSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(PRESSED_EFFECT, getPressedSelectedImageUrl(facesContext, selectOneRadio));
                selectedImagesObj.put(DISABLED_EFFECT, getDisabledSelectedImageUrl(facesContext, selectOneRadio));
                imagesObj.put(SELECTED_STATE, selectedImagesObj);
            }
            {
                JSONObject unselectedImagesObj = new JSONObject();
                unselectedImagesObj.put(PLAIN_EFFECT, getUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(ROLLOVER_EFFECT, getRolloverUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(PRESSED_EFFECT, getPressedUnselectedImageUrl(facesContext, selectOneRadio));
                unselectedImagesObj.put(DISABLED_EFFECT, getDisabledUnselectedImageUrl(facesContext, selectOneRadio));
                imagesObj.put(UNSELECTED_STATE, unselectedImagesObj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject stylesObj = new JSONObject();
        try {
            stylesObj.put(STYLE_CLASS_KEY, styleClass);
            stylesObj.put(ENABLED_CLASS_KEY, enabledStyleClass);
            stylesObj.put(DISABLED_CLASS_KEY, disabledStyleClass);
            stylesObj.put(ROLLOVER_CLASS_KEY, rolloverStyleClass);
            stylesObj.put(FOCUSED_CLASS_KEY, focusedStyleClass);
            stylesObj.put(SELECTED_CLASS_KEY, selectedStyleClass);
            stylesObj.put(UNSELECTED_CLASS_KEY, unselectedStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AnonymousFunction onchangeFunction = null;
        String onchange = selectOneRadio.getOnchange();

        if (onchange != null) {
            onchangeFunction = new AnonymousFunction(onchange, "event");
        }

        renderInitScript(facesContext, selectOneRadio, imagesObj, stylesObj, stylesItemObjs, onchangeFunction);

    }

    protected void renderWithHtmlRadioButton(FacesContext facesContext, ResponseWriter writer, SelectOneRadio selectOneRadio, boolean isLineLayout) throws IOException {
        String styleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getStyle(), StyleGroup.regularStyleGroup(), selectOneRadio.getStyleClass(), null);
        String enabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getEnabledStyle(), StyleGroup.regularStyleGroup(1), selectOneRadio.getEnabledClass(), null);
        String disabledStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getDisabledStyle(), StyleGroup.regularStyleGroup(2), selectOneRadio.getDisabledClass(), null);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getRolloverStyle(), StyleGroup.regularStyleGroup(3), selectOneRadio.getRolloverClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getFocusedStyle(), StyleGroup.regularStyleGroup(4), selectOneRadio.getFocusedClass(), null);
        String selectedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getSelectedStyle(), StyleGroup.regularStyleGroup(5), selectOneRadio.getSelectedClass(), null);
        String unselectedStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadio, selectOneRadio.getUnselectedStyle(), StyleGroup.regularStyleGroup(6), selectOneRadio.getUnselectedClass(), null);

        //<table id="form:subscriptions">
        //  <tr>
        //    <td>
        //      <input type="radio" name="form:subscriptions" value="1"/>
        //      <label for="">Label</label>
        //    </td>
        //    ...
        //  </tr>
        //</table>

        String clientId = selectOneRadio.getClientId(facesContext);

        if (isLineLayout) {
            writer.startElement("tr", null);
        }

        JSONArray stylesItemObjs = new JSONArray();
        List<SelectOneRadioItem> selectOneRadioItems = collectSelectOneRadioItems(selectOneRadio);
        for (int i = 0; i < selectOneRadioItems.size(); i++) {
            SelectOneRadioItem selectOneRadioItem = selectOneRadioItems.get(i);

            if (!isLineLayout) {
                writer.startElement("tr", null);
            }
            writer.startElement("td", selectOneRadioItem);

            writer.startElement(TAG_NAME, selectOneRadio);
            writeAttribute(writer, "type", "radio");
            writeAttribute(writer, "name", clientId);

            String id = getIdIndexed(clientId, i);
            writeAttribute(writer, "id", id);
            writeAttribute(writer, "value", selectOneRadioItem.getValue().toString());

            writeCommonAttributes(writer, selectOneRadio);

            if (selectOneRadio.isDisabled() || selectOneRadioItem.isDisabled()) {
                writeAttribute(writer, "disabled", "disabled");
            }

            if (isValueEquals(selectOneRadio, selectOneRadioItem)) {
                writeAttribute(writer, "checked", "checked");
            }

            writeAttribute(writer, "onchange", selectOneRadio.getOnchange());

            writer.endElement(TAG_NAME);

            writer.startElement("label", selectOneRadioItem);
            writeAttribute(writer, "for", id);
            writer.writeText(selectOneRadioItem.getItemLabel(), selectOneRadioItem.getItemLabel());
            writer.endElement("label");
            writer.endElement("td");

            String rolloverItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getRolloverItemStyle(), StyleGroup.regularStyleGroup(1), selectOneRadioItem.getRolloverItemClass(), null);
            String focusedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getFocusedItemStyle(), StyleGroup.regularStyleGroup(2), selectOneRadioItem.getFocusedItemClass(), null);
            String selectedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getSelectedItemStyle(), StyleGroup.regularStyleGroup(3), selectOneRadioItem.getSelectedItemClass(), null);
            String pressedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectOneRadioItem, selectOneRadioItem.getPressedItemStyle(), StyleGroup.regularStyleGroup(3), selectOneRadioItem.getPressedItemClass(), null);

            StyleUtil.renderStyleClasses(facesContext, selectOneRadioItem);

            JSONObject stylesItemObj = new JSONObject();
            try {
                stylesItemObj.put(ROLLOVER_CLASS_KEY, rolloverItemStyleClass);
                stylesItemObj.put(FOCUSED_CLASS_KEY, focusedItemStyleClass);
                stylesItemObj.put(SELECTED_CLASS_KEY, selectedItemStyleClass);
                stylesItemObj.put(PRESSED_CLASS_KEY, pressedItemStyleClass);

                stylesItemObjs.put(stylesItemObj);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            if (!isLineLayout) {
                writer.endElement("tr");
            }
        }
        if (isLineLayout) {
            writer.endElement("tr");
        }
        writer.endElement("table");

        StyleUtil.renderStyleClasses(facesContext, selectOneRadio);

        JSONObject stylesObj = new JSONObject();
        try {
            stylesObj.put(STYLE_CLASS_KEY, styleClass);
            stylesObj.put(ENABLED_CLASS_KEY, enabledStyleClass);
            stylesObj.put(DISABLED_CLASS_KEY, disabledStyleClass);
            stylesObj.put(ROLLOVER_CLASS_KEY, rolloverStyleClass);
            stylesObj.put(FOCUSED_CLASS_KEY, focusedStyleClass);
            stylesObj.put(SELECTED_CLASS_KEY, selectedStyleClass);
            stylesObj.put(UNSELECTED_CLASS_KEY, unselectedStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AnonymousFunction onchangeFunction = null;
        String onchange = selectOneRadio.getOnchange();

        if (onchange != null) {
            onchangeFunction = new AnonymousFunction(onchange, "event");
        }

        renderInitScript(facesContext, selectOneRadio, null, stylesObj, stylesItemObjs, onchangeFunction);

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

    private boolean isValueEquals(SelectOneRadio selectOneRadio, SelectOneRadioItem selectOneRadioItem) {
        return selectOneRadio.getValue() != null &&
               selectOneRadio.getValue().equals(selectOneRadioItem.getValue());
    }

    private String getIdIndexed(String clientId, int index) {
        return clientId + ":" + index;
    }

    private List<SelectOneRadioItem> collectSelectOneRadioItems(SelectOneRadio selectOneRadio) {
        Collection<UIComponent> children = selectOneRadio.getChildren();
        List<SelectOneRadioItem> items = new ArrayList<SelectOneRadioItem>();
        for (UIComponent child : children) {
            Collection<SelectOneRadioItem> tmpCollection = getItemsFromComponent(child);
            if (tmpCollection != null) {
                items.addAll(tmpCollection);
            }
        }
        return items;
    }

    private Collection<SelectOneRadioItem> getItemsFromComponent(UIComponent component) {
        if (component instanceof SelectOneRadioItem) {
            return Collections.singletonList((SelectOneRadioItem) component);
        }

        if (!(component instanceof SelectOneRadioItems))
            return null;

        Object itemsValue = ((SelectOneRadioItems) component).getValue();
        if (itemsValue == null)
            return null;

        if (itemsValue.getClass().isArray())
            itemsValue = Arrays.asList((Object[]) itemsValue);
        if (!(itemsValue instanceof Collection))
            throw new IllegalArgumentException("The 'value' attribute of <o:selectOneRadioItems> tag should contain either " +
                    "an array or a Collection, but the following type was encountered: " + itemsValue.getClass().getName());

        Collection itemCollection = (Collection) itemsValue;
        Collection<SelectOneRadioItem> items = new ArrayList<SelectOneRadioItem>(itemCollection.size());
        for (Object collectionItem : itemCollection) {
            if (collectionItem instanceof SelectOneRadioItem)
                items.add((SelectOneRadioItem) collectionItem);
            else
                items.add(new SelectOneRadioItem(collectionItem));
        }
        return items;
    }

    protected boolean isRenderedWithImage(SelectOneRadio selectOneRadio) {
        return hasImages(selectOneRadio);
    }

    protected void writeCommonAttributes(ResponseWriter writer, SelectOneRadio selectOneRadio) throws IOException {
        writeAttribute(writer, "title", selectOneRadio.getTitle());
        writeAttribute(writer, "accesskey", selectOneRadio.getAccesskey());
        writeAttribute(writer, "dir", selectOneRadio.getDir());
        writeAttribute(writer, "lang", selectOneRadio.getLang());
        writeAttribute(writer, "onselect", selectOneRadio.getOnselect());
        writeAttribute(writer, "tabindex", selectOneRadio.getTabindex());
        writeStandardEvents(writer, selectOneRadio);
    }

    protected void renderInitScript(FacesContext facesContext, SelectOneRadio selectOneRadio,
            JSONObject imagesObj, JSONObject stylesObj, JSONArray stylesItemObjs, AnonymousFunction onchangeFunction)
            throws IOException {

        Script initScript = new ScriptBuilder().initScript(facesContext, selectOneRadio, "O$.Radio._init",
                imagesObj,
                stylesObj,
                stylesItemObjs,
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
