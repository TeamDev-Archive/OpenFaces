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
package org.openfaces.testapp.support.QKS595;

import java.util.logging.Logger;

/**
 * @author Darya Shumilina
 */
public class QKS595ResizeRequest {

    Logger logger = Logger.getLogger(QKS595ResizeRequest.class.getName());

    public String getSize() {
        return "1024";
    }

    public void validateSize() {
        logger.info("validateSize done!");
    }


    public String getSizeUnits() {
        return "512";
    }

    public String getErrorMessage() {
        return "Error Message";
    }

    public void setSize(String size) {
    }

    public void setSizeUnits(String sizeUnits) {
    }
}