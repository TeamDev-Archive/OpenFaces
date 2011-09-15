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
package org.openfaces.demo.beans.levelindicator;

import javax.faces.event.ActionEvent;
import java.io.Serializable;

public class LevelIndicatorBean implements Serializable {
    private double batteryLevel = 1d;
    private double hddSpace = 1d;

    public LevelIndicatorBean() {
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public double getHddSpace() {
        return hddSpace;
    }

    public void setHddSpace(double hddSpace) {
        this.hddSpace = hddSpace;
    }
}
