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

package org.openfaces.testapp.support.QKS2116;

import java.util.Date;
import java.util.logging.Logger;


/**
 * @author Tatyana Matveyeva
 */
public class QKS2116 {

    Logger logger = Logger.getLogger(QKS2116.class.getName());

    private Date dataStart;
    private boolean groupSelected = false;


    public Date getDataStart() {
        return dataStart;
    }

    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }


    public boolean isGroupSelected() {
        return groupSelected;
    }

    public void setGroupSelected(boolean groupSelected) {
        this.groupSelected = groupSelected;
    }

    public String makeData() {
        logger.info("------------------action executed------------------");
        logger.info(dataStart.toString());
        return null;
    }

    public String showdropdown() {
        groupSelected = true;
        return null;
    }
}
