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
package org.openfaces.renderkit;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Scrolling;

import javax.faces.component.UIComponent;
import java.util.Collections;
import java.util.List;

public class DefaultTableStyles implements TableStyles {
    private static final String DEFAULT_HEADER_HORIZ_SEPARATOR = "1px solid #a0a0a0";
    private static final String DEFAULT_COMMON_HEADER_SEPARATOR = "1px solid #a0a0a0";
    private static final String DEFAULT_FOOTER_HORIZ_SEPARATOR = "1px solid #a0a0a0";
    private static final String DEFAULT_COMMON_FOOTER_SEPARATOR = "1px solid #a0a0a0";
    private static final String DEFAULT_HORIZONTAL_GRID_LINES = "1px solid #e0e0e0";
    private static final String DEFAULT_VERTICAL_GRID_LINES = "none, 1px solid #e0e0e0";
    private static final String DEFAULT_HEADER_VERTICAL_SEPARATOR = "none, 1px solid #a0a0a0";
    private static final String DEFAULT_FOOTER_VERTICAL_SEPARATOR = "none, 1px solid #a0a0a0";
    private static final String DEFAULT_MULTI_HEADER_SEPARATOR = "none, 1px solid #a0a0a0";
    private static final String DEFAULT_MULTI_FOOTER_SEPARATOR = "none, 1px solid #a0a0a0";

    private String horizontalGridLines;
    private String verticalGridLines;
    private String commonHeaderSeparator;
    private String commonFooterSeparator;

    private String headerHorizSeparator;
    private String headerVertSeparator;
    private String footerHorizSeparator;
    private String footerVertSeparator;
    private String multiHeaderSeparator;
    private String multiFooterSeparator;

    private String bodyRowStyle;
    private String bodyRowClass;
    private String bodyOddRowStyle;
    private String bodyOddRowClass;
    private String headerRowStyle;
    private String headerRowClass;
    private String footerRowStyle;
    private String footerRowClass;

    public DefaultTableStyles() {
        this(DEFAULT_HORIZONTAL_GRID_LINES,
                DEFAULT_VERTICAL_GRID_LINES,
                DEFAULT_COMMON_HEADER_SEPARATOR,
                DEFAULT_COMMON_FOOTER_SEPARATOR,
                DEFAULT_HEADER_HORIZ_SEPARATOR,
                DEFAULT_FOOTER_HORIZ_SEPARATOR,
                DEFAULT_HEADER_VERTICAL_SEPARATOR,
                DEFAULT_FOOTER_VERTICAL_SEPARATOR,
                DEFAULT_MULTI_HEADER_SEPARATOR,
                DEFAULT_MULTI_FOOTER_SEPARATOR);
    }

    public DefaultTableStyles(
            String horizontalGridLines,
            String verticalGridLines,
            String commonHeaderSeparator,
            String commonFooterSeparator,
            String headerHorizSeparator,
            String footerHorizSeparator,
            String headerVertSeparator,
            String footerVertSeparator,
            String multiHeaderSeparator,
            String multiFooterSeparator) {
        this.horizontalGridLines = horizontalGridLines;
        this.verticalGridLines = verticalGridLines;
        this.commonHeaderSeparator = commonHeaderSeparator;
        this.commonFooterSeparator = commonFooterSeparator;
        this.headerHorizSeparator = headerHorizSeparator;
        this.footerHorizSeparator = footerHorizSeparator;
        this.headerVertSeparator = headerVertSeparator;
        this.footerVertSeparator = footerVertSeparator;
        this.multiHeaderSeparator = multiHeaderSeparator;
        this.multiFooterSeparator = multiFooterSeparator;
    }

    public String getHorizontalGridLines() {
        return horizontalGridLines;
    }

    public void setHorizontalGridLines(String horizontalGridLines) {
        this.horizontalGridLines = horizontalGridLines;
    }

    public String getVerticalGridLines() {
        return verticalGridLines;
    }

