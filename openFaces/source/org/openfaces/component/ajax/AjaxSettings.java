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
package org.openfaces.component.ajax;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Eugene Goncharov
 */
public class AjaxSettings extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.AjaxSettings";
    public static final String COMPONENT_FAMILY = "org.openfaces.AjaxSettings";

    private static final String PROGRESS_MESSAGE_FACET = "progressMessage";
    private static final String SESSION_EXPIRATION_FACET = "sessionExpiration";
    private static final String ERROR_MESSAGE_FACET = "errorMessage";

    private String onajaxstart;
    private String onajaxend;
    private String onerror;
    private String onsessionexpired;

    public AjaxSettings() {
        setRendererType("org.openfaces.AjaxSettingsRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, onerror, onsessionexpired, onajaxstart, onajaxend};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        onerror = (String) stateArray[i++];
        onsessionexpired = (String) stateArray[i++];
        onajaxstart = (String) stateArray[i++];
        onajaxend = (String) stateArray[i++];
    }

    public ProgressMessage getProgressMessage() {
        ProgressMessage progressMessage = (ProgressMessage) getFacet(PROGRESS_MESSAGE_FACET);
        if (progressMessage == null) {
            progressMessage = new DefaultProgressMessage();
            setProgressMessage(progressMessage);
        }
        return progressMessage;
    }

    public SessionExpiration getSessionExpiration() {
        SessionExpiration sessionExpiration = (SessionExpiration) getFacet(SESSION_EXPIRATION_FACET);
        if (sessionExpiration == null) {
            sessionExpiration = new DefaultSessionExpiration();
            setSessionExpiration(sessionExpiration);
        }
        return sessionExpiration;
    }

    public ErrorMessage getErrorMessage() {
        return (ErrorMessage) getFacet(ERROR_MESSAGE_FACET);
    }

    public void setProgressMessage(ProgressMessage progressMessage) {
        getFacets().put(PROGRESS_MESSAGE_FACET, progressMessage);
    }

    public void setSessionExpiration(SessionExpiration sessionExpiration) {
        getFacets().put(SESSION_EXPIRATION_FACET, sessionExpiration);
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        getFacets().put(ERROR_MESSAGE_FACET, errorMessage);
    }

    public String getOnajaxstart() {
        return ValueBindings.get(this, "onajaxstart", onajaxstart);
    }

    public void setOnajaxstart(String onajaxstart) {
        this.onajaxstart = onajaxstart;
    }

    public String getOnajaxend() {
        return ValueBindings.get(this, "onajaxend", onajaxend);
    }

    public void setOnajaxend(String onajaxend) {
        this.onajaxend = onajaxend;
    }

    public String getOnerror() {
        return ValueBindings.get(this, "onerror", onerror);
    }

    public void setOnerror(String onerror) {
        this.onerror = onerror;
    }

    public String getOnsessionexpired() {
        return ValueBindings.get(this, "onsessionexpired", onsessionexpired);
    }

    public void setOnsessionexpired(String onsessionexpired) {
        this.onsessionexpired = onsessionexpired;
    }


}
