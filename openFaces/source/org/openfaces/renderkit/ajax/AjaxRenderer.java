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
package org.openfaces.renderkit.ajax;

import org.openfaces.ajax.AjaxRequest;
import org.openfaces.component.ajax.Ajax;
import org.openfaces.component.ajax.AjaxHelper;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.event.AjaxActionEvent;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ilya Musihin
 */
public class AjaxRenderer extends AbstractSettingsRenderer {
    private AjaxRendererHelper helper = new AjaxRendererHelper();
    public static final String ATTACH_ON_CLIENT = "_attachOnClient";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Ajax ajax = (Ajax) component;
        if (ajax.getAttributes().remove(ATTACH_ON_CLIENT) != null) {
            AjaxHelper ajaxHelper = new AjaxHelper(ajax);
            String handlerScript = ajaxHelper.getClientActionScript(context, ajax);
            ScriptBuilder initScript = new ScriptBuilder().functionCall("O$.Ajax._attachHandler", ajax.getFor(), ajax.getEvent(),
                    new AnonymousFunction(handlerScript)).semicolon();
            Rendering.renderInitScript(context, initScript, Resources.ajaxUtilJsURL(context));
        } else if (ajax.isStandalone())
            encodeStandaloneInvocationMode(context, ajax);
    }

    private void encodeStandaloneInvocationMode(FacesContext context, Ajax ajax) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", ajax);
        writer.writeAttribute("style", "display: none", null);
        String clientId = ajax.getClientId(context);
        writer.writeAttribute("id", clientId, null);

        AjaxInitializer ajaxInitializer = new AjaxInitializer();
        ScriptBuilder initScript = new ScriptBuilder();
        initScript.initScript(context, ajax, "O$._initAjax",
                ajaxInitializer.getRenderArray(context, ajax, ajax.getRender()),
                ajaxInitializer.getAjaxParams(context, ajax));

        helper.appendMissingParameters(context, ajax, initScript);

        AjaxUtil.renderAjaxSupport(context);
        Rendering.renderInitScript(context, initScript);

        writer.endElement("span");
    }

    @Override
    public void decode(final FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);

        final Ajax ajax = (Ajax) component;
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = component.getClientId(context);
        if (requestParameters.containsKey(key)) {
            AjaxActionEvent event = new AjaxActionEvent(ajax, new Behavior() {
                public void broadcast(BehaviorEvent event) {
                    FacesContext context = FacesContext.getCurrentInstance();

                    ValueExpression listener = ajax.getValueExpression("listener");
                    if (listener == null) return;
                    String valueExpression = listener.getExpressionString();
                    ELContext elContext = context.getELContext();
                    MethodExpression methodExpression = context.getApplication().getExpressionFactory().createMethodExpression(
                            elContext, valueExpression, void.class, new Class[]{AjaxBehaviorEvent.class});
                    try {
                        methodExpression.invoke(elContext, new Object[]{(AjaxBehaviorEvent) event});
                    } catch (MethodNotFoundException e) {
                        methodExpression = context.getApplication().getExpressionFactory().createMethodExpression(
                                elContext, valueExpression, void.class, new Class[]{AjaxActionEvent.class});
                        methodExpression.invoke(elContext, new Object[]{event});
                    }
                    Object listenerResult = ((AjaxActionEvent) event).getAjaxResult();
                    if (listenerResult != null)
                        AjaxRequest.getInstance().setAjaxResult(listenerResult);
                }
            });
            event.setPhaseId(ajax.isImmediate() ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
            component.queueEvent(event);
        }
    }


}