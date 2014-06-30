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

package org.openfaces.component;

import org.openfaces.taglib.facelets.FaceletsExpressionCreator;
import org.openfaces.taglib.internal.AbstractComponentTag;

/**
 * Created with IntelliJ IDEA.
 * User: dmitry.kashcheiev
 * Date: 12/9/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LazyProcessSupport {

    public void setLazyProcessAttributes(FaceletsExpressionCreator faceletsExpressionCreator, AbstractComponentTag abstractComponentTag);
}
