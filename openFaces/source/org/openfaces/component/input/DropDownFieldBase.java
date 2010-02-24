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
package org.openfaces.component.input;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.Side;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.AjaxUtil;

import javax.faces.context.FacesContext;

public abstract class DropDownFieldBase extends DropDownComponent implements CompoundComponent {
    private Boolean autoComplete;
    private Side listAlignment;
    private Boolean customValueAllowed;
    private String var;
    private SuggestionMode suggestionMode;
    private Integer suggestionDelay;
    private Integer timeout;
    private String ondropdown;
    private String oncloseup;
    private String horizontalGridLines;
    private String verticalGridLines;
    private String headerHorizSeparator;
    private String headerVertSeparator;
    private String multiHeaderSeparator;
    private String multiFooterSeparator;
    private String footerHorizSeparator;
    private String footerVertSeparator;
    private String listItemStyle;
    private String listItemClass;
    private String rolloverListItemStyle;
    private String rolloverListItemClass;
    private String oddListItemStyle;
    private String oddListItemClass;
    private String listHeaderRowStyle;
    private String listHeaderRowClass;
    private String listFooterRowStyle;
    private String listFooterRowClass;
    private Integer suggestionMinChars;

    private Integer maxlength;
    private Integer size;

    @Override
    public void setId(String id) {
        super.setId(id);
        DropDownPopup popup = getPopup();
        if (popup != null) {
            popup.setId(popup.getId());
        }
    }

