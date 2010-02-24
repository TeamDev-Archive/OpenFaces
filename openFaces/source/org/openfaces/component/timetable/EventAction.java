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

import org.openfaces.util.Rendering;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class EventAction extends UICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.EventAction";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventAction";

    private String style;
    private String styleClass;
    private String rolloverStyle;
    private String rolloverClass;
    private String pressedStyle;
    private String pressedClass;
    private String imageUrl;
    private String rolloverImageUrl;
    private String pressedImageUrl;
    private String onclick;
    private String hint;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                style, styleClass, rolloverStyle, rolloverClass, pressedStyle, pressedClass,
                imageUrl, rolloverImageUrl, pressedImageUrl, onclick, hint
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        style = (String) state[i++];
        styleClass = (String) state[i++];
        rolloverStyle = (String) state[i++];
        rolloverClass = (String) state[i++];
        pressedStyle = (String) state[i++];
        pressedClass = (String) state[i++];
        imageUrl = (String) state[i++];
        rolloverImageUrl = (String) state[i++];
        pressedImageUrl = (String) state[i++];
        onclick = (String) state[i++];
        hint = (String) state[i++];
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getRolloverStyle() {
        return ValueBindings.get(this, "rolloverStyle", rolloverStyle);
    }

    public void setRolloverStyle(String rolloverStyle) {
        this.rolloverStyle = rolloverStyle;
    }

    public String getRolloverClass() {
        return ValueBindings.get(this, "rolloverClass", rolloverClass);
    }

    public void setRolloverClass(String rolloverClass) {
        this.rolloverClass = rolloverClass;
    }

    public String getPressedStyle() {
        return ValueBindings.get(this, "pressedStyle", pressedStyle);
    }

    public void setPressedStyle(String pressedStyle) {
        this.pressedStyle = pressedStyle;
    }

    public String getPressedClass() {
        return ValueBindings.get(this, "pressedClass", pressedClass);
    }

    public void setPressedClass(String pressedClass) {
        this.pressedClass = pressedClass;
    }

    public String getImageUrl() {
        return ValueBindings.get(this, "imageUrl", imageUrl);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRolloverImageUrl() {
        return ValueBindings.get(this, "rolloverImageUrl", rolloverImageUrl);
    }

    public void setRolloverImageUrl(String rolloverImageUrl) {
        this.rolloverImageUrl = rolloverImageUrl;
    }

    public String getPressedImageUrl() {
        return ValueBindings.get(this, "pressedImageUrl", pressedImageUrl);
    }

    public void setPressedImageUrl(String pressedImageUrl) {
        this.pressedImageUrl = pressedImageUrl;
    }

    public String getOnclick() {
        return ValueBindings.get(this, "onclick", onclick);
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getHint() {
        return ValueBindings.get(this, "hint", hint, getDefaultHint());
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public static List<UIComponent> getEventActionComponents(EventActionBar bar) {
        List<UIComponent> result = new ArrayList<UIComponent>();
        List<UIComponent> children = bar.getChildren();
        for (Object aChildren : children) {
            UIComponent component = (UIComponent) aChildren;
            if (component instanceof EventAction && component.isRendered())
                result.add(component);
        }
        return result;
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);

        EventActionBar actionBar = (EventActionBar) getParent();
        int thisIndex = getEventActionComponents(actionBar).indexOf(this);
        if (thisIndex == -1)
            throw new IllegalStateException("Can't find this action in the parent list");
        String actionId = actionBar.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + thisIndex;

        Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();
        String eventId = requestParams.get(actionId);
        if (eventId == null)
            return;
        DayTable dayTable = (DayTable) actionBar.getParent();
        FacesEvent event = createEvent(dayTable, actionBar, eventId);
        queueEvent(event);
    }

    protected FacesEvent createEvent(DayTable dayTable, EventActionBar actionBar, String eventId) {
        return new EventActionEvent(this, eventId);
    }

    protected String getDefaultHint() {
        return null;
    }
}
