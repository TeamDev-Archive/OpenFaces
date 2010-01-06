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
package org.openfaces.taglib.facelets.chart;

import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.tag.TagAttribute;

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
