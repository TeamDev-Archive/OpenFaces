/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.util;

import org.openfaces.renderkit.DefaultImageDataModel;
import org.openfaces.renderkit.ImageDataModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicImagePool implements Serializable {

    private Map<String, ImageDataModel> pool = new Hashtable<String, ImageDataModel>();
    private transient Random RANDOM = new Random();

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        RANDOM = new Random();
    }

    public String putModel(ImageDataModel model) {
        long id = Math.abs(RANDOM.nextLong());

        if (pool.isEmpty() || !(model instanceof DefaultImageDataModel)) {
            pool.put(String.valueOf(id), model);
            return String.valueOf(id);
        }

        String storedObjectId = getStoredObjectId((DefaultImageDataModel) model);
        if (storedObjectId == null) {
            pool.put(String.valueOf(id), model);
            return String.valueOf(id);
        } else {
            return String.valueOf(storedObjectId);
        }
    }

    private String getStoredObjectId(DefaultImageDataModel model) {
        if (pool.values().contains(model)) {
            for (Map.Entry<String, ImageDataModel> stringImageDataModelEntry : pool.entrySet()) {
                ImageDataModel storedModel = stringImageDataModelEntry.getValue();
                if (storedModel.equals(model)) {
                    return stringImageDataModelEntry.getKey();
                }
            }
        }

        return null;
    }

    public byte[] getImage(String sid) {
        if (sid == null) {
            throw new IllegalArgumentException("sid shouldn't be null");
        }
        ImageDataModel model = pool.get(sid);
        if (model == null) {
            return null;
        }
        return model.getData();
    }
}