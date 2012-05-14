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
package org.openfaces.taglib.internal.validation;

import org.openfaces.validator.AbstractCustomValidator;
import org.openfaces.validator.RegexValidator;

/**
 * @author Pavel Kaplin
 */
public class ValidateRegexTag extends AbstractCustomValidatorTag {
    public void setProperties(AbstractCustomValidator validator) {
        super.setProperties(validator);
        String pattern = getPropertyValue("pattern");
        ((RegexValidator) validator).setPattern(pattern);
    }
}
