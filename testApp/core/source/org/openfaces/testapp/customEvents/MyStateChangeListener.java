/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.customEvents;

import org.openfaces.event.StateChangeEvent;
import org.openfaces.event.StateChangeListener;

import java.util.logging.Logger;

/**
 * @author Kharchenko
 */
public class MyStateChangeListener implements StateChangeListener {

    Logger logger = Logger.getLogger(MyStateChangeListener.class.getName());

    public void processStateChange(StateChangeEvent event) {
        logger.info("=============== StateChange occurred ===============");
    }
}
