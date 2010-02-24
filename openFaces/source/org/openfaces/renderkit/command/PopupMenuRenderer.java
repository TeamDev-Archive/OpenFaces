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
package org.openfaces.renderkit.command;


import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.MenuSeparator;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladimir Kurganov
 */
public class PopupMenuRenderer extends RendererBase {

    private static final String JS_SCRIPT_URL = "popupMenu.js";

    private static final String DEFAULT_CLASS = "o_popup_menu";
    private static final String DEFAULT_POPUPMENU_INDENTION_CLASS = "o_popup_menu_indent";

    private static final String DEFAULT_ITEM_CLASS = "o_menu_list_item_span";
    private static final String DEFAULT_SELECTED_ITEM_CLASS = "o_menu_item_selected";
    private static final String DEFAULT_CONTENT_ITEM_CLASS = "o_menu_list_item_content";
    private static final String DEFAULT_DISABLED_ITEM = "o_menu_list_item_disabled";

    private static final String DEFAULT_SUBMENU_IMAGE = "submenuImage.gif";
    private static final String DEFAULT_SELECTED_SUBMENU_IMAGE = "submenuImage.gif";//"selectedSubmenuImage.gif";
    private static final String DEFAULT_DISABLED_SUBMENU_IMAGE = "disabledSubmenuImage.gif";
    public static final String ATTR_DEFAULT_INDENT_CLASS = "_defaultIndentClass";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;

        PopupMenu popupMenu = (PopupMenu) component;
        ResponseWriter writer = context.getResponseWriter();

        String styleClass = Styles.getCSSClass(context, popupMenu, popupMenu.getStyle(), StyleGroup.regularStyleGroup(),
                popupMenu.getStyleClass(), DEFAULT_CLASS/* + " " + DefaultStyles.getPopupMenuBackgroundColorClass()*/);

        writer.startElement("ul", popupMenu);
        writeAttribute(writer, "id", popupMenu.getClientId(context));

        Rendering.writeStandardEvents(writer, popupMenu);
        writeAttribute(writer, "class", styleClass);
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        super.encodeEnd(facesContext, component);
        if (!component.isRendered()) return;

        PopupMenu popupMenu = (PopupMenu) component;

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.endElement("ul");

        Styles.renderStyleClasses(facesContext, popupMenu);

        renderInitJS(facesContext, popupMenu);
    }

    private JSONArray getMenuItemParameters(PopupMenu popupMenu) {
        JSONArray menuItemParameters = new JSONArray();
        List<UIComponent> components = popupMenu.getChildren();
        for (UIComponent component : components) {
            if (component instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) component;
                Map parameters = (Map) menuItem.getAttributes().get(MenuItemRenderer.MENU_ITEMS_PARAMETERS_KEY);
                if (parameters == null) {
                    parameters = new HashMap();
                }
                JSONObject obj = new JSONObject(parameters);
                menuItemParameters.put(obj);
            } else if (component instanceof MenuSeparator) {
                menuItemParameters.put(new JSONObject());
            }
        }
        return menuItemParameters;
    }

    private void renderInitJS(FacesContext context, PopupMenu popupMenu) throws IOException {
        String forId = OUIClientActionHelper.getClientActionInvoker(context, popupMenu);

        String indentClass = Styles.getCSSClass(context, popupMenu, popupMenu.getIndentStyle(), StyleGroup.regularStyleGroup(), popupMenu.getIndentClass(),
                getDefaultIndentClass(popupMenu));
        String defaultItemClass = Styles.getCSSClass(context, popupMenu, popupMenu.getItemStyle(), StyleGroup.regularStyleGroup(), popupMenu.getItemClass(),
                DEFAULT_ITEM_CLASS);
        String defaultSelectedClass = Styles.getCSSClass(context, popupMenu, popupMenu.getSelectedItemStyle(), StyleGroup.selectedStyleGroup(), popupMenu.getSelectedItemClass(),
                DEFAULT_SELECTED_ITEM_CLASS);
        String defaultContentClass = Styles.getCSSClass(context, popupMenu, popupMenu.getItemContentStyle(), StyleGroup.regularStyleGroup(), popupMenu.getItemContentClass(),
                DEFAULT_CONTENT_ITEM_CLASS);
        String defaultDisabledClass = Styles.getCSSClass(context, popupMenu, popupMenu.getDisabledItemStyle(), StyleGroup.disabledStyleGroup(1), popupMenu.getDisabledItemClass(),
                DEFAULT_DISABLED_ITEM);

        String submenuImageUrl = Resources.getURL(context, popupMenu.getSubmenuImageUrl(), PopupMenuRenderer.class, DEFAULT_SUBMENU_IMAGE);
        String disabledSubmenuImageUrl = Resources.getURL(context, popupMenu.getDisabledSubmenuImageUrl(), PopupMenuRenderer.class, DEFAULT_DISABLED_SUBMENU_IMAGE);
        String selectedSubmenuImageUrl = Resources.getURL(context, popupMenu.getSelectedSubmenuImageUrl(), PopupMenuRenderer.class, DEFAULT_SELECTED_SUBMENU_IMAGE);

        JSONObject eventsObj = new JSONObject();
        Rendering.addJsonParam(eventsObj, "onhide", popupMenu.getOnhide());
        Rendering.addJsonParam(eventsObj, "onshow", popupMenu.getOnshow());

        boolean isRootMenu = !(popupMenu.getParent() instanceof MenuItem);
        PopupMenu rootPopupMenu = getRootPopupMenu(popupMenu);
        String event = Rendering.getEventWithOnPrefix(context, popupMenu, "o:popupMenu");
        ScriptBuilder initScript = new ScriptBuilder();
        initScript.initScript(context, popupMenu, "O$.PopupMenu._init",
                Rendering.getRolloverClass(context, popupMenu),
                forId,
                event,
                popupMenu.isIndentVisible(),
                indentClass,
                defaultItemClass,
                defaultSelectedClass,
                defaultContentClass,
                defaultDisabledClass,

                popupMenu.getItemIconUrl(),
                popupMenu.getDisabledItemIconUrl(),
                popupMenu.getSelectedItemIconUrl(),
                popupMenu.getSelectedDisabledItemIconUrl(),

                submenuImageUrl,
                disabledSubmenuImageUrl,
                selectedSubmenuImageUrl,
                popupMenu.getSelectedDisabledSubmenuImageUrl(),

                isRootMenu,
                getMenuItemParameters(popupMenu),
                popupMenu.getSubmenuHorizontalOffset(),

                rootPopupMenu.getSubmenuShowDelay(),
                rootPopupMenu.getSubmenuHideDelay(),
                rootPopupMenu.isSelectDisabledItems(),

                eventsObj);

        Styles.renderStyleClasses(context, popupMenu);

        Rendering.renderInitScript(context, initScript,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, PopupMenuRenderer.class, JS_SCRIPT_URL),
                Resources.getInternalURL(context, RendererBase.class, "popup.js"));
    }

    private String getDefaultIndentClass(PopupMenu popupMenu) {
        String defaultIndentClass = (String) popupMenu.getAttributes().get(ATTR_DEFAULT_INDENT_CLASS);
        return defaultIndentClass != null ? defaultIndentClass : DEFAULT_POPUPMENU_INDENTION_CLASS;
    }

    private PopupMenu getRootPopupMenu(PopupMenu popupMenu) {
        while (popupMenu.getParent() instanceof MenuItem) {
            popupMenu = (PopupMenu) popupMenu.getParent().getParent();
        }
        return popupMenu;

    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        Rendering.renderChildren(context, component);
    }
}
