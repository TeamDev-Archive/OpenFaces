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

import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.component.select.TabSet;
import org.openfaces.component.select.TabSetItem;
import org.openfaces.component.select.TabSetItems;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.org.json.JSONArray;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.HTML;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.StyleGroup;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andrew Palval
 */
public class TabSetRenderer extends BaseTabSetRenderer {
    private static final String JS_SCRIPT_URL = "tabset.js";

    private static final String DEFAULT_EMPTY_SPACE_CLASS_PREFIX = "o_tabset_empty_space_";
    private static final String DEFAULT_CLASS_PREFIX = "o_tabset_";
    private static final String DEFAULT_ROLLOVER_CLASS_PREFIX = "o_tabset_rollover_";
    private static final MessageFormat DEFAULT_SELECTED_STYLE = new MessageFormat("background-color: transparent; border-{0}: #c9d8f5 3px solid;");
    private static final String DEFAULT_SELECTED_ROLLOVER_CLASS_PREFIX = "o_tabset_selected_rollover_";
    private static final String BACK_BORDER_CLASS_PREFIX = "o_tabset_back_border_";
    private static final String FRONT_BORDER_CLASS_PREFIX = "o_tabset_front_border_";
    public static final String ATTR_CONSIDER_TAB_NON_RENDERED = "o_considerTabNonRendered";

    protected void changeTabIndex(UIComponent component, int selectedIndex) {
        TabSet tabSet = (TabSet) component;
        int oldSelectedIndex = tabSet.getSelectedIndex();
        if (oldSelectedIndex != selectedIndex) {
            tabSet.setSelectedIndex(selectedIndex);
            tabSet.queueEvent(new SelectionChangeEvent(tabSet, oldSelectedIndex, selectedIndex));
        }

        List<Object> tabItems = getTabItems(tabSet);
        Object tabItem = tabItems.get(selectedIndex);
        Object tabValue = (tabItem instanceof TabSetItem) ? ((TabSetItem) tabItem).getItemValue() : null;
        tabSet.setSubmittedValue(tabValue);
    }

    protected String getSelectionHiddenFieldSuffix() {
        return SELECTED_INDEX_SUFFIX;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        TabSet tabSet = (TabSet) component;

        writer.startElement("table", tabSet);
        writeIdAttribute(context, tabSet);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);

        writeAttribute(writer, "style", tabSet.getStyle());
        writeAttribute(writer, "class", tabSet.getStyleClass());

        Rendering.writeStandardEvents(writer, tabSet);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        TabSet tabSet = (TabSet) component;

        TabPlacement tabPlacement = getTabPlacement(tabSet);
        boolean verticalTabs = tabPlacement.equals(TabPlacement.LEFT) || tabPlacement.equals(TabPlacement.RIGHT);

        TabAlignment tabAlign = tabSet.getAlignment();
        if (tabAlign == null) {
            tabAlign = TabAlignment.TOP_OR_LEFT;
        }

        List<Object> tabItems = getTabItems(tabSet);

        String emptySpaceClasses = getEmptySpaceClasses(tabSet, context);

        if (!verticalTabs) {
            writer.startElement("tr", tabSet);
            if (tabAlign.equals(TabAlignment.BOTTOM_OR_RIGHT)) {
                insertHorizontalSpacer(context, tabSet, emptySpaceClasses);
            }
        } else if (tabAlign.equals(TabAlignment.BOTTOM_OR_RIGHT)) {
            insertVerticalSpacer(context, tabSet, emptySpaceClasses);
        }

        String idPrefix = tabSet.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR;
        int gapWidth = tabSet.getGapWidth();

