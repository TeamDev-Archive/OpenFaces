/*
<<<<<<< HEAD:openFaces/source/org/openfaces/component/ComponentWithExternalParts.java
 * OpenFaces - JSF Component Library 3.0
=======
 * OpenFaces - JSF Component Library 2.0
>>>>>>> master:openFaces/source/org/openfaces/ajax/WrappedAjaxRoot.java
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component;

import java.util.Collection;

/**
 * @author Dmitry Pikhulya
 */
public interface ComponentWithExternalParts {
    public Collection<String> getExternalPartIds();
}
