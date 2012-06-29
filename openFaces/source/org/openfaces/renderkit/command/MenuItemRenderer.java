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
package org.openfaces.renderkit.command;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladimir Kurganov
 */
public class MenuItemRenderer extends RendererBase {

    private static final String DEFAULT_LIST_ITEM_CLASS = "o_menu_list_item";

    private static final String DEFAULT_IMG_CLASS = "o_menu_list_item_img";
    private static final String DEFAULT_ARROW_SPAN_CLASS = "o_menu_list_item_arrow_span";
    private static final String DEFAULT_INDENT_CLASS = "o_menu_list_item_image_span";
    private static final String DEFAULT_CONTENT_CLASS = "o_menu_list_item_content";

    protected static final String MENU_ITEMS_PARAMETERS_KEY = "menuItemsParametersKey";

    private static final String A_SUFIX = "::commandLink";
    private static final String SPAN_SUFIX = "::caption";
    private static final String ARROW_SPAN_SUFIX = "::arrowspan";
    private static final String ARROW_SUFIX = "::arrow";
    private static final String IMG_SUFIX = "::image";
    private static final String IMG_SPAN_SUFIX = "::imagespan";
    private static final String IMG_FAKE_SPAN_SUFIX = "::imagefakespan";
    private static final String ARROW_FAKE_SPAN_SUFIX = "::arrowfakespan";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        MenuItem menuItem = (MenuItem) component;
        ResponseWriter writer = context.getResponseWriter();
        PopupMenu popupMenu = (PopupMenu) menuItem.getParent();

        writer.startElement("li", menuItem);
        writeAttribute(writer, "class", DEFAULT_LIST_ITEM_CLASS);
        writeAttribute(writer, "id", menuItem.getClientId(context));

        renderStartMenuItemSpan(context, menuItem, writer);

        renderStyleClass(context, menuItem, writer);

        addSelectedStyleClass(context, menuItem);

        renderMenuItemImage(context, menuItem, writer, popupMenu);
        //content span
        writer.startElement("span", menuItem);
        writeAttribute(writer, "id", menuItem.getClientId(context) + SPAN_SUFIX);

        String contentClass = Styles.getCSSClass(context, menuItem, menuItem.getContentAreaStyle(), StyleGroup.regularStyleGroup(),
                menuItem.getContentAreaClass(), Styles.getCSSClass(context, popupMenu, popupMenu.getItemContentStyle(), StyleGroup.regularStyleGroup(),
                        popupMenu.getItemContentClass(), DEFAULT_CONTENT_CLASS));

        writeAttribute(writer, "class", contentClass);

        renderContentStyleClass(context, menuItem, writer);


        renderMenuItemChildren(context, menuItem, writer);

        writer.endElement("span");

        UIComponent popupMenuChild = getChildPopupMenu(menuItem);

        Styles.renderStyleClasses(context, menuItem);


        addSubmenuImage(context, menuItem, writer, popupMenu, popupMenuChild);
        if (popupMenuChild != null) {
            popupMenuChild.encodeAll(context);
        }

        Styles.renderStyleClasses(context, menuItem);

        writer.endElement("span");

