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
package org.openfaces.renderkit.chart;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartPopup;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.window.PopupLayerRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ChartPopupRenderer extends PopupLayerRenderer implements AjaxPortionRenderer {

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        final ChartPopup popup = (ChartPopup) component;
        if (popup.getLoadingMode().equals(LoadingMode.SERVER)) {
            throw new IllegalArgumentException("Server loading mode is unsupported by ChartPopup component.");
        }

        final boolean clientLoadingMode = popup.getLoadingMode().equals(LoadingMode.CLIENT);

        if (clientLoadingMode) {
            super.encodeChildren(context, component);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final ChartPopup popup = (ChartPopup) component;
        renderScripts(context, popup);
    }

    private void renderScripts(FacesContext context, UIComponent component) throws IOException {
        final ChartPopup popup = (ChartPopup) component;
        final String clientId = popup.getClientId(context);
        final boolean clientLoadingMode = popup.getLoadingMode().equals(LoadingMode.CLIENT);

        final UIComponent chartView = popup.getParent();
        final Chart chart = (Chart) chartView.getParent();
        final Integer entityIndex = chart.getEntityIndex();
        chart.setEntityIndex(-1);
        final String chartId = chart.getClientId(context);
        chart.setEntityIndex(entityIndex);

        ScriptBuilder buf = new ScriptBuilder();
        buf.functionCall("O$.ChartPopup._init", clientId, popup.getLoadingMode().toString(), chartId).semicolon();

        Rendering.renderInitScript(context, buf,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "chart/chart.js"),
                (!clientLoadingMode ? Resources.ajaxUtilJsURL(context) : null));
    }

    private void renderPopupContent(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        if (getRendersChildren()) {
            super.encodeChildren(context, component);
        }

        super.encodeEnd(context, component);

        renderScripts(context, component);
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component,
                                        String portionName, JSONObject jsonParam) throws IOException, JSONException {
        if (!portionName.equals("content"))
            throw new IllegalArgumentException("Unknown portionName: " + portionName);

        renderPopupContent(context, component);
        final UIComponent chartView = component.getParent();
        final Chart chart = (Chart) chartView.getParent();
        chart.setEntityIndex(-1);

        return null;
    }
}
