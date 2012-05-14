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

import org.openfaces.taglib.internal.table.DataTablePaginatorTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class DataTablePaginatorJspTag extends AbstractComponentJspTag {

    public DataTablePaginatorJspTag() {
        super(new DataTablePaginatorTag());
    }

    public void setPageNumberPrefix(ValueExpression pageNumberPrefix) {
        getDelegate().setPropertyValue("pageNumberPrefix", pageNumberPrefix);
    }

    public void setPreviousText(ValueExpression previousText) {
        getDelegate().setPropertyValue("previousText", previousText);
    }

    public void setNextText(ValueExpression nextText) {
        getDelegate().setPropertyValue("nextText", nextText);
    }

    public void setFirstText(ValueExpression firstText) {
        getDelegate().setPropertyValue("firstText", firstText);
    }

    public void setLastText(ValueExpression lastText) {
        getDelegate().setPropertyValue("lastText", lastText);
    }

    public void setShowIfOnePage(ValueExpression showIfOnePage) {
        getDelegate().setPropertyValue("showIfOnePage", showIfOnePage);
    }

    public void setShowPageCount(ValueExpression showPageCount) {
        getDelegate().setPropertyValue("showPageCount", showPageCount);
    }

    public void setPageCountPreposition(ValueExpression pageCountPreposition) {
        getDelegate().setPropertyValue("pageCountPreposition", pageCountPreposition);
    }

    public void setPageNumberFieldStyle(ValueExpression pageNumberFieldStyle) {
        getDelegate().setPropertyValue("pageNumberFieldStyle", pageNumberFieldStyle);
    }

    public void setPageNumberFieldClass(ValueExpression pageNumberFieldClass) {
        getDelegate().setPropertyValue("pageNumberFieldClass", pageNumberFieldClass);
    }

    public void setPreviousImageUrl(ValueExpression previousImageUrl) {
        getDelegate().setPropertyValue("previousImageUrl", previousImageUrl);
    }

    public void setNextImageUrl(ValueExpression nextImageUrl) {
        getDelegate().setPropertyValue("nextImageUrl", nextImageUrl);
    }

    public void setFirstImageUrl(ValueExpression firstImageUrl) {
        getDelegate().setPropertyValue("firstImageUrl", firstImageUrl);
    }

    public void setLastImageUrl(ValueExpression lastImageUrl) {
        getDelegate().setPropertyValue("lastImageUrl", lastImageUrl);
    }

    public void setPreviousDisabledImageUrl(ValueExpression previousDisabledImageUrl) {
        getDelegate().setPropertyValue("previousDisabledImageUrl", previousDisabledImageUrl);
    }

    public void setNextDisabledImageUrl(ValueExpression nextDisabledImageUrl) {
        getDelegate().setPropertyValue("nextDisabledImageUrl", nextDisabledImageUrl);
    }

    public void setFirstDisabledImageUrl(ValueExpression firstDisabledImageUrl) {
        getDelegate().setPropertyValue("firstDisabledImageUrl", firstDisabledImageUrl);
    }

    public void setLastDisabledImageUrl(ValueExpression lastDisabledImageUrl) {
        getDelegate().setPropertyValue("lastDisabledImageUrl", lastDisabledImageUrl);
    }

    public void setShowDisabledImages(ValueExpression showDisabledImages) {
        getDelegate().setPropertyValue("showDisabledImages", showDisabledImages);
    }

}
