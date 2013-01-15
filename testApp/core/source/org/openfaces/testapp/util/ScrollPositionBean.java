/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.util;

import java.awt.*;

/**
 * @author Dmitry Pikhulya
 */
public class ScrollPositionBean {
    private Point testPos1 = new Point(0, 350);
    private Point testPos2 = new Point(700, 600);

    public Point getTestPos1() {
        return testPos1;
    }

    public void setTestPos1(Point testPos1) {
        this.testPos1 = testPos1;
    }

    public Point getTestPos2() {
        return testPos2;
    }

    public void setTestPos2(Point testPos2) {
        this.testPos2 = testPos2;
    }
}
