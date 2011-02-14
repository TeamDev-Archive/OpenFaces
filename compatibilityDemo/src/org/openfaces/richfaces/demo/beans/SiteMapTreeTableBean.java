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
package org.openfaces.richfaces.demo.beans;

import org.openfaces.util.Faces;

import java.io.Serializable;
import java.util.StringTokenizer;

public class SiteMapTreeTableBean implements Serializable {
    public boolean getDisplayAsLink() {
        return getPageUrlFromPageInfo() != null;
    }

    public String getPageNameFromPageInfo() {
        String pageInfo = (String) Faces.var("pageInfo");
        return (String) new StringTokenizer(pageInfo, "|", false).nextElement();
    }

    public String getPageUrlFromPageInfo() {
        String pageInfo = (String) Faces.var("pageInfo");
        StringTokenizer stringTokenizer = new StringTokenizer(pageInfo, "|", false);
        stringTokenizer.nextElement();
        stringTokenizer.nextElement();
        if (!stringTokenizer.hasMoreElements())
            return null;
        return (String) stringTokenizer.nextElement();
    }

    public String getImageFileFromPageInfo() {
        String pageInfo = (String) Faces.var("pageInfo");
        StringTokenizer stringTokenizer = new StringTokenizer(pageInfo, "|", false);
        stringTokenizer.nextElement();
        if (!stringTokenizer.hasMoreElements())
            return null;
        return (String) stringTokenizer.nextElement();

    }


}
