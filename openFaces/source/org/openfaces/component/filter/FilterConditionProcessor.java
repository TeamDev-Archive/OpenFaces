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
package org.openfaces.component.filter;

/**
 * @author Dmitry Pikhulya
 */
public abstract class FilterConditionProcessor {
    public abstract Object processEmpty();
    public abstract Object processEquals();
    public abstract Object processContains();
    public abstract Object processBegins();
    public abstract Object processEnds();
    public abstract Object processLess();
    public abstract Object processGreater();
    public abstract Object processLessOrEqual();
    public abstract Object processGreaterOrEqual();
    public abstract Object processBetween();
}
