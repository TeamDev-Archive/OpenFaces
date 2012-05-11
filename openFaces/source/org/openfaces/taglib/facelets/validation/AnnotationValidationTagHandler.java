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

package org.openfaces.taglib.facelets.validation;


import org.openfaces.taglib.facelets.AbstractFaceletsComponentHandler;
import org.openfaces.taglib.internal.validation.AnnotationValidationTag;

import javax.faces.view.facelets.ComponentConfig;

/**
 * <p><strong>AnnotationValidationTagHandler</strong></p> is a Facelets tag handler for
 * {@link org.openfaces.component.validation.UIValidation} JSF component.
 *
 * @author Eugene Goncharov
 */
public class AnnotationValidationTagHandler extends
        AbstractFaceletsComponentHandler {

    public AnnotationValidationTagHandler(ComponentConfig componentConfig) {
        super(componentConfig, new AnnotationValidationTag());
    }

}
