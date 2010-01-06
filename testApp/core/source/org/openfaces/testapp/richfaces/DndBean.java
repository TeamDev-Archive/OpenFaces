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

package org.openfaces.testapp.richfaces;

import org.richfaces.event.DropEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class DndBean {

    private List<Framework> frameworks = new ArrayList<Framework>();
    private List<Framework> containerPHP = new ArrayList<Framework>();

    public DndBean() {
        frameworks.add(new Framework("Flexible Ajax", "PHP"));
        frameworks.add(new Framework("Symfony", "PHP"));
        frameworks.add(new Framework("AjaxAC", "PHP"));
        frameworks.add(new Framework("AJAX AGENT", "PHP"));
        frameworks.add(new Framework("PAJAJ", "PHP"));

    }

    public List<Framework> getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(List<Framework> frameworks) {
        this.frameworks = frameworks;
    }

    public void processDrop(DropEvent arg0) {
        frameworks.remove(arg0.getDragValue());
        containerPHP.add((Framework) arg0.getDragValue());
    }

    public List<Framework> getContainerPHP() {
        return containerPHP;
    }

    public void setContainerPHP(List<Framework> containerPHP) {
        this.containerPHP = containerPHP;
    }
}