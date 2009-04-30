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
package org.openfaces.demo.beans.dynamicimage;

import org.openfaces.component.select.TabSetItem;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class DynamicImageByteArrayBean implements Serializable {
    private ImageData currentImageData;
    private List<TabSetItem> imageDatas = new ArrayList<TabSetItem>();
    private int selectedImageIndex = 0;

    public DynamicImageByteArrayBean() {
        List<ImageData> images = imageDatas();
        for (ImageData imageItem : images) {
            TabSetItem tabSetItem = new TabSetItem();
            HtmlOutputText component = (HtmlOutputText) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
            component.setValue(imageItem.toString());
            tabSetItem.getChildren().add(component);
            tabSetItem.setItemValue(imageItem);
            imageDatas.add(tabSetItem);
        }
        currentImageData = images.get(0);
    }

    private static List<ImageData> imageDatas() {
        List<ImageData> tempImageData = new ArrayList<ImageData>();
        tempImageData.add(new ImageData("City flowers", "cityFlowers.jpg"));
        tempImageData.add(new ImageData("Tree stump", "bole.jpg"));
        tempImageData.add(new ImageData("Countryside", "meadow.jpg"));
        tempImageData.add(new ImageData("Tree on the meadow", "grassAndTheTree.jpg"));
        tempImageData.add(new ImageData("Poppy in blossom", "bigFlower.jpg"));
        tempImageData.add(new ImageData("Sandy lane", "pathway.jpg"));
        return tempImageData;
    }

    public byte[] getImageBytes() {
        InputStream stream = DynamicImageBean.class.getResourceAsStream("images/" + currentImageData.getFileName());
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[100000];
        try {
            int bytesRead;
            do {
                bytesRead = stream.read(buf);
                if (bytesRead != -1)
                    arrayOutputStream.write(buf, 0, bytesRead);
            } while (bytesRead != -1);
            arrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arrayOutputStream.toByteArray();
    }

    public int getSelectedImageIndex() {
        return selectedImageIndex;
    }

    public void setSelectedImageIndex(int selectedImageIndex) {
        this.selectedImageIndex = selectedImageIndex;
    }

    public ImageData getCurrentImageData() {
        return currentImageData;
    }

    public void setCurrentImageData(ImageData currentImageData) {
        this.currentImageData = currentImageData;
    }

    public List<TabSetItem> getImageDatas() {
        return imageDatas;
    }

    public void setImageDatas(List<TabSetItem> imageDatas) {
        this.imageDatas = imageDatas;
    }

    private static class ImageData implements Serializable {
        private String imageName;
        private String imageFileName;

        public ImageData(String imageName, String imageFileName) {
            this.imageName = imageName;
            this.imageFileName = imageFileName;
        }

        public String getImageName() {
            return imageName;
        }

        public String getFileName() {
            return imageFileName;
        }

        public String toString() {
            return getImageName();
        }
    }
}