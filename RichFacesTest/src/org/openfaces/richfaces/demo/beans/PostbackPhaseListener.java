package org.openfaces.richfaces.demo.beans;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public final class PostbackPhaseListener implements PhaseListener {

    public static final String POSTBACK_ATTRIBUTE_NAME = PostbackPhaseListener.class.getName();

    public void afterPhase(PhaseEvent arg0) {}

	public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        requestMap.put(POSTBACK_ATTRIBUTE_NAME, Boolean.TRUE);
	}

	public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
	}

    public static boolean isPostback() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {
                return Boolean.TRUE.equals(
                        externalContext.getRequestMap().get(POSTBACK_ATTRIBUTE_NAME));
            }
        }
        
        return false;
    }
}
