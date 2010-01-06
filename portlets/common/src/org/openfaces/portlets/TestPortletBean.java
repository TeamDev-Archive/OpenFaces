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
package org.openfaces.portlets;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Calendar;
import java.util.Date;

public class TestPortletBean {

  public Date getFromDate() {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, -11);
    return c.getTime();
  }

  public Date getToDate() {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, 4);
    return c.getTime();
  }

  public RenderedImage getTestImage() {
    BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
    image.getGraphics().drawLine(20, 20, 30, 30);
    image.getGraphics().drawLine(30, 20, 20, 30);
    return image;
  }
}