        boolean disabled = menuItem.isDisabled();
        addMenuItemParameter(menuItem, "disabled", disabled ? true : null);
    }

    private UIComponent getChildPopupMenu(MenuItem menuItem) {
        if (menuItem.getChildCount() > 0) {
            List<UIComponent> children = menuItem.getChildren();
            for (UIComponent child : children) {
                if (child instanceof PopupMenu) {
                    return child;
                }
            }
        }
        return null;
    }

    private void renderMenuItemChildren(FacesContext context, MenuItem menuItem, ResponseWriter writer) throws IOException {
        boolean renderValueAsContent = true;
        if (menuItem.getChildCount() > 0) {
            List<UIComponent> children = menuItem.getChildren();
            for (UIComponent child : children) {
                if (child instanceof PopupMenu) {
                    addMenuItemParameter(menuItem, "menuId", child.getClientId(context));
                } else {
                    child.encodeAll(context);
                    if (!(child instanceof OUIClientAction))
                        renderValueAsContent = false;
                }
            }
        }
        if (renderValueAsContent) {
            String value = (String) menuItem.getValue();
            if (value != null)
                writer.writeText(value, null);
        }
    }

    private void renderMenuItemImage(FacesContext context, MenuItem menuItem, ResponseWriter writer, PopupMenu popupMenu) throws IOException {
        if (popupMenu.isIndentVisible()) {
            writer.startElement("span", menuItem);
            writeAttribute(writer, "id", menuItem.getClientId(context) + IMG_SPAN_SUFIX);

            String indentAreaClass = Styles.getCSSClass(context, menuItem, menuItem.getIndentAreaStyle(), StyleGroup.regularStyleGroup(),
                    menuItem.getIndentAreaClass(), Styles.getCSSClass(context, popupMenu, popupMenu.getItemIndentStyle(), StyleGroup.regularStyleGroup(),
                            popupMenu.getItemIndentClass(), DEFAULT_INDENT_CLASS));
            writeAttribute(writer, "class", indentAreaClass);

            writer.startElement("span", menuItem);
            writeAttribute(writer, "id", menuItem.getClientId(context) + IMG_FAKE_SPAN_SUFIX);
            writeAttribute(writer, "class", "o_menu_list_item_img_fakespan");
            writer.endElement("span");

            String imgSrc = menuItem.getIconUrl();
            String imgSelectedSrc = menuItem.getSelectedIconUrl();
            String disabledIconUrl = menuItem.getDisabledIconUrl();
            String disabledSelectedIconUrl = menuItem.getSelectedDisabledIconUrl();

            writer.startElement("img", menuItem);
            writeAttribute(writer, "id", menuItem.getClientId(context) + IMG_SUFIX);
            if (imgSrc != null)
                writeAttribute(writer, "src", Resources.applicationURL(context, imgSrc));
            writeAttribute(writer, "class", DEFAULT_IMG_CLASS);
            writer.endElement("img");

            addMenuItemParameter(menuItem, "imgSelectedSrc", Resources.applicationURL(context, imgSelectedSrc));
            addMenuItemParameter(menuItem, "imgSrc", Resources.applicationURL(context, imgSrc));
            addMenuItemParameter(menuItem, "disabledImgSelectedSrc", Resources.applicationURL(context, disabledSelectedIconUrl));
            addMenuItemParameter(menuItem, "disabledImgSrc", Resources.applicationURL(context, disabledIconUrl));
            writer.endElement("span");
        }
    }

    private void addSubmenuImage(FacesContext context, MenuItem menuItem, ResponseWriter writer, PopupMenu popupMenu, UIComponent popupMenuChild) throws IOException {
        writer.startElement("span", menuItem);
        writeAttribute(writer, "id", menuItem.getClientId(context) + ARROW_SPAN_SUFIX);

        String submenuIconAreaClass = Styles.getCSSClass(context, menuItem, menuItem.getSubmenuIconAreaStyle(), StyleGroup.regularStyleGroup(),
                menuItem.getSubmenuIconAreaClass(), Styles.getCSSClass(context, popupMenu, popupMenu.getItemSubmenuIconStyle(), StyleGroup.regularStyleGroup(),
                        popupMenu.getItemSubmenuIconClass(), DEFAULT_ARROW_SPAN_CLASS));
        writeAttribute(writer, "class", submenuIconAreaClass);
        if (popupMenuChild != null) {
            String submenuImageUrl = menuItem.getSubmenuImageUrl();
            writer.startElement("span", menuItem);
            writeAttribute(writer, "id", menuItem.getClientId(context) + ARROW_FAKE_SPAN_SUFIX);
            writeAttribute(writer, "class", "o_menu_list_item_img_fakespan");
            writer.endElement("span");

            writer.startElement("img", menuItem);
            writeAttribute(writer, "id", menuItem.getClientId(context) + ARROW_SUFIX);
            if (submenuImageUrl != null)
                writeAttribute(writer, "src", Resources.applicationURL(context, submenuImageUrl));
            writeAttribute(writer, "class", DEFAULT_IMG_CLASS);
            writer.endElement("img");

            String disabledSubmenuImageUrl = menuItem.getDisabledSubmenuImageUrl();
            if (disabledSubmenuImageUrl != null) {
                addMenuItemParameter(menuItem, "disabledSubmenuImageUrl",
                        Resources.getURL(context, disabledSubmenuImageUrl,
                                ""));
            }

            String selectedDisabledSubmenuImageUrl = menuItem.getSelectedDisabledSubmenuImageUrl();
            if (selectedDisabledSubmenuImageUrl != null) {
                addMenuItemParameter(menuItem, "selectedDisabledSubmenuImageUrl",
                        Resources.getURL(context, selectedDisabledSubmenuImageUrl,
                                ""));
            }


            if (submenuImageUrl != null) {
                addMenuItemParameter(menuItem, "submenuImageUrl",
                        Resources.getURL(context, submenuImageUrl,
                                ""));
            }


            String selectedSubmenuImageUrl = menuItem.getSelectedSubmenuImageUrl();
            if (selectedSubmenuImageUrl != null) {
                addMenuItemParameter(menuItem, "selectedSubmenuImageUrl",
                        Resources.getURL(context, selectedSubmenuImageUrl,
                                ""));
            }
        }
        writer.endElement("span");
    }

    private void addMenuItemParameter(MenuItem popupMenuItem, String paramName, Object paramValue) {
        Map<String, Object> attributes = popupMenuItem.getAttributes();
        Map<String, Object> parameters = (Map<String, Object>) attributes.get(MENU_ITEMS_PARAMETERS_KEY);
        if (parameters == null) {
            parameters = new HashMap<String, Object>();
            attributes.put(MENU_ITEMS_PARAMETERS_KEY, parameters);
        }
        if (paramValue == null || paramValue.equals("")) {
            parameters.remove(paramName);
            return;
        }

        parameters.put(paramName, paramValue);
    }

    private void renderStartMenuItemSpan(FacesContext context, MenuItem menuItem, ResponseWriter writer) throws IOException {
        writer.startElement("span", menuItem);
        writeAttribute(writer, "id", menuItem.getClientId(context) + A_SUFIX);

        boolean ajaxJsRequired = writeEventsWithAjaxSupport(context, writer, menuItem);
        if (ajaxJsRequired)
            menuItem.getAttributes().put("_ajaxRequired", Boolean.TRUE);
        else {
            if ((menuItem.getActionExpression() != null || menuItem.getActionListeners().length > 0))
                addMenuItemParameter(menuItem, "action", "O$.submitWithParam(this, '" + menuItem.getClientId(context) + "::clicked', 'true');");
        }
    }

    private void addSelectedStyleClass(FacesContext context, MenuItem menuItem) {
        String styleClass = Styles.getCSSClass(context, menuItem, menuItem.getSelectedStyle(), StyleGroup.selectedStyleGroup(2),
                menuItem.getSelectedClass(), null);
        addMenuItemParameter(menuItem, "selectedClass", styleClass);
    }


    private void renderContentStyleClass(FacesContext context, MenuItem menuItem, ResponseWriter writer) throws IOException {
        String styleClass = Styles.getCSSClass(context, menuItem, menuItem.getContentAreaStyle(), StyleGroup.regularStyleGroup(2),
                menuItem.getContentAreaClass(), null);
        writeAttribute(writer, "class", styleClass);
    }

    private void renderStyleClass(FacesContext context, MenuItem menuItem, ResponseWriter writer) throws IOException {
        String styleClass = Styles.getCSSClass(context, menuItem, menuItem.getStyle(), StyleGroup.regularStyleGroup(2),
                menuItem.getStyleClass(), null);
        String disabledStyleClass = Styles.getCSSClass(context, menuItem, menuItem.getDisabledStyle(), StyleGroup.disabledStyleGroup(2), menuItem.getDisabledClass(), null);
        addMenuItemParameter(menuItem, "disabledClass", disabledStyleClass);

        writeAttribute(writer, "class", styleClass);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (component.getAttributes().remove("_ajaxRequired") != null)
            AjaxUtil.renderJSLinks(context);
        writer.endElement("li");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);
        
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = component.getClientId(context) + "::clicked";
        if (requestParameters.containsKey(key)) {
            component.queueEvent(new ActionEvent(component));
        }
    }
}
