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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.DataTablePaginator;
import org.openfaces.component.table.DataTablePaginatorAction;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.HTML;
import org.openfaces.util.Log;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DataTablePaginatorRenderer extends RendererBase {
    public static final String FIRST_PAGE_COMPONENT = "firstPage";
    public static final String LAST_PAGE_COMPONENT = "lastPage";
    public static final String NEXT_PAGE_COMPONENT = "nextPage";
    public static final String PREV_PAGE_COMPONENT = "prevPage";
    private static final String SELECT_PAGE_NO_COMMAND = "selectPageNo:";
    private static final String DEFAULT_PAGE_COUNT_PREPOSITION = "of";
    private static final String DEFAULT_PAGE_NUMBER_PREFIX = "Page";
    private static final String DEFAULT_FIELD_CLASS = "o_table_paginator_field";
    private static final String DEFAULT_CLASS = "o_table_paginator";
    private static final String DEFAULT_IMAGE_CLASS = "o_table_paginator_image";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        if (!component.isRendered())
            return;
        DataTablePaginator pager = ((DataTablePaginator) component);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", component);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writeIdAttribute(context, component);

        String style = pager.getStyle();
        String styleClass = pager.getStyleClass();
        DataTable table = pager.getTable();
        boolean dontShow = (table.getPageCount() < 2 && !pager.getShowIfOnePage());
        String className = Styles.getCSSClass(context, component, style, DEFAULT_CLASS, styleClass);
        if (dontShow)
            writer.writeAttribute("style", "display: none", null);
        else
            writer.writeAttribute("class", className, null);

        Resources.renderJSLinkIfNeeded(context, Resources.getUtilJsURL(context));

        writer.startElement("tr", component);
        writer.startElement("td", component);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private String getFirstImageUrl(FacesContext context, DataTablePaginator pager, boolean active) {
        return active
                ? Resources.getURL(context, pager.getFirstImageUrl(), DataTablePaginatorRenderer.class, "first.gif", false)
                : Resources.getURL(context, pager.getFirstDisabledImageUrl(), DataTablePaginatorRenderer.class, "firstDisabled.gif", false);
    }

    private String getLastImageUrl(FacesContext context, DataTablePaginator pager, boolean active) {
        return active
                ? Resources.getURL(context, pager.getLastImageUrl(), DataTablePaginatorRenderer.class, "last.gif", false)
                : Resources.getURL(context, pager.getLastDisabledImageUrl(), DataTablePaginatorRenderer.class, "lastDisabled.gif", false);
    }

    private String getPreviousImageUrl(FacesContext context, DataTablePaginator pager, boolean active) {
        return active
                ? Resources.getURL(context, pager.getPreviousImageUrl(), DataTablePaginatorRenderer.class, "prev.gif", false)
                : Resources.getURL(context, pager.getPreviousDisabledImageUrl(), DataTablePaginatorRenderer.class, "prevDisabled.gif", false);
    }

    private String getNextImageUrl(FacesContext context, DataTablePaginator pager, boolean active) {
        return active
                ? Resources.getURL(context, pager.getNextImageUrl(), DataTablePaginatorRenderer.class, "next.gif", false)
                : Resources.getURL(context, pager.getNextDisabledImageUrl(), DataTablePaginatorRenderer.class, "nextDisabled.gif", false);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        DataTablePaginator pager = ((DataTablePaginator) component);
        DataTable table = pager.getTable();
        int pageCount = table.getPageCount();
        if (pageCount < 2 && !pager.getShowIfOnePage())
            return;
        int pageSize = table.getPageSize();
        if (pageSize == 0)
            throw new IllegalStateException("DataTablePaginator can't be placed into a non-pageable dataTable. Set " +
                    "dataTable's pageSize to a non-zero value. dataTable's clientId is: " + table.getClientId(context));

        boolean firstLinkActive = canSelectFirstPage(table);
        boolean lastLinkActive = canSelectLastPage(table);
        boolean nextLinkActive = canSelectNextPage(table);
        boolean previousLinkActive = canSelectPreviousPage(table);

        List<UIComponent> children = component.getChildren();
        children.clear();
        boolean useAjax = table.getUseAjax();
        String actionFieldName = getActionFieldName(context, pager);
        List<String> preloadImages = new ArrayList<String>();
        createAndAddActionLink(context, children, pager, actionFieldName, "selectFirstPage",
                getFirstImageUrl(context, pager, firstLinkActive), getFirstText(pager),
                FIRST_PAGE_COMPONENT, useAjax, table, firstLinkActive);
        preloadImages.add(getFirstImageUrl(context, pager, !firstLinkActive));
        boolean showDisabledImages = pager.getShowDisabledImages();
        if ((firstLinkActive && previousLinkActive) || showDisabledImages)
            children.add(Components.createOutputText(context, HTML.NBSP_ENTITY, false));
        createAndAddActionLink(context, children, pager, actionFieldName, "selectPrevPage",
                getPreviousImageUrl(context, pager, previousLinkActive), getPreviousText(pager),
                PREV_PAGE_COMPONENT, useAjax, table, previousLinkActive);
        preloadImages.add(getPreviousImageUrl(context, pager, !previousLinkActive));

        if (firstLinkActive || previousLinkActive || showDisabledImages)
            children.add(Components.createOutputText(context, HTML.NBSP_ENTITY + HTML.NBSP_ENTITY, false));

        String pageNumberPrefix = pager.getPageNumberPrefix();
        if (pageNumberPrefix == null)
            pageNumberPrefix = DEFAULT_PAGE_NUMBER_PREFIX;
        if (pageNumberPrefix.length() > 0)
            children.add(Components.createOutputText(context, pageNumberPrefix + HTML.NBSP_ENTITY, false));

        HtmlInputText inputText = (HtmlInputText) context.getApplication().createComponent(HtmlInputText.COMPONENT_TYPE);
        inputText.setId(Components.generateIdWithSuffix(component, "pageNo"));
        String pageNo = String.valueOf(table.getPageIndex() + 1);
        inputText.setValue(pageNo);
        inputText.setStyle(pager.getPageNumberFieldStyle());
        String fieldClass = Styles.getCSSClass(
                context, component, null, DEFAULT_FIELD_CLASS, pager.getPageNumberFieldClass());
        inputText.setStyleClass(fieldClass);
        inputText.setOnkeypress("if (event.keyCode == 13) {this.onchange(); event.cancelBubble = true; return false;}");
        Script selectPageNoScript = getSubmitComponentWithParamScript(
                useAjax, table, actionFieldName, new RawScript("'selectPageNo:' + this.value"), false);
        inputText.setOnchange(selectPageNoScript.toString());
        children.add(inputText);
        if (pager.getShowPageCount()) {
            String pageCountPreposition = pager.getPageCountPreposition();
            if (pageCountPreposition == null)
                pageCountPreposition = DEFAULT_PAGE_COUNT_PREPOSITION;
            children.add(Components.createOutputText(context, HTML.NBSP_ENTITY + pageCountPreposition + HTML.NBSP_ENTITY, false));
            children.add(Components.createOutputText(context, String.valueOf(pageCount), false));
        }

        if (lastLinkActive || nextLinkActive || showDisabledImages)
            children.add(Components.createOutputText(context, HTML.NBSP_ENTITY + HTML.NBSP_ENTITY, false));

        createAndAddActionLink(context, children, pager, actionFieldName, "selectNextPage",
                getNextImageUrl(context, pager, nextLinkActive), getNextText(pager),
                NEXT_PAGE_COMPONENT, useAjax, table, nextLinkActive);
        preloadImages.add(getNextImageUrl(context, pager, !nextLinkActive));
        if (lastLinkActive && nextLinkActive || showDisabledImages)
            children.add(Components.createOutputText(context, HTML.NBSP_ENTITY, false));
        createAndAddActionLink(context, children, pager, actionFieldName, "selectLastPage",
                getLastImageUrl(context, pager, lastLinkActive), getLastText(pager),
                LAST_PAGE_COMPONENT, useAjax, table, lastLinkActive);
        preloadImages.add(getLastImageUrl(context, pager, !lastLinkActive));
        Rendering.renderPreloadImagesScript(context, preloadImages, true);

        ResponseWriter writer = context.getResponseWriter();
        for (UIComponent child : children) {
            writer.startElement("td", component);
            child.encodeAll(context);
            writer.endElement("td");
        }
        children.clear();
    }

    private String getLastText(DataTablePaginator pager) {
        String lastText = pager.getLastText();
        return lastText != null ? lastText : "Go to last page";
    }

    private String getNextText(DataTablePaginator pager) {
        String nextText = pager.getNextText();
        return nextText != null ? nextText : "Go to next page";
    }

    private String getPreviousText(DataTablePaginator pager) {
        String previousText = pager.getPreviousText();
        return previousText != null ? previousText : "Go to previous page";
    }

    private String getFirstText(DataTablePaginator pager) {
        String firstText = pager.getFirstText();
        return firstText != null ? firstText : "Go to first page";
    }

    private static void createAndAddActionLink(
            FacesContext context,
            List<UIComponent> children,
            DataTablePaginator pager,
            String fieldName,
            String fieldValue,
            String imageUrl,
            String hintText,
            String idSuffix,
            boolean useAjax,
            UIComponent componentToReload, boolean linkActive) {
        boolean showDisabledImages = pager.getShowDisabledImages();
        if (!linkActive && !showDisabledImages)
            return;
        UIComponent actionLink = createActionLink(context, pager, fieldName, fieldValue,
                imageUrl, hintText, idSuffix, useAjax, componentToReload, linkActive);
        children.add(actionLink);
    }

    private static UIComponent createActionLink(
            FacesContext context,
            UIComponent eventReceiver,
            String fieldName,
            String fieldValue,
            String imageUrl,
            String text,
            String idSuffix,
            boolean useAjax,
            UIComponent componentToReload, boolean linkActive) {
        HtmlGraphicImage image = createGraphicImage(context, imageUrl);
        if (!linkActive)
            return image;

        Script submitScript = getSubmitComponentWithParamScript(useAjax, componentToReload, fieldName, fieldValue, true);
        image.setOnclick(submitScript + "event.cancelBubble = true; return false;");
        image.setId(Components.generateIdWithSuffix(eventReceiver, idSuffix));
        image.setStyleClass(DEFAULT_IMAGE_CLASS);
        image.setTitle(text);
        image.setAlt(text);
        return image;
    }

    public static HtmlGraphicImage createGraphicImage(FacesContext context, String imageUrl) {
        HtmlGraphicImage outputText = (HtmlGraphicImage) context.getApplication().createComponent(HtmlGraphicImage.COMPONENT_TYPE);
        outputText.setUrl(imageUrl);
        return outputText;
    }


    private static Script getSubmitComponentWithParamScript(
            boolean useAjax, UIComponent table, String paramName, Object paramValue,
            boolean focusTable) {
        Script submitScript = useAjax
                ? new ScriptBuilder().functionCall("O$.Table._performPaginatorAction",
                                        table, new RawScript(focusTable ? "null" : "this"), paramName, paramValue).semicolon()
                : new ScriptBuilder().functionCall("O$.submitFormWithAdditionalParam",
                                        new RawScript("this"), paramName, paramValue).semicolon();
        return submitScript;
    }

    public boolean canSelectFirstPage(DataTable table) {
        return table.getPageIndex() > 0;
    }

    public static boolean canSelectPreviousPage(DataTable table) {
        return table.getPageIndex() > 0;
    }

    public static boolean canSelectNextPage(DataTable table) {
        int pageCount = table.getPageCount();
        if (pageCount != -1)
            return table.getPageIndex() < pageCount - 1;
        else {
            throw new UnsupportedOperationException("pageCount is unknown");
        }
    }

    public static boolean canSelectLastPage(DataTable table) {
        int pageCount = table.getPageCount();
        if (pageCount != -1)
            return table.getPageIndex() < pageCount - 1;
        else {
            return false;
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();
        DataTablePaginator pager = (DataTablePaginator) component;
        String clientId = getActionFieldName(context, pager);
        String actionStr = requestParams.get(clientId);
        if (actionStr == null || actionStr.length() == 0)
            return;
        DataTable table = pager.getTable();
        executePaginationAction(context, table, actionStr);
    }

    public static void executePaginationAction(FacesContext context, DataTable table, String actionStr) {
        DataTablePaginatorAction action;
        if ("selectNextPage".equals(actionStr))
            action = new SelectNextPageAction();
        else if ("selectPrevPage".equals(actionStr))
            action = new SelectPreviousPageAction();
        else if ("selectFirstPage".equals(actionStr))
            action = new SelectFirstPageAction();
        else if ("selectLastPage".equals(actionStr))
            action = new SelectLastPageAction();
        else if (actionStr.startsWith(SELECT_PAGE_NO_COMMAND)) {
            String pageNoStr = actionStr.substring(SELECT_PAGE_NO_COMMAND.length());
            int pageNo;
            try {
                pageNo = Integer.parseInt(pageNoStr);
            } catch (NumberFormatException e) {
                return;
            }
            action = new SelectPageNoAction(pageNo);
        } else {
            Log.log(context, "Unknown DataTablePaginator action came from client: " + actionStr);
            return;
        }

        action.execute(table);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        if (!component.isRendered())
            return;

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("td");
        writer.endElement("tr");
        Styles.renderStyleClasses(context, component);
        writer.endElement("table");
    }

    private String getActionFieldName(FacesContext context, DataTablePaginator pager) {
        String result = pager.getClientId(context) + "::action";
        return result;
    }

}
