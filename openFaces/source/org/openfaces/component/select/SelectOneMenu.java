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

import org.openfaces.component.Side;
import org.openfaces.component.input.DropDownFieldBase;
import org.openfaces.component.input.SuggestionMode;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class SelectOneMenu extends DropDownFieldBase {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectOneMenu";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectOneMenu";
    private static final int DEFAULT_ITEM_PRESENTATION_COLUMN = -1;

    private Integer itemPresentationColumn;

    public SelectOneMenu() {
        setRendererType("org.openfaces.SelectOneMenuRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                itemPresentationColumn

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        itemPresentationColumn = (Integer) state[i++];

    }

    @Override
    public String getListClass() {
        return super.getListClass();
    }

    @Override
    public void setListClass(String listClass) {
        super.setListClass(listClass);
    }

    @Override
    public String getRolloverListClass() {
        return super.getRolloverListClass();
    }

    @Override
    public void setRolloverListClass(String rolloverListClass) {
        super.setRolloverButtonClass(rolloverListClass);
    }

    @Override
    public String getListStyle() {
        return super.getListStyle();
    }

    @Override
    public void setListStyle(String listStyle) {
        super.setListStyle(listStyle);
    }

    @Override
    public String getRolloverListStyle() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverListStyle(String rolloverListStyle) {
        super.setRolloverButtonClass(rolloverListStyle);
    }

    @Override
    public String getFieldStyle() {
        return super.getFieldStyle();
    }

    @Override
    public void setFieldStyle(String fieldStyle) {
        super.setFieldStyle(fieldStyle);
    }

    @Override
    public String getRolloverFieldStyle() {
        return super.getRolloverFieldStyle();
    }

    @Override
    public void setRolloverFieldStyle(String rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }


    @Override
    public String getFieldClass() {
        return super.getFieldClass();
    }

    @Override
    public void setFieldClass(String fieldClass) {
        super.setFieldClass(fieldClass);
    }

    @Override
    public String getRolloverFieldClass() {
        return super.getRolloverFieldClass();
    }

    @Override
    public void setRolloverFieldClass(String rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }


    @Override
    public String getButtonStyle() {
        return super.getButtonStyle();
    }

    @Override
    public void setButtonStyle(String buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    @Override
    public String getRolloverButtonStyle() {
        return super.getRolloverButtonStyle();
    }

    @Override
    public void setRolloverButtonStyle(String rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    @Override
    public String getButtonImageUrl() {
        return super.getButtonImageUrl();
    }

    @Override
    public void setButtonImageUrl(String buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    @Override
    public String getPressedButtonStyle() {
        return super.getPressedButtonStyle();
    }

    @Override
    public void setPressedButtonStyle(String pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    @Override
    public String getPressedButtonClass() {
        return super.getPressedButtonClass();
    }

    @Override
    public void setPressedButtonClass(String pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    @Override
    public String getButtonClass() {
        return super.getButtonClass();
    }

    @Override
    public void setButtonClass(String buttonClass) {
        super.setButtonClass(buttonClass);
    }

    @Override
    public String getRolloverButtonClass() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverButtonClass(String rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    @Override
    public Side getButtonAlignment() {
        return super.getButtonAlignment();
    }

    @Override
    public void setButtonAlignment(Side buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    @Override
    public String getDisabledButtonStyle() {
        return super.getDisabledButtonStyle();
    }

    @Override
    public void setDisabledButtonStyle(String disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    @Override
    public String getDisabledButtonClass() {
        return super.getDisabledButtonClass();
    }

    @Override
    public void setDisabledButtonClass(String disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    @Override
    public String getDisabledButtonImageUrl() {
        return super.getDisabledButtonImageUrl();
    }

    @Override
    public void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }

    @Override
    public boolean getCustomValueAllowed() {
        return false;
    }

    protected SuggestionMode getDefaultSuggestionMode() {
        return SuggestionMode.ALL;
    }

    @Override
    protected int getDefaultSuggestionDelay() {
        return 0;
    }

    protected boolean getDefaultAutoComplete() {
        return true;
    }

    public int getItemPresentationColumn() {
        return ValueBindings.get(this, "itemPresentationColumn", itemPresentationColumn, DEFAULT_ITEM_PRESENTATION_COLUMN);
    }

    public void setItemPresentationColumn(int itemPresentationColumn) {
        this.itemPresentationColumn = itemPresentationColumn;
    }

    @Override
    public Boolean getChangeValueOnSelect() {
        return super.getChangeValueOnSelect();
    }

    @Override
    public void setChangeValueOnSelect(Boolean changeValueOnSelect) {
        super.setChangeValueOnSelect(changeValueOnSelect);
    }

}
