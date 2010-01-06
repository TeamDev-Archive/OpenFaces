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
package org.openfaces.component.table;

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.awt.*;

/**
 * @author Dmitry Pikhulya
 */
public class Scrolling extends UIComponentBase implements ComponentConfigurator {
    public static final String COMPONENT_TYPE = "org.openfaces.Scrolling";
    public static final String COMPONENT_FAMILY = "org.openfaces.Scrolling";

    private Boolean vertical;
    private Boolean horizontal;
    private Point position;

    public Scrolling() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[] {
                super.saveState(context),
                vertical,
                horizontal,
                position
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        vertical = (Boolean) state[i++];
        horizontal = (Boolean) state[i++];
        position = (Point) state[i++];
    }

    public boolean isVertical() {
        return ValueBindings.get(this, "vertical", vertical, true);
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isHorizontal() {
        return ValueBindings.get(this, "horizontal", horizontal, false);
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public Point getPosition() {
        return ValueBindings.get(this, "position", position, new Point(), Point.class);
    }

    public void setPosition(Point position) {
        this.position = position;
    }


    public UIComponent getConfiguredComponent() {
        return getTable();
    }

    private AbstractTable getTable() {
        return (AbstractTable) getParent();
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        String key = getTable().getClientId(context) + "::scrollPos";
        String scrolling = context.getExternalContext().getRequestParameterMap().get(key);
        if (scrolling == null)
            position = new Point();
        else {
            int x, y;
            try {
                JSONArray arr = new JSONArray(scrolling);
                x = arr.getInt(0);
                y = arr.getInt(1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            position = new Point(x, y);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (position != null && ValueBindings.set(this, "position", position))
            position = null;
    }


}
