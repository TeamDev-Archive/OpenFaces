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
package org.openfaces.taglib.facelets;

import org.openfaces.component.select.TabSelectionHolder;
import org.openfaces.event.SelectionChangeListener;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

/**
 * @author Vladimir Kurganov
 */
public class SelectionChangeListenerTagHandler extends TagHandler {
    private final TagAttribute type;

    public SelectionChangeListenerTagHandler(TagConfig config) {
        super(config);
        type = getRequiredAttribute("type");
    }

    public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException, FacesException, ELException {
        // only process if parent was just created
        if (parent.getParent() != null)
            return;
        if (!(parent instanceof TabSelectionHolder))
            throw new FacesException("Component " + parent.getClass().getName() + " does not support" +
                    " SelectionChange event.");
        String className;
        if (!type.isLiteral()) {
            className = type.getValue();
        } else {
            className = type.getValue(faceletContext);
        }
        SelectionChangeListener listener;
        try {
            listener = (SelectionChangeListener) Class.forName(className).newInstance();
            ((TabSelectionHolder) parent).addSelectionListener(listener);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
