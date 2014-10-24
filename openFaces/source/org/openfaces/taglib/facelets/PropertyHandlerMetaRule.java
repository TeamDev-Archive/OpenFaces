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
package org.openfaces.taglib.facelets;

import org.openfaces.taglib.internal.AbstractTag;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PropertyHandlerMetaRule extends MetaRule {
    private AbstractTag tag;

    public PropertyHandlerMetaRule(AbstractTag tag) {
        if (tag == null)
            throw new IllegalArgumentException("tag shouldn't be null");
        this.tag = tag;
    }

    public AbstractTag getTag() {
        return tag;
    }

    public Metadata applyRule(String string, final TagAttribute tagAttribute, MetadataTarget metadataTarget) {
        return new Metadata() {
            public void applyMetadata(FaceletContext faceletContext, Object object) {
                String attributeName = tagAttribute.getLocalName();
                String value = tagAttribute.getValue();
                tag.setPropertyValue(attributeName, value);
            }
        };
    }
}
