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
package org.openfaces.component;

import javax.faces.context.FacesContext;

/**
 * This is an interface for OpenFaces components that need to create other subcomponents for rendering their contents.
 * Application developers should pay attention to this interface when creating components programmatically in backing
 * beans. All components that implement this interface should have their createSubComponents method invoked after the
 * component's properties are configured.
 * <p/>
 * When declaring the components in the usual way (with Facelets/JSP tags) no special actions should be taken since
 * createSubComponents is invoked in component's tag handlers automatically in this case.
 */
public interface CompoundComponent {

    void createSubComponents(FacesContext context);
}
