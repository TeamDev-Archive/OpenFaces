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

import org.openfaces.component.TableStyles;
import org.openfaces.component.input.DropDownComponent;
import org.openfaces.component.input.DropDownFieldBase;
import org.openfaces.component.input.DropDownItem;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.component.input.DropDownPopup;
import org.openfaces.component.input.SuggestionMode;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.util.InitScript;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.TableStructure;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Andriy Palval
 */
public class DropDownFieldRenderer extends DropDownComponentRenderer implements AjaxPortionRenderer {
    private static final String DEFAULT_LIST_ITEM_CLASS = "o_dropdown_list_item";

    private static final String JS_SCRIPT_URL = "dropDownField.js";

    private static TableStyles POPUP_TABLE_DEFAULT_STYLES = new DefaultTableStyles();

    static {
        POPUP_TABLE_DEFAULT_STYLES.setBodyRowClass(DEFAULT_LIST_ITEM_CLASS);
        POPUP_TABLE_DEFAULT_STYLES.setHorizontalGridLines(null);
    }

    private static final String ITEM_VALUES_ATTR_NAME = "_of_convertedItemValues_";
    public static final String ORIGINAL_VALUE_ATTR = "_of_originalValue";
    public static final String DISPLAYED_VALUE_ATTR = "_of_convertedValue";
    private static final String CURRENT_FIELD_VALUE_ATTR = "_of_currentFieldValue";

