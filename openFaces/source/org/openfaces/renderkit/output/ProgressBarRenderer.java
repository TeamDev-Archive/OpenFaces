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
package org.openfaces.renderkit.output;

import org.openfaces.component.output.ProgressBar;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ProgressBarRenderer extends RendererBase {
    public static final String DEF_PROGRESS_CLASS = "o_progress_bar_uploaded";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ProgressBar progressBar = (ProgressBar) component;
        ResponseWriter writer = context.getResponseWriter();
        final String clientId = progressBar.getClientId(context);
        writer.startElement("div", progressBar);
        Rendering.writeIdAttribute(context, progressBar);
        Rendering.writeStyleAndClassAttributes(writer, progressBar.getStyle(), progressBar.getStyleClass(), "o_progress_bar");

        //Rendering.writeStandardEvents(writer, progressBar);
        writeUploadedDiv(context, progressBar, writer);

        writeNotUploadedDiv(context, progressBar, writer);

        writeLabel(context, progressBar, writer);

        encodeScriptAndStyles(context, progressBar, clientId);
        writer.endElement("div");

    }

    private void writeUploadedDiv(FacesContext context, ProgressBar progressBar, ResponseWriter writer) throws IOException {
        writer.startElement("div", progressBar);
        String uploadedClass = Styles.getCSSClass(context, progressBar, progressBar.getProgressStyle(), StyleGroup.regularStyleGroup(), progressBar.getProgressClass(), DEF_PROGRESS_CLASS);
        writer.writeAttribute("class", uploadedClass, null);
        writer.endElement("div");
    }

    private void writeNotUploadedDiv(FacesContext context, ProgressBar progressBar, ResponseWriter writer) throws IOException {
        writer.startElement("div", progressBar);
        String notUploadedClass = Styles.getCSSClass(context, progressBar, progressBar.getNotProcessedStyle(), StyleGroup.regularStyleGroup(), progressBar.getNotProcessedClass(), "o_progress_bar_n_uploaded");
        writer.writeAttribute("class", notUploadedClass, null);
        writer.endElement("div");
    }

    private void writeLabel(FacesContext context, ProgressBar progressBar, ResponseWriter writer) throws IOException {
        writer.startElement("div", progressBar);
        String labelClass = Styles.getCSSClass(context, progressBar, progressBar.getLabelStyle(), StyleGroup.regularStyleGroup(), progressBar.getLabelClass(), "o_progress_bar_label");
        writer.writeAttribute("class", labelClass, null);
        writer.endElement("div");
    }

    private void encodeScriptAndStyles(FacesContext context, ProgressBar progressBar, String clientId) throws IOException {
        Styles.renderStyleClasses(context, progressBar);

        List<String> listOfImages = new LinkedList<String>();

        String defaultProgressImgUrl = Resources.getURL(context, null, progressBar.getDefaultProgressImgUrl());
        listOfImages.add(defaultProgressImgUrl);

        Rendering.renderPreloadImagesScript(context, listOfImages, false);

        Script initScript = new ScriptBuilder().initScript(context, progressBar, "O$.ProgressBar._init",
                progressBar.getValue(),
                progressBar.getLabelAlignment(),
                progressBar.getLabelFormat(),
                defaultProgressImgUrl,
                DEF_PROGRESS_CLASS
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "output/progressBar.js")
        );
    }


}
