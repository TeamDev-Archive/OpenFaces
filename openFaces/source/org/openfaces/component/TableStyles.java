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
package org.openfaces.component;

import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Scrolling;

import javax.faces.component.UIComponent;
import java.util.List;

public interface TableStyles {

    String getHorizontalGridLines();

    void setHorizontalGridLines(String horizontalGridLines);

    String getVerticalGridLines();

    void setVerticalGridLines(String verticalGridLines);

    String getCommonHeaderSeparator();

    void setCommonHeaderSeparator(String commonHeaderSeparator);

    String getCommonFooterSeparator();

    void setCommonFooterSeparator(String commonFooterSeparator);

    String getHeaderHorizSeparator();

    void setHeaderHorizSeparator(String headerHorizSeparator);

    String getHeaderVertSeparator();

    void setHeaderVertSeparator(String headerVertSeparator);

    String getMultiHeaderSeparator();

    void setMultiHeaderSeparator(String multiHeaderSeparator);

    String getMultiFooterSeparator();

    void setMultiFooterSeparator(String multiFooterSeparator);

    String getFooterHorizSeparator();

    void setFooterHorizSeparator(String footerHorizSeparator);

    String getFooterVertSeparator();

    void setFooterVertSeparator(String footerVertSeparator);

    String getBodyRowStyle();

    void setBodyRowStyle(String bodyRowStyle);

    String getBodyRowClass();

    void setBodyRowClass(String bodyRowClass);

    String getBodyOddRowStyle();

    void setBodyOddRowStyle(String bodyOddRowStyle);

    String getBodyOddRowClass();

    void setBodyOddRowClass(String bodyOddRowClass);

    String getHeaderRowStyle();

    void setHeaderRowStyle(String headerRowStyle);

    String getHeaderRowClass();

    void setHeaderRowClass(String headerRowClass);

    String getFooterRowStyle();

    void setFooterRowStyle(String footerRowStyle);

    String getFooterRowClass();

    void setFooterRowClass(String footerRowClass);

    List<BaseColumn> getRenderedColumns();

    UIComponent getHeader();

    UIComponent getFooter();

    String getBodySectionStyle();

    String getHeaderSectionStyle();

    String getBodySectionClass();

    String getHeaderSectionClass();

    String getFooterSectionStyle();

    String getFooterSectionClass();

    boolean getApplyDefaultStyle();

    Scrolling getScrolling();
}
