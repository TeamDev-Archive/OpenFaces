/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.util;

import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.panel.TabbedPaneItem;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.TableColumn;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.component.LoadingMode;
import org.openfaces.renderkit.CompoundComponentRenderer;
import org.openfaces.renderkit.window.WindowRenderer;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class DebugRenderer extends WindowRenderer implements CompoundComponentRenderer {
    @Override
    protected String getDefaultClassName() {
        return StyleUtil.mergeClassNames(super.getDefaultClassName(), "o_debug");
    }

    public void createSubComponents(FacesContext context, UIComponent component) {
        TabbedPane tabbedPane = (TabbedPane) ComponentUtil.createChildComponent(context, component, TabbedPane.COMPONENT_TYPE, "pages");
        tabbedPane.setLoadingMode(LoadingMode.CLIENT);
        tabbedPane.setStyle("width: 100%; height: 100%;");

        tabbedPane.getChildren().add(new TabbedPaneItem(
                ComponentUtil.createOutputText(context, "Console"),
                ComponentUtil.createChildComponent(context, component, HtmlOutputText.COMPONENT_TYPE, "log")
        ));
        DataTable elementProperties = (DataTable) ComponentUtil.createChildComponent(
                context, component, DataTable.COMPONENT_TYPE, "elementProperties");
        tabbedPane.getChildren().add(new TabbedPaneItem(
                ComponentUtil.createOutputText(context, "Element Inspector"),
                elementProperties
        ));
        elementProperties.setStyle("width: 100%;");
//        elementProperties.getChildren().add(new ColumnResizing());
        elementProperties.setVerticalGridLines("1px solid gray");
        TableColumn col1 = new TableColumn();
        col1.setStyle("width: 150px");
        col1.setHeader(ComponentUtil.createOutputText(context, "Property"));
        col1.createSubComponents(context);
        elementProperties.getChildren().add(col1);
        TableColumn col2 = new TableColumn();
        col2.setHeader(ComponentUtil.createOutputText(context, "Value"));
        col2.createSubComponents(context);
        elementProperties.getChildren().add(col2);
    }

    @Override
    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);
        RenderingUtil.renderInitScript(context, new ScriptBuilder().initScript(context, component, "O$._initDebug"),
                new String[]{
                        ResourceUtil.getInternalResourceURL(context, DebugRenderer.class, "debug.js")
                });

    }
}
