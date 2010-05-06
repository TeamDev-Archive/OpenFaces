/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.jBossSeam.seamJSFComponents;

import static org.jboss.seam.ScopeType.SESSION;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * @author Tatyana Matveyeva
 */
@Stateful
@Scope(SESSION)
@Name("seamtest")
public class SeamTestBean implements SeamTest {

    private boolean rendered = true;
    private Picture picture = new Picture();


    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String hide() {
        rendered = false;
        return null;
    }

    public String show() {
        rendered = true;
        return null;
    }

    @Remove
    @Destroy
    public void destroy() {
    }
}
