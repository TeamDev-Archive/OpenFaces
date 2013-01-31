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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.AbstractTableTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableJspTag extends AbstractComponentJspTag {

    protected AbstractTableJspTag(AbstractTableTag delegate) {
        super(delegate);
    }

    public void setSortAscending(ValueExpression sortAscending) {
        getDelegate().setPropertyValue("sortAscending", sortAscending);
    }

    public void setSortColumnId(ValueExpression sortColumnId) {
        getDelegate().setPropertyValue("sortColumnId", sortColumnId);
    }

    public void setHeaderSectionStyle(ValueExpression headerSectionStyle) {
        getDelegate().setPropertyValue("headerSectionStyle", headerSectionStyle);
    }

    public void setHeaderSectionClass(ValueExpression headerSectionClass) {
        getDelegate().setPropertyValue("headerSectionClass", headerSectionClass);
    }

    public void setBodySectionStyle(ValueExpression bodySectionStyle) {
        getDelegate().setPropertyValue("bodySectionStyle", bodySectionStyle);
    }

    public void setBodySectionClass(ValueExpression bodySectionClass) {
        getDelegate().setPropertyValue("bodySectionClass", bodySectionClass);
    }

    public void setFooterSectionStyle(ValueExpression footerSectionStyle) {
        getDelegate().setPropertyValue("footerSectionStyle", footerSectionStyle);
    }

    public void setFooterSectionClass(ValueExpression footerSectionClass) {
        getDelegate().setPropertyValue("footerSectionClass", footerSectionClass);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setAlign(ValueExpression align) {
        getDelegate().setPropertyValue("align", align);
    }

    public void setBgcolor(ValueExpression bgcolor) {
        getDelegate().setPropertyValue("bgcolor", bgcolor);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setRules(ValueExpression rules) {
        getDelegate().setPropertyValue("rules", rules);
    }

    public void setWidth(ValueExpression width) {
        getDelegate().setPropertyValue("width", width);
    }

    public void setBorder(ValueExpression border) {
        getDelegate().setPropertyValue("border", border);
    }

    public void setCellspacing(ValueExpression cellspacing) {
        getDelegate().setPropertyValue("cellspacing", cellspacing);
    }

    public void setCellpadding(ValueExpression cellpadding) {
        getDelegate().setPropertyValue("cellpadding", cellpadding);
    }

    public void setVar(ValueExpression var) {
        getDelegate().setPropertyValue("var", var);
    }

    public void setBodyRowStyle(ValueExpression bodyRowStyle) {
        getDelegate().setPropertyValue("bodyRowStyle", bodyRowStyle);
    }

    public void setBodyRowClass(ValueExpression bodyRowClass) {
        getDelegate().setPropertyValue("bodyRowClass", bodyRowClass);
    }

    public void setBodyOddRowStyle(ValueExpression bodyOddRowStyle) {
        getDelegate().setPropertyValue("bodyOddRowStyle", bodyOddRowStyle);
    }

    public void setBodyOddRowClass(ValueExpression bodyOddRowClass) {
        getDelegate().setPropertyValue("bodyOddRowClass", bodyOddRowClass);
    }

    public void setHeaderRowStyle(ValueExpression headerRowStyle) {
        getDelegate().setPropertyValue("headerRowStyle", headerRowStyle);
    }

    public void setHeaderRowClass(ValueExpression headerRowClass) {
        getDelegate().setPropertyValue("headerRowClass", headerRowClass);
    }

    public void setCommonHeaderRowStyle(ValueExpression commonHeaderRowStyle) {
        getDelegate().setPropertyValue("commonHeaderRowStyle", commonHeaderRowStyle);
    }

    public void setCommonHeaderRowClass(ValueExpression commonHeaderRowClass) {
        getDelegate().setPropertyValue("commonHeaderRowClass", commonHeaderRowClass);
    }

    public void setFooterRowStyle(ValueExpression footerRowStyle) {
        getDelegate().setPropertyValue("footerRowStyle", footerRowStyle);
    }

    public void setFooterRowClass(ValueExpression footerRowClass) {
        getDelegate().setPropertyValue("footerRowClass", footerRowClass);
    }

    public void setCommonFooterRowStyle(ValueExpression commonFooterRowStyle) {
        getDelegate().setPropertyValue("commonFooterRowStyle", commonFooterRowStyle);
    }

    public void setCommonFooterRowClass(ValueExpression commonFooterRowClass) {
        getDelegate().setPropertyValue("commonFooterRowClass", commonFooterRowClass);
    }

    public void setSortableHeaderStyle(ValueExpression sortableHeaderStyle) {
        getDelegate().setPropertyValue("sortableHeaderStyle", sortableHeaderStyle);
    }

    public void setSortableHeaderClass(ValueExpression sortableHeaderClass) {
        getDelegate().setPropertyValue("sortableHeaderClass", sortableHeaderClass);
    }

    public void setSortableHeaderRolloverStyle(ValueExpression sortableHeaderRolloverStyle) {
        getDelegate().setPropertyValue("sortableHeaderRolloverStyle", sortableHeaderRolloverStyle);
    }

    public void setSortableHeaderRolloverClass(ValueExpression sortableHeaderRolloverClass) {
        getDelegate().setPropertyValue("sortableHeaderRolloverClass", sortableHeaderRolloverClass);
    }

    public void setSortedColumnStyle(ValueExpression sortedColumnStyle) {
        getDelegate().setPropertyValue("sortedColumnStyle", sortedColumnStyle);
    }

    public void setSortedColumnClass(ValueExpression sortedColumnClass) {
        getDelegate().setPropertyValue("sortedColumnClass", sortedColumnClass);
    }

    public void setSortedColumnHeaderStyle(ValueExpression sortedColumnHeaderStyle) {
        getDelegate().setPropertyValue("sortedColumnHeaderStyle", sortedColumnHeaderStyle);
    }

    public void setSortedColumnHeaderClass(ValueExpression sortedColumnHeaderClass) {
        getDelegate().setPropertyValue("sortedColumnHeaderClass", sortedColumnHeaderClass);
    }

    public void setSortedColumnBodyStyle(ValueExpression sortedColumnBodyStyle) {
        getDelegate().setPropertyValue("sortedColumnBodyStyle", sortedColumnBodyStyle);
    }

    public void setSortedColumnBodyClass(ValueExpression sortedColumnBodyClass) {
        getDelegate().setPropertyValue("sortedColumnBodyClass", sortedColumnBodyClass);
    }

    public void setSortedColumnFooterStyle(ValueExpression sortedColumnFooterStyle) {
        getDelegate().setPropertyValue("sortedColumnFooterStyle", sortedColumnFooterStyle);
    }

    public void setSortedColumnFooterClass(ValueExpression sortedColumnFooterClass) {
        getDelegate().setPropertyValue("sortedColumnFooterClass", sortedColumnFooterClass);
    }


    public void setHorizontalGridLines(ValueExpression horizontalGridLines) {
        getDelegate().setPropertyValue("horizontalGridLines", horizontalGridLines);
    }

    public void setVerticalGridLines(ValueExpression verticalGridLines) {
        getDelegate().setPropertyValue("verticalGridLines", verticalGridLines);
    }

    public void setCommonHeaderSeparator(ValueExpression commonHeaderSeparator) {
        getDelegate().setPropertyValue("commonHeaderSeparator", commonHeaderSeparator);
    }

    public void setCommonFooterSeparator(ValueExpression commonFooterSeparator) {
        getDelegate().setPropertyValue("commonFooterSeparator", commonFooterSeparator);
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

    public void setApplyDefaultStyle(ValueExpression applyDefaultStyle) {
        getDelegate().setPropertyValue("applyDefaultStyle", applyDefaultStyle);
    }

    public void setUseAjax(ValueExpression useAjax) {
        getDelegate().setPropertyValue("useAjax", useAjax);
    }

    public void setAllRecordsFilterText(ValueExpression allRecordsFilterText) {
        getDelegate().setPropertyValue("allRecordsFilterText", allRecordsFilterText);
    }

    public void setEmptyRecordsFilterText(ValueExpression emptyRecordsFilterText) {
        getDelegate().setPropertyValue("emptyRecordsFilterText", emptyRecordsFilterText);
    }

    public void setNonEmptyRecordsFilterText(ValueExpression nonEmptyRecordsFilterText) {
        getDelegate().setPropertyValue("nonEmptyRecordsFilterText", nonEmptyRecordsFilterText);
    }

    public void setSubHeaderRowStyle(ValueExpression subHeaderRowStyle) {
        getDelegate().setPropertyValue("subHeaderRowStyle", subHeaderRowStyle);
    }

    public void setSubHeaderRowClass(ValueExpression subHeaderRowClass) {
        getDelegate().setPropertyValue("subHeaderRowClass", subHeaderRowClass);
    }

    public void setSubHeaderRowSeparator(ValueExpression subHeaderRowSeparator) {
        getDelegate().setPropertyValue("subHeaderRowSeparator", subHeaderRowSeparator);
    }

    public void setRolloverRowStyle(ValueExpression rolloverRowStyle) {
        getDelegate().setPropertyValue("rolloverRowStyle", rolloverRowStyle);
    }

    public void setRolloverRowClass(ValueExpression rolloverRowClass) {
        getDelegate().setPropertyValue("rolloverRowClass", rolloverRowClass);
    }

    public void setNoDataRowStyle(ValueExpression noDataRowStyle) {
        getDelegate().setPropertyValue("noDataRowStyle", noDataRowStyle);
    }

    public void setNoDataRowClass(ValueExpression noDataRowClass) {
        getDelegate().setPropertyValue("noDataRowClass", noDataRowClass);
    }

    public void setNoDataMessageAllowed(ValueExpression noDataMessageAllowed) {
        getDelegate().setPropertyValue("noDataMessageAllowed", noDataMessageAllowed);
    }

    public void setKeepSelectionVisible(ValueExpression keepSelectionVisible) {
        getDelegate().setPropertyValue("keepSelectionVisible", keepSelectionVisible);
    }

    public void setColumnIndexVar(ValueExpression columnIndexVar) {
        getDelegate().setPropertyValue("columnIndexVar", columnIndexVar);
    }

    public void setColumnIdVar(ValueExpression columnIdVar) {
        getDelegate().setPropertyValue("columnIdVar", columnIdVar);
    }

    public void setColumnsOrder(ValueExpression columnsOrder) {
        getDelegate().setPropertyValue("columnsOrder", columnsOrder);
    }


    public void setSortedAscendingImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("sortedAscendingImageUrl", value);
    }

    public void setSortedDescendingImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("sortedDescendingImageUrl", value);
    }

    public void setAutoFilterDelay(ValueExpression autoFilterDelay) {
        getDelegate().setPropertyValue("autoFilterDelay", autoFilterDelay);
    }

    public void setDeferBodyLoading(ValueExpression deferBodyLoading) {
        getDelegate().setPropertyValue("deferBodyLoading", deferBodyLoading);
    }

    public void setUnsortedStateAllowed(ValueExpression unsortedStateAllowed) {
        getDelegate().setPropertyValue("unsortedStateAllowed", unsortedStateAllowed);
    }

    public void setUnDisplayedSelectionAllowed(ValueExpression unDisplayedSelectionAllowed) {
        getDelegate().setPropertyValue("unDisplayedSelectionAllowed", unDisplayedSelectionAllowed);
    }

}
