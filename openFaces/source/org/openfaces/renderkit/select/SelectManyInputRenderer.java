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

import org.openfaces.component.select.OUISelectManyInputBase;
import org.openfaces.component.select.SelectItem;
import org.openfaces.component.select.SelectItems;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import static org.openfaces.renderkit.select.SelectManyInputImageManager.*;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
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

/**
 * @author Oleg Marshalenko
 */
public abstract class SelectManyInputRenderer extends RendererBase {

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

    protected static final String SPACE = " ";

    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        OUISelectManyInputBase selectManyInputBase = (OUISelectManyInputBase) component;
        ComponentUtil.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
        if (!component.isRendered()) return;
        renderSelectManyInputComponent(context, selectManyInputBase);
    }

    private void renderSelectManyInputComponent(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = selectManyInputBase.getClientId(facesContext);

        writer.startElement("table", selectManyInputBase);
        writeAttribute(writer, "id", clientId);

        if (selectManyInputBase.getBorder() != null && selectManyInputBase.getBorder().trim().length() > 0) {
            writeAttribute(writer, "border", selectManyInputBase.getBorder() + "px");
        }

        if (!OUISelectManyInputBase.LAYOUT_LINE_DIRECTION.equals(selectManyInputBase.getLayout()) &&
            !OUISelectManyInputBase.LAYOUT_PAGE_DIRECTION.equals(selectManyInputBase.getLayout())) {
            throw new RuntimeException("Attribute 'layout' should be value one of the: 'lineDirection' or 'pageDirection'.");
        }
        boolean isLineLayout = OUISelectManyInputBase.LAYOUT_LINE_DIRECTION.equals(selectManyInputBase.getLayout());

        List<SelectItem> selectItems = collectSelectItems(selectManyInputBase);
        JSONObject imagesObj = null;
        if (isRenderedWithImage(selectManyInputBase)) {
            imagesObj = new JSONObject();
            try {
                JSONObject selectedImagesObj = new JSONObject();
                selectedImagesObj.put(PLAIN_EFFECT, getSelectedImageUrl(facesContext, selectManyInputBase));
                selectedImagesObj.put(ROLLOVER_EFFECT, getRolloverSelectedImageUrl(facesContext, selectManyInputBase));
                selectedImagesObj.put(PRESSED_EFFECT, getPressedSelectedImageUrl(facesContext, selectManyInputBase));
                selectedImagesObj.put(DISABLED_EFFECT, getDisabledSelectedImageUrl(facesContext, selectManyInputBase));
                imagesObj.put(SELECTED_STATE, selectedImagesObj);

                JSONObject unselectedImagesObj = new JSONObject();
                unselectedImagesObj.put(PLAIN_EFFECT, getUnselectedImageUrl(facesContext, selectManyInputBase));
                unselectedImagesObj.put(ROLLOVER_EFFECT, getRolloverUnselectedImageUrl(facesContext, selectManyInputBase));
                unselectedImagesObj.put(PRESSED_EFFECT, getPressedUnselectedImageUrl(facesContext, selectManyInputBase));
                unselectedImagesObj.put(DISABLED_EFFECT, getDisabledUnselectedImageUrl(facesContext, selectManyInputBase));
                imagesObj.put(UNSELECTED_STATE, unselectedImagesObj);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            renderWithImages(facesContext, writer, selectManyInputBase, selectItems, isLineLayout);
        } else {
            renderWithHtmlElements(facesContext, writer, selectManyInputBase, selectItems, isLineLayout);
        }
        if (isLineLayout) {
            writer.startElement("td", selectManyInputBase);
        } else {
            writer.startElement("tr", selectManyInputBase);
            writer.startElement("td", selectManyInputBase);
        }
        addStyleClassesAndJS(facesContext, selectManyInputBase, selectItems, imagesObj);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    protected abstract void renderWithImages(FacesContext facesContext, ResponseWriter writer, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, boolean isLineLayout) throws IOException;

    protected abstract void renderWithHtmlElements(FacesContext facesContext, ResponseWriter writer, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, boolean isLineLayout) throws IOException;

    protected String getIdIndexed(String clientId, int index) {
        return clientId + ":" + index;
    }

    private List<SelectItem> collectSelectItems(OUISelectManyInputBase selectManyInputBase) {
        Collection<UIComponent> children = selectManyInputBase.getChildren();
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

    protected boolean isRenderedWithImage(OUISelectManyInputBase selectManyInputBase) {
        return hasImages(selectManyInputBase);
    }

    protected void writeCommonAttributes(ResponseWriter writer, OUISelectManyInputBase selectManyInputBase, SelectItem selectItem) throws IOException {
        writeAttribute(writer, "title", selectManyInputBase.getTitle());
        writeAttribute(writer, "accesskey", selectManyInputBase.getAccesskey());
        writeAttribute(writer, "dir", selectManyInputBase.getDir());
        writeAttribute(writer, "lang", selectManyInputBase.getLang());
        writeAttribute(writer, "onselect", selectManyInputBase.getOnselect());
        writeAttribute(writer, "tabindex", selectManyInputBase.getTabindex());

        if (!selectManyInputBase.isDisabled() && !selectManyInputBase.isReadonly() && !selectItem.isItemDisabled()) {
            RenderingUtil.writeStandardEvents(writer, selectManyInputBase);
        }
    }

    protected void writeLabelText(ResponseWriter writer, SelectItem selectItem) throws IOException {
        writer.writeText(SPACE + selectItem.getItemLabel(), null);    
    }

    private void addStyleClassesAndJS(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, JSONObject imagesObj) throws IOException {
        String styleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getStyle(), StyleGroup.regularStyleGroup(), selectManyInputBase.getStyleClass(), null);
        String enabledStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getEnabledStyle(), StyleGroup.regularStyleGroup(1), selectManyInputBase.getEnabledClass(), null);
        String disabledStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getDisabledStyle(), StyleGroup.regularStyleGroup(2), selectManyInputBase.getDisabledClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getFocusedStyle(), StyleGroup.regularStyleGroup(3), selectManyInputBase.getFocusedClass(), null);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getRolloverStyle(), StyleGroup.regularStyleGroup(4), selectManyInputBase.getRolloverClass(), null);

        String selectedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getSelectedItemStyle(), StyleGroup.regularStyleGroup(5), selectManyInputBase.getSelectedItemClass(), null);
        String focusedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getFocusedItemStyle(), StyleGroup.regularStyleGroup(6), selectManyInputBase.getFocusedItemClass(), null);
        String rolloverItemStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getRolloverItemStyle(), StyleGroup.regularStyleGroup(7), selectManyInputBase.getRolloverItemClass(), null);
        String pressedItemStyleClass = StyleUtil.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getPressedItemStyle(), StyleGroup.regularStyleGroup(8), selectManyInputBase.getPressedItemClass(), null);

        StyleUtil.renderStyleClasses(facesContext, selectManyInputBase);

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
        String onchange = selectManyInputBase.getOnchange();

        if (onchange != null) {
            onchangeFunction = new AnonymousFunction(onchange, "event");
        }

        renderInitScript(facesContext, selectManyInputBase, imagesObj, stylesObj, selectItems.size(), onchangeFunction);
    }

    protected abstract void renderInitScript(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase,
            JSONObject imagesObj, JSONObject stylesObj, int selectItemCount, AnonymousFunction onchangeFunction)
            throws IOException;
}
