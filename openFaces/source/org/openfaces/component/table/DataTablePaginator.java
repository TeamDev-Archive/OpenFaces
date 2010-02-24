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
package org.openfaces.component.table;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class DataTablePaginator extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.DataTablePaginator";
    public static final String COMPONENT_FAMILY = "org.openfaces.DataTablePaginator";

    private String style;
    private String styleClass;
    private String activeLinkStyle;
    private String activeLinkClass;
    private String inactiveLinkStyle;
    private String inactiveLinkClass;
    private String currentPageLinkStyle;
    private String currentPageLinkClass;
    private Boolean showPageCount;
    private String pageCountPreposition;
    private Boolean showIfOnePage;
    private String pageNumberPrefix;
    private String pageNumberFieldStyle;
    private String pageNumberFieldClass;
    private String previousImageUrl;
    private String nextImageUrl;
    private String firstImageUrl;
    private String lastImageUrl;
    private String previousDisabledImageUrl;
    private String nextDisabledImageUrl;
    private String firstDisabledImageUrl;
    private String lastDisabledImageUrl;

    private Boolean showDisabledImages;

    private String previousText;
    private String nextText;
    private String firstText;
    private String lastText;

    public DataTablePaginator() {
        setRendererType("org.openfaces.DataTablePaginatorRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public DataTable getTable() {
        for (UIComponent component = getParent(); component != null; component = component.getParent())
            if (component instanceof DataTable)
                return (DataTable) component;
        throw new RuntimeException("<o:DataTablePaginator> should be embedded into a <o:dataTable>");
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style, styleClass, activeLinkStyle, activeLinkClass,
                inactiveLinkStyle, inactiveLinkClass, currentPageLinkStyle, currentPageLinkClass,
                showIfOnePage, showPageCount, pageCountPreposition, pageNumberFieldStyle, pageNumberFieldClass,
                firstImageUrl, lastImageUrl, previousImageUrl, nextImageUrl, firstText, lastText, previousText,
                nextText, pageNumberPrefix, firstDisabledImageUrl, lastDisabledImageUrl, previousDisabledImageUrl,
                nextDisabledImageUrl, showDisabledImages};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        style = (String) state[i++];
        styleClass = (String) state[i++];
        activeLinkStyle = (String) state[i++];
        activeLinkClass = (String) state[i++];
        inactiveLinkStyle = (String) state[i++];
        inactiveLinkClass = (String) state[i++];
        currentPageLinkStyle = (String) state[i++];
        currentPageLinkClass = (String) state[i++];
        showIfOnePage = (Boolean) state[i++];
        showPageCount = (Boolean) state[i++];
        pageCountPreposition = (String) state[i++];
        pageNumberFieldStyle = (String) state[i++];
        pageNumberFieldClass = (String) state[i++];
        firstImageUrl = (String) state[i++];
        lastImageUrl = (String) state[i++];
        previousImageUrl = (String) state[i++];
        nextImageUrl = (String) state[i++];
        firstText = (String) state[i++];
        lastText = (String) state[i++];
        previousText = (String) state[i++];
        nextText = (String) state[i++];
        pageNumberPrefix = (String) state[i++];
        firstDisabledImageUrl = (String) state[i++];
        lastDisabledImageUrl = (String) state[i++];
        previousDisabledImageUrl = (String) state[i++];
        nextDisabledImageUrl = (String) state[i++];
        showDisabledImages = (Boolean) state[i++];
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getPageNumberPrefix() {
        return ValueBindings.get(this, "pageNumberPrefix", pageNumberPrefix);
    }

    public void setPageNumberPrefix(String pageNumberPrefix) {
        this.pageNumberPrefix = pageNumberPrefix;
    }

//  public String getActiveLinkStyle() {
//    return Components.getBoundPropertyValueAsString(this, "activeLinkStyle", activeLinkStyle);
//  }
//
//  public void setActiveLinkStyle(String activeLinkStyle) {
//    activeLinkStyle = activeLinkStyle;
//  }
//
//  public String getActiveLinkClass() {
//    return Components.getBoundPropertyValueAsString(this, "activeLinkClass", activeLinkClass);
//  }
//
//  public void setActiveLinkClass(String activeLinkClass) {
//    activeLinkClass = activeLinkClass;
//  }
//
//  public String getInactiveLinkStyle() {
//    return Components.getBoundPropertyValueAsString(this, "inactiveLinkStyle", inactiveLinkStyle);
//  }
//
//  public void setInactiveLinkStyle(String inactiveLinkStyle) {
//    inactiveLinkStyle = inactiveLinkStyle;
//  }
//
//  public String getInactiveLinkClass() {
//    return Components.getBoundPropertyValueAsString(this, "inactiveLinkClass", inactiveLinkClass);
//  }
//
//  public void setInactiveLinkClass(String inactiveLinkClass) {
//    inactiveLinkClass = inactiveLinkClass;
//  }

    public boolean getShowIfOnePage() {
        return ValueBindings.get(this, "showIfOnePage", showIfOnePage, false);
    }

    public void setShowIfOnePage(boolean showIfOnePage) {
        this.showIfOnePage = showIfOnePage;
    }

    public boolean getShowPageCount() {
        return ValueBindings.get(this, "showPageCount", showPageCount, true);
    }

    public void setShowPageCount(boolean showPageCount) {
        this.showPageCount = showPageCount;
    }

    public String getPageCountPreposition() {
        return ValueBindings.get(this, "pageCountPreposition", pageCountPreposition);
    }

    public void setPageCountPreposition(String pageCountPreposition) {
        this.pageCountPreposition = pageCountPreposition;
    }

    public String getPreviousImageUrl() {
        return ValueBindings.get(this, "previousImageUrl", previousImageUrl);
    }

    public void setPreviousImageUrl(String previousImageUrl) {
        this.previousImageUrl = previousImageUrl;
    }

    public String getNextImageUrl() {
        return ValueBindings.get(this, "nextImageUrl", nextImageUrl);
    }

    public void setNextImageUrl(String nextImageUrl) {
        this.nextImageUrl = nextImageUrl;
    }

    public String getFirstImageUrl() {
        return ValueBindings.get(this, "firstImageUrl", firstImageUrl);
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getLastImageUrl() {
        return ValueBindings.get(this, "lastImageUrl", lastImageUrl);
    }

    public void setLastImageUrl(String lastImageUrl) {
        this.lastImageUrl = lastImageUrl;
    }

    public String getPreviousText() {
        return ValueBindings.get(this, "previousText", previousText);
    }

    public void setPreviousText(String previousText) {
        this.previousText = previousText;
    }

    public String getNextText() {
        return ValueBindings.get(this, "nextText", nextText);
    }

    public void setNextText(String nextText) {
        this.nextText = nextText;
    }

    public String getFirstText() {
        return ValueBindings.get(this, "firstText", firstText);
    }

    public void setFirstText(String firstText) {
        this.firstText = firstText;
    }

    public String getLastText() {
        return ValueBindings.get(this, "lastText", lastText);
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public String getPageNumberFieldStyle() {
        return ValueBindings.get(this, "pageNumberFieldStyle", pageNumberFieldStyle);
    }

    public void setPageNumberFieldStyle(String pageNumberFieldStyle) {
        this.pageNumberFieldStyle = pageNumberFieldStyle;
    }

    public String getPageNumberFieldClass() {
        return ValueBindings.get(this, "pageNumberFieldClass", pageNumberFieldClass);
    }

    public void setPageNumberFieldClass(String pageNumberFieldClass) {
        this.pageNumberFieldClass = pageNumberFieldClass;
    }

//  public String getCurrentPageLinkStyle() {
//    return Components.getBoundPropertyValueAsString(this, "currentPageLinkStyle", currentPageLinkStyle);
//  }
//
//  public void setCurrentPageLinkStyle(String currentPageLinkStyle) {
//    currentPageLinkStyle = currentPageLinkStyle;
//  }
//
//  public String getCurrentPageLinkClass() {
//    return Components.getBoundPropertyValueAsString(this, "currentPageLinkClass", currentPageLinkClass);
//  }
//
//  public void setCurrentPageLinkClass(String currentPageLinkClass) {
//    currentPageLinkClass = currentPageLinkClass;
//  }

    public String getLastDisabledImageUrl() {
        return ValueBindings.get(this, "lastDisabledImageUrl", lastDisabledImageUrl);
    }

    public void setLastDisabledImageUrl(String lastDisabledImageUrl) {
        this.lastDisabledImageUrl = lastDisabledImageUrl;
    }

    public String getFirstDisabledImageUrl() {
        return ValueBindings.get(this, "firstDisabledImageUrl", firstDisabledImageUrl);
    }

    public void setFirstDisabledImageUrl(String firstDisabledImageUrl) {
        this.firstDisabledImageUrl = firstDisabledImageUrl;
    }

    public String getNextDisabledImageUrl() {
        return ValueBindings.get(this, "nextDisabledImageUrl", nextDisabledImageUrl);
    }

    public void setNextDisabledImageUrl(String nextDisabledImageUrl) {
        this.nextDisabledImageUrl = nextDisabledImageUrl;
    }

    public String getPreviousDisabledImageUrl() {
        return ValueBindings.get(this, "previousDisabledImageUrl", previousDisabledImageUrl);
    }

    public void setPreviousDisabledImageUrl(String previousDisabledImageUrl) {
        this.previousDisabledImageUrl = previousDisabledImageUrl;
    }

    public boolean getShowDisabledImages() {
        return ValueBindings.get(this, "showDisabledImages", showDisabledImages, true);
    }

    public void setShowDisabledImages(boolean showDisabledImages) {
        this.showDisabledImages = showDisabledImages;
    }
}
