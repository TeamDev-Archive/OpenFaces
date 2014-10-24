/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.tomahawk;

import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Thomas Spiegl (latest modification by $Author: mbr $)
 * @version $Revision: 226969 $ $Date: 2005-08-02 08:41:56 +0000 (Tue, 02 Aug 2005) $
 */
public class DataScrollerListBean {
    private List<SimpleCar> list = new ArrayList<SimpleCar>();

    public DataScrollerListBean() {
        for (int i = 1; i < 995; i++) {
            list.add(new SimpleCar(i, "Car Type " + i, "blue"));
        }
    }

    public List<SimpleCar> getList() {
        return list;
    }

    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: "
                        + scrollerEvent.getScrollerfacet()
                        + ", pageindex: "
                        + scrollerEvent.getPageIndex());
    }
}
