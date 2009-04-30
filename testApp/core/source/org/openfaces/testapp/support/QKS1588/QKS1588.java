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

package org.openfaces.testapp.support.QKS1588;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS1588 {
    private List<QKS1588TableItem> data = new ArrayList<QKS1588TableItem>();


    public QKS1588() {
        for (int i = 0; i < 100; i++) {
            data.add(new QKS1588TableItem("HintLabel component test column 1 value" + i, "HintLabel component test column 2 value" + i, "HintLabel component test column 3 value" + i, "HintLabel component test column 4 value" + i, "HintLabel component test column 5 value" + i));
        }
    }


    public List<QKS1588TableItem> getData() {
        return data;
    }

    public void setData(List<QKS1588TableItem> data) {
        this.data = data;
    }
}
