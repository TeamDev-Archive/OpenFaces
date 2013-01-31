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
package org.openfaces.taglib.internal.table;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class AbstractTableTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "var", false, true);

        setBooleanProperty(component, "sortAscending");
        setStringProperty(component, "sortColumnId");
        setValueExpressionProperty(component, "displayedRowDatas");

        setStringProperty(component, "cellspacing");
        setStringProperty(component, "cellpadding");
        setIntProperty(component, "border");
        setStringProperty(component, "align");
        setStringProperty(component, "bgcolor");
        setStringProperty(component, "dir");
        setStringProperty(component, "rules");
        setStringProperty(component, "width");
        setStringProperty(component, "tabindex");

        setBooleanProperty(component, "applyDefaultStyle");
        setStringProperty(component, "headerSectionStyle");
        setStringProperty(component, "headerSectionClass");
        setStringProperty(component, "bodySectionStyle");
        setStringProperty(component, "bodySectionClass");
        setStringProperty(component, "footerSectionStyle");
        setStringProperty(component, "footerSectionClass");

        setStringProperty(component, "bodyRowStyle");
        setStringProperty(component, "bodyRowClass");
        setStringProperty(component, "bodyOddRowStyle");
        setStringProperty(component, "bodyOddRowClass");
        setStringProperty(component, "headerRowStyle");
        setStringProperty(component, "headerRowClass");
        setStringProperty(component, "commonHeaderRowStyle");
        setStringProperty(component, "commonHeaderRowClass");
        setStringProperty(component, "footerRowStyle");
        setStringProperty(component, "footerRowClass");
        setStringProperty(component, "commonFooterRowStyle");
        setStringProperty(component, "commonFooterRowClass");

        setStringProperty(component, "sortedColumnStyle");
        setStringProperty(component, "sortedColumnClass");
        setStringProperty(component, "sortedColumnHeaderStyle");
        setStringProperty(component, "sortedColumnHeaderClass");
        setStringProperty(component, "sortedColumnBodyStyle");
        setStringProperty(component, "sortedColumnBodyClass");
        setStringProperty(component, "sortedColumnFooterStyle");
        setStringProperty(component, "sortedColumnFooterClass");
        setStringProperty(component, "sortableHeaderStyle");
        setStringProperty(component, "sortableHeaderClass");
        setStringProperty(component, "sortableHeaderRolloverStyle");
        setStringProperty(component, "sortableHeaderRolloverClass");

        setLineStyleProperty(component, "horizontalGridLines");
        setLineStyleProperty(component, "verticalGridLines");
        setLineStyleProperty(component, "commonHeaderSeparator");
        setLineStyleProperty(component, "commonFooterSeparator");
        setLineStyleProperty(component, "headerHorizSeparator");
        setLineStyleProperty(component, "headerVertSeparator");
        setLineStyleProperty(component, "multiHeaderSeparator");
        setLineStyleProperty(component, "multiFooterSeparator");
        setLineStyleProperty(component, "footerHorizSeparator");
        setLineStyleProperty(component, "footerVertSeparator");

        setBooleanProperty(component, "useAjax");

        setStringProperty(component, "allRecordsFilterText");
        setStringProperty(component, "emptyRecordsFilterText");
        setStringProperty(component, "nonEmptyRecordsFilterText");

        setStringProperty(component, "subHeaderRowStyle");
        setStringProperty(component, "subHeaderRowClass");
        setStringProperty(component, "subHeaderRowSeparator");

        setStringProperty(component, "sortedAscendingImageUrl");
        setStringProperty(component, "sortedDescendingImageUrl");

        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "rolloverClass");
        setStringProperty(component, "rolloverRowStyle");
        setStringProperty(component, "rolloverRowClass");

        setStringProperty(component, "noDataRowStyle");
        setStringProperty(component, "noDataRowClass");
        setBooleanProperty(component, "noDataMessageAllowed");
        setStringProperty(component, "columnIndexVar");
        setStringProperty(component, "columnIdVar");

        setStringProperty(component, "onfocus");
        setStringProperty(component, "onblur");
        setStringProperty(component, "onkeydown");
        setStringProperty(component, "onkeyup");
        setStringProperty(component, "onkeypress");
        setLiteralCollectionProperty(component, "columnsOrder");

        setIntProperty(component, "autoFilterDelay");
        setBooleanProperty(component, "deferBodyLoading");
        setBooleanProperty(component, "unsortedStateAllowed");

        setBooleanProperty(component, "keepSelectionVisible");

        setStringProperty(component, "onbeforeajaxreload");
        setStringProperty(component, "onafterajaxreload");

        setBooleanProperty(component, "unDisplayedSelectionAllowed");
    }
}
