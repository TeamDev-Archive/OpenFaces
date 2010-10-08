/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.ajax;

import org.openfaces.application.ViewExpiredExceptionHandler;
import org.openfaces.component.ComponentWithExternalParts;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONString;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.util.*;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Pikhulya
 */
public class PartialViewContext extends PartialViewContextWrapper {
    private javax.faces.context.PartialViewContext wrapped;
    private static final String PORTION_DATAS_KEY = PartialViewContext.class.getName() + ".ajaxPortionDatas";
    private static final String SESSION_EXPIRATION_EXTENSION_KEY = PartialViewContext.class.getName() + ".sessionExpirationExtension";

    public PartialViewContext(javax.faces.context.PartialViewContext wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        PartialResponseWriter originalWriter = super.getPartialResponseWriter();
        return new PartialResponseWriterWrapper(originalWriter) {
            private boolean additionalResponseRendered;

            @Override
            public void startUpdate(String targetId) throws IOException {
                if (targetId.equals(PartialResponseWriter.VIEW_STATE_MARKER)) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    prepareViewExpirationExtension(context);
                    prepareAjaxPortions(context);
                }
                super.startUpdate(targetId);
            }

            @Override
            public void startExtension(Map<String, String> attributes) throws IOException {
                if (!additionalResponseRendered) {
                    additionalResponseRendered = true;
                    renderAdditionalPartialResponse();
                }
                super.startExtension(attributes);
            }

            @Override
            public void endDocument() throws IOException {
                if (!additionalResponseRendered) {
                    additionalResponseRendered = true;
                    renderAdditionalPartialResponse();
                }
                super.endDocument();
            }
        };
    }

    @Override
    public javax.faces.context.PartialViewContext getWrapped() {
        return wrapped;
    }

    @Override
    public void setPartialRequest(boolean isPartialRequest) {
        wrapped.setPartialRequest(isPartialRequest);
    }

    @Override
    public void processPartial(PhaseId phaseId) {
        super.processPartial(phaseId);
        if (isAjaxRequest()) {
            if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                UtilPhaseListener.processAjaxExecutePhase(FacesContext.getCurrentInstance());
            }
        }

    }

    @Override
    public boolean isRenderAll() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (AjaxUtil.isAjaxPortionRequest(context))
            return false;
        return super.isRenderAll();
    }

    @Override
    public Collection<String> getExecuteIds() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (ViewExpiredExceptionHandler.isExpiredView(context))
            return Collections.emptyList();

        return super.getExecuteIds();
    }

    @Override
    public Collection<String> getRenderIds() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (AjaxUtil.isAjaxPortionRequest(context) || ViewExpiredExceptionHandler.isExpiredView(context))
            return Collections.emptyList();
        Set<String> result = new LinkedHashSet<String>(super.getRenderIds());
        result.addAll(AjaxRequest.getInstance().getReloadedComponentIds());
        List<String> additionalComponents = new ArrayList<String>();
        UIViewRoot viewRoot = context.getViewRoot();
        for (String id : result) {
            UIComponent component = viewRoot.findComponent(id);
            if (component == null) continue;
            if (!(component instanceof ComponentWithExternalParts)) continue;
            Collection<String> externalPartIds = ((ComponentWithExternalParts) component).getExternalPartIds();
            String componentClientId = component.getClientId(context);
            List<String> unprocessableIds = new ArrayList<String>();
            for (String externalPartId : externalPartIds) {
                // component's facets such as DataTable's "above" and "below" components are included into the
                // "external components" (as returned by getExternalPartIds), but these components are skipped by the
                // table's visitTree method and thus these facets don't get automatically included into the rerendered
                // component list, so we need to collect them and render their "update" portions manually here.
                if (externalPartId.startsWith(componentClientId)) {
                    unprocessableIds.add(externalPartId);
                }
            }
            context.getExternalContext().getRequestMap().put(getUnprocessableComponentIdsKey(), unprocessableIds);
            additionalComponents.addAll(externalPartIds);
        }

        result.addAll(additionalComponents);
        return result;
    }

    private String getUnprocessableComponentIdsKey() {
        return PartialViewContext.class.getName() + ".unprocessableComponentIds";
    }

    public Collection<String> getRenderIdsNotRenderedYet(FacesContext context) {
        Map<String, Object> map = context.getExternalContext().getRequestMap();
        return (Collection<String>) map.get(getUnprocessableComponentIdsKey());
    }

    private void writeComponentUpdate(FacesContext context, UIComponent component) throws IOException {
        PartialResponseWriter writer = getPartialResponseWriter();
        writer.startUpdate(component.getClientId(context));
        component.encodeAll(context);
        writer.endUpdate();
    }

    public void renderAdditionalPartialResponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            renderAdditionalPartialResponse(context);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    private void renderAdditionalPartialResponse(FacesContext context) throws IOException {
        Collection<String> additionalUpdateList = getRenderIdsNotRenderedYet(context);
        UIViewRoot viewRoot = context.getViewRoot();
        if (additionalUpdateList != null)
            for (String componentId : additionalUpdateList) {
                UIComponent component = viewRoot.findComponent(componentId);
                if (component == null) continue;
                writeComponentUpdate(context, component);
            }
        renderAjaxPortions(context);
        renderAjaxInitScripts(context);
        renderAjaxResult(context);
    }

    private void prepareViewExpirationExtension(final FacesContext context) throws IOException {
        if (!ViewExpiredExceptionHandler.isExpiredView(context)) return;
        final ExternalContext externalContext = context.getExternalContext();
        final AjaxExtension extension = prepareExtension(context, "sessionExpiration", "", new ExtensionRenderer() {
            public JSONObject render() throws IOException {
                AtomicReference<RequestFacade> requestFacade = new AtomicReference<RequestFacade>(RequestFacade.getInstance(externalContext.getRequest()));
                CommonAjaxViewRoot.handleSessionExpirationOnEncodeChildren(context, requestFacade.get());
                return null;
            }
        }, true);

        externalContext.getRequestMap().put(SESSION_EXPIRATION_EXTENSION_KEY, extension);

    }

    private static List<String> prepareResourceUrls(Collection<String> resources) {
        FacesContext context = FacesContext.getCurrentInstance();
        List<String> result = new ArrayList<String>(resources.size());
        for (String resource : resources) {
            result.add(Resources.getInternalURL(context, resource));
        }
        return result;
    }

    private static void renderAjaxInitScripts(FacesContext context) throws IOException {
        InitScript script = getCombinedAjaxInitScripts(context);
        if (script == null) return;

        javax.faces.context.PartialViewContext partialViewContext = context.getPartialViewContext();
        PartialResponseWriter partialWriter = partialViewContext.getPartialResponseWriter();
        partialWriter.startEval();
        partialWriter.write(
                new FunctionCallScript("O$._runScript", script.getScript(), script.getJsFiles()).toString()
        );
        partialWriter.endEval();
    }

    public static InitScript getCombinedAjaxInitScripts(FacesContext context) {
        ScriptBuilder sb = new ScriptBuilder();
        Set<String> jsFiles = new LinkedHashSet<String>();
        List<InitScript> initScripts = Rendering.getAjaxInitScripts(context);
        if (initScripts.isEmpty()) return null;
        boolean semicolonNeeded = false;
        for (InitScript initScript : initScripts) {
            Script script = initScript.getScript();
            if (semicolonNeeded) sb.semicolon();
            sb.append(script);
            semicolonNeeded = true;
            String[] files = initScript.getJsFiles();
            if (files != null)
                jsFiles.addAll(Arrays.asList(files));
        }
        initScripts.clear();
        // remove the possible null references, which are normally allowed to present, from js library list
        jsFiles.remove(null);
        InitScript script = new InitScript(new AnonymousFunction(sb), jsFiles.toArray(new String[jsFiles.size()]));
        return script;
    }

    private interface ExtensionRenderer {
        public JSONObject render() throws IOException;
    }

    private static void prepareAjaxPortions(final FacesContext context) throws IOException {
        List<String> updatePortions = AjaxUtil.getAjaxPortionNames(context);
        if (updatePortions.isEmpty()) return;
        ExternalContext externalContext = context.getExternalContext();
        String renderParam = externalContext.getRequestParameterMap().get(
                javax.faces.context.PartialViewContext.PARTIAL_RENDER_PARAM_NAME);
        String[] renderIds = renderParam.split("[ \t]+");
        if (renderIds.length != 1)
            throw new RuntimeException("There should be one target component but was: " + renderIds.length);
        final UIComponent component = UtilPhaseListener.findComponentById(context.getViewRoot(), renderIds[0],
                false, true, true);

        RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = factory.getRenderKit(context, context.getViewRoot().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(component.getFamily(), component.getRendererType());
        final JSONObject customJSONParam = AjaxUtil.getCustomJSONParam(context);
        final AjaxPortionRenderer ajaxComponentRenderer = (AjaxPortionRenderer) renderer;
        List<AjaxExtension> extensions = new ArrayList<AjaxExtension>();
        for (final String portionName : updatePortions) {
            AjaxExtension extension = prepareExtension(context, "portionData", portionName, new ExtensionRenderer() {
                public JSONObject render() throws IOException {
                    try {
                        return ajaxComponentRenderer.encodeAjaxPortion(context, component, portionName, customJSONParam);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, false);
            extensions.add(extension);
        }
        context.getExternalContext().getRequestMap().put(PORTION_DATAS_KEY, extensions);
    }

    private static AjaxExtension prepareExtension(FacesContext context,
                                                  String extensionType,
                                                  String portionName,
                                                  ExtensionRenderer extensionRenderer,
                                                  boolean appendAjaxInitScripts) throws IOException {
        StringBuilder portionOutput;
        JSONObject responseData;
        StringWriter stringWriter = new StringWriter();
        ResponseWriter originalWriter = CommonAjaxViewRoot.substituteResponseWriter(context, stringWriter);
        try {
            responseData = extensionRenderer.render();
            portionOutput = new StringBuilder(stringWriter.toString());
        } finally {
            CommonAjaxViewRoot.restoreWriter(context, originalWriter);
        }

        StringBuilder rawScriptsBuffer = new StringBuilder();
        StringBuilder rtLibraryScriptsBuffer = new StringBuilder();
        CommonAjaxViewRoot.extractScripts(portionOutput, rawScriptsBuffer, rtLibraryScriptsBuffer);

        Collection<String> libraries = new LinkedHashSet<String>();
        List<String> registeredJsLibs = Resources.getRegisteredJsLibraries();
        if (registeredJsLibs != null)
            libraries.addAll(registeredJsLibs);

        if (appendAjaxInitScripts) {
            InitScript script = PartialViewContext.getCombinedAjaxInitScripts(context);
            libraries.addAll(Arrays.asList(script.getJsFiles()));
            Script additionalScript = new ScriptBuilder("_temp_=").append(script.getScript()).append("();");
            rawScriptsBuffer.append(additionalScript);
        }

        AjaxExtension extension = new AjaxExtension(
                extensionType,
                portionName,
                portionOutput.toString(),
                responseData,
                libraries,
                rawScriptsBuffer.toString());
        return extension;
    }

    private static void renderAjaxPortions(FacesContext context) throws IOException {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        AjaxExtension sessionExpirationExtension = (AjaxExtension) requestMap.get(SESSION_EXPIRATION_EXTENSION_KEY);
        javax.faces.context.PartialViewContext partialViewContext = context.getPartialViewContext();
        PartialResponseWriter partialWriter = partialViewContext.getPartialResponseWriter();
        if (sessionExpirationExtension != null) {
            sessionExpirationExtension.render(partialWriter);
            return;
        }
        List<AjaxExtension> extensions = (List<AjaxExtension>) requestMap.get(PORTION_DATAS_KEY);
        if (extensions != null)
            for (AjaxExtension extension : extensions) {
                extension.render(partialWriter);
            }
    }

    private static class AjaxExtension {
        private String type;
        private String portionName;
        private String portionOutput;
        private JSONObject responseData;
        private Collection<String> registeredJsLibraries;
        private String scripts;

        private AjaxExtension(String type, String portionName, String portionOutput, JSONObject responseData, Collection<String> registeredJsLibraries, String scripts) {
            this.type = type;
            this.portionName = portionName;
            this.portionOutput = portionOutput;
            this.responseData = responseData;
            this.registeredJsLibraries = registeredJsLibraries;
            this.scripts = scripts;
        }

        public void render(PartialResponseWriter writer) throws IOException {
            Map<String, String> extensionAttributes = new HashMap<String, String>();
            extensionAttributes.put("ln", "openfaces");
            extensionAttributes.put("type", type);
            extensionAttributes.put("portion", portionName);
            extensionAttributes.put("text", portionOutput);
            extensionAttributes.put("data", responseData != null ? responseData.toString() : "null");
            extensionAttributes.put("jsLibs", new JSONArray(registeredJsLibraries).toString());
            extensionAttributes.put("scripts", scripts);

            writer.startExtension(extensionAttributes);
            writer.endExtension();
        }

    }

    private static void renderAjaxResult(FacesContext context) throws IOException {
        javax.faces.context.PartialViewContext partialViewContext = context.getPartialViewContext();
        PartialResponseWriter partialWriter = partialViewContext.getPartialResponseWriter();
        Map<String, String> extensionAttributes = new HashMap<String, String>();
        extensionAttributes.put("ln", "openfaces");
        extensionAttributes.put("type", "ajaxResult");
        Object ajaxResult = AjaxRequest.getInstance(context).getAjaxResult();
        extensionAttributes.put("ajaxResult", resultValueToJsValue(ajaxResult));

        partialWriter.startExtension(extensionAttributes);
        partialWriter.endExtension();
    }

    private static String resultValueToJsValue(Object resultValue) {
        if (resultValue != null && resultValue.getClass().isArray()) {
            List<Object> resultAsList = new ArrayList<Object>();
            for (int i = 0, count = Array.getLength(resultValue); i < count; i++) {
                resultAsList.add(Array.get(resultValue, i));
            }
            resultValue = resultAsList;
        }
        String value;
        if (resultValue == null)
            value = "null";
        else if (resultValue instanceof String)
            value = "\"" + resultValue + "\"";
        else if (resultValue instanceof JSONString)
            value = ((JSONString) resultValue).toJSONString();
        else if (resultValue instanceof Iterable) {
            StringBuilder sb = new StringBuilder("[");
            for (Object entry : (Iterable) resultValue) {
                if (sb.length() > 1) sb.append(",");
                sb.append(resultValueToJsValue(entry));
            }
            sb.append("]");
            value = sb.toString();
        } else if (resultValue instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            Set<Map.Entry> entries = ((Map) resultValue).entrySet();
            for (Map.Entry entry : entries) {
                if (sb.length() > 1) sb.append(",");
                sb.append("\"").append(entry.getKey()).append("\":").append(resultValueToJsValue(entry.getValue()));
            }
            sb.append("}");
            value = sb.toString();
        } else
            value = resultValue.toString();
        return value;
    }


}
