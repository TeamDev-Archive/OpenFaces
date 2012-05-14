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

import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import java.lang.reflect.Method;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartDomainPropertyTagRule extends MetaRule {

    public static final MetaRule Instance = new ChartDomainPropertyTagRule();

    public Metadata applyRule(String name, TagAttribute tagAttribute, MetadataTarget meta) {
        if (name.equals("domain") && tagAttribute.isLiteral()) {

            Method m = meta.getWriteMethod("domain");
            if (m != null)
                return new ChartDomainPropertyMetadata(m, tagAttribute);
        }
        return null;
    }
}
