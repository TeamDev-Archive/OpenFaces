/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.support.QKS1152;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Tatyana Matveyeva
 */
public class QKS1152 {

    Logger logger = Logger.getLogger(QKS1152.class.getName());

    private List<DataTableItem> collection = new ArrayList<DataTableItem>();


    public QKS1152() {
        collection.add(new DataTableItem("Item1", 1));
        collection.add(new DataTableItem("Item2", 2));
        collection.add(new DataTableItem("Item3", 3));
        collection.add(new DataTableItem("Item4", 4));
        collection.add(new DataTableItem("Item5", 5));
        collection.add(new DataTableItem("Item6", 6));
        collection.add(new DataTableItem("Item7", 7));
    }


    public List<DataTableItem> getCollection() {
        return collection;
    }

    public void setCollection(List<DataTableItem> collection) {
        this.collection = collection;
    }

    public String startNewApplication() {
        logger.info("------------------application stated------------------");
        return null;
    }
}
