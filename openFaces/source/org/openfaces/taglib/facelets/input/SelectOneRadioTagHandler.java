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

package org.openfaces.taglib.facelets.input;

import com.sun.facelets.tag.jsf.ComponentConfig;
import org.openfaces.taglib.facelets.AbstractFaceletsComponentHandler;
import org.openfaces.taglib.internal.input.SelectOneRadioTag;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 23, 2009
 * Time: 12:10:11 PM
 */
public class SelectOneRadioTagHandler extends AbstractFaceletsComponentHandler {

    public SelectOneRadioTagHandler(ComponentConfig componentConfig) {
        super(componentConfig, new SelectOneRadioTag());
    }

}
