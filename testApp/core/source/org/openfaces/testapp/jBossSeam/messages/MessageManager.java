/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

//$Id: MessageManager.java,v 1.2 2006/05/23 05:30:24 gavin Exp $
package org.openfaces.testapp.jBossSeam.messages;

import javax.ejb.Local;

@Local
public interface MessageManager {

    public void findMessages();

    public void select();

    public void delete();

    public void destroy();
}