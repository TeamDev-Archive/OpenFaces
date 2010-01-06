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
package org.openfaces.taglib.facelets.validation;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import java.io.IOException;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class MessageParameterTagHandler extends TagHandler {

    private String value;

    public MessageParameterTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        value = getAttribute("value").getValue();
    }

    public void apply(FaceletContext context, UIComponent uiComponent) throws IOException, FacesException, ELException {
        if (!(uiComponent instanceof ValueHolder))
            return;
        ValueHolder fake = (ValueHolder) uiComponent;
        fake.setValue(value);
    }
}
