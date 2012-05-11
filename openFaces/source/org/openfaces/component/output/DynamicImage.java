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

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * The DynamicImage component provides the ability to display an image which is
 * dynamically generated at run time, or which is not available as an application
 * file but, for example, is retrieved from a database. There are two ways to specify
 * the data model: specifying an image as a byte array or specifying it as an implementation
 * of the java.awt.image.RenderedImage interface.
 *
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicImage extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.DynamicImage";
    public static final String COMPONENT_FAMILY = "org.openfaces.DynamicImage";

    private String mapId;
    private String map;
    private Integer width;
    private Integer height;
    private String alt;

    private ImageType imageType;

    public DynamicImage() {
        setRendererType("org.openfaces.DynamicImageRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getAlt() {
        return ValueBindings.get(this, "alt", alt);
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public int getWidth() {
        return ValueBindings.get(this, "width", width, -1);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return ValueBindings.get(this, "height", height, -1);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMapId() {
        return ValueBindings.get(this, "mapId", mapId);
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMap() {
        return ValueBindings.get(this, "map", map);
    }

    public void setMap(String map) {
        this.map = map;
    }

    public ValueExpression getDataExpression() {
        return getValueExpression("data");
    }

    public void setDataExpression(ValueExpression dataExpression) {
        setValueExpression("data", dataExpression);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);

        return new Object[]{
                superState,
                height,
                width,
                alt,
                imageType,
                map,
                mapId
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        height = (Integer) state[i++];
        width = (Integer) state[i++];
        alt = (String) state[i++];
        imageType = (ImageType) state[i++];
        map = (String) state[i++];
        mapId = (String) state[i++];
    }


}
