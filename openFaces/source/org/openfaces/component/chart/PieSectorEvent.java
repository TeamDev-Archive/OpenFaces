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
package org.openfaces.component.chart;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieSectorEvent extends ActionEvent {
    private final PieSectorInfo sectorInfo;

    public PieSectorEvent(UIComponent uiComponent, PieSectorInfo info) {
        super(uiComponent);
        sectorInfo = info;
    }

    public PieSectorInfo getSector() {
        return sectorInfo;
    }

}
