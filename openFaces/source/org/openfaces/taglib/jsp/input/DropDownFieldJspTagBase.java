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
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.DropDownComponentTag;

import javax.el.ValueExpression;

public abstract class DropDownFieldJspTagBase extends DropDownComponentJspTag {
    protected DropDownFieldJspTagBase(DropDownComponentTag delegate) {
        super(delegate);
    }

    public void setVar(ValueExpression var) {
        getDelegate().setPropertyValue("var", var);
    }

    public void setCustomValueAllowed(ValueExpression value) {
        getDelegate().setPropertyValue("customValueAllowed", value);
    }

    public void setListItemStyle(ValueExpression listItemStyle) {
        getDelegate().setPropertyValue("listItemStyle", listItemStyle);
    }

    public void setRolloverListItemStyle(ValueExpression rolloverListItemStyle) {
        getDelegate().setPropertyValue("rolloverListItemStyle", rolloverListItemStyle);
    }

    public void setListItemClass(ValueExpression listItemClass) {
        getDelegate().setPropertyValue("listItemClass", listItemClass);
    }

    public void setRolloverListItemClass(ValueExpression rolloverListItemClass) {
        getDelegate().setPropertyValue("rolloverListItemClass", rolloverListItemClass);
    }

    public void setListAlignment(ValueExpression listAlignment) {
        getDelegate().setPropertyValue("listAlignment", listAlignment);
    }

    public void setOndropdown(ValueExpression ondropdown) {
        getDelegate().setPropertyValue("ondropdown", ondropdown);
    }

    public void setOncloseup(ValueExpression oncloseup) {
        getDelegate().setPropertyValue("oncloseup", oncloseup);
    }

    public void setListClass(ValueExpression listClass) {
        getDelegate().setPropertyValue("listClass", listClass);
    }

    public void setRolloverListClass(ValueExpression rolloverListClass) {
        getDelegate().setPropertyValue("rolloverListClass", rolloverListClass);
    }

    public void setTimeout(ValueExpression timeout) {
        getDelegate().setPropertyValue("timeout", timeout);
    }

    public void setListStyle(ValueExpression listStyle) {
        getDelegate().setPropertyValue("listStyle", listStyle);
    }

    public void setRolloverListStyle(ValueExpression rolloverListStyle) {
        getDelegate().setPropertyValue("rolloverListStyle", rolloverListStyle);
    }

    public void setSuggestionMode(ValueExpression suggestionMode) {
        getDelegate().setPropertyValue("suggestionMode", suggestionMode);
    }

    public void setSuggestionDelay(ValueExpression suggestionDelay) {
        getDelegate().setPropertyValue("suggestionDelay", suggestionDelay);
    }

    public void setHorizontalGridLines(ValueExpression horizontalGridLines) {
        getDelegate().setPropertyValue("horizontalGridLines", horizontalGridLines);
    }

    public void setVerticalGridLines(ValueExpression verticalGridLines) {
        getDelegate().setPropertyValue("verticalGridLines", verticalGridLines);
    }

    public void setHeaderHorizSeparator(ValueExpression headerHorizSeparator) {
        getDelegate().setPropertyValue("headerHorizSeparator", headerHorizSeparator);
    }

    public void setHeaderVertSeparator(ValueExpression headerVertSeparator) {
        getDelegate().setPropertyValue("headerVertSeparator", headerVertSeparator);
    }

    public void setMultiHeaderSeparator(ValueExpression multiHeaderSeparator) {
        getDelegate().setPropertyValue("multiHeaderSeparator", multiHeaderSeparator);
    }

    public void setMultiFooterSeparator(ValueExpression multiFooterSeparator) {
        getDelegate().setPropertyValue("multiFooterSeparator", multiFooterSeparator);
    }

    public void setFooterHorizSeparator(ValueExpression footerHorizSeparator) {
        getDelegate().setPropertyValue("footerHorizSeparator", footerHorizSeparator);
    }

    public void setFooterVertSeparator(ValueExpression footerVertSeparator) {
        getDelegate().setPropertyValue("footerVertSeparator", footerVertSeparator);
    }

    public void setOddListItemStyle(ValueExpression oddListItemStyle) {
        getDelegate().setPropertyValue("oddListItemStyle", oddListItemStyle);
    }

    public void setOddListItemClass(ValueExpression oddListItemClass) {
        getDelegate().setPropertyValue("oddListItemClass", oddListItemClass);
    }

    public void setListHeaderRowStyle(ValueExpression listHeaderRowStyle) {
        getDelegate().setPropertyValue("listHeaderRowStyle", listHeaderRowStyle);
    }

    public void setListHeaderRowClass(ValueExpression listHeaderRowClass) {
        getDelegate().setPropertyValue("listHeaderRowClass", listHeaderRowClass);
    }

    public void setListFooterRowStyle(ValueExpression listFooterRowStyle) {
        getDelegate().setPropertyValue("listFooterRowStyle", listFooterRowStyle);
    }

    public void setListFooterRowClass(ValueExpression listFooterRowClass) {
        getDelegate().setPropertyValue("listFooterRowClass", listFooterRowClass);
    }

    public void setAutoComplete(ValueExpression autoComplete) {
        getDelegate().setPropertyValue("autoComplete", autoComplete);
    }

    public void setSuggestionMinChars(ValueExpression suggestionMinChars) {
        getDelegate().setPropertyValue("suggestionMinChars", suggestionMinChars);
    }

    protected void setSize(ValueExpression size) {
        getDelegate().setPropertyValue("size", size);
    }

    public void setMaxlength(ValueExpression maxlength) {
        getDelegate().setPropertyValue("maxlength", maxlength);
    }

    
    public void setPreloadedItemCount(ValueExpression preloadedItemCount) {
        getDelegate().setPropertyValue("preloadedItemCount", preloadedItemCount);
    }


    public void setPageSize(ValueExpression pageSize) {
        getDelegate().setPropertyValue("pageSize", pageSize);
    }

    public void setTotalItemCount(ValueExpression totalItemCount) {
        getDelegate().setPropertyValue("totalItemCount", totalItemCount);
    }

    public void setCachingAllowed(ValueExpression cachingAllowed) {
        getDelegate().setPropertyValue("cachingAllowed", cachingAllowed);
    }


}
