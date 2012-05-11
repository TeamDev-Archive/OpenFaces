/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.foldingpanel;

import org.openfaces.demo.beans.treetable.ForumTreeTableBean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FoldingPanelBean implements Serializable {

    private static List PHOTO_LIST = Arrays.asList(
            new Photo("Poppy in blossom", "William Green", ForumTreeTableBean.createDate(2005, 4, 11, 14, 25), "bigFlower.jpg"),
            new Photo("Tree stump", "Jane White", ForumTreeTableBean.createDate(2005, 5, 9, 17, 44), "bole.jpg"),
            new Photo("City flowers", "William Green", ForumTreeTableBean.createDate(2005, 8, 28, 11, 52), "cityFlowers.jpg"),
            new Photo("Tree on the meadow", "Jane White", ForumTreeTableBean.createDate(2005, 7, 9, 15, 20), "grassAndTheTree.jpg"),
            new Photo("Countryside", "William Green", ForumTreeTableBean.createDate(2005, 9, 4, 12, 35), "meadow.jpg"),
            new Photo("Sandy lane", "Christian Smile", ForumTreeTableBean.createDate(2005, 6, 25, 14, 17), "pathway.jpg"));

    public FoldingPanelBean() {
    }

    public List getPhotos() {
        return PHOTO_LIST;
    }

    public String getAmp() {
        return "&amp;";
    }
}
