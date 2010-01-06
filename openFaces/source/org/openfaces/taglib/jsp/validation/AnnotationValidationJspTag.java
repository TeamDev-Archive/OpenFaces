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

package org.openfaces.taglib.jsp.validation;

import org.openfaces.taglib.internal.validation.AnnotationValidationTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;


/**
 * <p><strong>AnnotationValidationJspTag</strong></p> is a JSP tag for {@link UIValidation} JSF component
 *
 * @author Eugene Goncharov
 */
public class AnnotationValidationJspTag extends AbstractComponentJspTag {

    public AnnotationValidationJspTag() {
        super(new AnnotationValidationTag());
    }

}
