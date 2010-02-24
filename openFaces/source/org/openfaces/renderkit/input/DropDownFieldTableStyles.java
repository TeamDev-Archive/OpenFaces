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
package org.openfaces.renderkit.input;

import org.openfaces.component.TableStyles;
import org.openfaces.component.input.DropDownFieldBase;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.Scrolling;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownFieldTableStyles implements TableStyles {
    private DropDownFieldBase dropDownField;
    private List<UIComponent> childComponents;

    public DropDownFieldTableStyles(DropDownFieldBase dropDownField, List<UIComponent> childComponents) {
        this.dropDownField = dropDownField;
        this.childComponents = childComponents;
    }

    public String getHorizontalGridLines() {
        return dropDownField.getHorizontalGridLines();
    }

    public void setHorizontalGridLines(String horizontalGridLines) {
        dropDownField.setHorizontalGridLines(horizontalGridLines);
    }

    public String getVerticalGridLines() {
        return dropDownField.getVerticalGridLines();
    }

    public void setVerticalGridLines(String verticalGridLines) {
        dropDownField.setVerticalGridLines(verticalGridLines);
    }

    public String getCommonHeaderSeparator() {
        return null;//dropDownField.getCommonHeaderSeparator();
    }

    public void setCommonHeaderSeparator(String commonHeaderSeparator) {
//    dropDownField.setCommonHeaderSeparator(commonHeaderSeparator);
    }

    public String getCommonFooterSeparator() {
        return null;
    }

    public void setCommonFooterSeparator(String commonFooterSeparator) {
//    dropDownField.setCommonFooterSeparator(commonFooterSeparator);
    }

    public String getHeaderHorizSeparator() {
        return dropDownField.getHeaderHorizSeparator();
    }

    public void setHeaderHorizSeparator(String headerHorizSeparator) {
        dropDownField.setHeaderHorizSeparator(headerHorizSeparator);
    }

    public String getHeaderVertSeparator() {
        return dropDownField.getHeaderVertSeparator();
    }

    public void setHeaderVertSeparator(String headerVertSeparator) {
        dropDownField.setHeaderVertSeparator(headerVertSeparator);
    }

    public String getMultiHeaderSeparator() {
        return dropDownField.getMultiHeaderSeparator();
    }

    public void setMultiHeaderSeparator(String multiHeaderSeparator) {
        dropDownField.setMultiHeaderSeparator(multiHeaderSeparator);
    }

    public String getMultiFooterSeparator() {
        return dropDownField.getMultiFooterSeparator();
    }

    public void setMultiFooterSeparator(String multiFooterSeparator) {
        dropDownField.setMultiFooterSeparator(multiFooterSeparator);
    }

    public String getFooterHorizSeparator() {
        return dropDownField.getFooterHorizSeparator();
    }

    public void setFooterHorizSeparator(String footerHorizSeparator) {
        dropDownField.setFooterHorizSeparator(footerHorizSeparator);
    }

    public String getFooterVertSeparator() {
        return dropDownField.getFooterVertSeparator();
    }

    public void setFooterVertSeparator(String footerVertSeparator) {
        dropDownField.setFooterVertSeparator(footerVertSeparator);
    }

    public String getBodyRowStyle() {
        return dropDownField.getListItemStyle();
    }

    public void setBodyRowStyle(String bodyRowStyle) {
        dropDownField.setListItemStyle(bodyRowStyle);
    }

    public String getBodyRowClass() {
        return dropDownField.getListItemClass();
    }

    public void setBodyRowClass(String bodyRowClass) {
        dropDownField.setListItemClass(bodyRowClass);
    }

    public String getBodyOddRowStyle() {
        return dropDownField.getOddListItemStyle();
    }

    public void setBodyOddRowStyle(String bodyOddRowStyle) {
        dropDownField.setOddListItemStyle(bodyOddRowStyle);
    }

    public String getBodyOddRowClass() {
        return dropDownField.getOddListItemClass();
    }

    public void setBodyOddRowClass(String bodyOddRowClass) {
        dropDownField.setOddListItemClass(bodyOddRowClass);
    }

    public String getHeaderRowStyle() {
        return dropDownField.getListHeaderRowStyle();
    }

    public void setHeaderRowStyle(String headerRowStyle) {
        dropDownField.setListHeaderRowStyle(headerRowStyle);
    }

    public String getHeaderRowClass() {
        return dropDownField.getListHeaderRowClass();
    }

    public void setHeaderRowClass(String headerRowClass) {
        dropDownField.setListHeaderRowClass(headerRowClass);
    }

    public String getFooterRowStyle() {
        return dropDownField.getListFooterRowStyle();
    }

    public void setFooterRowStyle(String footerRowStyle) {
        dropDownField.setListFooterRowStyle(footerRowStyle);
    }

    public String getFooterRowClass() {
        return dropDownField.getListFooterRowClass();
    }

    public void setFooterRowClass(String footerRowClass) {
        dropDownField.setListFooterRowClass(footerRowClass);
    }

    public List<BaseColumn> getRenderedColumns() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<BaseColumn> columns = TableUtil.getColumnsFromList(context, dropDownField.getChildren());
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            if (!column.isRendered())
                iterator.remove();
        }
        if (childComponents.size() > 0 || columns.size() == 0)
            columns.add(new Column());

        return columns;
    }

    public UIComponent getHeader() {
        return null;
    }

    public UIComponent getFooter() {
        return null;
    }

    public String getBodySectionStyle() {
        return null;
    }

    public String getFooterSectionStyle() {
        return null;
    }

    public String getHeaderSectionStyle() {
        return null;
    }

    public String getBodySectionClass() {
        return null;
    }

    public String getHeaderSectionClass() {
        return null;
    }

    public String getFooterSectionClass() {
        return null;
    }

    public boolean getApplyDefaultStyle() {
        return true;
    }

    public Scrolling getScrolling() {
        return Components.findChildWithClass(dropDownField, Scrolling.class, "<o:scrolling>");
    }
}
