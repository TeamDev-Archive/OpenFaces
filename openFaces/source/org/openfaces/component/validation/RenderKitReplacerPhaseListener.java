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
package org.openfaces.component.validation;

import org.apache.myfaces.trinidad.render.DialogRenderKitService;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.renderkit.validation.HtmlMessageRenderer;
import org.openfaces.renderkit.validation.HtmlMessagesRenderer;
import org.openfaces.util.Log;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavel Kaplin
 */
public class RenderKitReplacerPhaseListener implements PhaseListener {

    private static final String FLAG_VALIDATION_RENDERERS_CHANGED = "org.openfaces.validation.standardMessagesRendererChanged";
    private static final String HTML_BASIC = "HTML_BASIC";


    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        boolean openFacesValidationDisabled = ValidationProcessor.isOpenFacesValidationDisabled(context);
        if (openFacesValidationDisabled) return;

        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        Boolean validationRenderersChanged = (Boolean) applicationMap.get(FLAG_VALIDATION_RENDERERS_CHANGED);
        if (validationRenderersChanged == null) {
            replaceRenderKit(context);
            applicationMap.put(FLAG_VALIDATION_RENDERERS_CHANGED, Boolean.TRUE);
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private void replaceRenderKit(FacesContext context) {
        RenderKitFactory factory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        Iterator<String> renderKitIds = factory.getRenderKitIds();
        int countCatch;

        while (renderKitIds.hasNext()) {
            String renderKitId = renderKitIds.next();
            RenderKit renderKit;

            if (isBackbasePresent() && renderKitId.equalsIgnoreCase(HTML_BASIC)) {
                renderKit = getBackbaseDefaultRenderKit(factory);
            } else {
                renderKit = factory.getRenderKit(context, renderKitId);
            }

            renderKit.addRenderer("javax.faces.Message", "javax.faces.Message", new HtmlMessageRenderer());
            renderKit.addRenderer("javax.faces.Messages", "javax.faces.Messages", new HtmlMessagesRenderer());

            countCatch = 0;

            try {
                if (renderKit instanceof Service.Provider)
                    processRenderKitSupportForTrinidad(false, renderKit, factory, renderKitId);
                else if (renderKit instanceof ExtendedRenderKitService && renderKit instanceof DialogRenderKitService)
                    processRenderKitSupportForTrinidad(true, renderKit, factory, renderKitId);
            } catch (NoClassDefFoundError e) {
                countCatch++;
            }

            try {
                if (renderKit instanceof oracle.adf.view.faces.render.ExtendedRenderKitService
                        && renderKit instanceof oracle.adf.view.faces.render.DialogRenderKitService)
                    processRenderKitSupportForADFFaces(renderKit, factory, renderKitId);
            } catch (NoClassDefFoundError e) {
                countCatch++;
            }
            if (countCatch > 1)
                factory.addRenderKit(renderKitId, new ValidationSupportRenderKit(renderKit));

        }
    }

    private RenderKit getBackbaseDefaultRenderKit(RenderKitFactory factory) {
        Class backbaseRenderKitFactoryClass;
        try {
            backbaseRenderKitFactoryClass = Class.forName("com.backbase.bjs.application.BackbaseRenderKitFactory");
            Method defaultRenderKitMethod = backbaseRenderKitFactoryClass.getMethod("getDefaultRenderKit", new Class[]{});
            if (defaultRenderKitMethod != null) {
                RenderKit defaultRenderKit = (RenderKit) defaultRenderKitMethod.invoke(factory,
                        new Object[]{});
                if (defaultRenderKit != null) {
                    return defaultRenderKit;
                }
            }

        } catch (ClassNotFoundException e) {
            Log.log("Backbase support is disabled, because no BackbaseRenderKitFactory class was found.");
        } catch (NoSuchMethodException e) {
            Log.log("Backbase support is disabled, because there is no " +
                    "getDefaultRenderKit method in BackbaseRenderKitFactory.");
        } catch (IllegalAccessException e) {
            Log.log("Backbase support is disabled, because exception was thrown during execution of " +
                    "getDefaultRenderKit method in BackbaseRenderKitFactory.");
        } catch (InvocationTargetException e) {
            Log.log("Backbase support is disabled, because exception was thrown during execution of " +
                    "getDefaultRenderKit method in BackbaseRenderKitFactory.");
        }

        return null;
    }

    private boolean isBackbasePresent() {
        Class backBaseFacesContextClass = null;

        try {
            backBaseFacesContextClass = Class.forName("com.backbase.bjs.context.BackbaseFacesContextFactoryImpl");
        } catch (ClassNotFoundException e) {
            Log.log("Backbase support is disabled, because no Backbase Context was found.");
        }

        return backBaseFacesContextClass != null;
    }


    private void processRenderKitSupportForTrinidad(boolean isCoreRenderKit,
                                                    RenderKit renderKit, RenderKitFactory factory, String renderKitId) {
        RenderKit proxy = null;
        try {
            if (isCoreRenderKit) {
                proxy = (RenderKit) Class.forName("org.openfaces.component.validation.RenderKitTrinidadCoreProxy").
                        getConstructor(RenderKit.class).newInstance(renderKit);
            } else {
                proxy = (RenderKit) Class.forName("org.openfaces.component.validation.RenderKitTrinidadProxy").
                        getConstructor(RenderKit.class).newInstance(renderKit);
            }
        } catch (Throwable e) {
            Rendering.logWarning(FacesContext.getCurrentInstance(), "Exception was thrown during processing renderkit for" +
                    "Trinidad support.");
        }

        if (proxy != null) {
            factory.addRenderKit(renderKitId, proxy);
            Map applicationMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
            Boolean trinidadSupport = (Boolean) applicationMap.get(Environment.PARAM_ENVIRONMENT_TRINIDAD_SUPPORT);
            if (trinidadSupport == null) {
                applicationMap.put(Environment.PARAM_ENVIRONMENT_TRINIDAD_SUPPORT, Boolean.TRUE);
            }
        } else {
            Rendering.logWarning(FacesContext.getCurrentInstance(), "Trinidad support for renderkit was disabled.");
            factory.addRenderKit(renderKitId, new ValidationSupportRenderKit(renderKit));
        }
    }

    private void processRenderKitSupportForADFFaces(RenderKit renderKit, RenderKitFactory factory, String renderKitId) {
        RenderKit proxy = null;
        try {
            proxy = (RenderKit) Class.forName("org.openfaces.component.validation.RenderKitADFFacesProxy").
                    getConstructor(RenderKit.class).newInstance(renderKit);
        } catch (Throwable e) {
            Rendering.logWarning(FacesContext.getCurrentInstance(), "Exception was thrown during processing renderkit for" +
                    "ADF Faces support.");
        }
        if (proxy != null) {
            factory.addRenderKit(renderKitId, proxy);
        } else {
            Rendering.logWarning(FacesContext.getCurrentInstance(), "ADF Faces support for renderkit was disabled.");
            factory.addRenderKit(renderKitId, new ValidationSupportRenderKit(renderKit));
        }
    }
}
