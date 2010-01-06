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

import org.openfaces.org.json.JSONObject;
import org.openfaces.util.PhaseListenerBase;
import org.openfaces.util.RequestFacade;
import org.openfaces.util.ResponseFacade;
import org.openfaces.validator.AjaxSupportedConverter;

import javax.faces.FacesException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidatorPhaseListener extends PhaseListenerBase {
    private static final String AJAX_VALIDATOR_REQUEST_HEADER = "teamdev_ajax_VALIDATOR";
    private static final int BUFFER_SIZE = 10000;

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void afterPhase(PhaseEvent phaseEvent) {
        if (phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW) {
            ValidationProcessor.resetVerifiableComponents(phaseEvent.getFacesContext());
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        if (checkPortletMultipleNotifications(phaseEvent, true))
            return;

        FacesContext context = phaseEvent.getFacesContext();

        Object requestObj = context.getExternalContext().getRequest();
        RequestFacade request = RequestFacade.getInstance(requestObj);
        checkOurPhaseListenerInvokedOnce(phaseEvent);

        if (isAjaxValidatorRequest(request)) {
            try {
                ResponseFacade response = ResponseFacade.getInstance(context.getExternalContext().getResponse());
                processValidation(context, request, response);
            } catch (IOException e) {
                throw new FacesException(e);
            }
        }

    }

    private boolean isAjaxValidatorRequest(RequestFacade request) {
        // for portlets: String browser = request.getProperty(...);
        return request.getHeader(AJAX_VALIDATOR_REQUEST_HEADER) != null; // todo: getHeader can't be used on portlets - doesn't work in Liferay
    }

    private void processValidation(FacesContext context, RequestFacade request, ResponseFacade response) throws IOException {
        Converter converter = null;

        response.setContentType("text/plain");

        Writer writer = response.getWriter();

        InputStream input = request.getInputStream();

        boolean isValid = true;

        if (input != null) {
            InputStreamReader reader = new InputStreamReader(input);
            char[] target = new char[BUFFER_SIZE];
            int readed = reader.read(target);
            String params = new String(target, 0, readed);
            try {
                JSONObject jParams = new JSONObject(params);
                JSONObject jValidatorTransportObject = jParams.getJSONObject("params");

                String jValue = jValidatorTransportObject.getString("value");
                JSONObject jValidator = jValidatorTransportObject.getJSONObject("validator");
                String javaClassName = jValidator.getString("javaClassName");
                if (javaClassName != null) {
                    Object validator = Class.forName(javaClassName).newInstance();
                    if (validator instanceof AjaxSupportedConverter) {
                        converter = ((AjaxSupportedConverter) validator).getConverter(context, jValidator);
                    }
                }

                if (converter != null) {
                    try {
                        converter.getAsObject(context, new UIInput(), jValue);
                    } catch (RuntimeException e) {
                        isValid = false;
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            reader.close();

        }

        writer.write(Boolean.toString(isValid));
        writer.flush();
        writer.close();
        context.responseComplete();
    }

}
