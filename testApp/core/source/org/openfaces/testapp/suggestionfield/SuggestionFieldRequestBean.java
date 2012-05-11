/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.suggestionfield;

import org.openfaces.component.input.DropDownItem;

import javax.faces.component.html.HtmlOutputText;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class SuggestionFieldRequestBean {

    List<DropDownItem> dropDownValues = new ArrayList<DropDownItem>();

    public SuggestionFieldRequestBean() {
        for (int i = 0; i < 4; i++) {
            dropDownValues.add(createFontColorItem());
        }
    }

    private DropDownItem createFontColorItem() {
        Random rand = new Random();
        DropDownItem fontColor = new DropDownItem();
        HtmlOutputText text = new HtmlOutputText();
        String value = "Item #" + rand.nextInt(37000);
        text.setValue(value);
        text.setStyle("padding-left: 10px;");
        fontColor.setValue(value);
        fontColor.getChildren().add(text);
        return fontColor;
    }

    public List<DropDownItem> getDropDownValues() {
        return dropDownValues;
    }
}