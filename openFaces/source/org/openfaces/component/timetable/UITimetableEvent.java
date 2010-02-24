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
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.ConvertibleToJSON;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class UITimetableEvent extends OUIPanel implements ConvertibleToJSON {
    public static final String COMPONENT_TYPE = "org.openfaces.TimetableEvent";
    public static final String COMPONENT_FAMILY = "org.openfaces.TimetableEvent";

    private Boolean escapeName;
    private Boolean escapeDescription;
    private String nameStyle;
    private String nameClass;
    private String descriptionStyle;
    private String descriptionClass;

    private Double backgroundTransparency;
    private Double backgroundIntensity;

    private String oncreate;

    public UITimetableEvent() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean getEscapeName() {
        return ValueBindings.get(this, "escapeName", escapeName, true);
    }

    public void setEscapeName(boolean escapeName) {
        this.escapeName = escapeName;
    }

    public boolean getEscapeDescription() {
        return ValueBindings.get(this, "escapeDescription", escapeDescription, true);
    }

    public void setEscapeDescription(boolean escapeDescription) {
        this.escapeDescription = escapeDescription;
    }

    public String getNameStyle() {
        return ValueBindings.get(this, "nameStyle", nameStyle);
    }

    public void setNameStyle(String nameStyle) {
        this.nameStyle = nameStyle;
    }

    public String getNameClass() {
        return ValueBindings.get(this, "nameClass", nameClass);
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public String getDescriptionStyle() {
        return ValueBindings.get(this, "descriptionStyle", descriptionStyle);
    }

    public void setDescriptionStyle(String descriptionStyle) {
        this.descriptionStyle = descriptionStyle;
    }

    public String getDescriptionClass() {
        return ValueBindings.get(this, "descriptionClass", descriptionClass);
    }

    public void setDescriptionClass(String descriptionClass) {
        this.descriptionClass = descriptionClass;
    }


    public String getOncreate() {
        return ValueBindings.get(this, "oncreate", oncreate);
    }

    public void setOncreate(String oncreate) {
        this.oncreate = oncreate;
    }

    public double getBackgroundTransparency() {
        return ValueBindings.get(this, "backgroundTransparency", backgroundTransparency, 0.2);
    }

    public void setBackgroundTransparency(double backgroundTransparency) {
        this.backgroundTransparency = backgroundTransparency;
    }

    public double getBackgroundIntensity() {
        return ValueBindings.get(this, "backgroundIntensity", backgroundIntensity, 0.25);
    }

    public void setBackgroundIntensity(double backgroundIntensity) {
        this.backgroundIntensity = backgroundIntensity;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                escapeName,
                escapeDescription,
                nameStyle,
                nameClass,
                descriptionStyle,
                descriptionClass,
                backgroundTransparency,
                backgroundIntensity,
                oncreate};
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        escapeName = (Boolean) state[i++];
        escapeDescription = (Boolean) state[i++];
        nameStyle = (String) state[i++];
        nameClass = (String) state[i++];
        descriptionStyle = (String) state[i++];
        descriptionClass = (String) state[i++];
        backgroundTransparency = (Double) state[i++];
        backgroundIntensity = (Double) state[i++];
        oncreate = (String) state[i++];
    }

    public JSONObject toJSONObject(Map params) throws JSONException {
        FacesContext context = getFacesContext();
        JSONObject obj = new JSONObject();
        Styles.addStyleJsonParam(context, this, obj, "style", getStyle(), getStyleClass());
        Styles.addStyleJsonParam(context, this, obj, "rolloverStyle", getRolloverStyle(), getRolloverClass());

        Styles.addStyleJsonParam(context, this, obj, "nameStyle", getNameStyle(), getNameClass());
        Styles.addStyleJsonParam(context, this, obj, "descriptionStyle", getDescriptionStyle(), getDescriptionClass());
        Rendering.addJsonParam(obj, "escapeName", getEscapeName(), true);
        Rendering.addJsonParam(obj, "escapeDescription", getEscapeDescription(), true);

        Rendering.addJsonParam(obj, "backgroundTransparency", getBackgroundTransparency(), 0.2);
        Rendering.addJsonParam(obj, "backgroundIntensity", getBackgroundIntensity(), 0.25);

        Rendering.addJsonParam(obj, "onclick", getOnclick());
        Rendering.addJsonParam(obj, "ondblclick", getOndblclick());
        Rendering.addJsonParam(obj, "onmousedown", getOnmousedown());
        Rendering.addJsonParam(obj, "onmouseup", getOnmouseup());
        Rendering.addJsonParam(obj, "onmousemove", getOnmousemove());
        Rendering.addJsonParam(obj, "onmouseover", getOnmouseover());
        Rendering.addJsonParam(obj, "onmouseout", getOnmouseout());

        Rendering.addJsonParam(obj, "onkeydown", getOnkeydown());
        Rendering.addJsonParam(obj, "onkeyup", getOnkeyup());
        Rendering.addJsonParam(obj, "onkeypress", getOnkeypress());

        Rendering.addJsonParam(obj, "oncreate", getOncreate());

        try {
            Styles.renderStyleClasses(context, this);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        return obj;
    }
}
