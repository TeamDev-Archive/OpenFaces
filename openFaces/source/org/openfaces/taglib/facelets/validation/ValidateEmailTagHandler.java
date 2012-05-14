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

import org.openfaces.taglib.facelets.PropertyHandlerMetaRule;
import org.openfaces.taglib.internal.validation.ValidateEmailTag;

import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.ValidatorConfig;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidateEmailTagHandler extends ValidateCustomTagHandler {
    public ValidateEmailTagHandler(TagConfig config) {
        super(config);
        setMetaRule(new PropertyHandlerMetaRule(new ValidateEmailTag()));
    }

    public ValidateEmailTagHandler(ValidatorConfig config) {
        super(config);
        setMetaRule(new PropertyHandlerMetaRule(new ValidateEmailTag()));
    }

    public String getValidatorId() {
        return "org.openfaces.EMail";
    }
}
