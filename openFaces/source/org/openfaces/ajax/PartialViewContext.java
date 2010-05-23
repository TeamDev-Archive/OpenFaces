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

import org.openfaces.util.UtilPhaseListener;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.event.PhaseId;

/**
 * @author Dmitry Pikhulya
 */
public class PartialViewContext extends PartialViewContextWrapper {
    private javax.faces.context.PartialViewContext wrapped;

    public PartialViewContext(javax.faces.context.PartialViewContext wrapped) {
        this.wrapped = wrapped;
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
        if (isAjaxRequest())
            UtilPhaseListener.processAjaxExecutePhase(FacesContext.getCurrentInstance());
    }
}
