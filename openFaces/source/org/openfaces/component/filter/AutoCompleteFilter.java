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
package org.openfaces.component.filter;

import org.openfaces.component.Side;
import org.openfaces.component.input.SuggestionMode;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AutoCompleteFilter extends TextSearchFilter {

    private Boolean autoComplete;
    private Side listAlignment;
    private Boolean customValueAllowed;
    private SuggestionMode suggestionMode;
    private Integer suggestionDelay;
    private Integer timeout;
    private String horizontalGridLines;
    private String listItemStyle;
    private String listItemClass;
    private String rolloverListItemStyle;
    private String rolloverListItemClass;
    private String oddListItemStyle;
    private String oddListItemClass;
    private Integer suggestionMinChars;


    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                customValueAllowed,
                listItemStyle,
                rolloverListItemStyle,
                listItemClass,
                rolloverListItemClass,

                timeout,

                listAlignment,
                suggestionMode,
                suggestionDelay,
                suggestionMinChars,
                autoComplete,

                horizontalGridLines,
                oddListItemStyle,
                oddListItemClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        customValueAllowed = (Boolean) values[i++];
        listItemStyle = (String) values[i++];
        rolloverListItemStyle = (String) values[i++];
        listItemClass = (String) values[i++];
        rolloverListItemClass = (String) values[i++];

        timeout = (Integer) values[i++];

        listAlignment = (Side) values[i++];
        suggestionMode = (SuggestionMode) values[i++];
        suggestionDelay = (Integer) values[i++];
        suggestionMinChars = (Integer) values[i++];
        autoComplete = (Boolean) values[i++];

        horizontalGridLines = (String) values[i++];
        oddListItemStyle = (String) values[i++];
        oddListItemClass = (String) values[i++];
    }

    public boolean getCustomValueAllowed() {
        return ValueBindings.get(this, "customValueAllowed", customValueAllowed, true);
    }

    public void setCustomValueAllowed(boolean customValueAllowed) {
        this.customValueAllowed = customValueAllowed;
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


    public SuggestionMode getSuggestionMode() {
        return ValueBindings.get(
                this, "suggestionMode", suggestionMode, SuggestionMode.STRING_START, SuggestionMode.class);
    }

    public void setSuggestionMode(SuggestionMode suggestionMode) {
        this.suggestionMode = suggestionMode;
    }

    public int getSuggestionDelay() {
        return ValueBindings.get(this, "suggestionDelay", suggestionDelay, 0);
    }

    public void setSuggestionDelay(int suggestionDelay) {
        this.suggestionDelay = suggestionDelay;
    }


    public boolean getAutoComplete() {
        return ValueBindings.get(this, "autoComplete", autoComplete, true);
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public int getSuggestionMinChars() {
        return ValueBindings.get(this, "suggestionMinChars", suggestionMinChars, 0);
    }

    public void setSuggestionMinChars(int suggestionMinChars) {
        this.suggestionMinChars = suggestionMinChars;
    }


}
