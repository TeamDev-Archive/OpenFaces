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

package org.openfaces.testapp.jBossSeam.seamJSFComponents;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.openfaces.testapp.jBossSeam.seamJSFComponents.ValidatorsBean.Honorific;

/**
 * @author Tatyana Matveyeva
 */
@Name("factories")
public class Factories {
    @Factory("honorifics")
    public Honorific[] getHonorifics() {
        return Honorific.values();
    }

}
