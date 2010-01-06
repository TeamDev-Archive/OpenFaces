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
package org.openfaces.util;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class Log {
    public static void log(FacesContext context, String message) {
        context.getExternalContext().log(message);
    }

    public static void log(FacesContext context, String message, Throwable exception) {
        context.getExternalContext().log(message, exception);
    }

    public static void log(String message) {
        log(FacesContext.getCurrentInstance(), message);
    }

    public static void log(String message, Throwable exception) {
        log(FacesContext.getCurrentInstance(), message, exception);
    }

    private Log() {
    }
}
