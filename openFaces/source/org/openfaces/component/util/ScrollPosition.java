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
package org.openfaces.component.util;

import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * The ScrollPosition component is a non-visual component that controls the scroll
 * position of the page or an associated component. By using it, you can specify the
 * position of the scroll when the page is loaded and save the scroll position between
 * page submissions.
 *
 * @author Dmitry Pikhulya
 */
public class ScrollPosition extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.ScrollPosition";
    public static final String COMPONENT_FAMILY = "org.openfaces.ScrollPosition";

    private Point value;
    private Boolean autoSaveScrollPos;
    private String _for;

    public ScrollPosition() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Point getValue() {
        return ValueBindings.get(this, "value", value, new Point(), Point.class);
    }

    public void setValue(Point value) {
        this.value = value;
    }

    public boolean getAutoSaveScrollPos() {
        return ValueBindings.get(this, "autoSaveScrollPos", autoSaveScrollPos, true);
    }

    public void setAutoSaveScrollPos(boolean autoSaveScrollPos) {
        this.autoSaveScrollPos = autoSaveScrollPos;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String aFor) {
        _for = aFor;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        Point pos = getValue();
        int scrollX = pos.x;
        int scrollY = pos.y;
        String scrollPosStr = formatPoint(scrollX, scrollY);

        ResponseWriter writer = context.getResponseWriter();

        String clientId = getClientId(context);
        Rendering.renderHiddenField(writer, clientId, scrollPosStr);

        String scrollableComponentId = Components.referenceIdToClientId(context, this, getFor());

        ScriptBuilder buf = new ScriptBuilder();
        buf.initScript(context, this, "O$.initScrollPosition",
                getAutoSaveScrollPos(),
                scrollableComponentId);

        Rendering.renderInitScript(context, buf, Resources.getUtilJsURL(context));
    }

    private String formatPoint(int scrollX, int scrollY) {
        return "[" + scrollX + "," + scrollY + "]";
    }

    private Point parsePoint(String scrollPosStr) {
        if (scrollPosStr == null || scrollPosStr.length() == 0)
            return null;
        if (!scrollPosStr.startsWith("[") || !scrollPosStr.endsWith("]"))
            throw new IllegalArgumentException("Improperly formatted scrollPosStr: " + scrollPosStr);
        scrollPosStr = scrollPosStr.substring(1, scrollPosStr.length() - 1);
        return parsePointFromTwoComponents(scrollPosStr);
    }

    private Point parsePointFromTwoComponents(String scrollPosStr) {
        StringTokenizer stringTokenizer = new StringTokenizer(scrollPosStr, ",", false);
        if (!stringTokenizer.hasMoreTokens())
            throw new IllegalArgumentException("Improperly formatted scrollPosStr. Couldn't find x coordinate: " + scrollPosStr);
        String xStr = stringTokenizer.nextToken();
        if (!stringTokenizer.hasMoreTokens())
            throw new IllegalArgumentException("Improperly formatted scrollPosStr. Couldn't find y coordinate: " + scrollPosStr);
        String yStr = stringTokenizer.nextToken();
        if (stringTokenizer.hasMoreTokens())
            throw new IllegalArgumentException("Improperly formatted scrollPosStr. There's more than two coordinate components: " + scrollPosStr);
        int x = Integer.parseInt(xStr.trim());
        int y = Integer.parseInt(yStr.trim());
        return new Point(x, y);
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        if (!getAutoSaveScrollPos())
            return;
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String scrollPosStr = (String) requestMap.get(getClientId(context));
        value = parsePoint(scrollPosStr);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, value, autoSaveScrollPos, _for};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        value = (Point) stateArray[i++];
        autoSaveScrollPos = (Boolean) stateArray[i++];
        _for = (String) stateArray[i++];
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (value != null && ValueBindings.set(this, "value", value))
            value = null;
    }

}
