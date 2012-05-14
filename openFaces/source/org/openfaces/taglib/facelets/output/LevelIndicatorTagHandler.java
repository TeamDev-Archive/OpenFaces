/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.facelets.output;

import org.openfaces.taglib.internal.output.LevelIndicatorTag;

import javax.faces.view.facelets.ComponentConfig;

public class LevelIndicatorTagHandler extends org.openfaces.taglib.facelets.AbstractFaceletsComponentHandler {

    public LevelIndicatorTagHandler(ComponentConfig componentConfig) {
        super(componentConfig, new LevelIndicatorTag());
    }

}
