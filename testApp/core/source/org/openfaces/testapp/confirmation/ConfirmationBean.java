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
package org.openfaces.testapp.confirmation;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Darya Shumilina
 */
public class ConfirmationBean {

    private Logger logger = Logger.getLogger(ConfirmationBean.class.getName());

    private String cancelButtonText;
    private String okButtonText;
    private String details;
    private String message;
    private String captionText;
    private String testValue;
    
    private boolean confirmedAction;
    private boolean commandLinkAction;
    private boolean outputLinkAction;

    public ConfirmationBean() {
    }

    public String getCancelButtonText() {
        Random rand = new Random();
        cancelButtonText = "Cancel button #" + String.valueOf(rand.nextInt(37000));
        return cancelButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public String getOkButtonText() {
        Random rand = new Random();
        okButtonText = "Ok button #" + String.valueOf(rand.nextInt(37000));
        return okButtonText;
    }

    public void setOkButtonText(String okButtonText) {
        this.okButtonText = okButtonText;
    }

    public String getDetails() {
        Random rand = new Random();
        details = "Details #" + String.valueOf(rand.nextInt(37000));
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMessage() {
        Random rand = new Random();
        message = "Message #" + String.valueOf(rand.nextInt(37000));
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCaptionText() {
        Random rand = new Random();
        captionText = "Caption #" + String.valueOf(rand.nextInt(37000));
        return captionText;
    }

    public void setCaptionText(String captionText) {
        this.captionText = captionText;
    }

    public String getTestValue() {
        Random rand = new Random();
        testValue = "Current value #" + rand.nextInt(37000);
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public String confirmedActionString() {
        confirmedAction = !confirmedAction;
        logger.info(Boolean.toString(confirmedAction));
        return "";
    }

    public boolean isConfirmedAction() {
        return confirmedAction;
    }

    public String commandLinkActionString() {
        commandLinkAction = !commandLinkAction;
        return "";
    }

    public boolean isCommandLinkAction() {
        return commandLinkAction;
    }

    public String outputLinkActionString() {
        outputLinkAction = !outputLinkAction;
        return "";
    }

    public boolean isOutputLinkAction() {
        return outputLinkAction;
    }
}