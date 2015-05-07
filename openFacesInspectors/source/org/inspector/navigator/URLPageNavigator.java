/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.navigator;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.inspector.webriver.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.inspector.navigator.FuncTestsPages.*;

/**
 * @author Max Yurin
 */
public class URLPageNavigator {
    public static final int DEFAULT_TIMEOUT = 10;
    private static final ImmutableMap<FuncTestsPages, FuncTestURL> URLS =
            new ImmutableMap.Builder<FuncTestsPages, FuncTestURL>()
                    .put(AJAX, new FuncTestURL("/components/ajax/ajax.jsf"))
                    .put(BORDER_LAYOUT_PANEL, new FuncTestURL("/components/borderlayoutpanel/borderLayoutPanel.jsf"))
                    .put(CALENDAR, new FuncTestURL("/components/calendar/calendar.jsf"))
                    .put(CHART, new FuncTestURL("/components/chart/chart.jsf"))
                    .put(CHECKBOX, new FuncTestURL("/components/checkbox/selectBooleanCheckbox.jsf"))
                    .put(CONFIRMATION, new FuncTestURL("/components/confirmation/confirmation.jsf"))
                    .put(COMPOSITE_FILTER, new FuncTestURL("/components/filter/compositeFilter.jsf"))

                    .put(DATATABLE_GENERAL, new FuncTestURL("/components/datatable/general.jsf")) //TODO Done
                    .put(DATATABLE_PAGINATION, new FuncTestURL("/components/datatable/datatablePagination.jsf")) //Done
                    .put(DATATABLE_SORTING, new FuncTestURL("/components/datatable/datatableSorting.jsf")) //Done
                    .put(DATATABLE_FILTERING, new FuncTestURL("/components/datatable/datatableFiltering.jsf")) //Done
                    .put(DATATABLE_AJAX, new FuncTestURL("/components/datatable/dataTableAjax.jsf")) //Done
                    .put(DATATABLE_COLUMN_GROUPS, new FuncTestURL("/components/datatable/dataTableColumnGroups.jsf")) //Done
                    .put(DATATABLE_EVENTS, new FuncTestURL("/components/datatable/dataTable_events.jsf")) //Done

                    .put(DATECHOOSER, new FuncTestURL("/components/datechooser/dateChooser.jsf"))

                    .put(DROPDOWN_FIELD, new FuncTestURL("/components/dropdown/dropDown.jsf")) //Done

                    .put(DYNAMIC_IMAGE, new FuncTestURL("/components/dynamicimage/dynamicImage_defaultView.jsf"))
                    .put(FILE_UPLOAD, new FuncTestURL("/components/fileupload/singleFileupload.jsf"))
                    .put(FOLDING_PANEL, new FuncTestURL("/components/foldingpanel/foldingPanel.jsf"))
                    .put(FOREACH, new FuncTestURL("/components/foreach/forEach.jsf"))
                    .put(GRAPHIC_TEXT, new FuncTestURL("/components/graphicText/graphicText.jsf"))
                    .put(HINT_LABEL, new FuncTestURL("/components/hintLabel/hintLabel.jsf"))
                    .put(INPUTTEXT, new FuncTestURL("/components/inputText/inputText.jsf"))
                    .put(INPUTTEXTAREA, new FuncTestURL("/components/inputtextarea/inputTextArea.jsf"))
                    .put(LAYERED_PANEL, new FuncTestURL("/components/layeredpane/layeredpane.jsf"))
                    .put(TABBED_PANE, new FuncTestURL("/components/tabbedpane/tabbedpane_defaultView.jsf"))

                    .put(TAB_SET, new FuncTestURL("/components/tabset/tabSet.jsf"))

                    .put(TIME_TABLE, new FuncTestURL("/components/timetable/dayTable.jsf"))
                    .put(TREETABLE, new FuncTestURL("/components/treetable/treeTableTest.jsf"))
                    .put(TWO_LIST_SELECTION, new FuncTestURL("/components/twolistselection/twolistselection.jsf"))
                    .put(POPUPLAYER, new FuncTestURL("/components/popuplayer/popupLayer.jsf"))
                    .put(POPUPMENU, new FuncTestURL("/components/popupMenu/popupMenu.jsf"))
                    .put(RADIO, new FuncTestURL("/components/radio/selectOneRadio.jsf"))
                    .put(SCROLL_FOCUS, new FuncTestURL("/components/scrollFocus/focus.jsf"))
                    .put(SESSION_EXPIRATION, new FuncTestURL("/components/sessionExpiration/rawTesting.jsf"))
                    .put(SUGGESTION_FIELD, new FuncTestURL("/components/suggestionfield/suggestionField.jsf"))
                    .put(VALIDATOR, new FuncTestURL("/components/validator/validator.jsf"))
                    .put(WINDOW, new FuncTestURL("/components/window/window.jsf"))
                    .build();

    private String appTestUrl = "";
    private List<String> errors = newArrayList();
    ;

    public URLPageNavigator(String testAppUrlPerfix) {
        this.appTestUrl = testAppUrlPerfix;
    }

    public WebDriver getDriver() {
        return WebDriverManager.getWebDriver();
    }

    public void navigateTo(FuncTestsPages page) {
        final FuncTestURL url = URLS.get(page);
        navigateTo(url);
    }

    public void navigateTo(FuncTestURL url) {
        if (url == null) {
            throw new IllegalArgumentException("Page: <" + url + "> is not defined.");
        }

        final String pageUrl = appTestUrl + url.getUrl();
        getDriver().get(pageUrl);
        waitForPageLoad(DEFAULT_TIMEOUT);

        checkPageExists(pageUrl);
    }

    private void checkPageExists(String url) {
        final String title = getDriver().getTitle();

        if (StringUtils.isBlank(title) || title.toLowerCase().contains("error")) {
            errors.add(url);
        }
    }

    public List<String> checkAllPages() {
        for (FuncTestURL url : URLS.values()) {
            navigateTo(url);
        }
        return errors;
    }

    private void clear() {
        errors = newArrayList();
    }

    public void waitForPageLoad(int timeout) {
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), timeout);
        webDriverWait.until(new DocumentReadyCondition());
    }

    private static class FuncTestURL {
        private final String url;

        public FuncTestURL(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
