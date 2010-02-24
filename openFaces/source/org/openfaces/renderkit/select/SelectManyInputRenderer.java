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
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import static org.openfaces.renderkit.select.SelectManyInputImageManager.*;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Components.generateIdIfNotSpecified(component);
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

    protected String getFormattedValue(UIComponent component, Object value) {
            String result;
            Converter converter = null;
            FacesContext context = FacesContext.getCurrentInstance();
            // If there is a converter attribute, use it to to ask application
            // instance for a converter with this identifer.

            if (component instanceof ValueHolder) {
                converter = ((ValueHolder) component).getConverter();
            }

            // if value is null and no converter attribute is specified, then
            // return a zero length String.
            if (converter == null && value == null) {
                return "";
            }

            if (converter == null) {
                // Do not look for "by-type" converters for Strings
                if (value instanceof String) {
                    return (String) value;
                }

                // if converter attribute set, try to acquire a converter
                // using its class type.

                Class converterType = value.getClass();
                converter = getConverterForClass(converterType, context);

                // if there is no default converter available for this identifier,
                // assume the model type to be String.
                if (converter == null) {
                    result = value.toString();
                    return result;
                }
            }

            result = converter.getAsString(context, component, value);
            return result;
        }

        private Converter getConverterForClass(Class converterClass,
                                               FacesContext facesContext) {
            if (converterClass == null) {
                return null;
            }
            try {
                Application application = facesContext.getApplication();
                return application.createConverter(converterClass);
            } catch (RuntimeException e) {
                return null;
            }
        }

    private List<SelectItem> collectSelectItems(OUISelectManyInputBase selectManyInputBase) {
        List<javax.faces.model.SelectItem> result = new ArrayList<javax.faces.model.SelectItem>();
        List<UIComponent> children = selectManyInputBase.getChildren();
        if (children != null) {
            for (UIComponent uiComponent : children) {
                if (uiComponent instanceof UISelectItem) {
                    UISelectItem item = (UISelectItem) uiComponent;
                    Object itemValue = item.getValue();
                    javax.faces.model.SelectItem si;
                    if (itemValue != null) {
                        if (!(itemValue instanceof javax.faces.model.SelectItem)) {
                            String clientId = selectManyInputBase.getClientId(FacesContext.getCurrentInstance());
                            throw new IllegalArgumentException(
                                    "The 'value' attribute <f:selectItem> tag should be null or an instance of SelectItem, " +
                                            "but the following type was encountered: " + itemValue.getClass().getName() +
                                            "; Select component client id: " + clientId);
                        }
                        si = (javax.faces.model.SelectItem) itemValue;
                    } else {
                        si = new javax.faces.model.SelectItem(item.getItemValue(), item.getItemLabel(), item.getItemDescription(),
                                item.isItemDisabled());
                    }
                    result.add(si);
                } else if (uiComponent instanceof UISelectItems) {
                    UISelectItems items = (UISelectItems) uiComponent;
                    Object value = items.getValue();
                    if (value != null) {
                        if (value instanceof Collection) {
                            Collection col = (Collection) value;
                            for (Object item : col) {
                                if (item instanceof javax.faces.model.SelectItem) {
                                    result.add((javax.faces.model.SelectItem) item);
                                } else {
                                    String clientId = selectManyInputBase.getClientId(FacesContext.getCurrentInstance());
                                    throw new IllegalArgumentException(
                                            "The items specified inside the <f:selectItems> collection should be of type javax.faces.model.SelectItem, but the following type was encountered: " +
                                                    item.getClass().getName() + "; Select component client id: " + clientId);
                                }
                            }
                        } else if (value instanceof Object[]) {
                            Object[] arrayValue = (Object[]) value;
                            for (Object item : arrayValue) {
                                if (item instanceof javax.faces.model.SelectItem) {
                                    result.add((javax.faces.model.SelectItem) item);
                                } else {
                                    String clientId = selectManyInputBase.getClientId(FacesContext.getCurrentInstance());
                                    throw new IllegalArgumentException(
                                            "The items specified inside the <f:selectItems> array should be of type javax.faces.model.SelectItem, but the following type was encountered: " +
                                                    item.getClass().getName() + "; Select component client id: " + clientId);
                                }
                            }
                        } else if (value instanceof Map) {
                            Map mapValue = (Map) value;
                            Set set = mapValue.entrySet();
                            for (Object aSet : set) {
                                Map.Entry entry = (Map.Entry) aSet;
                                result.add(new javax.faces.model.SelectItem(entry.getValue(), (String) entry.getKey()));
                            }
                        } else {
                            String clientId = selectManyInputBase.getClientId(FacesContext.getCurrentInstance());
                            throw new IllegalArgumentException(
                                    "The 'value' attribute <f:selectItems> tag should be specified as a collection or an array " +
                                            "of SelectItem instances, or as a Map, but the following type was encountered: " +
                                            value.getClass().getName() + "; Select component client id: " + clientId);
                        }
                    }
                }
            }
        }
        return result.size() > 0 ? result : null;
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

        if (!selectManyInputBase.isDisabled() && !selectManyInputBase.isReadonly() && !selectItem.isDisabled()) {
            Rendering.writeStandardEvents(writer, selectManyInputBase);
        }
    }

    protected void writeLabelText(ResponseWriter writer, SelectItem selectItem) throws IOException {
        writer.writeText(SPACE + selectItem.getLabel(), null);    
    }

    private void addStyleClassesAndJS(FacesContext facesContext, OUISelectManyInputBase selectManyInputBase, List<SelectItem> selectItems, JSONObject imagesObj) throws IOException {
        String styleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getStyle(), StyleGroup.regularStyleGroup(), selectManyInputBase.getStyleClass(), null);
        String enabledStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getEnabledStyle(), StyleGroup.regularStyleGroup(1), selectManyInputBase.getEnabledClass(), null);
        String disabledStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getDisabledStyle(), StyleGroup.regularStyleGroup(2), selectManyInputBase.getDisabledClass(), null);
        String focusedStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getFocusedStyle(), StyleGroup.regularStyleGroup(3), selectManyInputBase.getFocusedClass(), null);
        String rolloverStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getRolloverStyle(), StyleGroup.regularStyleGroup(4), selectManyInputBase.getRolloverClass(), null);

        String selectedItemStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getSelectedItemStyle(), StyleGroup.regularStyleGroup(5), selectManyInputBase.getSelectedItemClass(), null);
        String focusedItemStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getFocusedItemStyle(), StyleGroup.regularStyleGroup(6), selectManyInputBase.getFocusedItemClass(), null);
        String rolloverItemStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getRolloverItemStyle(), StyleGroup.regularStyleGroup(7), selectManyInputBase.getRolloverItemClass(), null);
        String pressedItemStyleClass = Styles.getCSSClass(facesContext, selectManyInputBase, selectManyInputBase.getPressedItemStyle(), StyleGroup.regularStyleGroup(8), selectManyInputBase.getPressedItemClass(), null);

        Styles.renderStyleClasses(facesContext, selectManyInputBase);

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
