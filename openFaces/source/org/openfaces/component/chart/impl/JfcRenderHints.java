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
package org.openfaces.component.chart.impl;

import org.jfree.chart.ChartRenderingInfo;
import org.openfaces.component.chart.Chart;

import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class JfcRenderHints implements Serializable {
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 8100508075695195293L;

    private String map;
    private ChartRenderingInfo renderingInfo;
    private ModelInfo modelInfo;

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public ChartRenderingInfo getRenderingInfo() {
        return renderingInfo;
    }

    public void setRenderingInfo(ChartRenderingInfo renderingInfo) {
        this.renderingInfo = renderingInfo;
    }

    public String getMapId(Chart chart) {
        return chart.getClientId(FacesContext.getCurrentInstance()) + "_map";
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }


}
