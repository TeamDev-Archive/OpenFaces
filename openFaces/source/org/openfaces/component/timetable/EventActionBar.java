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
package org.openfaces.component.timetable;

import org.openfaces.component.OUIPanel;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class EventActionBar extends OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.EventActionBar";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventActionBar";

    private String noteText;
    private Double backgroundIntensity;
    private Double actionRolloverBackgroundIntensity;
    private Double actionPressedBackgroundIntensity;


    public EventActionBar() {
        setRendererType("org.openfaces.EventActionBarRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                noteText,
                backgroundIntensity, actionRolloverBackgroundIntensity, actionPressedBackgroundIntensity
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        noteText = (String) state[i++];
        backgroundIntensity = (Double) state[i++];
        actionRolloverBackgroundIntensity = (Double) state[i++];
        actionPressedBackgroundIntensity = (Double) state[i++];
    }

    public String getNoteText() {
        return ValueBindings.get(this, "noteText", noteText, "Click to edit");
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public double getBackgroundIntensity() {
        return ValueBindings.get(this, "backgroundIntensity", backgroundIntensity, 0.5);
    }

    public void setBackgroundIntensity(double backgroundIntensity) {
        this.backgroundIntensity = backgroundIntensity;
    }

    public double getActionRolloverBackgroundIntensity() {
        return ValueBindings.get(this, "actionRolloverBackgroundIntensity", actionRolloverBackgroundIntensity, 0.75);
    }

    public void setActionRolloverBackgroundIntensity(double actionRolloverBackgroundIntensity) {
        this.actionRolloverBackgroundIntensity = actionRolloverBackgroundIntensity;
    }

    public double getActionPressedBackgroundIntensity() {
        return ValueBindings.get(this, "actionPressedBackgroundIntensity", actionPressedBackgroundIntensity, 1.0);
    }

    public void setActionPressedBackgroundIntensity(double actionPressedBackgroundIntensity) {
        this.actionPressedBackgroundIntensity = actionPressedBackgroundIntensity;
    }

    @Override
    public void setId(String id) {
        super.setId("_eventActionBar");  //this component should be only the one at DayTable. See JSFC-3916
    }
}
