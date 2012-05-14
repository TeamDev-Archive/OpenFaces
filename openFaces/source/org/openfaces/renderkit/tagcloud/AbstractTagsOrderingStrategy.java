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

package org.openfaces.renderkit.tagcloud;

import org.openfaces.component.tagcloud.TagCloudItem;
import org.openfaces.component.tagcloud.TagsOrder;

import javax.faces.FacesException;
import java.util.List;

/**
 * @author : roman.nikolaienko
 */
public abstract class AbstractTagsOrderingStrategy implements TagsOrderingStrategy{
    
    public abstract void order(List<TagCloudItem> items);

    public static TagsOrderingStrategy getInstance(TagsOrder order) {
        switch (order) {
            case ALPHABETICALLY:
                return new TagsAlphabeticallyOrdering();
            case ORIGINAL:
                return new TagsOriginalOrdering();
            case RANDOM:
                return new TagsRandomOrdering();
            case WEIGHT:
                return new TagsWeightOrdering();
            case WEIGHT_REVERS:
                return new TagsWeightReverseOrdering();
        }
        throw new FacesException("Wrong type of ordering!");
    }
}
