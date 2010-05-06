/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.dynamicImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Darya Shumilina
 */
public class DynamicImageBean {

    private Logger logger = Logger.getLogger(DynamicImageBean.class.getName());

    private List<DynamicImageItem> dynamicImageList = new ArrayList<DynamicImageItem>();

    public DynamicImageBean() {
        try {
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("watches.jpg")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("albert_fiddle.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("andrew_ambitious.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("ann_adolescent.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("bob_forgetive.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("bushy_toy.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("chris_lee.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("christina_strange.gif")));
            dynamicImageList.add(new DynamicImageItem(getAvailableImage("christine_smile.gif")));
        } catch (IOException e) {
            logger.log(Level.INFO, "", e);
        }
    }

    private byte[] getAvailableImage(String name) throws IOException {
        InputStream stream = DynamicImageDataBean.class.getResourceAsStream(name);
        byte[] b = new byte[stream.available()];
        try {
            stream.read(b);
        } catch (IOException e) {
            logger.log(Level.INFO, "", e);
        } finally {
            stream.close();
        }
        return b;
    }

    public byte[] getTreeImage() throws IOException {
        return getAvailableImage("watches.jpg");
    }

    public byte[] getHeaderImage() throws IOException {
        return getAvailableImage("sky_active.jpg");
    }


    public List<DynamicImageItem> getDynamicImageList() {
        return dynamicImageList;
    }

    public void setDynamicImageList(List<DynamicImageItem> dynamicImageList) {
        this.dynamicImageList = dynamicImageList;
    }
}