    public int getMaxlength() {
        return ValueBindings.get(this, "maxlength", maxlength, Integer.MIN_VALUE);
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    protected int getSize() {
        return ValueBindings.get(this, "size", size, Integer.MIN_VALUE);
    }

    protected void setSize(int size) {
        this.size = size;
    }

    public boolean getCustomValueAllowed() {
        return ValueBindings.get(this, "customValueAllowed", customValueAllowed, true);
    }

    public void setCustomValueAllowed(boolean customValueAllowed) {
        this.customValueAllowed = customValueAllowed;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getOndropdown() {
        return ValueBindings.get(this, "ondropdown", ondropdown);
    }

    public void setOndropdown(String ondropdown) {
        this.ondropdown = ondropdown;
    }

    public String getOncloseup() {
        return ValueBindings.get(this, "oncloseup", oncloseup);
    }

    public void setOncloseup(String oncloseup) {
        this.oncloseup = oncloseup;
    }

    public String getListItemClass() {
        return ValueBindings.get(this, "listItemClass", listItemClass);
    }

    public void setListItemClass(String listItemClass) {
        this.listItemClass = listItemClass;
    }

    public String getRolloverListItemClass() {
        return ValueBindings.get(this, "rolloverListItemClass", rolloverListItemClass);
    }

    public void setRolloverListItemClass(String rolloverListItemClass) {
        this.rolloverListItemClass = rolloverListItemClass;
    }

    public int getTimeout() {
        return ValueBindings.get(this, "timeout", timeout, -1);
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getListItemStyle() {
        return ValueBindings.get(this, "listItemStyle", listItemStyle);
    }

    public void setListItemStyle(String listItemStyle) {
        this.listItemStyle = listItemStyle;
    }

    public String getRolloverListItemStyle() {
        return ValueBindings.get(this, "rolloverListItemStyle", rolloverListItemStyle);
    }

    public void setRolloverListItemStyle(String rolloverListItemStyle) {
        this.rolloverListItemStyle = rolloverListItemStyle;
    }

    public Side getListAlignment() {
        return ValueBindings.get(
                this, "listAlignment", listAlignment, Side.LEFT, Side.class);
    }

    public void setListAlignment(Side listAlignment) {
        this.listAlignment = listAlignment;
    }

    public String getHorizontalGridLines() {
        return ValueBindings.get(this, "horizontalGridLines", horizontalGridLines);
    }

    public void setHorizontalGridLines(String horizontalGridLines) {
        this.horizontalGridLines = horizontalGridLines;
    }

    public String getVerticalGridLines() {
        return ValueBindings.get(this, "verticalGridLines", verticalGridLines);
    }

    public void setVerticalGridLines(String verticalGridLines) {
        this.verticalGridLines = verticalGridLines;
    }

    public String getHeaderHorizSeparator() {
        return ValueBindings.get(this, "headerHorizSeparator", headerHorizSeparator);
    }

    public void setHeaderHorizSeparator(String headerHorizSeparator) {
        this.headerHorizSeparator = headerHorizSeparator;
    }

    public String getHeaderVertSeparator() {
        return ValueBindings.get(this, "headerVertSeparator", headerVertSeparator);
    }

    public void setHeaderVertSeparator(String headerVertSeparator) {
        this.headerVertSeparator = headerVertSeparator;
    }

    public String getFooterHorizSeparator() {
        return ValueBindings.get(this, "footerHorizSeparator", footerHorizSeparator);
    }

    public void setFooterHorizSeparator(String footerHorizSeparator) {
        this.footerHorizSeparator = footerHorizSeparator;
    }

    public String getMultiHeaderSeparator() {
        return ValueBindings.get(this, "multiHeaderSeparator", multiHeaderSeparator);
    }

    public void setMultiHeaderSeparator(String multiHeaderSeparator) {
        this.multiHeaderSeparator = multiHeaderSeparator;
    }

    public String getMultiFooterSeparator() {
        return ValueBindings.get(this, "multiFooterSeparator", multiFooterSeparator);
    }

    public void setMultiFooterSeparator(String multiFooterSeparator) {
        this.multiFooterSeparator = multiFooterSeparator;
    }

    public String getFooterVertSeparator() {
        return ValueBindings.get(this, "footerVertSeparator", footerVertSeparator);
    }

    public void setFooterVertSeparator(String footerVertSeparator) {
        this.footerVertSeparator = footerVertSeparator;
    }

    public String getOddListItemStyle() {
        return ValueBindings.get(this, "oddListItemStyle", oddListItemStyle);
    }

    public void setOddListItemStyle(String oddListItemStyle) {
        this.oddListItemStyle = oddListItemStyle;
    }

    public String getOddListItemClass() {
        return ValueBindings.get(this, "oddListItemClass", oddListItemClass);
    }

    public void setOddListItemClass(String oddListItemClass) {
        this.oddListItemClass = oddListItemClass;
    }

    public String getListHeaderRowStyle() {
        return ValueBindings.get(this, "listHeaderRowStyle", listHeaderRowStyle);
    }

    public void setListHeaderRowStyle(String listHeaderRowStyle) {
        this.listHeaderRowStyle = listHeaderRowStyle;
    }

    public String getListHeaderRowClass() {
        return ValueBindings.get(this, "listHeaderRowClass", listHeaderRowClass);
    }

    public void setListHeaderRowClass(String listHeaderRowClass) {
        this.listHeaderRowClass = listHeaderRowClass;
    }

    public String getListFooterRowStyle() {
        return ValueBindings.get(this, "listFooterRowStyle", listFooterRowStyle);
    }

    public void setListFooterRowStyle(String listFooterRowStyle) {
        this.listFooterRowStyle = listFooterRowStyle;
    }

    public String getListFooterRowClass() {
        return ValueBindings.get(this, "listFooterRowClass", listFooterRowClass);
    }

    public void setListFooterRowClass(String listFooterRowClass) {
        this.listFooterRowClass = listFooterRowClass;
    }

    public SuggestionMode getSuggestionMode() {
        return ValueBindings.get(
                this, "suggestionMode", suggestionMode, getDefaultSuggestionMode(), SuggestionMode.class);
    }

    protected SuggestionMode getDefaultSuggestionMode() {
        return SuggestionMode.NONE;
    }

    public void setSuggestionMode(SuggestionMode suggestionMode) {
        this.suggestionMode = suggestionMode;
    }

    public int getSuggestionDelay() {
        return ValueBindings.get(this, "suggestionDelay", suggestionDelay, 350);
    }

    public void setSuggestionDelay(int suggestionDelay) {
        this.suggestionDelay = suggestionDelay;
    }


    public boolean getAutoComplete() {
        return ValueBindings.get(this, "autoComplete", autoComplete, getDefaultAutoComplete());
    }

    protected boolean getDefaultAutoComplete() {
        return false;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                customValueAllowed,
                var,
                listItemStyle,
                rolloverListItemStyle,
                listItemClass,
                rolloverListItemClass,


                timeout,

                ondropdown,
                oncloseup,

                listAlignment,
                suggestionMode,
                suggestionDelay,
                suggestionMinChars,
                autoComplete,

                horizontalGridLines,
                verticalGridLines,
                headerHorizSeparator,
                headerVertSeparator,
                multiHeaderSeparator,
                multiFooterSeparator,
                footerHorizSeparator,
                footerVertSeparator,
                oddListItemStyle,
                oddListItemClass,
                listHeaderRowStyle,
                listHeaderRowClass,
                listFooterRowStyle,
                listFooterRowClass,

                maxlength,
                size
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        customValueAllowed = (Boolean) values[i++];
        var = (String) values[i++];
        listItemStyle = (String) values[i++];
        rolloverListItemStyle = (String) values[i++];
        listItemClass = (String) values[i++];
        rolloverListItemClass = (String) values[i++];

        timeout = (Integer) values[i++];

        ondropdown = (String) values[i++];
        oncloseup = (String) values[i++];

        listAlignment = (Side) values[i++];
        suggestionMode = (SuggestionMode) values[i++];
        suggestionDelay = (Integer) values[i++];
        suggestionMinChars = (Integer) values[i++];
        autoComplete = (Boolean) values[i++];

        horizontalGridLines = (String) values[i++];
        verticalGridLines = (String) values[i++];
        headerHorizSeparator = (String) values[i++];
        headerVertSeparator = (String) values[i++];
        multiHeaderSeparator = (String) values[i++];
        multiFooterSeparator = (String) values[i++];
        footerHorizSeparator = (String) values[i++];
        footerVertSeparator = (String) values[i++];
        oddListItemStyle = (String) values[i++];
        oddListItemClass = (String) values[i++];
        listHeaderRowStyle = (String) values[i++];
        listHeaderRowClass = (String) values[i++];
        listFooterRowStyle = (String) values[i++];
        listFooterRowClass = (String) values[i++];


        maxlength = (Integer) values[i++];
        size = (Integer) values[i++];
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public void createSubComponents(FacesContext context) {
        Components.getOrCreateFacet(context, this, DropDownPopup.COMPONENT_TYPE, "popup", DropDownPopup.class);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public DropDownPopup getPopup() {
        return (DropDownPopup) getFacet("popup");
    }


    protected int getDefaultSuggestionMinChars() {
        return 0;
    }

    public int getSuggestionMinChars() {
        return ValueBindings.get(this, "suggestionMinChars", suggestionMinChars, getDefaultSuggestionMinChars());
    }

    public void setSuggestionMinChars(int suggestionMinChars) {
        this.suggestionMinChars = suggestionMinChars;
    }
}
