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
package org.openfaces.component.select;

import org.openfaces.component.OUISelectMany;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * The TwoListSelection component provides an alternative interface for selecting a
 * list of items. It displays two lists of data and allows the user moving items between
 * them thus constructing a list of selected items in one of them. As opposed to the
 * ordinary selection components such as the standard HtmlSelectManyListbox it also
 * allows the user to reorder the selected items.
 *
 * @author Kharchenko
 */
public class TwoListSelection extends OUISelectMany {
    public static final String COMPONENT_TYPE = "org.openfaces.TwoListSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.TwoListSelection";

    // todo: add support for focusedStyle/focusedClass attributes inherited from OUIInput
    // todo: add support for onfocus/onblur events inherited from OUISelectMany (onfocus/onblur shouldn't fire when focus is transferred within TwoListSelection)

    private String leftListboxSelectedItems;
    private String rightListboxSelectedItems;

    private Integer size;
    private String tabindex;

    private String leftListboxHeader;
    private String rightListboxHeader;

    private Boolean sortingAllowed;
    private Boolean reorderingAllowed;

    private String onadd;
    private String onremove;

    private Boolean allowAddRemoveAll;

    private String addAllHint;
    private String addHint;
    private String removeAllHint;
    private String removeHint;
    private String moveUpHint;
    private String moveDownHint;

    private String addText;
    private String removeText;
    private String addAllText;
    private String removeAllText;
    private String moveUpText;
    private String moveDownText;

    private String disabledStyle; // todo: shouldn't these two properties be pulled up the hierarchy to OUIInput?
    private String disabledClass;

    private String headerStyle;
    private String headerClass;
    private String listStyle;
    private String listClass;
    private String buttonStyle;
    private String buttonClass;

    private String disabledHeaderStyle;
    private String disabledHeaderClass;
    private String disabledListStyle;
    private String disabledListClass;
    private String disabledButtonStyle;
    private String disabledButtonClass;

