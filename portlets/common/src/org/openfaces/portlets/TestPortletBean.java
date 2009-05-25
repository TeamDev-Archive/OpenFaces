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
