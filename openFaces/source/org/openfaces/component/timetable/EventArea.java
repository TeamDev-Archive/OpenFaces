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
package org.openfaces.component.timetable;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.OUIPanel;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventArea extends OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.EventArea";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventArea";

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    public EventArea() {
        setRendererType("org.openfaces.EventAreaRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return ValueBindings.get(
                this, "horizontalAlignment", horizontalAlignment, HorizontalAlignment.LEFT, HorizontalAlignment.class);
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return ValueBindings.get(
                this, "verticalAlignment", verticalAlignment, VerticalAlignment.TOP, VerticalAlignment.class);
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                horizontalAlignment,
                verticalAlignment
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;

        int i = 0;
        super.restoreState(context, state[i++]);
        horizontalAlignment = (HorizontalAlignment) state[i++];
        verticalAlignment = (VerticalAlignment) state[i++];
    }

    private AbstractTimetableEvent getEvent() {
        UIComponent parent = getParent();
        if (parent instanceof TimetableView) {
            return ((TimetableView) parent).getEvent();
        }
        throw new IllegalStateException("EventArea parent is not a TimetableView");
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (!(getEvent() instanceof TimetableEvent))
            return;
        super.processDecodes(context);
    }

    @Override
    public void processValidators(FacesContext context) {
        if (!(getEvent() instanceof TimetableEvent))
            return;
        super.processValidators(context);
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (!(getEvent() instanceof TimetableEvent))
            return;
        super.processUpdates(context);
    }

}