        int allTabCount = tabItems.size();
        List<Object> renderedTabItems = new ArrayList<Object>(allTabCount);
        int[] originalTabIndexes = new int[allTabCount];
        for (int i = 0, tabsAdded = 0; i < allTabCount; i++) {
            Object item = tabItems.get(i);
            if (isItemRendered(item)) {
                renderedTabItems.add(item);
                originalTabIndexes[tabsAdded++] = i;
            }
        }
        for (int i = 0, tabCount = renderedTabItems.size(); i < tabCount; i++) {
            Object item = renderedTabItems.get(i);
            if (!isItemRendered(item))
                continue;

            if (gapWidth > 0) {
                if (TabAlignment.TOP_OR_LEFT.equals(tabAlign)) {
                    if (verticalTabs) {
                        writeVerticalGap(writer, tabSet, gapWidth, emptySpaceClasses);
                    } else {
                        writeHorizontalGap(writer, tabSet, gapWidth, emptySpaceClasses);
                    }
                }
            }

            if (verticalTabs) {
                writer.startElement("tr", tabSet);
            }

            writer.startElement("td", tabSet);

            String attr = verticalTabs ? "align" : "valign";
            writer.writeAttribute(attr, tabPlacement.toString(), null);

            //        writer.startElement("div", tabSet);
            int absoluteTabIndex = originalTabIndexes[i];
            writer.writeAttribute("id", idPrefix + absoluteTabIndex, "id");

            if (tabSet.isFocusable()) {
                writer.startElement("div", tabSet);
                String defaultFocusedClass = Styles.getCSSClass(context, component, "border: 1px solid transparent;", StyleGroup.selectedStyleGroup(1));
                writer.writeAttribute("class", defaultFocusedClass, null);
            }

            if (item instanceof String) {
                writer.writeText(item, null);
            } else if (item instanceof UIComponent) {
                ((UIComponent) item).encodeAll(context);
            } else if (item != null) {
                writer.writeText(item.toString(), null);
            }

            if (tabSet.isFocusable()) {
                writer.endElement("div");
            }
            //        writer.endElement("div");

            if (i == (tabCount - 1)) {
                encodeScriptsAndStyles(context, component);
            }

            writer.endElement("td");
            if (verticalTabs) {
                writer.endElement("tr");
            }

            if (gapWidth > 0) {
                if (TabAlignment.BOTTOM_OR_RIGHT.equals(tabAlign)) {
                    if (verticalTabs) {
                        writeVerticalGap(writer, tabSet, gapWidth, emptySpaceClasses);
                    } else {
                        writeHorizontalGap(writer, tabSet, gapWidth, emptySpaceClasses);
                    }
                }
            }
        }

