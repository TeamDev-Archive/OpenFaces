/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table.export;

import javax.faces.component.UIComponent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Roman Perin
 * Date: 12.08.14
 */
public class ExtractionFilter {

    private Set<Class> extractionIgnoredClasses = new HashSet<Class>();

    public ExtractionFilter(Class... extractionIgnoredClasses) {
        this.extractionIgnoredClasses.addAll(Arrays.asList(extractionIgnoredClasses));
    }

    public boolean checkOnUnextractable(UIComponent component){
        if ((component instanceof Unextractable) || extractionIgnoredClasses.contains(component.getClass())){
            return true;
        }
        return false;
    }

}
