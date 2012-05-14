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

import org.openfaces.component.select.TwoListSelection;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.HTML;
import org.openfaces.util.Log;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.openfaces.renderkit.select.SelectUtil.collectSelectItems;

/**
 * @author Kharchenko
 */
public class TwoListSelectionRenderer extends RendererBase {
    public static final String LEFT_LIST_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "left";
    public static final String RIGHT_LIST_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "right";

    private static final String LEFT_LISTBOX_SELECTION_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "left_listBox_selection";
    private static final String RIGHT_LISTBOX_SELECTION_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "right_listBox_selection";
    private static final String SELECTED_ITEMS_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "selected_items";

    public static final String ADD_ALL_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "select_all";
    public static final String ADD_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "select";
    public static final String REMOVE_ALL_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "remove_all";
    public static final String REMOVE_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "remove";
    public static final String MOVE_UP_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "up";
    public static final String MOVE_DOWN_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "down";

    public static final String LEFT_LIST_HEADER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "left_caption";
    public static final String RIGHT_LIST_HEADER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "right_caption";

    private static final String SORT_ASC_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "sort_asc";
    private static final String SORT_DESC_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "sort_desc";

    private static final String CAPTION_CLASS = "o_tls_caption";
    private static final String LIST_CLASS = "o_tls_list";
    private static final String DISABLED_OPTION_CLASS = "o_tls_disabled_option";

    private static final String DEFAULT_CLASS = "o_twoListSelection";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        TwoListSelection tls = (TwoListSelection) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", tls);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        String clientId = tls.getClientId(context);
        writer.writeAttribute("id", clientId, null);
        String styleClass = Styles.getCSSClass(context, tls, tls.getStyle(), DEFAULT_CLASS, tls.getStyleClass());
        if (tls.isDisabled()) {
            String disabledTwoListSelectionStyleClass = Styles.getCSSClass(context, tls, tls.getDisabledStyle(),
                    StyleGroup.disabledStyleGroup(), tls.getDisabledClass(), null);

            if (Rendering.isNullOrEmpty(tls.getDisabledStyle()) && Rendering.isNullOrEmpty(tls.getDisabledClass())) {
                styleClass = Styles.mergeClassNames(disabledTwoListSelectionStyleClass, styleClass);
            } else {
                styleClass = Styles.mergeClassNames(disabledTwoListSelectionStyleClass, Styles.getCSSClass(context, tls, null
                        , StyleGroup.regularStyleGroup(), null, DEFAULT_CLASS));
            }
        }
        writer.writeAttribute("class", styleClass, null);
        Rendering.writeStandardEvents(writer, tls);

        writer.startElement("col", tls);
        writer.writeAttribute("style", "width: 50%;", null);
        writer.endElement("col");
        writer.startElement("col", tls);
        writer.writeAttribute("style", "width: 1px;", null);
        writer.endElement("col");
        writer.startElement("col", tls);
        writer.writeAttribute("style", "width: 50%;", null);
        writer.endElement("col");
        if (tls.getReorderingAllowed()) {
            writer.startElement("col", tls);
            writer.writeAttribute("style", "width: 1px;", null);
            writer.endElement("col");
        }

        renderHeaderRowIfNecessary(tls, context);

        writer.startElement("tr", tls);
        writer.startElement("td", tls);
        writer.writeAttribute("valign", "top", null);

        renderLeftList(tls, context);

        writer.endElement("td");
        writer.startElement("td", tls);
        renderButtons(context, tls);
        writer.endElement("td");
        writer.startElement("td", tls);
        writer.writeAttribute("valign", "top", null);
        renderRightList(tls, context);
        writer.endElement("td");

