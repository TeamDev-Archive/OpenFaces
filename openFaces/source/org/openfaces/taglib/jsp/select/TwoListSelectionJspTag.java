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
package org.openfaces.taglib.jsp.select;

import org.openfaces.taglib.internal.select.TwoListSelectionTag;
import org.openfaces.taglib.jsp.UIInputJspTag;

import javax.el.ValueExpression;

/**
 * @author Kharchenko
 */
public class TwoListSelectionJspTag extends UIInputJspTag {

    public TwoListSelectionJspTag() {
        super(new TwoListSelectionTag());
    }

    public void setReorderingAllowed(ValueExpression reorderingAllowed) {
        getDelegate().setPropertyValue("reorderingAllowed", reorderingAllowed);
    }

    public void setMoveUpHint(ValueExpression moveUpHint) {
        getDelegate().setPropertyValue("moveUpHint", moveUpHint);
    }

    public void setMoveDownHint(ValueExpression moveDownHint) {
        getDelegate().setPropertyValue("moveDownHint", moveDownHint);
    }

    public void setLeftListboxHeader(ValueExpression leftListboxHeader) {
        getDelegate().setPropertyValue("leftListboxHeader", leftListboxHeader);
    }

    public void setRightListboxHeader(ValueExpression rightListboxHeader) {
        getDelegate().setPropertyValue("rightListboxHeader", rightListboxHeader);
    }

    public void setHeaderStyle(ValueExpression headerStyle) {
        getDelegate().setPropertyValue("headerStyle", headerStyle);
    }

    public void setHeaderClass(ValueExpression headerClass) {
        getDelegate().setPropertyValue("headerClass", headerClass);
    }

    public void setSize(ValueExpression size) {
        getDelegate().setPropertyValue("size", size);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setListStyle(ValueExpression listStyle) {
        getDelegate().setPropertyValue("listStyle", listStyle);
    }

    public void setListClass(ValueExpression listClass) {
        getDelegate().setPropertyValue("listClass", listClass);
    }

    public void setSortingAllowed(ValueExpression sortingAllowed) {
        getDelegate().setPropertyValue("sortingAllowed", sortingAllowed);
    }

    public void setOnadd(ValueExpression onadd) {
        getDelegate().setPropertyValue("onadd", onadd);
    }

    public void setOnremove(ValueExpression onremove) {
        getDelegate().setPropertyValue("onremove", onremove);
    }

    public void setAllowAddRemoveAll(ValueExpression allowAddRemoveAll) {
        getDelegate().setPropertyValue("allowAddRemoveAll", allowAddRemoveAll);
    }

    public void setAddAllHint(ValueExpression addAllHint) {
        getDelegate().setPropertyValue("addAllHint", addAllHint);
    }

    public void setAddHint(ValueExpression addHint) {
        getDelegate().setPropertyValue("addHint", addHint);
    }

    public void setRemoveAllHint(ValueExpression removeAllHint) {
        getDelegate().setPropertyValue("removeAllHint", removeAllHint);
    }

    public void setRemoveHint(ValueExpression removeHint) {
        getDelegate().setPropertyValue("removeHint", removeHint);
    }

    public void setAddText(ValueExpression addText) {
        getDelegate().setPropertyValue("addText", addText);
    }

    public void setRemoveText(ValueExpression removeText) {
        getDelegate().setPropertyValue("removeText", removeText);
    }

    public void setAddAllText(ValueExpression addAllText) {
        getDelegate().setPropertyValue("addAllText", addAllText);
    }

    public void setRemoveAllText(ValueExpression removeAllText) {
        getDelegate().setPropertyValue("removeAllText", removeAllText);
    }

    public void setMoveUpText(ValueExpression moveUpText) {
        getDelegate().setPropertyValue("moveUpText", moveUpText);
    }

    public void setMoveDownText(ValueExpression moveDownText) {
        getDelegate().setPropertyValue("moveDownText", moveDownText);
    }

    public void setButtonStyle(ValueExpression buttonStyle) {
        getDelegate().setPropertyValue("buttonStyle", buttonStyle);
    }

    public void setButtonClass(ValueExpression buttonClass) {
        getDelegate().setPropertyValue("buttonClass", buttonClass);
    }

    public void setDisabledButtonClass(ValueExpression disabledButtonClass) {
        getDelegate().setPropertyValue("disabledButtonClass", disabledButtonClass);
    }

    public void setDisabledButtonStyle(ValueExpression disabledButtonStyle) {
        getDelegate().setPropertyValue("disabledButtonStyle", disabledButtonStyle);
    }

    public void setDisabledClass(ValueExpression disabledClass) {
        getDelegate().setPropertyValue("disabledClass", disabledClass);
    }

    public void setDisabledHeaderClass(ValueExpression disabledHeaderClass) {
        getDelegate().setPropertyValue("disabledHeaderClass", disabledHeaderClass);
    }

    public void setDisabledHeaderStyle(ValueExpression disabledHeaderStyle) {
        getDelegate().setPropertyValue("disabledHeaderStyle", disabledHeaderStyle);
    }

    public void setDisabledListClass(ValueExpression disabledListClass) {
        getDelegate().setPropertyValue("disabledListClass", disabledListClass);
    }

    public void setDisabledListStyle(ValueExpression disabledListStyle) {
        getDelegate().setPropertyValue("disabledListStyle", disabledListStyle);
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        getDelegate().setPropertyValue("disabledStyle", disabledStyle);
    }

}
