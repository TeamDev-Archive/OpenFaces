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
package org.openfaces.component.validation;

import org.openfaces.renderkit.validation.HtmlMessageRenderer;
import org.openfaces.renderkit.validation.HtmlMessagesRenderer;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavel Kaplin
 */
public class RenderKitReplacerPhaseListener implements PhaseListener {
    private static final String FLAG_VALIDATION_RENDERERS_CHANGED = "org.openfaces.validation.standardMessagesRendererChanged";

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

        while (renderKitIds.hasNext()) {
            String renderKitId = renderKitIds.next();
            RenderKit renderKit = factory.getRenderKit(context, renderKitId);

            renderKit.addRenderer("javax.faces.Message", "javax.faces.Message", new HtmlMessageRenderer());
            renderKit.addRenderer("javax.faces.Messages", "javax.faces.Messages", new HtmlMessagesRenderer());

            factory.addRenderKit(renderKitId, new ValidationSupportRenderKit(renderKit));
        }
    }

}
