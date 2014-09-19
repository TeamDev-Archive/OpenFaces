/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.testBeans;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Kharchenko
 */
public class FoldingPanelTestBean {

    Logger logger = Logger.getLogger(FoldingPanelTestBean.class.getName());

    private boolean state = true;
    private String value = "+";
    private String title = "Press to unfold";

    public void testMethod() {
        logger.info(":::: This output was called from getTest bean. ::::");
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void changeState() {
        setState(!isState());
        if (state) {
            value = "+";
            title = "Press to unfold";
        } else {
            value = "-";
            title = "Press to fold";
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getFromDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 2);
        return c.getTime();
    }

    public Date getToDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 17);
        return c.getTime();
    }

    public Date getFromDate2() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        return c.getTime();
    }

    public Date getToDate2() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }
}
