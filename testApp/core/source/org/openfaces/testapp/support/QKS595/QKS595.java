/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.support.QKS595;

import javax.faces.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Darya Shumilina
 */
public class QKS595 {

    Logger logger = Logger.getLogger(QKS595.class.getName());

    public void extendMultipleSelected() {
        logger.info("extendMultipleSelected action done!");
    }

    public void refreshListener(ActionEvent event) {

    }

    public void refreshAction() {
        logger.info("refreshAction done!");
    }

    public void deleteMultipleAction() {
        logger.info("deleteMultipleAction done!");
    }

    public String getNumberOfSelected() {
        return "9";
    }

    public Date getLatestExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 3, 23);
        return c.getTime();
    }

    public Date getEndOfTime() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 3, 27);
        return c.getTime();
    }

    public void resizeMultipleSelected() {
        logger.info("resizeMultipleSelected done!");
    }

}