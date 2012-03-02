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

package org.openfaces.component.table.export;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.openfaces.component.table.TableDataExtractor.EXTRACTED_DATA_ATTRIBUTE;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class CellComponentsDataExtractor implements CellDataExtractor {

    private List<ComponentDataExtractor> componentExtractors;

    public CellComponentsDataExtractor(List<ComponentDataExtractor> componentExtractors) {
        this.componentExtractors = componentExtractors;
    }

    public boolean isApplicableFor(Object rowData, UIColumn column) {
        return true;
    }

    public Object getData(Object rowData, UIColumn column) {
        List<Object> result = getData(column);
        if (result.isEmpty()) {
            return null;
        } else if (result.size() == 1) {
            return result.get(0);
        }
        return result;
    }

    private List<Object> getData(UIComponent component) {
        if (component.getAttributes().containsKey(EXTRACTED_DATA_ATTRIBUTE)) {
            return asList(component.getAttributes().get(EXTRACTED_DATA_ATTRIBUTE));
        }
        for (ComponentDataExtractor componentExtractor : componentExtractors) {
            if (componentExtractor.isApplicableFor(component)) {
                return asList(componentExtractor.getData(component));
            }
        }
        List<Object> result = new LinkedList<Object>();
        for (UIComponent child : component.getChildren()) {
            result.addAll(getData(child));
        }
        return result;
    }
}
