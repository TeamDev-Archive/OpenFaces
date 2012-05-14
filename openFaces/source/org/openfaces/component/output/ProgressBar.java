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
package org.openfaces.component.output;

import org.openfaces.component.OUIOutput;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class ProgressBar extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.ProgressBar";
    public static final String COMPONENT_FAMILY = "org.openfaces.ProgressBar";

    private static final String DEF_LABEL_FORMAT = "{value} %";

    private String progressStyle;
    private String progressClass;
    private String notProcessedStyle;
    private String notProcessedClass;
    private String labelStyle;
    private String labelClass;
    private String labelFormat;
    private LabelAlignment labelAlignment;
    private String processedImg;
    private String notProcessedImg;
    private String defaultProgressImgUrl;

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
                progressStyle,
                progressClass,
                notProcessedStyle,
                notProcessedClass,
                labelStyle,
                labelClass,
                labelFormat,
                processedImg,
                notProcessedImg,
                defaultProgressImgUrl
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        progressStyle = (String) state[i++];
        progressClass = (String) state[i++];
        notProcessedStyle = (String) state[i++];
        notProcessedClass = (String) state[i++];
        labelStyle = (String) state[i++];
        labelClass = (String) state[i++];
        labelFormat = (String) state[i++];
        processedImg = (String) state[i++];
        notProcessedImg = (String) state[i++];
        defaultProgressImgUrl = (String)state[i++];
    }

    public String getProgressStyle() {
        return ValueBindings.get(this, "uploadedStyle", progressStyle);
    }

    public void setProgressStyle(String progressStyle) {
        this.progressStyle = progressStyle;
    }

    public String getProgressClass() {
        return ValueBindings.get(this, "uploadedClass", progressClass);
    }

    public void setProgressClass(String progressClass) {
        this.progressClass = progressClass;
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

    public String getDefaultProgressImgUrl() {
        return defaultProgressImgUrl;
    }

    public void setDefaultProgressImgUrl(String defaultProgressImgUrl) {
        this.defaultProgressImgUrl = defaultProgressImgUrl;
    }
}
