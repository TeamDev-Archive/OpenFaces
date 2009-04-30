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

package org.openfaces.testapp.richfaces;

import org.openfaces.component.LoadingMode;

/**
 * @author Tatyana Matveyeva
 */
public class A4jParametersBean {

    private boolean pollEnabled = false;
    private boolean useAjax = false;
    private LoadingMode loadingMode = LoadingMode.SERVER;

    public String defineAjaxMode() {
        useAjax = true;
        loadingMode = LoadingMode.AJAX;
        return null;
    }

    public String defineServerMode() {
        useAjax = false;
        loadingMode = LoadingMode.SERVER;
        return null;
    }

    public String defineClientMode() {
        useAjax = true;
        loadingMode = LoadingMode.CLIENT;
        return null;
    }

    public boolean isUseAjax() {
        return useAjax;
    }

    public void setUseAjax(boolean useAjax) {
        this.useAjax = useAjax;
    }

    public LoadingMode getLoadingMode() {
        return loadingMode;
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }

    public String startPolling() {
        pollEnabled = true;
        return null;
    }

    public String stopPolling() {
        pollEnabled = false;
        return null;
    }
}
