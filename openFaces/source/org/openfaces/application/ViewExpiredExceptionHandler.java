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

package org.openfaces.application;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ViewExpiredExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler wrapped;
    private static final String SESSION_EXPIRED_KEY = ViewExpiredExceptionHandler.class.getName() + ".SESSION_EXPIRED";
    private static final String SESSION_EXPIRATION_TESTING_PARAM = "of__session_expiration_testing";

    public ViewExpiredExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();
        while (it.hasNext()) {
            ExceptionQueuedEvent exceptionQueuedEvent = it.next();
            ExceptionQueuedEventContext eventContext = (ExceptionQueuedEventContext) exceptionQueuedEvent.getSource();
            Throwable exception = eventContext.getException();
            if (exception instanceof ViewExpiredException) {
                it.remove();
                handleViewExpiration(eventContext);
            }


        }
        super.handle();
    }

    private void handleViewExpiration(ExceptionQueuedEventContext eventContext) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(SESSION_EXPIRED_KEY, true);
    }

    public static boolean isExpiredView(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        return externalContext.getRequestMap().containsKey(SESSION_EXPIRED_KEY) ||
                externalContext.getRequestParameterMap().containsKey(SESSION_EXPIRATION_TESTING_PARAM);
    }
}
