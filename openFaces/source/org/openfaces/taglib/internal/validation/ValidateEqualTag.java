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
package org.openfaces.taglib.internal.validation;

import org.openfaces.validator.AbstractCustomValidator;
import org.openfaces.validator.EqualValidator;

/**
 * @author Pavel Kaplin
 */
public class ValidateEqualTag extends AbstractCustomValidatorTag {
    @Override
    public void setProperties(AbstractCustomValidator abstractCustomValidator) {
        super.setProperties(abstractCustomValidator);
        EqualValidator equalValidator = (EqualValidator) abstractCustomValidator;
        String aFor = getPropertyValue("for");
        if (aFor == null)
            throw new IllegalArgumentException("EqualValidatorError. 'for' can not be null");
        equalValidator.setFor(aFor);
    }
}
