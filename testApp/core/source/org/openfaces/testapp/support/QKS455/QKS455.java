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

package org.openfaces.testapp.support.QKS455;

import org.openfaces.component.input.DateChooser;
import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.panel.TabbedPaneItem;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Tatyana Matveyeva
 */
public class QKS455 {
    private HtmlForm testForm;
    private Collection<TabbedPaneItem> tabbedPaneItems;
    private TabbedPane tabbedPane;


    public QKS455() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        if (tabbedPaneItems == null) {
            tabbedPaneItems = new ArrayList<TabbedPaneItem>();
            TabbedPaneItem item;
            HtmlOutputText tabValue;
            HtmlOutputText containerValue;

            item = new TabbedPaneItem();
            tabValue = new HtmlOutputText();
            tabValue.setValue("tab1");
            item.getFacets().put("tab", tabValue);
            containerValue = new HtmlOutputText();
            containerValue.setValue("content 1");
            item.getChildren().add(containerValue);
            tabbedPaneItems.add(item);

            item = new TabbedPaneItem();
            tabValue = new HtmlOutputText();
            tabValue.setValue("tab2");
            item.getFacets().put("tab", tabValue);
            containerValue = new HtmlOutputText();
            containerValue.setValue("content 2");
            item.getChildren().add(containerValue);
            tabbedPaneItems.add(item);

            item = new TabbedPaneItem();
            DateChooser dateChooser = (DateChooser) app.createComponent(DateChooser.COMPONENT_TYPE);
            dateChooser.setId("dch");
            tabValue = new HtmlOutputText();
            tabValue.setValue("tab3");
            item.getFacets().put("tab", tabValue);
            item.getChildren().add(dateChooser);
            tabbedPaneItems.add(item);

        }

        tabbedPane = (TabbedPane) app.createComponent(TabbedPane.COMPONENT_TYPE);
        tabbedPane.setId("tp");
        tabbedPane.getChildren().addAll(tabbedPaneItems);
        tabbedPane.createSubComponents(facesContext);

    }

    public HtmlForm getTestForm() {
        return testForm;
    }


    public void setTestForm(HtmlForm testForm) {
        this.testForm = testForm;
    }

    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}