    public TwoListSelection() {
        setRendererType("org.openfaces.TwoListSelectionRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                leftListboxSelectedItems, rightListboxSelectedItems, leftListboxHeader, rightListboxHeader,
                headerStyle, headerClass, size, tabindex, listStyle, listClass, sortingAllowed,
                reorderingAllowed, onadd, onremove, allowAddRemoveAll, addAllHint, addHint, removeAllHint,
                removeHint, moveUpHint, moveDownHint, addAllText, addText, removeAllText, removeText,
                moveUpText, moveDownText, buttonStyle, buttonClass, disabledHeaderStyle, disabledHeaderClass,
                disabledStyle, disabledClass, disabledListStyle, disabledListClass, disabledButtonStyle,
                disabledButtonClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        leftListboxSelectedItems = (String) values[i++];
        rightListboxSelectedItems = (String) values[i++];
        leftListboxHeader = (String) values[i++];
        rightListboxHeader = (String) values[i++];

        headerStyle = (String) values[i++];
        headerClass = (String) values[i++];
        size = (Integer) values[i++];
        tabindex = (String) values[i++];
        listStyle = (String) values[i++];
        listClass = (String) values[i++];
        sortingAllowed = (Boolean) values[i++];
        reorderingAllowed = (Boolean) values[i++];
        onadd = (String) values[i++];
        onremove = (String) values[i++];
        allowAddRemoveAll = (Boolean) values[i++];
        addAllHint = (String) values[i++];
        addHint = (String) values[i++];
        removeAllHint = (String) values[i++];
        removeHint = (String) values[i++];
        moveUpHint = (String) values[i++];
        moveDownHint = (String) values[i++];

        addAllText = (String) values[i++];
        addText = (String) values[i++];
        removeAllText = (String) values[i++];
        removeText = (String) values[i++];
        moveUpText = (String) values[i++];
        moveDownText = (String) values[i++];

        buttonStyle = (String) values[i++];
        buttonClass = (String) values[i++];

        disabledHeaderStyle = (String) values[i++];
        disabledHeaderClass = (String) values[i++];
        disabledStyle = (String) values[i++];
        disabledClass = (String) values[i++];
        disabledListStyle = (String) values[i++];
        disabledListClass = (String) values[i++];
        disabledButtonStyle = (String) values[i++];
        disabledButtonClass = (String) values[i++];
    }

    public boolean getReorderingAllowed() {
        return ValueBindings.get(this, "reorderingAllowed", reorderingAllowed, true);
    }

    public void setReorderingAllowed(boolean reorderingAllowed) {
        this.reorderingAllowed = reorderingAllowed;
    }

    public String getMoveUpHint() {
        return ValueBindings.get(this, "moveUpHint", moveUpHint, "Move Up");
    }

    public void setMoveUpHint(String moveUpHint) {
        this.moveUpHint = moveUpHint;
    }

    public String getMoveDownHint() {
        return ValueBindings.get(this, "moveDownHint", moveDownHint, "Move Down");
    }

    public void setMoveDownHint(String moveDownHint) {
        this.moveDownHint = moveDownHint;
    }

    public String getLeftListboxSelectedItems() {
        return leftListboxSelectedItems;
    }

    public void setLeftListboxSelectedItems(String leftListboxSelectedItems) {
        this.leftListboxSelectedItems = leftListboxSelectedItems;
    }

    public String getRightListboxSelectedItems() {
        return rightListboxSelectedItems;
    }

    public void setRightListboxSelectedItems(String rightListboxSelectedItems) {
        this.rightListboxSelectedItems = rightListboxSelectedItems;
    }

    public String getLeftListboxHeader() {
        return ValueBindings.get(this, "leftListboxHeader", leftListboxHeader);
    }

    public void setLeftListboxHeader(String leftListboxHeader) {
        this.leftListboxHeader = leftListboxHeader;
    }

    public String getRightListboxHeader() {
        return ValueBindings.get(this, "rightListboxHeader", rightListboxHeader);
    }

    public void setRightListboxHeader(String rightListboxHeader) {
        this.rightListboxHeader = rightListboxHeader;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public int getSize() {
        return ValueBindings.get(this, "size", size, -1);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTabindex() {
        return ValueBindings.get(this, "tabindex", tabindex);
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getListStyle() {
        return ValueBindings.get(this, "listStyle", listStyle);
    }

    public void setListStyle(String listStyle) {
        this.listStyle = listStyle;
    }

    public String getListClass() {
        return ValueBindings.get(this, "listClass", listClass);
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public boolean isSortingAllowed() {
        return ValueBindings.get(this, "sortingAllowed", sortingAllowed, false);
    }

    public void setSortingAllowed(boolean sortingAllowed) {
        this.sortingAllowed = sortingAllowed;
    }

    public String getOnadd() {
        return ValueBindings.get(this, "onadd", onadd);
    }

    public void setOnadd(String onadd) {
        this.onadd = onadd;
    }

    public String getOnremove() {
        return ValueBindings.get(this, "onremove", onremove);
    }

    public void setOnremove(String onremove) {
        this.onremove = onremove;
    }

    public boolean isAllowAddRemoveAll() {
        return ValueBindings.get(this, "allowAddRemoveAll", allowAddRemoveAll, true);
    }

    public void setAllowAddRemoveAll(boolean allowAddRemoveAll) {
        this.allowAddRemoveAll = allowAddRemoveAll;
    }

    public String getAddAllHint() {
        return ValueBindings.get(this, "addAllHint", addAllHint, "Add All");
    }

    public void setAddAllHint(String addAllHint) {
        this.addAllHint = addAllHint;
    }

    public String getAddHint() {
        return ValueBindings.get(this, "addHint", addHint, "Add Selected");
    }

    public void setAddHint(String addHint) {
        this.addHint = addHint;
    }

    public String getRemoveAllHint() {
        return ValueBindings.get(this, "removeAllHint", removeAllHint, "Remove All");
    }

    public void setRemoveAllHint(String removeAllHint) {
        this.removeAllHint = removeAllHint;
    }

    public String getRemoveHint() {
        return ValueBindings.get(this, "removeHint", removeHint, "Remove Selected");
    }

    public void setRemoveHint(String removeHint) {
        this.removeHint = removeHint;
    }

    public String getButtonClass() {
        return ValueBindings.get(this, "buttonClass", buttonClass);
    }

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public String getButtonStyle() {
        return ValueBindings.get(this, "buttonStyle", buttonStyle);
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public String getMoveDownText() {
        return ValueBindings.get(this, "moveDownText", moveDownText, "Down");
    }

    public void setMoveDownText(String moveDownText) {
        this.moveDownText = moveDownText;
    }

    public String getMoveUpText() {
        return ValueBindings.get(this, "moveUpText", moveUpText, "Up");
    }

    public void setMoveUpText(String moveUpText) {
        this.moveUpText = moveUpText;
    }

    public String getRemoveAllText() {
        return ValueBindings.get(this, "removeAllText", removeAllText, "<<");
    }

    public void setRemoveAllText(String removeAllText) {
        this.removeAllText = removeAllText;
    }

    public String getAddAllText() {
        return ValueBindings.get(this, "addAllText", addAllText, ">>");
    }

    public void setAddAllText(String addAllText) {
        this.addAllText = addAllText;
    }

    public String getRemoveText() {
        return ValueBindings.get(this, "removeText", removeText, "<");
    }

    public void setRemoveText(String removeText) {
        this.removeText = removeText;
    }

    public String getAddText() {
        return ValueBindings.get(this, "addText", addText, ">");
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public String getDisabledButtonClass() {
        return ValueBindings.get(this, "disabledButtonClass", disabledButtonClass);
    }

    public void setDisabledButtonClass(String disabledButtonClass) {
        this.disabledButtonClass = disabledButtonClass;
    }

    public String getDisabledButtonStyle() {
        return ValueBindings.get(this, "disabledButtonStyle", disabledButtonStyle);
    }

    public void setDisabledButtonStyle(String disabledButtonStyle) {
        this.disabledButtonStyle = disabledButtonStyle;
    }

    public String getDisabledClass() {
        return ValueBindings.get(this, "disabledClass", disabledClass);
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    public String getDisabledHeaderClass() {
        return ValueBindings.get(this, "disabledHeaderClass", disabledHeaderClass);
    }

    public void setDisabledHeaderClass(String disabledHeaderClass) {
        this.disabledHeaderClass = disabledHeaderClass;
    }

    public String getDisabledHeaderStyle() {
        return ValueBindings.get(this, "disabledHeaderStyle", disabledHeaderStyle);
    }

    public void setDisabledHeaderStyle(String disabledHeaderStyle) {
        this.disabledHeaderStyle = disabledHeaderStyle;
    }

    public String getDisabledListClass() {
        return ValueBindings.get(this, "disabledListClass", disabledListClass);
    }

    public void setDisabledListClass(String disabledListClass) {
        this.disabledListClass = disabledListClass;
    }

    public String getDisabledListStyle() {
        return ValueBindings.get(this, "disabledListStyle", disabledListStyle);
    }

    public void setDisabledListStyle(String disabledListStyle) {
        this.disabledListStyle = disabledListStyle;
    }

    public String getDisabledStyle() {
        return ValueBindings.get(this, "disabledStyle", disabledStyle);
    }

    public void setDisabledStyle(String disabledStyle) {
        this.disabledStyle = disabledStyle;
    }
}
