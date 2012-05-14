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

import org.openfaces.component.output.DynamicImage;
import org.openfaces.renderkit.ImageDataModel;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicImageRenderer extends RendererBase {
    public static final String IMAGE_POOL = "_of_dynamicImagePool";
    private static final String DIMG_EXTENSION = "dimg";
    public static final String DEFAULT_STYLE_ATTR = "_defaultStyle";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        DynamicImage dynamicImage = (DynamicImage) component;
        ValueExpression dataExpression = dynamicImage.getDataExpression();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Object data = dataExpression.getValue(elContext);

        if (data == null)
            return;

        ImageDataModel model = Rendering.getDataModel(data);

        String extension = DIMG_EXTENSION;
        if (dynamicImage.getImageType() != null)
            extension = dynamicImage.getImageType().getExtension();

        ResponseWriter writer = facesContext.getResponseWriter();
        Rendering.startWriteIMG(writer, facesContext, dynamicImage, extension, model, null);

        writeAttribute(writer, "style", dynamicImage.getStyle());
        writeAttribute(writer, "class", Styles.mergeClassNames(dynamicImage.getStyleClass(), (String) dynamicImage.getAttributes().get(DEFAULT_STYLE_ATTR)));
        writeAttribute(writer, "width", dynamicImage.getWidth(), -1);
        writeAttribute(writer, "height", dynamicImage.getHeight(), -1);
        writeAttribute(writer, "alt", dynamicImage.getAlt());

        Rendering.writeStandardEvents(writer, dynamicImage);

        String mapId = dynamicImage.getMapId();
        if (mapId != null)
            writer.writeAttribute("usemap", "#" + mapId, null);

        Rendering.encodeInitComponentCall(facesContext, dynamicImage, true);

        writer.endElement("img");

        if (dynamicImage.getMap() != null) {
            Rendering.writeNewLine(writer);
            writer.write(dynamicImage.getMap());
        }
    }

}
