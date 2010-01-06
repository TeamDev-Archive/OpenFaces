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

package org.openfaces.testapp.support.QKS340;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class QKS340 {
    private boolean rendered = false;
    private boolean renderedMain = true;
    private boolean renderedDataTable = false;
    private boolean renderedTreeTable = false;
    private String includedPage = "/ajax4jsf/include/main_page_include.jsp";


    public QKS340() {
        updateSessionScopeVar();
    }

    public boolean isRenderedMain() {
        return renderedMain;
    }

    public void setRenderedMain(boolean renderedMain) {
        this.renderedMain = renderedMain;
    }

    public boolean isRenderedDataTable() {
        return renderedDataTable;
    }

    public void setRenderedDataTable(boolean renderedDataTable) {
        this.renderedDataTable = renderedDataTable;
    }

    public boolean isRenderedTreeTable() {
        return renderedTreeTable;
    }

    public void setRenderedTreeTable(boolean renderedTreeTable) {
        this.renderedTreeTable = renderedTreeTable;
    }

    public String renderMain() {
        renderedMain = true;
        renderedDataTable = false;
        renderedTreeTable = false;
        return null;
    }

    public String renderDataTable() {
        renderedMain = false;
        renderedDataTable = true;
        renderedTreeTable = false;
        return null;
    }

    public String renderTreeTable() {
        renderedMain = false;
        renderedDataTable = false;
        renderedTreeTable = true;
        return null;
    }

    public String getIncludedPage() {
        return includedPage;
    }

    public void setIncludedPage(String includedPage) {
        this.includedPage = includedPage;
    }

    public String goToDataTable() {
        includedPage = "/ajax4jsf/include/dataTable_include.jsp";
        updateSessionScopeVar();
        return null;
    }

    private void updateSessionScopeVar() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("includedPage", includedPage);
    }

    public String goToTreeTable() {
        includedPage = "/ajax4jsf/include/treeTable_include.jsp";
        updateSessionScopeVar();
        return null;
    }

    public String goToMain() {
        includedPage = "/ajax4jsf/include/main_page_include.jsp";
        updateSessionScopeVar();
        return null;
    }

    public String goToDateChooser() {
        includedPage = "/ajax4jsf/include/dateChooser_include.jsp";
        updateSessionScopeVar();
        return null;
    }

    public String goToTabbedPane() {
        includedPage = "/ajax4jsf/include/tabbedPane_include.jsp";
        updateSessionScopeVar();
        return null;
    }

    public String goToFoldingPanel() {
        includedPage = "/ajax4jsf/include/foldingPane_includel.jsp";
        updateSessionScopeVar();
        return null;
    }


    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String showForm() {
        rendered = true;
        return null;
    }

    public String getCurrentPage() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return (String) externalContext.getSessionMap().get("includedPage");
    }
}
