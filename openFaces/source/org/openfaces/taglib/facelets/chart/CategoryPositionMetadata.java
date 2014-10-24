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
package org.openfaces.taglib.facelets.chart;

import org.openfaces.component.chart.CategoryAxisLabelPosition;
import org.openfaces.util.Enumerations;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author Ekaterina Shliakhovetskaya
 */
public class CategoryPositionMetadata extends Metadata { // todo: use common internal tag handlers and remove these classes
    private final Method method;
    private final TagAttribute attribute;

    public void applyMetadata(FaceletContext ctx, Object instance) {

        CategoryAxisLabelPosition position = Enumerations.valueByString(
                CategoryAxisLabelPosition.class, attribute.getValue(), CategoryAxisLabelPosition.STANDARD, "position");
        try {
            method.invoke(instance, new Object[]{position});
        }
        catch (InvocationTargetException e) {
            throw new TagAttributeException(attribute, e.getCause());
        }
        catch (Exception e) {
            throw new TagAttributeException(attribute, e);
        }
    }

    public CategoryPositionMetadata(Method method, TagAttribute attribute) {
        this.method = method;
        this.attribute = attribute;
    }
}

