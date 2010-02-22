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
package org.openfaces.demo.beans.layeredpane;

import org.openfaces.component.LoadingMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

/**
 * @author Dmitry Pikhulya
 */
public class LayeredPaneBean implements Serializable {
    private LoadingMode loadingMode = LoadingMode.CLIENT;

    private Converter loadingModeConverter = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            for (LoadingMode lm : LoadingMode.values()) {
                String valueAsString = lm.toString();
                if (valueAsString.equals(value))
                    return lm;
            }
            throw new IllegalArgumentException(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return value.toString();
        }
    };


    private List<SelectItem> loadingModeItems = new ArrayList<SelectItem>();

    public LayeredPaneBean() {
        for (LoadingMode mode : Arrays.asList(
                    LoadingMode.CLIENT,
                    LoadingMode.AJAX_LAZY,
                    LoadingMode.AJAX_ALWAYS,
                    LoadingMode.SERVER
            )) {
            loadingModeItems.add(new SelectItem(mode, mode.toString()));
        }
    }

    public LoadingMode getLoadingMode() {
        return loadingMode;
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }

    public List<SelectItem> getLoadingModeItems() {
        return loadingModeItems;
    }

    public Converter getLoadingModeConverter() {
        return loadingModeConverter;
    }
}
