/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector;

import org.openqa.selenium.WebDriverException;

/**
 * @author Max Yurin
 */
public class ElementInspectorException extends WebDriverException {

    public ElementInspectorException(String message) {
        super(message);
    }

    public ElementInspectorException(Throwable cause) {
        super(cause);
    }

    public ElementInspectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
