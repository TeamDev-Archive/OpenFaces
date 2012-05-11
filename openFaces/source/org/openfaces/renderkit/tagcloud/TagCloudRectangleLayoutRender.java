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

package org.openfaces.renderkit.tagcloud;

import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.component.tagcloud.TagCloudItem;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudRectangleLayoutRender extends AbstractTagCloudLayoutRender {

    @Override
    protected void renderItems(FacesContext context, TagCloud cloud, List<TagCloudItem> items)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (!items.isEmpty()) {
            for (TagCloudItem item : items) {                                
                item.encodeBegin(context);
                item.encodeEnd(context);
            }
        } else {
            writer.writeText("EMPTY RECTANGLE TAG CLOUD", null);
        }

    }
}
