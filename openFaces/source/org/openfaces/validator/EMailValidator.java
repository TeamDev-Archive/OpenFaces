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
package org.openfaces.validator;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class EMailValidator extends AbstractRegexValidator {
    public static final String VALIDATOR_ID = "org.openfaces.EMail";

    private static final String EMAIL_VALIDATOR_MESSAGE_ID = "org.openfaces.EMailValidatorMessage";

    public String getPattern() {
        return "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    }

    @Override
    public String getValidatorMessageID() {
        return EMAIL_VALIDATOR_MESSAGE_ID;
    }

    public String getType() {
        return "email";
    }

    @Override
    protected boolean isTrimNeeded() {
        return true;
    }
}
