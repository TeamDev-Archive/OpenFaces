/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.customEvents;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import java.util.logging.Logger;

/**
 * @author Dmitry Pikhulya
 */
public class MyValueChangeListener implements ValueChangeListener {

    Logger logger = Logger.getLogger(MyValueChangeListener.class.getName());

    public void processValueChange(ValueChangeEvent event) {
        logger.info("===  Value changed: old=" + event.getOldValue() + "; new=" + event.getNewValue() + "  ===");
    }
}
