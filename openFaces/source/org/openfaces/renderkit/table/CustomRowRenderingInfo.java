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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.Row;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class CustomRowRenderingInfo implements Serializable {
    private CustomCellRenderingInfo[] customCellInfos;
    private List<Integer> a4jEnabledRowDeclarationIndexes;

    public CustomRowRenderingInfo(int renderedColumnCount) {
        customCellInfos = new CustomCellRenderingInfo[renderedColumnCount];
    }

    public void setA4jEnabledRowDeclarationIndexes(List<Integer> a4jEnabledRowDeclarationIndexes) {
        this.a4jEnabledRowDeclarationIndexes = a4jEnabledRowDeclarationIndexes;
    }

    public void setCustomCellRenderingInfo(int colIndex, CustomCellRenderingInfo customCellRenderingInfo) {
        customCellInfos[colIndex] = customCellRenderingInfo;
    }

    public CustomCellRenderingInfo getCustomCellRenderingInfo(int colIndex) {
        return customCellInfos[colIndex];
    }

    public List<UIComponent> getA4jSupportComponentsForThisRow(List customRows) {
        if (a4jEnabledRowDeclarationIndexes == null)
            return Collections.emptyList();
        List<UIComponent> result = new ArrayList<UIComponent>();
        for (Integer index : a4jEnabledRowDeclarationIndexes) {
            Row tableRow = (Row) customRows.get(index);
            UIComponent a4jSupport = Rendering.getA4jSupportForComponent(tableRow);
            result.add(a4jSupport);
        }

        return result;
    }


}
