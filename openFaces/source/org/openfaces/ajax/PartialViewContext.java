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

import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.FunctionCallScript;
import org.openfaces.util.InitScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.UtilPhaseListener;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class PartialViewContext extends PartialViewContextWrapper {
    private javax.faces.context.PartialViewContext wrapped;

    public PartialViewContext(javax.faces.context.PartialViewContext wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        PartialResponseWriter originalWriter = super.getPartialResponseWriter();
        return new PartialResponseWriterWrapper(originalWriter) {
            @Override
            public void endDocument() throws IOException {
                renderAdditionalPartialResponse();
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

    public static void renderAdditionalPartialResponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            renderAdditionalPartialResponse(context);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    private static void renderAdditionalPartialResponse(FacesContext context) throws IOException {
        javax.faces.context.PartialViewContext partialViewContext = context.getPartialViewContext();
        PartialResponseWriter partialWriter = partialViewContext.getPartialResponseWriter();
        ScriptBuilder sb = new ScriptBuilder();
        Set<String> jsFiles = new LinkedHashSet<String>();
        List<InitScript> initScripts = Rendering.getAjaxInitScripts(context);
        if (initScripts.isEmpty()) return;
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

        partialWriter.startEval();
        partialWriter.writeText(new FunctionCallScript("O$._runScript",
                new AnonymousFunction(sb),
                jsFiles), null);
        partialWriter.endEval();
    }

}
