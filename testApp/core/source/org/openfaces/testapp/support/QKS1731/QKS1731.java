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

package org.openfaces.testapp.support.QKS1731;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */

public class QKS1731 {
    private String selectedContextNavigation;
    private List<String> contextNavigation = new ArrayList<String>();
    private boolean rendered = false;


    public QKS1731() {
        contextNavigation.add("item1");
        contextNavigation.add("item2");
        contextNavigation.add("item3");
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String getSelectedContextNavigation() {
        return selectedContextNavigation;
    }

    public void setSelectedContextNavigation(String selectedContextNavigation) {
        this.selectedContextNavigation = selectedContextNavigation;
    }

    public List<String> getContextNavigation() {
        return contextNavigation;
    }

    public void setContextNavigation(List<String> contextNavigation) {
        this.contextNavigation = contextNavigation;
    }

    public String makeRendered() {
        rendered = true;
        return null;
    }

    public String hide() {
        rendered = false;
        return null;
    }


    public List getColOrder() {
        return Arrays.asList("col1");
    }

}