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
package org.openfaces.component.action;

import org.openfaces.component.OUICommand;

/**
 * This component is under construction. API is subject to change. Please avoid using this component in a production
 * environment.
 *
 * @author Dmitry Pikhulya
 */
public class CommandButton extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.CommandButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.CommandButton";

    public CommandButton() {
        setRendererType("org.openfaces.CommandButtonRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}