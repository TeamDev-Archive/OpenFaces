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
package org.openfaces.component.action;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.OUIComponentBase;
import org.openfaces.component.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class PopupMenu extends OUIComponentBase implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.PopupMenu";
    public static final String COMPONENT_FAMILY = "org.openfaces.PopupMenu";

    /*  properties for the root PopupMenu only */
    private String _for;
    private String event;
    private Boolean standalone;

    private Integer submenuShowDelay;
    private Integer submenuHideDelay;

    private Boolean selectDisabledItems;

    /*  properties that inherited from the parent PopupMenu */
    private Boolean indentVisible;
    private Integer submenuHorisontalOffset;

    private String itemIconUrl;
    private String disabledItemIconUrl;
    private String selectedItemIconUrl;
    private String selectedDisabledItemIconUrl;

    private String submenuImageUrl;
    private String disabledSubmenuImageUrl;
    private String selectedSubmenuImageUrl;
    private String selectedDisabledSubmenuImageUrl;

    private String itemStyle;
    private String itemClass;
    private String selectedItemStyle;
    private String selectedItemClass;
    private String disabledItemStyle;
    private String disabledItemClass;
    private String contentItemStyle;// todo: consider renaming to itemContentStyle/Class
    private String contentItemClass;
    private String indentItemStyle;// todo: consider renaming to itemIndentStyle/Class
    private String indentItemClass;
    private String submenuIconItemStyle;// todo: consider renaming to itemSugmenuIconStyle/Class
    private String submenuIconItemClass;
    private String indentStyle;
    private String indentClass;

    /* properties that are not inherited */
    private String onshow;
    private String onhide;

    public String getStyle() {
        String result = ValueBindings.get(this, "style", style);
        if (result == null && getPopupMenuParent() != null) result = getPopupMenuParent().style;
        return result == null ? ValueBindings.get(this, "style", style) : result;
    }

    public String getStyleClass() {
        String result = ValueBindings.get(this, "styleClass", styleClass);
        if (result == null && getPopupMenuParent() != null) result = getPopupMenuParent().styleClass;
        return result == null ? ValueBindings.get(this, "styleClass", styleClass) : result;
    }

    public String getRolloverStyle() {
        String result = ValueBindings.get(this, "rolloverStyle", rolloverStyle);
        if (result == null && getPopupMenuParent() != null) result = getPopupMenuParent().rolloverStyle;
        return result == null ? ValueBindings.get(this, "rolloverStyle", rolloverStyle) : result;
    }

    public String getRolloverClass() {
        String result = ValueBindings.get(this, "rolloverClass", rolloverClass);
        if (result == null && getPopupMenuParent() != null) result = getPopupMenuParent().rolloverClass;
        return result == null ? ValueBindings.get(this, "rolloverClass", rolloverClass) : result;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public PopupMenu() {
        setRendererType("org.openfaces.PopupMenuRenderer");
    }


    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", event);
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isStandalone() {
        return ValueBindings.get(this, "standalone", standalone, false);
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public int getSubmenuShowDelay() {
        return ValueBindings.get(this, "submenuShowDelay", submenuShowDelay, 500);
    }

    public void setSubmenuShowDelay(int submenuShowDelay) {
        this.submenuShowDelay = submenuShowDelay;
    }

    public int getSubmenuHideDelay() {
        return ValueBindings.get(this, "submenuHideDelay", submenuHideDelay, 500);
    }

    public void setSubmenuHideDelay(int submenuHideDelay) {
        this.submenuHideDelay = submenuHideDelay;
    }

    public boolean isIndentVisible() {
        Boolean result = ValueBindings.get(this, "indentVisible", indentVisible, Boolean.class);
        if (result == null && getPopupMenuParent() != null) result = getPopupMenuParent().indentVisible;
        return result == null ? ValueBindings.get(this, "indentVisible", indentVisible, true) : result;
    }

    private PopupMenu getPopupMenuParent() {
        UIComponent parent = this.getParent();
        if (parent != null && !parent.getClass().isAssignableFrom(PopupMenu.class)) {
            parent = parent.getParent();
        }
        if (parent != null && parent.getClass().isAssignableFrom(PopupMenu.class)) {
            return (PopupMenu) parent;
        }
        return null;
    }

    public void setIndentVisible(boolean indentVisible) {
        this.indentVisible = indentVisible;
    }

    public boolean isSelectDisabledItems() {
        return ValueBindings.get(this, "selectDisabledItems", selectDisabledItems, true);
    }

    public void setSelectDisabledItems(boolean selectDisabledItems) {
        this.selectDisabledItems = selectDisabledItems;
    }

    public String getIndentStyle() {
        String result = ValueBindings.get(this, "indentStyle", indentStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().indentStyle;
        }
        return result;
    }

    public void setIndentStyle(String indentStyle) {
        this.indentStyle = indentStyle;
    }

    public String getIndentClass() {
        String result = ValueBindings.get(this, "indentClass", indentClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().indentClass;
        }
        return result;
    }

    public void setIndentClass(String indentClass) {
        this.indentClass = indentClass;
    }

    public void setSelectedDisabledItemIconUrl(String selectedDisabledItemIconUrl) {
        this.selectedDisabledItemIconUrl = selectedDisabledItemIconUrl;
    }

    public String getSelectedDisabledItemIconUrl() {
        String result = ValueBindings.get(this, "selectedDisabledItemIconUrl", selectedDisabledItemIconUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedDisabledItemIconUrl;
        }
        return result;
    }

    public String getItemIconUrl() {
        String result = ValueBindings.get(this, "itemIconUrl", itemIconUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().itemIconUrl;
        }
        return result;
    }

    public void setItemIconUrl(String itemIconUrl) {
        this.itemIconUrl = itemIconUrl;
    }

    public String getDisabledItemIconUrl() {
        String result = ValueBindings.get(this, "disabledItemIconUrl", disabledItemIconUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().disabledItemIconUrl;
        }
        return result;
    }

    public void setDisabledItemIconUrl(String disabledItemIconUrl) {
        this.disabledItemIconUrl = disabledItemIconUrl;
    }

    public String getSelectedItemIconUrl() {
        String result = ValueBindings.get(this, "selectedIconItemUrl", selectedItemIconUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedItemIconUrl;
        }
        return result;
    }

    public void setsSelectedItemIconUrl(String selectedItemIconUrl) {
        this.selectedItemIconUrl = selectedItemIconUrl;
    }


    public String getSubmenuImageUrl() {
        String result = ValueBindings.get(this, "submenuImageUrl", submenuImageUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().submenuImageUrl;
        }
        return result;
    }

    public void setSubmenuImageUrl(String submenuImageUrl) {
        this.submenuImageUrl = submenuImageUrl;
    }

    public String getSelectedSubmenuImageUrl() {
        String result = ValueBindings.get(this, "selectedSubmenuImageUrl", selectedSubmenuImageUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedSubmenuImageUrl;
        }
        return result;
    }

    public void setSelectedSubmenuImageUrl(String selectedSubmenuImageUrl) {
        this.selectedSubmenuImageUrl = selectedSubmenuImageUrl;
    }

    public String getSelectedDisabledSubmenuImageUrl() {
        String result = ValueBindings.get(this, "selectedDisabledSubmenuImageUrl", selectedDisabledSubmenuImageUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedDisabledSubmenuImageUrl;
        }
        return result;
    }

    public void setSelectedDisabledSubmenuImageUrl(String selectedDisabledSubmenuImageUrl) {
        this.selectedDisabledSubmenuImageUrl = selectedDisabledSubmenuImageUrl;
    }

    public String getDisabledSubmenuImageUrl() {
        String result = ValueBindings.get(this, "disabledSubmenuImageUrl", disabledSubmenuImageUrl);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().disabledSubmenuImageUrl;
        }
        return result;
    }

    public void setDisabledSubmenuImageUrl(String disabledSubmenuImageUrl) {
        this.disabledSubmenuImageUrl = disabledSubmenuImageUrl;
    }

    public String getItemStyle() {
        String result = ValueBindings.get(this, "itemStyle", itemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().itemStyle;
        }
        return result;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public int getSubmenuHorisontalOffset() {
        Integer result = ValueBindings.get(this, "submenuHorisontalOffset", submenuHorisontalOffset, Integer.class);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().submenuHorisontalOffset;
        }
        return result == null ? ValueBindings.get(this, "submenuHorisontalOffset", submenuHorisontalOffset, 0) : result;
    }

    public void setSubmenuHorisontalOffset(int submenuHorisontalOffset) {
        this.submenuHorisontalOffset = submenuHorisontalOffset;
    }

    public String getOnshow() {
        return ValueBindings.get(this, "onshow", onshow);
    }

    public void setOnshow(String onshow) {
        this.onshow = onshow;
    }

    public String getOnhide() {
        return ValueBindings.get(this, "onhide", onhide);
    }

    public void setOnhide(String onhide) {
        this.onhide = onhide;
    }

    public String getItemClass() {
        String result = ValueBindings.get(this, "itemClass", itemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().itemClass;
        }
        return result;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getSelectedItemStyle() {
        String result = ValueBindings.get(this, "selectedItemStyle", selectedItemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedItemStyle;
        }
        return result;
    }

    public void setSelectedItemStyle(String selectedItemStyle) {
        this.selectedItemStyle = selectedItemStyle;
    }

    public String getSelectedItemClass() {
        String result = ValueBindings.get(this, "selectedItemClass", selectedItemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().selectedItemClass;
        }
        return result;
    }

    public void setSelectedItemClass(String selectedItemClass) {
        this.selectedItemClass = selectedItemClass;
    }

    public String getDisabledItemStyle() {
        String result = ValueBindings.get(this, "disabledItemStyle", disabledItemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().disabledItemStyle;
        }
        return result;
    }

    public void setIndentItemStyle(String indentItemStyle) {
        this.indentItemStyle = indentItemStyle;
    }

    public String getIndentItemStyle() {
        String result = ValueBindings.get(this, "indentItemStyle", indentItemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().indentItemStyle;
        }
        return result;
    }

    public void setIndentItemClass(String indentItemClass) {
        this.indentItemClass = indentItemClass;
    }

    public String getIndentItemClass() {
        String result = ValueBindings.get(this, "indentItemClass", indentItemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().indentItemClass;
        }
        return result;
    }


    public void setSubmenuIconItemStyle(String submenuIconItemStyle) {
        this.submenuIconItemStyle = submenuIconItemStyle;
    }

    public String getSubmenuIconItemStyle() {
        String result = ValueBindings.get(this, "submenuIconItemStyle", submenuIconItemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().submenuIconItemStyle;
        }
        return result;
    }

    public void setSubmenuIconItemClass(String submenuIconItemClass) {
        this.submenuIconItemClass = submenuIconItemClass;
    }

    public String getSubmenuIconItemClass() {
        String result = ValueBindings.get(this, "submenuIconItemClass", submenuIconItemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().submenuIconItemClass;
        }
        return result;
    }


    public void setDisabledItemStyle(String disabledItemStyle) {
        this.disabledItemStyle = disabledItemStyle;
    }

    public String getDisabledItemClass() {
        String result = ValueBindings.get(this, "disabledItemClass", disabledItemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().disabledItemClass;
        }
        return result;
    }

    public String getContentItemClass() {
        String result = ValueBindings.get(this, "contentItemClass", contentItemClass);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().contentItemClass;
        }
        return result;
    }

    public void setContentItemClass(String contentItemClass) {
        this.contentItemClass = contentItemClass;
    }

    public String getContentItemStyle() {
        String result = ValueBindings.get(this, "contentItemStyle", contentItemStyle);
        if (result == null && getPopupMenuParent() != null) {
            result = getPopupMenuParent().contentItemStyle;
        }
        return result;
    }

    public void setContentItemStyle(String contentItemStyle) {
        this.contentItemStyle = contentItemStyle;
    }

    public void setDisabledItemClass(String disabledItemClass) {
        this.disabledItemClass = disabledItemClass;
    }

    public void setParent(UIComponent parent) {
        super.setParent(parent);
        OUIClientActionHelper.ensureComponentIdSpecified(parent);
    }

    public Object saveState(FacesContext facesContext) {
        return new Object[]{
                super.saveState(facesContext),
                _for,
                event,
                standalone,

                submenuShowDelay,
                submenuHideDelay,
                submenuHorisontalOffset,

                indentVisible,
                selectDisabledItems,

                submenuImageUrl,
                disabledSubmenuImageUrl,
                selectedSubmenuImageUrl,
                selectedDisabledSubmenuImageUrl,

                itemIconUrl,
                disabledItemIconUrl,
                selectedItemIconUrl,
                selectedDisabledItemIconUrl,

                itemStyle,
                itemClass,
                selectedItemStyle,
                selectedItemClass,
                disabledItemStyle,
                disabledItemClass,
                contentItemStyle,
                contentItemClass,
                indentStyle,
                indentClass,
                indentItemStyle,
                indentItemClass,
                submenuIconItemStyle,
                submenuIconItemClass,

                onshow,
                onhide
        };
    }

    public void restoreState(FacesContext facesContext, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(facesContext, values[i++]);

        _for = (String) values[i++];
        event = (String) values[i++];
        standalone = (Boolean) values[i++];

        submenuShowDelay = (Integer) values[i++];
        submenuHideDelay = (Integer) values[i++];
        submenuHorisontalOffset = (Integer) values[i++];

        indentVisible = (Boolean) values[i++];
        selectDisabledItems = (Boolean) values[i++];

        submenuImageUrl = (String) values[i++];
        disabledSubmenuImageUrl = (String) values[i++];
        selectedSubmenuImageUrl = (String) values[i++];
        selectedDisabledSubmenuImageUrl = (String) values[i++];

        itemIconUrl = (String) values[i++];
        disabledItemIconUrl = (String) values[i++];
        selectedItemIconUrl = (String) values[i++];
        selectedDisabledItemIconUrl = (String) values[i++];

        itemStyle = (String) values[i++];
        itemClass = (String) values[i++];
        selectedItemStyle = (String) values[i++];
        selectedItemClass = (String) values[i++];
        disabledItemStyle = (String) values[i++];
        disabledItemClass = (String) values[i++];
        contentItemStyle = (String) values[i++];
        contentItemClass = (String) values[i++];
        indentStyle = (String) values[i++];
        indentClass = (String) values[i++];
        indentItemStyle = (String) values[i++];
        indentItemClass = (String) values[i++];
        submenuIconItemStyle = (String) values[i++];
        submenuIconItemClass = (String) values[i++];

        onshow = (String) values[i++];
        onhide = (String) values[i++];
    }
}
