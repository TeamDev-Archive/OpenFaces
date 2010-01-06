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
package org.openfaces.taglib.facelets;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import org.openfaces.component.panel.FoldingPanel;
import org.openfaces.event.StateChangeListener;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * @author Vladimir Kurganov
 */
public class StateChangeListenerTagHandler extends TagHandler {
    private final TagAttribute type;

    public StateChangeListenerTagHandler(TagConfig componentConfig) {
        super(componentConfig);
        type = getRequiredAttribute("type");
    }

    public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException, FacesException, ELException {
        // only process if parent was just created
        if (parent.getParent() != null)
            return;
        if (!(parent instanceof FoldingPanel))
            throw new FacesException("Component " + parent.getClass().getName() + " does not support" +
                    " StateChange event.");
        String className;
        if (!type.isLiteral()) {
            className = type.getValue();
        } else {
            className = type.getValue(faceletContext);
        }
        StateChangeListener listener;
        try {
            listener = (StateChangeListener) Class.forName(className).newInstance();
            ((FoldingPanel) parent).addStateChangeListener(listener);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
