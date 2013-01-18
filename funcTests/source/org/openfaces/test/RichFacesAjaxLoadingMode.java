/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.test;

import org.seleniuminspector.LoadingMode;

/**
 * @author Eugene Goncharov
 */
public class RichFacesAjaxLoadingMode extends org.seleniuminspector.LoadingMode {
    private static LoadingMode loadingMode = new RichFacesAjaxLoadingMode();

    private RichFacesAjaxLoadingMode() {
    }


    public static LoadingMode getInstance() {
        return loadingMode;
    }

    public void waitForLoad() {
        sleep(4000);
    }
}