    private static final String LOAD_FILTERED_ROWS_PORTION = "filterCriterion:";
    private static final String[] EMPTY_ARRAY = new String[]{};

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
        return RenderingUtil.convertFromString(context, component, (String) submittedValue);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AjaxUtil.prepareComponentForAjax(context, component);
        super.encodeBegin(context, component);
    }

    @Override
    protected void writeFieldAttributes(ResponseWriter writer, DropDownComponent fieldComponent) throws IOException {
        super.writeFieldAttributes(writer, fieldComponent);
        DropDownFieldBase dropDownField = (DropDownFieldBase) fieldComponent;
        writeAttribute(writer, "maxlength", dropDownField.getMaxlength(), Integer.MIN_VALUE);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (AjaxUtil.isAjaxPortionRequest(context, component))
            return;
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        DropDownFieldBase dropDownField = (DropDownFieldBase) component;
        String fieldId = getFieldClientId(context, dropDownField);
        String valueFieldId = getValueFieldId(context, dropDownField);

        String text = requestMap.get(fieldId);
        String value = requestMap.get(valueFieldId);
        if (text == null || value == null)
            RenderingUtil.ensureComponentInsideForm(component);

        String submittedValue;
        if (value == null)
            return;
        if (value.length() == 0) {
            submittedValue = dropDownField.getCustomValueAllowed() ? text : null;
        } else {
            if (!value.startsWith("[") || !value.endsWith("]"))
                throw new IllegalStateException("Illegally formatted value received in request: " + value);
            submittedValue = value.substring(1, value.length() - 1);
        }

        if (submittedValue == null) {
            // from UIInput documentation: If the component wishes to indicate that no particular value was submitted, it can either do nothing, or set the submitted value to null.
            // so we're setting the value as empty string because we the empty submitted value shouldn't be skipped
            submittedValue = "";
        }

        String state = requestMap.get(fieldId + STATE_PROMPT_SUFFIX);
        if ("false".equals(state)) {
            dropDownField.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        DropDownFieldBase dropDownField = (DropDownFieldBase) uiComponent;

        SuggestionMode suggestionMode = dropDownField.getSuggestionMode();
        boolean needRootItemsPreloading;
        if (
                SuggestionMode.ALL.equals(suggestionMode) ||
                        SuggestionMode.STRING_START.equals(suggestionMode) ||
                        SuggestionMode.SUBSTRING.equals(suggestionMode) ||
                        SuggestionMode.STRING_END.equals(suggestionMode)) {
            needRootItemsPreloading = true;
        } else if (SuggestionMode.NONE.equals(suggestionMode)) {
            needRootItemsPreloading = isManualListOpeningAllowed(dropDownField) || dropDownField.getAutoComplete();
        } else if (SuggestionMode.CUSTOM.equals(suggestionMode)) {
            needRootItemsPreloading = isManualListOpeningAllowed(dropDownField);
        } else {
            throw new IllegalStateException("Unknown SuggestionMode enumeration value: " + suggestionMode);
        }
        Collection<DropDownItem> items = needRootItemsPreloading ? collectDropDownItems(dropDownField) : null;
        List<String[]> itemValues = prepareItemValues(context, dropDownField, items);
        if (itemValues != null) {
            dropDownField.getAttributes().put(ITEM_VALUES_ATTR_NAME, itemValues);
        } else {
            dropDownField.getAttributes().remove(ITEM_VALUES_ATTR_NAME);
        }

        DropDownPopup popup = dropDownField.getPopup();
        popup.setDropDownList(items);
        popup.encodeAll(context);
        RenderingUtil.encodeClientActions(context, uiComponent);
    }

    private List<String[]> prepareItemValues(FacesContext context, DropDownFieldBase dropDownField, Collection<DropDownItem> items) {
        String currentValueConverted = RenderingUtil.getStringValue(context, dropDownField);
        String currentValueText = null;

        List<String[]> itemValues = null;
        if (items != null) {
            itemValues = new ArrayList<String[]>(items.size());
            for (DropDownItem item : items) {
                Object itemValue = item.getValue();
                String convertedItemValue = RenderingUtil.convertToString(context, dropDownField, itemValue);

                Map<String, Object> itemAttributes = item.getAttributes();
                itemAttributes.put(ORIGINAL_VALUE_ATTR, itemValue);
                itemAttributes.put(DISPLAYED_VALUE_ATTR, convertedItemValue);
                itemValues.add(new String[]{convertedItemValue, convertedItemValue});

                if (currentValueText == null) {
                    if ((convertedItemValue != null && convertedItemValue.equals(currentValueConverted)) ||
                            (convertedItemValue == null && currentValueConverted == null))
                        currentValueText = convertedItemValue;
                }
            }
        }

        if (currentValueText == null) {
            if (dropDownField.getCustomValueAllowed() || items == null || SuggestionMode.CUSTOM.equals(dropDownField.getSuggestionMode()))
                currentValueText = currentValueConverted;
            else
                currentValueText = "";
        }

        dropDownField.getAttributes().put(CURRENT_FIELD_VALUE_ATTR, currentValueText);
        return itemValues;
    }

    @Override
    protected String getFieldText(FacesContext facesContext, DropDownComponent dropDown) {
        String result = (String) dropDown.getAttributes().get(CURRENT_FIELD_VALUE_ATTR);
        return result;
    }

    private Collection<DropDownItem> getItemsFromComponent(UIComponent component) {
        if (component instanceof DropDownItem) {
            return Collections.singletonList((DropDownItem) component);
        }

        if (!(component instanceof DropDownItems))
            return null;

        Object itemsValue = ((DropDownItems) component).getValue();
        if (itemsValue == null)
            return null;

        if (itemsValue.getClass().isArray())
            itemsValue = Arrays.asList((Object[]) itemsValue);
        if (!(itemsValue instanceof Collection))
            throw new IllegalArgumentException("The 'value' attribute of <o:dropDownItems> tag should contain either " +
                    "an array or a Collection, but the following type was encountered: " + itemsValue.getClass().getName());

        Collection itemCollection = (Collection) itemsValue;
        Collection<DropDownItem> items = new ArrayList<DropDownItem>(itemCollection.size());
        for (Object collectionItem : itemCollection) {
            if (collectionItem instanceof DropDownItem)
                items.add((DropDownItem) collectionItem);
            else
                items.add(new DropDownItem(collectionItem));
        }
        return items;
    }

    private List<DropDownItem> collectDropDownItems(DropDownFieldBase dropDownField) {
        Collection<UIComponent> children = dropDownField.getChildren();
        List<DropDownItem> items = new ArrayList<DropDownItem>();
        for (UIComponent child : children) {
            Collection<DropDownItem> tmpCollection = getItemsFromComponent(child);
            if (tmpCollection != null) {
                items.addAll(tmpCollection);
            }
        }
        return items;
    }

    @Override
    protected void renderEndTags(FacesContext context, DropDownComponent dropDown) throws IOException {
        super.renderEndTags(context, dropDown);
        ResponseWriter responseWriter = context.getResponseWriter();
        String value = dropDown.getValue() != null
                ? "[" + RenderingUtil.getStringValue(context, dropDown) + "]"
                : "";
        RenderingUtil.renderHiddenField(responseWriter, getValueFieldId(context, dropDown), value);
    }

    private String getValueFieldId(FacesContext context, DropDownComponent dropDown) {
        return dropDown.getClientId(context) + "::value";
    }

    protected InitScript renderInitScript(FacesContext context, DropDownComponent dropDown) throws IOException {
        DropDownFieldBase dropDownField = (DropDownFieldBase) dropDown;
        String clientId = dropDownField.getClientId(context);

        DropDownPopup popup = dropDownField.getPopup();

        ScriptBuilder buf = new ScriptBuilder();
        TableStructure tableStructure = popup.getChildData().getTableStructure();
        buf.initScript(context, dropDownField, "O$.DropDownField._init",
                dropDownField.getTimeout(),
                dropDownField.getListAlignment(),

                StyleUtil.getStyleClassesStr(context, dropDownField, dropDownField.getRolloverListItemStyle(),
                        dropDownField.getRolloverListItemClass(), DefaultStyles.getDefaultSelectionStyle(), StyleGroup.rolloverStyleGroup()),

                getItemValuesArray(getItemValues(dropDown)),
                dropDownField.getCustomValueAllowed(),
                dropDownField.isRequired(),
                dropDownField.getSuggestionMode(),
                dropDownField.getSuggestionDelay(),
                dropDownField.getSuggestionMinChars(),
                isManualListOpeningAllowed(dropDownField),
                dropDownField.getAutoComplete(),

                tableStructure.getInitParam(context, POPUP_TABLE_DEFAULT_STYLES)
        );
        popup.resetChildData();
        if (!dropDown.isDisabled()) {  // todo: write the event parameters more economically
            String onValueChanged = dropDownField.getOnchange();
            if (onValueChanged != null) {
                buf.append("O$.DropDownField._setFieldOnChange('");
                buf.append(clientId);
                buf.append("', function(event){");
                buf.append(onValueChanged);
                buf.append("});");
            }
            String onKeyPressed = dropDownField.getOnkeypress();
            if (onKeyPressed != null) {
                buf.append("O$.DropDownField._setOnKeyPress('");
                buf.append(clientId);
                buf.append("', function(event){");
                buf.append(onKeyPressed);
                buf.append("});");
            }
            String onDropDown = dropDownField.getOndropdown();
            if (onDropDown != null) {
                buf.append("O$.DropDownField._setOnDropDown('");
                buf.append(clientId);
                buf.append("', function(event){");
                buf.append(onDropDown);
                buf.append("});");
            }
            String onCloseUp = dropDownField.getOncloseup();
            if (onCloseUp != null) {
                buf.append("O$.DropDownField._setOnCloseUp('");
                buf.append(clientId);
                buf.append("', function(event){");
                buf.append(onCloseUp);
                buf.append("});");
            }
        }
        StyleUtil.renderStyleClasses(context, dropDownField); // encoding styles before scripts is important for tableUtil.js to be able to compute row and column styles correctly
        return new InitScript(buf.toString(), new String[]{
                TableUtil.getTableUtilJsURL(context),
                ResourceUtil.getInternalResourceURL(context,
                        DropDownFieldRenderer.class, DropDownFieldRenderer.JS_SCRIPT_URL)
        });
    }

    @Override
    protected boolean isAutomaticStyleRenderingNeeded() {
        return false;
    }

    protected boolean isManualListOpeningAllowed(DropDownFieldBase dropDownField) {
        return true;
    }

    private List<String[]> getItemValuesArray(List<String[]> itemValues) {
        if (itemValues == null)
            return null;

        List<String[]> result = new ArrayList<String[]>();
        for (String[] itemData : itemValues) {
            String itemValueStr = itemData[0];
            String itemLabelStr = itemData[1];
            if ((itemValueStr != null && itemValueStr.equals(itemLabelStr)) ||
                    (itemValueStr == null && itemLabelStr == null))
                result.add(new String[]{itemValueStr});
            else
                result.add(new String[]{itemValueStr, itemLabelStr});
        }
        return result;
    }

    private List<String[]> getItemValues(DropDownComponent dropDown) {
        return (List<String[]>) dropDown.getAttributes().get(ITEM_VALUES_ATTR_NAME);
    }

    @Override
    protected void release(DropDownComponent dropDown) {
        super.release(dropDown);
        dropDown.getAttributes().remove(ITEM_VALUES_ATTR_NAME);
    }


    public JSONObject encodeAjaxPortion(
            FacesContext context,
            UIComponent component,
            String portionName,
            JSONObject jsonParam
    ) throws IOException {
        if (!portionName.startsWith(LOAD_FILTERED_ROWS_PORTION))
            throw new IllegalArgumentException("Unknown portionName: " + portionName);
        String portionNameSuffix = portionName.substring(LOAD_FILTERED_ROWS_PORTION.length());
        String criterion;
        if (portionNameSuffix.equals("null"))
            criterion = null;
        else {
            if (portionNameSuffix.charAt(0) != '[')
                throw new IllegalArgumentException("Improperly formatted portionName suffix: " + portionNameSuffix);
            criterion = portionNameSuffix.substring(1, portionNameSuffix.length() - 1);
        }

        DropDownFieldBase dropDownField = (DropDownFieldBase) component;

        ResponseWriter responseWriter = context.getResponseWriter();
        Writer stringWriter = new StringWriter();
        ResponseWriter clonedResponseWriter = responseWriter.cloneWithWriter(stringWriter);
        context.setResponseWriter(clonedResponseWriter);
        List<String[]> itemValues;
        try {
            String var = "searchString";
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            Collection<DropDownItem> items;
            Object oldVarValue = requestMap.put(var, criterion);
            try {
                items = collectDropDownItems(dropDownField);
                itemValues = prepareItemValues(context, dropDownField, items);
            } finally {
                requestMap.put(var, oldVarValue);
            }

            DropDownPopup popup = dropDownField.getPopup();
            popup.setDropDownList(items);

            DropDownPopup.ChildData childData = popup.getChildData();
            popup.renderRows(context, dropDownField, childData, items);
            popup.resetChildData();
        } finally {
            context.setResponseWriter(responseWriter);
        }

        ScriptBuilder sb = new ScriptBuilder();
        sb.functionCall("O$.DropDownField._acceptLoadedItems",
                dropDownField,
                new NewInstanceScript("Array", null, null, getItemValuesArray(itemValues))
        );

        RenderingUtil.renderInitScript(context, sb, EMPTY_ARRAY);
        responseWriter.write(stringWriter.toString());
        return null;
    }
}
