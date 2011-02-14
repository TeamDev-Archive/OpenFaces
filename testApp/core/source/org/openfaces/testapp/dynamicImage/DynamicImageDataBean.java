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
package org.openfaces.testapp.dynamicImage;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicImageDataBean {

    private Logger logger = Logger.getLogger(DynamicImageDataBean.class.getName());

    public DynamicImageDataBean() {
    }

    public byte[] getTestData() {
        InputStream stream = DynamicImageDataBean.class.getResourceAsStream("polarbear.gif");
        byte[] b = new byte[157000];
        try {
            stream.read(b);
        } catch (IOException e) {
            logger.log(Level.INFO, "", e);
        }
        return b;
    }

    public RenderedImage getTestImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawLine(20, 20, 30, 30);
        image.getGraphics().drawLine(30, 20, 20, 30);
        return image;
    }


    public RenderedImage getA4jImage() {
        Random rand = new Random();
        BufferedImage image = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawString(String.valueOf(rand.nextInt(37000)), 30, 75);
        return image;
    }


}