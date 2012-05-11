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
package org.openfaces.renderkit.util;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.Scrolling;
import org.openfaces.component.util.Debug;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.renderkit.CompoundComponentRenderer;
import org.openfaces.renderkit.window.WindowRenderer;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class DebugRenderer extends WindowRenderer implements CompoundComponentRenderer {
    @Override
    protected String getDefaultClassName() {
        return Styles.mergeClassNames(super.getDefaultClassName(), "o_debug");
    }

    public void createSubComponents(FacesContext context, UIComponent component) {
        Debug debug = (Debug) component;
        TabbedPane tabbedPane = (TabbedPane) Components.createChildComponent(context, debug, TabbedPane.COMPONENT_TYPE, "pages");
        tabbedPane.setLoadingMode(LoadingMode.CLIENT);
        tabbedPane.setStyle("width: 100%; height: 100%;");

        tabbedPane.getChildren().add(new SubPanel(
                Components.createOutputText(context, "Console"),
                createLogPageContent(context, debug)
        ));
        DataTable elementProperties = (DataTable) Components.createChildComponent(
                context, debug, DataTable.COMPONENT_TYPE, "elementProperties");
        tabbedPane.getChildren().add(new SubPanel(
                Components.createOutputText(context, "Element Inspector"),
                elementProperties
        ));
        elementProperties.setStyle("width: 100%; height: 100%;");
        elementProperties.getChildren().add(new Scrolling());
        elementProperties.getChildren().add(new ColumnResizing());
        elementProperties.setVerticalGridLines("1px solid gray");
        Column col1 = new Column();
        col1.setStyle("width: 150px");
        col1.setHeader(Components.createOutputText(context, "Property"));
        elementProperties.getChildren().add(col1);
        Column col2 = new Column();
        col2.setHeader(Components.createOutputText(context, "Value"));
        elementProperties.getChildren().add(col2);
    }

    private UIComponent createLogPageContent(FacesContext context, Debug debug) {
        HtmlPanelGrid panelGrid = (HtmlPanelGrid) Components.createChildComponent(context, debug, HtmlPanelGrid.COMPONENT_TYPE, "consoleContainer");
        panelGrid.setStyle("width: 100%; height: 100%;");
        panelGrid.setColumns(1);
        panelGrid.setRowClasses("o_debugRow1, o_debugRow2");
        HtmlPanelGroup toolbar = (HtmlPanelGroup) Components.createChildComponent(context, panelGrid, HtmlPanelGroup.COMPONENT_TYPE, "consoleToolbar");
        addButton(context, toolbar, "clearLog", "Clear");
        addButton(context, toolbar, "pauseLog", "Pause");


        DataTable logTable = (DataTable) Components.createChildComponent(
                context, panelGrid, DataTable.COMPONENT_TYPE, "log");
        Scrolling scrolling = new Scrolling();
        logTable.getChildren().add(scrolling);
        logTable.getChildren().add(new ColumnResizing());
        if (Environment.isExplorer())
            logTable.setStyle("width: 100%");
        else
            logTable.setStyle("width: 100%; height: 100%;");
        logTable.setVerticalGridLines("1px solid gray");
        Column col1 = new Column();
        col1.setStyle("width: 150px");
        col1.setHeader(Components.createOutputText(context, "Time"));
        logTable.getChildren().add(col1);
        Column col2 = new Column();
        col2.setHeader(Components.createOutputText(context, "Message"));
        logTable.getChildren().add(col2);

        return panelGrid;
    }

    private HtmlCommandButton addButton(FacesContext context, UIComponent parent, String id, String text) {
        HtmlCommandButton btn = (HtmlCommandButton) Components.createChildComponent(context, parent, HtmlCommandButton.COMPONENT_TYPE, id);
        btn.setValue(text);
        return btn;
    }

    @Override
    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);
        Rendering.renderInitScript(context,
                new ScriptBuilder().initScript(context, component, "O$.Debug._init"),
                Resources.internalURL(context, "util/debug.js"));

    }
}