    public void setVerticalGridLines(String verticalGridLines) {
        this.verticalGridLines = verticalGridLines;
    }

    public String getCommonHeaderSeparator() {
        return commonHeaderSeparator;
    }

    public void setCommonHeaderSeparator(String commonHeaderSeparator) {
        this.commonHeaderSeparator = commonHeaderSeparator;
    }

    public String getCommonFooterSeparator() {
        return commonFooterSeparator;
    }

    public void setCommonFooterSeparator(String commonFooterSeparator) {
        this.commonFooterSeparator = commonFooterSeparator;
    }

    public String getHeaderHorizSeparator() {
        return headerHorizSeparator;
    }

    public void setHeaderHorizSeparator(String headerHorizSeparator) {
        this.headerHorizSeparator = headerHorizSeparator;
    }

    public String getHeaderVertSeparator() {
        return headerVertSeparator;
    }

    public void setHeaderVertSeparator(String headerVertSeparator) {
        this.headerVertSeparator = headerVertSeparator;
    }


    public String getFooterHorizSeparator() {
        return footerHorizSeparator;
    }

    public void setFooterHorizSeparator(String footerHorizSeparator) {
        this.footerHorizSeparator = footerHorizSeparator;
    }

    public String getFooterVertSeparator() {
        return footerVertSeparator;
    }

    public void setFooterVertSeparator(String footerVertSeparator) {
        this.footerVertSeparator = footerVertSeparator;
    }

    public String getMultiHeaderSeparator() {
        return multiHeaderSeparator;
    }

    public void setMultiHeaderSeparator(String multiHeaderSeparator) {
        this.multiHeaderSeparator = multiHeaderSeparator;
    }

    public String getMultiFooterSeparator() {
        return multiFooterSeparator;
    }

    public void setMultiFooterSeparator(String multiFooterSeparator) {
        this.multiFooterSeparator = multiFooterSeparator;
    }

    public String getBodyRowStyle() {
        return bodyRowStyle;
    }

    public void setBodyRowStyle(String bodyRowStyle) {
        this.bodyRowStyle = bodyRowStyle;
    }

    public String getBodyRowClass() {
        return bodyRowClass;
    }

    public void setBodyRowClass(String bodyRowClass) {
        this.bodyRowClass = bodyRowClass;
    }

    public String getBodyOddRowStyle() {
        return bodyOddRowStyle;
    }

    public void setBodyOddRowStyle(String bodyOddRowStyle) {
        this.bodyOddRowStyle = bodyOddRowStyle;
    }

    public String getBodyOddRowClass() {
        return bodyOddRowClass;
    }

    public void setBodyOddRowClass(String bodyOddRowClass) {
        this.bodyOddRowClass = bodyOddRowClass;
    }

    public String getHeaderRowStyle() {
        return headerRowStyle;
    }

    public void setHeaderRowStyle(String headerRowStyle) {
        this.headerRowStyle = headerRowStyle;
    }

    public String getHeaderRowClass() {
        return headerRowClass;
    }

    public void setHeaderRowClass(String headerRowClass) {
        this.headerRowClass = headerRowClass;
    }

    public String getFooterRowStyle() {
        return footerRowStyle;
    }

    public void setFooterRowStyle(String footerRowStyle) {
        this.footerRowStyle = footerRowStyle;
    }

    public String getFooterRowClass() {
        return footerRowClass;
    }

    public void setFooterRowClass(String footerRowClass) {
        this.footerRowClass = footerRowClass;
    }

    public List<BaseColumn> getRenderedColumns() {
        return Collections.emptyList();
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

    public String getHeaderSectionStyle() {
        return null;
    }

    public String getBodySectionClass() {
        return null;
    }

    public String getHeaderSectionClass() {
        return null;
    }

    public String getFooterSectionStyle() {
        return null;
    }

    public String getFooterSectionClass() {
        return null;
    }

    public boolean getApplyDefaultStyle() {
        return true;
    }

    public Scrolling getScrolling() {
        return null;
    }
}