        if (tls.getReorderingAllowed()) {
            writer.startElement("td", tls);
            renderOrderingButtons(context, tls);
            writer.endElement("td");
        }

    }

    private void renderHeaderRowIfNecessary(TwoListSelection tls, FacesContext context) throws IOException {
        if (!isHeaderRowNeeded(tls)) return;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", tls);
        writer.startElement("td", tls);
        writer.writeAttribute("id", tls.getClientId(context) + LEFT_LIST_HEADER_SUFFIX, null);
        String captionStyleClass = getCaptionStyleClass(context, tls);
        writer.writeAttribute("class", captionStyleClass, null);

        String caption = tls.getLeftListboxHeader();
        renderLeftCaption(context, caption);

        writer.endElement("td");
        writer.startElement("td", tls);
        writer.write(HTML.NBSP_ENTITY);
        writer.endElement("td");
        writer.startElement("td", tls);

        renderRightCaption(tls, context);

        writer.endElement("td");
        if (tls.getReorderingAllowed()) {
            writer.startElement("td", tls);
            writer.write(HTML.NBSP_ENTITY);
            writer.endElement("td");
        }
        writer.endElement("tr");
    }

    private String getCaptionStyleClass(FacesContext context, TwoListSelection tls) {
        String captionStyleClass = Styles.getCSSClass(context, tls, tls.getHeaderStyle(), CAPTION_CLASS, tls.getHeaderClass());
        if (tls.isDisabled()) {
            String disabledHeaderStyleClass = Styles.getCSSClass(context, tls, tls.getDisabledHeaderStyle(),
                    StyleGroup.disabledStyleGroup(), tls.getDisabledHeaderClass(), null);

            if (Rendering.isNullOrEmpty(tls.getDisabledHeaderStyle()) && Rendering.isNullOrEmpty(tls.getDisabledHeaderClass())) {
                captionStyleClass = Styles.mergeClassNames(disabledHeaderStyleClass, captionStyleClass);
            } else {
                captionStyleClass = Styles.mergeClassNames(disabledHeaderStyleClass, Styles.getCSSClass(context, tls, null
                        , StyleGroup.regularStyleGroup(), null, CAPTION_CLASS));
            }
        }
        return captionStyleClass;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return Rendering.getConvertedUISelectManyValue(context, (UISelectMany) component, submittedValue);
    }

    private void renderRightList(TwoListSelection tls, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = tls.getClientId(context);
        writer.startElement("select", tls);
        writer.writeAttribute("id", clientId + RIGHT_LIST_SUFFIX, null);
        writer.writeAttribute("name", clientId + RIGHT_LIST_SUFFIX, null);
        writeAttribute(writer, "tabindex", tls.getTabindex());
        String listStyleClass = getListStyleClass(context, tls);
        writer.writeAttribute("class", listStyleClass, null);
        writer.writeAttribute("multiple", "multiple", null);
        if (tls.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        List<SelectItem> items = getSelectedItems(tls);
        renderListItems(tls, writer, items);
        writer.endElement("select");
    }

    private void renderListItems(TwoListSelection tls, ResponseWriter writer, List<SelectItem> items) throws IOException {
        int size = tls.getSize();
        if (size < 2) {
            writer.writeAttribute("size", "10", null);
        } else {
            writer.writeAttribute("size", String.valueOf(size), null);
        }

        if (items == null)
            return;

        boolean customDisabledItemStyle = Environment.isExplorer() || Environment.isUndefinedBrowser();
        for (SelectItem item : items) {
            writer.startElement("option", tls);
            //Here is converter processing
            String value = getFormattedValue(tls, item.getValue());
            writeAttribute(writer, "value", value);
            String title = getFormattedValue(tls, item.getDescription());
            writeAttribute(writer, "title", title);
            if (item.isDisabled()) {
                if (customDisabledItemStyle) {
                    writer.writeAttribute("class", DISABLED_OPTION_CLASS, null);
                }
                writer.writeAttribute("disabled", "disabled", null);
            }
            writer.write(item.getLabel());
            writer.endElement("option");
        }
    }

    private String getFormattedValue(UIComponent component, Object value) {
        String result;
        Converter converter = null;
        FacesContext context = FacesContext.getCurrentInstance();
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifier.

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
                Log.log("No converter was specified for TwoListSelection with id \"" + component.getClientId(context) +
                        "\", and item value type is not String: " + value.getClass().getName());
                result = value.toString();
                return result;
            }
        }

        result = converter.getAsString(context, component, value);
        return result;
    }

    private Converter getConverterForClass(Class converterClass, FacesContext context) {
        if (converterClass == null) {
            return null;
        }
        try {
            Application application = context.getApplication();
            return application.createConverter(converterClass);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private void renderLeftList(TwoListSelection tls, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = tls.getClientId(context);
        // left list rendering itself
        writer.startElement("select", tls);
        writer.writeAttribute("id", clientId + LEFT_LIST_SUFFIX, null);
        writer.writeAttribute("name", clientId + LEFT_LIST_SUFFIX, null);
        writeAttribute(writer, "tabindex", tls.getTabindex());
        String listStyleClass = getListStyleClass(context, tls);
        writer.writeAttribute("class", listStyleClass, null);
        writer.writeAttribute("multiple", "multiple", null);
        if (tls.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        List<SelectItem> items = getNotSelectedItems(tls);
        renderListItems(tls, writer, items);

        writer.endElement("select");
    }

    private String getListStyleClass(FacesContext context, TwoListSelection tls) {
        String listStyleClass = Styles.getCSSClass(context, tls, tls.getListStyle(), LIST_CLASS, tls.getListClass());
        if (tls.isDisabled()) {
            String disabledListStyleClass = Styles.getCSSClass(context, tls, tls.getDisabledListStyle(),
                    StyleGroup.disabledStyleGroup(), tls.getDisabledListClass(), null);

            if (Rendering.isNullOrEmpty(tls.getDisabledListStyle()) && Rendering.isNullOrEmpty(tls.getDisabledListClass())) {
                listStyleClass = Styles.mergeClassNames(disabledListStyleClass, listStyleClass);
            } else {
                listStyleClass = Styles.mergeClassNames(disabledListStyleClass, Styles.getCSSClass(context, tls, null
                        , StyleGroup.regularStyleGroup(), null, LIST_CLASS));
            }
        }
        return listStyleClass;
    }

    private boolean isHeaderRowNeeded(TwoListSelection tls) {
        return !Rendering.isNullOrEmpty(tls.getLeftListboxHeader()) || !Rendering.isNullOrEmpty(tls.getLeftListboxHeader()) || tls.isSortingAllowed();
    }

    private void renderRightCaption(TwoListSelection tls, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = tls.getClientId(context);
        boolean allowSorting = tls.isSortingAllowed();
        writer.startElement("table", tls);
        writer.writeAttribute("id", tls.getClientId(context) + RIGHT_LIST_HEADER_SUFFIX, null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("style", "width: 100%;", null);
        String captionStyleClass = getCaptionStyleClass(context, tls);
        writer.writeAttribute("class", captionStyleClass, null);
        writer.startElement("tr", tls);
        writer.startElement("td", tls);

        String caption = tls.getRightListboxHeader();
        if (!Rendering.isNullOrEmpty(caption)) {
            writer.write(caption);
        }

        writer.endElement("td");
        writer.startElement("td", tls);

        if (allowSorting) {
            writer.startElement("img", tls);
            writer.writeAttribute("id", clientId + SORT_ASC_SUFFIX, null);
            String ascImage = Resources.internalURL(context, "select/ascending.gif");
            writer.writeAttribute("src", ascImage, null);
            writer.writeAttribute("style", "display: none;", null);
            writer.endElement("img");

            writer.startElement("img", tls);
            writer.writeAttribute("id", clientId + SORT_DESC_SUFFIX, null);
            String desImage = Resources.internalURL(context, "select/descending.gif");
            writer.writeAttribute("src", desImage, null);
            writer.writeAttribute("style", "display: none;", null);
            writer.endElement("img");
        }
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void renderLeftCaption(FacesContext context, String caption) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (!Rendering.isNullOrEmpty(caption)) {
            writer.write(caption);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        TwoListSelection tls = (TwoListSelection) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", tls);
        String leftSelected = tls.getLeftListboxSelectedItems();
        String clientId = tls.getClientId(context);

        writer.startElement("input", tls);
        writer.writeAttribute("id", clientId + LEFT_LISTBOX_SELECTION_SUFFIX, null);
        writer.writeAttribute("name", clientId + LEFT_LISTBOX_SELECTION_SUFFIX, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", leftSelected == null ? "" : leftSelected, null);
        writer.endElement("input");

        String rightSelected = tls.getRightListboxSelectedItems();

        writer.startElement("input", tls);
        writer.writeAttribute("id", clientId + RIGHT_LISTBOX_SELECTION_SUFFIX, null);
        writer.writeAttribute("name", clientId + RIGHT_LISTBOX_SELECTION_SUFFIX, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", rightSelected == null ? "" : rightSelected, null);
        writer.endElement("input");

        writer.startElement("input", tls);
        writer.writeAttribute("id", clientId + SELECTED_ITEMS_SUFFIX, null);
        writer.writeAttribute("name", clientId + SELECTED_ITEMS_SUFFIX, null);
        writer.writeAttribute("type", "hidden", null);
        List selectedItems;
        if (tls.getSubmittedValue() != null) {
            selectedItems = getSelectedItemsFromSubmittedValue(tls);
        } else {
            selectedItems = getSelectedItems(tls);
        }
        StringBuffer result = new StringBuffer();
        if (selectedItems != null && selectedItems.size() > 0) {
            Object[] items = selectedItems.toArray();
            for (int i = 0; i < items.length; i++) {
                SelectItem item = (SelectItem) items[i];
                result.append(item.getValue().toString());
                if (i < items.length)
                    result.append(",");
            }
            writer.writeAttribute("value", result.toString(), null);
        }

        writer.endElement("input");

        renderInitScript(context, tls);

        Styles.renderStyleClasses(context, tls);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private List getSelectedItemsFromSubmittedValue(TwoListSelection tls) {
        List<SelectItem> allItems = collectSelectItems(tls);
        if (allItems == null) return Collections.EMPTY_LIST;
        List<SelectItem> result = new ArrayList<SelectItem>();
        String[] submittedValues = (String[]) tls.getSubmittedValue();
        for (SelectItem item : allItems) {
            String stringValue = item.getValue().toString();
            for (String submittedValue : submittedValues) {
                if (submittedValue.equals(stringValue)) {
                    result.add(item);
                    break;
                }
            }
        }
        return result;
    }

    private void renderInitScript(FacesContext context, UIComponent component) throws IOException {
        TwoListSelection tls = (TwoListSelection) component;

        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, tls, "O$.TwoListSelection._init",
                Rendering.getEventsParam(tls, "add", "remove", "change"),
                tls.isAllowAddRemoveAll(),
                tls.getReorderingAllowed(),
                tls.isDisabled(),
                Rendering.getRolloverClass(context, tls));

        Rendering.renderInitScript(context, sb,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "select/twoListSelection.js"));
        Rendering.encodeClientActions(context, component);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    private List<SelectItem> getSelectedItems(TwoListSelection tls) {
        List<SelectItem> allItems = collectSelectItems(tls);
        if (allItems == null) return null;

        List<SelectItem> result = new ArrayList<SelectItem>();

        List value = getValueAsList(tls.getValue());

        Map<Object, SelectItem> itemsMap = new HashMap<Object, SelectItem>();
        for (SelectItem item : allItems) {
            itemsMap.put(item.getValue(), item);
        }

        if (value != null) {
            for (Object itemValue : value) {
                if (itemsMap.containsKey(itemValue)) {
                    result.add(itemsMap.get(itemValue));
                }
            }
        }
        return result.size() > 0 ? result : null;
    }

    private List<SelectItem> getNotSelectedItems(TwoListSelection tls) {
        List<SelectItem> allItems = collectSelectItems(tls);
        if (allItems == null) return null;

        List<SelectItem> result = new ArrayList<SelectItem>();
        List value = getValueAsList(tls.getValue());

        for (SelectItem item : allItems) {
            if ((value != null && !value.contains(item.getValue())) || value == null) {
                result.add(item);
            }
        }
        return result.size() > 0 ? result : null;
    }

    private List getValueAsList(Object value) {
        if (value == null) return null;

        if (value instanceof Object[]) {
            return Arrays.asList((Object[]) value);
        } else if (value instanceof List) {
            return (List) value;
        } else {
            throw new IllegalArgumentException("Value property of TwoListSelection component is not of type List or Array. Value is : " + value);
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (!component.isRendered()) return;
        Rendering.decodeBehaviors(context, component);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context);
        String key = clientId + LEFT_LISTBOX_SELECTION_SUFFIX;
        String value = params.get(key);
        ((TwoListSelection) component).setLeftListboxSelectedItems(value);

        key = clientId + RIGHT_LISTBOX_SELECTION_SUFFIX;
        value = params.get(key);
        ((TwoListSelection) component).setRightListboxSelectedItems(value);

        key = clientId + SELECTED_ITEMS_SUFFIX;
        value = params.get(key);

        String[] vals = new String[]{};
        if (value != null && value.length() > 0) {
            vals = getArrayFromString(value);
        }
        ((TwoListSelection) component).setSubmittedValue(vals);
    }

    private void encodeButton(
            FacesContext context,
            TwoListSelection tls,
            String idSuffix,
            String text,
            String hint,
            boolean disabled) throws IOException {
        String tlsClientId = tls.getClientId(context);
        String buttonClientId = tlsClientId + idSuffix;

        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement("input", tls);
        responseWriter.writeAttribute("id", buttonClientId, null);
        responseWriter.writeAttribute("type", "button", null);
        responseWriter.writeAttribute("title", hint, null);
        writeAttribute(responseWriter, "tabindex", tls.getTabindex());
        responseWriter.writeAttribute("value", text, null);
        responseWriter.writeAttribute("disabled", Boolean.toString(disabled), null);

        String buttonStyleClass = Styles.getCSSClass(context, tls, tls.getButtonStyle(), StyleGroup.regularStyleGroup(), tls.getButtonClass(),
                null);

        if (tls.isDisabled()) {
            String disabledCalendarStyleClass = Styles.getCSSClass(context, tls, tls.getDisabledButtonStyle(),
                    StyleGroup.disabledStyleGroup(), tls.getDisabledButtonClass(), null);

            if (Rendering.isNullOrEmpty(tls.getDisabledButtonStyle()) && Rendering.isNullOrEmpty(tls.getDisabledButtonClass())) {
                buttonStyleClass = Styles.mergeClassNames(disabledCalendarStyleClass, buttonStyleClass);
            } else {
                buttonStyleClass = disabledCalendarStyleClass;
            }
        }

        writeAttribute(responseWriter, "class", buttonStyleClass);

//    String buttonStyle = tls.getButtonStyle();
//    if (buttonStyle != null) {
//      responseWriter.writeAttribute(HTML.STYLE_ATTR, buttonStyle, null);
//    }
//    String buttonClass = tls.getButtonClass();
//    if (buttonClass != null) {
//      responseWriter.writeAttribute(HTML.CLASS_ATTR, buttonClass, null);
//    }

        responseWriter.endElement("input");
    }

    private void renderButtons(FacesContext context, UIComponent component) throws IOException {
        TwoListSelection tls = (TwoListSelection) component;
        ResponseWriter writer = context.getResponseWriter();
        boolean allowAll = tls.isAllowAddRemoveAll();
        writer.startElement("table", component);
        writer.writeAttribute("style", "text-align: center;", null);
        if (allowAll) {
            writer.startElement("tr", component);
            writer.startElement("td", component);
            encodeButton(context, tls, ADD_ALL_BUTTON_SUFFIX, tls.getAddAllText(), tls.getAddAllHint(), tls.isDisabled());
            writer.endElement("td");
            writer.endElement("tr");
        }
        writer.startElement("tr", component);
        writer.startElement("td", component);
        encodeButton(context, tls, ADD_BUTTON_SUFFIX, tls.getAddText(), tls.getAddHint(), tls.isDisabled());
        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", component);
        writer.startElement("td", component);
        encodeButton(context, tls, REMOVE_BUTTON_SUFFIX, tls.getRemoveText(), tls.getRemoveHint(), tls.isDisabled());
        writer.endElement("td");
        writer.endElement("tr");
        if (allowAll) {
            writer.startElement("tr", component);
            writer.startElement("td", component);
            encodeButton(context, tls, REMOVE_ALL_BUTTON_SUFFIX, tls.getRemoveAllText(), tls.getRemoveAllHint(), tls.isDisabled());
            writer.endElement("td");
            writer.endElement("tr");
        }
        writer.endElement("table");
    }

    private void renderOrderingButtons(FacesContext context, UIComponent component) throws IOException {
        TwoListSelection tls = (TwoListSelection) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", component);
        writer.writeAttribute("style", "text-align: center;", null);
        writer.startElement("tr", component);
        writer.startElement("td", component);
        encodeButton(context, tls, MOVE_UP_BUTTON_SUFFIX, tls.getMoveUpText(), tls.getMoveUpHint(), true);
        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", component);
        writer.startElement("td", component);
        encodeButton(context, tls, MOVE_DOWN_BUTTON_SUFFIX, tls.getMoveDownText(), tls.getMoveDownHint(), true);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private String[] getArrayFromString(String string) {
        List<String> result = new ArrayList<String>();
        int idx = string.indexOf(",");
        if (idx == -1) {
            result.add(AjaxUtil.unescapeSymbol(string));
        } else {
            while (idx != -1) {
                String part = string.substring(0, idx);
                result.add(AjaxUtil.unescapeSymbol(part.trim()));
                string = string.substring(idx + 1);
                idx = string.indexOf(",");
            }
            string = AjaxUtil.unescapeSymbol(string);
            result.add(string.trim());
        }
        String[] resultArray = new String[result.size()];
        result.toArray(resultArray);
        if (resultArray.length == 0) {
            return new String[]{};
        }
        return resultArray;
    }
}
