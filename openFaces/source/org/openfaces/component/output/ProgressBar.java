/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.output;

import org.openfaces.component.OUIOutput;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class ProgressBar extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.ProgressBar";
    public static final String COMPONENT_FAMILY = "org.openfaces.ProgressBar";

    private static final String DEF_LABEL_FORMAT = "{value} %";

    private String processedStyle;
    private String processedClass;
    private String notProcessedStyle;
    private String notProcessedClass;
    private String labelStyle;
    private String labelClass;
    private String labelFormat;
    private LabelAlignment labelAlignment;
    private String processedImg;
    private String notProcessedImg;
    private boolean isSmallProgressByDefault;

    public ProgressBar() {
        setRendererType("org.openfaces.ProgressBarRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                processedStyle,
                processedClass,
                notProcessedStyle,
                notProcessedClass,
                labelStyle,
                labelClass,
                labelFormat,
                processedImg,
                notProcessedImg,
                isSmallProgressByDefault
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        processedStyle = (String) state[i++];
        processedClass = (String) state[i++];
        notProcessedStyle = (String) state[i++];
        notProcessedClass = (String) state[i++];
        labelStyle = (String) state[i++];
        labelClass = (String) state[i++];
        labelFormat = (String) state[i++];
        processedImg = (String) state[i++];
        notProcessedImg = (String) state[i++];
        isSmallProgressByDefault = (Boolean)state[i++];
    }

    public String getProcessedStyle() {
        return ValueBindings.get(this, "uploadedStyle", processedStyle);
    }

    public void setProcessedStyle(String processedStyle) {
        this.processedStyle = processedStyle;
    }

    public String getProcessedClass() {
        return ValueBindings.get(this, "uploadedClass", processedClass);
    }

    public void setProcessedClass(String processedClass) {
        this.processedClass = processedClass;
    }

    public String getNotProcessedStyle() {
        return ValueBindings.get(this, "notUploadedStyle", notProcessedStyle);
    }

    public void setNotProcessedStyle(String notProcessedStyle) {
        this.notProcessedStyle = notProcessedStyle;
    }

    public String getNotProcessedClass() {
        return ValueBindings.get(this, "notUploadedClass", notProcessedClass);
    }

    public void setNotProcessedClass(String notProcessedClass) {
        this.notProcessedClass = notProcessedClass;
    }

    public String getLabelStyle() {
        return ValueBindings.get(this, "labelStyle", labelStyle);
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getLabelClass() {
        return ValueBindings.get(this, "labelClass", labelClass);
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getLabelFormat() {
        return ValueBindings.get(this, "labelFormat", labelFormat, DEF_LABEL_FORMAT);
    }

    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    public LabelAlignment getLabelAlignment() {
        return ValueBindings.get(this, "labelAlignment", labelAlignment, LabelAlignment.CENTER, LabelAlignment.class);
    }

    public void setLabelAlignment(LabelAlignment labelAlignment) {
        this.labelAlignment = labelAlignment;
    }

    public String getProcessedImg() {
        return ValueBindings.get(this, "uploadedProgressImg", processedImg);
    }

    public void setProcessedImg(String processedImg) {
        this.processedImg = processedImg;
    }

    public String getNotProcessedImg() {
        return ValueBindings.get(this, "notUploadedProgressImg", notProcessedImg);
    }

    public void setNotProcessedImg(String notProcessedImg) {
        this.notProcessedImg = notProcessedImg;
    }

    public boolean isSmallProgressByDefault() {
        return isSmallProgressByDefault;
    }

    public void setSmallProgressByDefault(boolean smallProgressByDefault) {
        isSmallProgressByDefault = smallProgressByDefault;
    }
}
