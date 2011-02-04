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
package org.openfaces.ajax;

import javax.faces.component.UIForm;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public class AjaxSavedStateIdxHolder {
    // Treat state key for R.I.
    // see JSFC-1898 JS error if try to expand nodes in the TreeTable (JSF RI 1.2 and server state saving)
    // compound id for view state (actual for R.I. server side state saving
    private Object viewStructureId;
    //javax.faces.ViewState  - sequence string for MyFaces
    // This case is for MyFaces viewState sequence updating on client-side
    private String viewStateIdentifier;

    private List<UIForm> forms;

    public AjaxSavedStateIdxHolder() {
        forms = new ArrayList<UIForm>();
    }

    public Object getViewStructureId() {
        return viewStructureId;
    }

    public void setViewStructureId(Object viewStructureId) {
        this.viewStructureId = viewStructureId;
    }

    public String getViewStateIdentifier() {
        return viewStateIdentifier;
    }

    public void setViewStateIdentifier(String viewStateIdentifier) {
        this.viewStateIdentifier = viewStateIdentifier;
    }

    public List<UIForm> getForms() {
        return forms;
    }

    public void setForms(List<UIForm> forms) {
        this.forms = forms;
    }
}