        if (!verticalTabs) {
            if (tabAlign.equals(TabAlignment.TOP_OR_LEFT)) {
                insertHorizontalSpacer(context, tabSet, emptySpaceClasses);
            }
            writer.endElement("tr");
        } else if (tabAlign.equals(TabAlignment.TOP_OR_LEFT)) {
            insertVerticalSpacer(context, tabSet, emptySpaceClasses);
        }
    }

    private boolean isItemRendered(Object item) {
        if (!(item instanceof UIComponent))
            return true;
        UIComponent component = (UIComponent) item;
        boolean rendered = component.isRendered();
        if (!rendered)
            return false;
        return !component.getAttributes().containsKey(ATTR_CONSIDER_TAB_NON_RENDERED);
    }

    private void writeHorizontalGap(ResponseWriter writer, TabSet tabSet, int gapWidth, String emptySpaceClasses) throws IOException {
        writer.startElement("td", tabSet);
//    writer.writeAttribute("style", "width: " + gapWidth + ";", null);
        writer.writeAttribute("width", gapWidth + "px", null);
        writer.writeAttribute("class", emptySpaceClasses, null);
        writer.writeAttribute("nowrap", "nowrap", null);
        writer.write(HTML.NBSP_ENTITY);
        writer.endElement("td");
    }

    private void writeVerticalGap(ResponseWriter writer, TabSet tabSet, int gapWidth, String emptySpaceClasses) throws IOException {
        writer.startElement("tr", tabSet);
        writer.writeAttribute("style", "height: " + gapWidth + "px;", null);
        writer.startElement("td", tabSet);
        writer.writeAttribute("style", "height: " + gapWidth + "px;", null);
        writer.writeAttribute("class", emptySpaceClasses, null);
        writer.startElement("div", tabSet);
        writer.writeAttribute("style", "height: 1px; width: 1px; font-size: 1px;", null);
        writer.endElement("div");
        writer.endElement("td");
        writer.endElement("tr");
    }

    private void insertHorizontalSpacer(FacesContext context, TabSet tabSet, String emptySpaceClasses) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("td", tabSet);
        writer.writeAttribute("class", emptySpaceClasses, null);
        if (tabSet.isFocusable()) {
            writer.writeAttribute("onmousedown", "O$.cancelBubble(event);", null);
            writer.writeAttribute("onmouseup", "O$.cancelBubble(event);", null);
            writer.writeAttribute("onclick", "O$.cancelBubble(event);", null);
        }
        if (Environment.isMozilla()) { // the div is actually needed only for Mozilla running on Mac, to solve the width problem (JSFC-2713)
            writer.startElement("div", tabSet);
            writer.writeAttribute("class", "o_tabset_moz_empty_space", null);
            writer.endElement("div");
        } else {
            writer.write(HTML.NBSP_ENTITY);
        }
        writer.endElement("td");
    }

    private void insertVerticalSpacer(FacesContext context, TabSet tabSet, String emptySpaceClasses) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", tabSet);
        writer.writeAttribute("height", "100%", null);
        writer.startElement("td", tabSet);
        if (tabSet.isFocusable()) {
            writer.writeAttribute("onmousedown", "O$.cancelBubble(event);", null);
            writer.writeAttribute("onmouseup", "O$.cancelBubble(event);", null);
            writer.writeAttribute("onclick", "O$.cancelBubble(event);", null);
        }
        writer.writeAttribute("height", "100%", null);
        writer.writeAttribute("class", emptySpaceClasses, null);
        writer.write(HTML.NBSP_ENTITY);
        writer.endElement("td");
        writer.endElement("tr");
    }

    private String getEmptySpaceClasses(TabSet tabSet, FacesContext context) {
        TabPlacement placement = getTabPlacement(tabSet);

        String borderStyle = tabSet.getFrontBorderStyle();
        String style = tabSet.getEmptySpaceStyle();
        if (borderStyle != null && borderStyle.length() > 0) {
            if (style == null) {
                style = "";
            }
            style += " border-" + placement.getOppositeValue() + ": " + borderStyle + ";";
        }
        String defaultClass = DEFAULT_EMPTY_SPACE_CLASS_PREFIX + placement;
        return Styles.getCSSClass(context, tabSet, style, defaultClass, tabSet.getEmptySpaceClass());
    }

    private TabPlacement getTabPlacement(TabSet tabSet) {
        TabPlacement placement = tabSet.getPlacement();
        if (placement == null) {
            placement = TabPlacement.TOP;
        }
        return placement;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("table");
        writer.flush();
    }

    private void encodeScriptsAndStyles(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        TabSet tabSet = (TabSet) component;

        List<Object> tabs = getTabItems(tabSet);
        int selectedIndex = getSelectedIndex(context, tabSet, tabs);

        Rendering.renderHiddenField(writer,
                component.getClientId(context) + SELECTED_INDEX_SUFFIX,
                String.valueOf(selectedIndex));

        TabPlacement placement = tabSet.getPlacement();
        if (placement == null) {
            placement = TabPlacement.TOP;
        }

        List<Object> borderSideNames = borderNames.getListWithFirst(placement, -1);
        String borderMask = "border-{0}: {4}; border-{1}: {4}; border-{2}: {4}; border-{3}: {5};";
        String frontBorderClass = FRONT_BORDER_CLASS_PREFIX + placement;

        int gapWidth = tabSet.getGapWidth();
        boolean hasGap = gapWidth > 0;

        String borderMaskPre;
        if (hasGap) {
            borderMaskPre = "border-{0}: {4}; border-{1}: {4}; border-{2}: {4}; border-{3}: {5};";
        } else {
            borderMaskPre = "border-{0}: {4}; border-{1}: {4}; border-{2}: 0px; border-{3}: {5};";
        }

        String backBorderClassPre = BACK_BORDER_CLASS_PREFIX + placement;

        String borderMaskPost;
        if (hasGap) {
            borderMaskPost = borderMaskPre;
        } else {
            borderMaskPost = "border-{0}: 0px;border-{1}: {4};border-{2}: {4};border-{3}: {5};";
        }
        String backBorderClassPost = backBorderClassPre;

        String frontBorderStyle = tabSet.getFrontBorderStyle();
        boolean frontBorderDefined = frontBorderStyle != null && frontBorderStyle.length() > 0;
        if (frontBorderDefined) {
            String frontBorder = formatBorderClassString(borderMask, borderSideNames, frontBorderStyle, "0px");
            frontBorderClass = Styles.getCSSClass(context, tabSet, frontBorder, frontBorderClass);
        }

        String backBorderStyle = tabSet.getBackBorderStyle();
        boolean backBorderDefined = backBorderStyle != null && backBorderStyle.length() > 0;
        if (backBorderDefined || frontBorderDefined) {
            String backBorderPre = formatBorderClassString(borderMaskPre, borderSideNames, backBorderStyle, frontBorderStyle);
            backBorderClassPre = Styles.getCSSClass(context, tabSet, backBorderPre, backBorderClassPre);

            String backBorderPost = formatBorderClassString(borderMaskPost, borderSideNames, backBorderStyle, frontBorderStyle);
            backBorderClassPost = Styles.getCSSClass(context, component, backBorderPost);
        }

        String defaultClass = DEFAULT_CLASS_PREFIX + placement;
        String tabClass = Styles.getCSSClass(context, component, tabSet.getTabStyle(), defaultClass, tabSet.getTabClass());
        String defaultRolloverClass = DEFAULT_ROLLOVER_CLASS_PREFIX + placement;
        String rolloverClass = Styles.getCSSClass(context, component, tabSet.getRolloverTabStyle(), StyleGroup.rolloverStyleGroup(), tabSet.getRolloverTabClass(), defaultRolloverClass);
        String defaultSelectedClass = DEFAULT_SELECTED_STYLE.format(new Object[]{placement});
        String selectedClass = Styles.getStyleClassesStr(context, component, tabSet.getSelectedTabStyle(), tabSet.getSelectedTabClass(), defaultSelectedClass, StyleGroup.selectedStyleGroup());
        String defaultSelectedRolloverClass = DEFAULT_SELECTED_ROLLOVER_CLASS_PREFIX + placement.toString();
        String selectedRolloverClass = Styles.getCSSClass(context, component, tabSet.getRolloverSelectedTabStyle(), StyleGroup.selectedStyleGroup(1), tabSet.getRolloverSelectedTabClass(), defaultSelectedRolloverClass);
        String focusedTabClass = Styles.getStyleClassesStr(context, component, tabSet.getFocusedTabStyle(), tabSet.getFocusedTabClass(), null, StyleGroup.selectedStyleGroup(2));
        /// focus area style
        String defaultFocusedClass = Styles.getCSSClass(context, component, Rendering.DEFAULT_FOCUSED_STYLE, StyleGroup.selectedStyleGroup(1));
        String focusAreaClass = Styles.getCSSClass(context, component, tabSet.getFocusAreaStyle(),
                StyleGroup.selectedStyleGroup(2), tabSet.getFocusAreaClass(), defaultFocusedClass);

        String focusedClass = Styles.getCSSClass(context, tabSet,
                tabSet.getFocusedStyle(), StyleGroup.selectedStyleGroup(1), tabSet.getFocusedClass(), null);

        String onchange = tabSet.getOnchange();

        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, tabSet, "O$.TabSet._init",
                getTabIds(context, tabSet, tabs),
                selectedIndex,
                placement,
                new String[]{
                        tabClass,
                        selectedClass,
                        rolloverClass,
                        selectedRolloverClass,
                        focusedTabClass
                },
                new String[]{
                        frontBorderClass,
                        backBorderClassPre,
                        backBorderClassPost
                },
                tabSet.isFocusable(),
                focusAreaClass,
                focusedClass,
                onchange != null ? new AnonymousFunction(onchange, "event") : null);

        Rendering.renderInitScript(context, sb,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, TabSetRenderer.class, JS_SCRIPT_URL));

        Rendering.encodeClientActions(context, component);

        Styles.renderStyleClasses(context, tabSet);
    }

    private int getSelectedIndex(FacesContext context, TabSet tabSet, List<Object> allTabItems) {
        int selectedIndex;
        if (tabSet.isLocalValueSet()) {
            Object tabValue = tabSet.getLocalValue();
            selectedIndex = tabIndexByValue(allTabItems, tabValue);
        } else if (tabSet.getLocalSelectedIndex() != null) {
            selectedIndex = tabSet.getLocalSelectedIndex();
        } else {
            ValueExpression valueExpression = tabSet.getValueExpression("value");
            ValueExpression selectedIndexExpression = tabSet.getValueExpression("selectedIndex");
            ELContext elContext = context.getELContext();
            if (valueExpression != null) {
                Object tabValue = valueExpression.getValue(elContext);
                selectedIndex = tabIndexByValue(allTabItems, tabValue);
            } else {
                Integer indexObj;
                if (selectedIndexExpression != null)
                    indexObj = (Integer) selectedIndexExpression.getValue(elContext);
                else
                    indexObj = null;
                selectedIndex = indexObj != null
                        ? indexObj
                        : tabSet.getSelectedIndex();
            }
        }

        if (selectedIndex < 0 || selectedIndex >= allTabItems.size())
            selectedIndex = 0;

        Object selectedItem = allTabItems.get(selectedIndex);
        if (!isItemRendered(selectedItem)) {
            selectedIndex = -1;
            for (int tabIndex = 0; tabIndex < allTabItems.size(); tabIndex++) {
                Object item = allTabItems.get(tabIndex);
                if (isItemRendered(item)) {
                    selectedIndex = tabIndex;
                    break;
                }
            }
        }
        return selectedIndex;
    }

    private int tabIndexByValue(List<Object> allTabItems, Object tabValue) {
        for (int i = 0, count = allTabItems.size(); i < count; i++) {
            Object tabItem = allTabItems.get(i);
            if (tabItem instanceof TabSetItem) {
                Object itemValue = ((TabSetItem) tabItem).getItemValue();
                if ((tabValue == null && itemValue == null) || (tabValue != null && tabValue.equals(itemValue)))
                    return i;
            }
        }
        return -1;
    }

    private String formatBorderClassString(
            String borderMaskPre,
            List<Object> borderSideNames,
            String backBorderStyle,
            String frontBorderStyle) {
        if (Rendering.isNullOrEmpty(backBorderStyle))
            backBorderStyle = "none";
        if (Rendering.isNullOrEmpty(frontBorderStyle))
            frontBorderStyle = "none";
        String result = MessageFormat.format(borderMaskPre, borderSideNames.get(0),
                borderSideNames.get(1),
                borderSideNames.get(2),
                borderSideNames.get(3),
                backBorderStyle,
                frontBorderStyle);
        return result;
    }

    private List<Object> getTabItems(TabSet tabSet) {
        List<Object> itemsList = new ArrayList<Object>();

        List<UIComponent> children = tabSet.getChildren();
        for (Object child : children) {
            if (child instanceof TabSetItem) {
                itemsList.add(child);
            } else if (child instanceof TabSetItems) {
                Object value = ((TabSetItems) child).getValue();
                if (value != null && value instanceof Collection) {
                    itemsList.addAll((Collection<Object>) value);
                }
            }
        }

        return itemsList;
    }

    private JSONArray getTabIds(FacesContext context, TabSet tabSet, List<Object> tabs) {
        JSONArray result = new JSONArray();

        String clientId = tabSet.getClientId(context);
        for (int tabIndex = 0, tabCount = tabs.size(); tabIndex < tabCount; tabIndex++) {
            Object tabObj = tabs.get(tabIndex);
            if (!isItemRendered(tabObj))
                continue;

            result.put(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + tabIndex);
        }
        return result;
    }
}
