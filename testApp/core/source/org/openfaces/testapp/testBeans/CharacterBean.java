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
package org.openfaces.testapp.testBeans;

import org.openfaces.component.input.DropDownItem;

import java.util.List;

/**
 * @author Darya Shumilina
 */
public class CharacterBean {

    private String addAllText;
    private String addAllHint;
    private String addText;
    private String addHint;

    private String removeAllText;
    private String removeAllHint;
    private String removeText;
    private String removeHint;

    private List<DropDownItem> dropDownCollection;
    private String confirmationDetails;
    private String confirmationCancelButton;
    private String confirmationOkButton;

    private String confirmationCaption;

    public CharacterBean() {
        addAllText = "Add All";
        addAllHint = "Add All Hint";
        addText = "Add";
        addHint = "Add Hint";

        removeAllText = "Remove All";
        removeAllHint = "Remove All Hint";
        removeText = "Remove";
        removeHint = "Remove Hint";

        dropDownCollection.add(createFontColorItem("первая строка"));
        dropDownCollection.add(createFontColorItem("вторая строка"));
        dropDownCollection.add(createFontColorItem("третья строка"));

        confirmationCancelButton = "Отмена";
        confirmationCaption = "Заголовок";
        confirmationDetails = "Детали";
        confirmationOkButton = "Выполнить";
    }

    private DropDownItem createFontColorItem(String value) {
        DropDownItem currentItem = new DropDownItem();
        currentItem.setValue(value);
        return currentItem;
    }

    public String getAddAllText() {
        return addAllText;
    }

    public String getAddAllHint() {
        return addAllHint;
    }

    public String getAddText() {
        return addText;
    }

    public String getAddHint() {
        return addHint;
    }

    public String getRemoveAllText() {
        return removeAllText;
    }

    public String getRemoveAllHint() {
        return removeAllHint;
    }

    public String getRemoveText() {
        return removeText;
    }

    public String getRemoveHint() {
        return removeHint;
    }

    public void setAddAllText(String addAllText) {
        this.addAllText = addAllText;
    }

    public void setAddAllHint(String addAllHint) {
        this.addAllHint = addAllHint;
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public void setAddHint(String addHint) {
        this.addHint = addHint;
    }

    public void setRemoveAllText(String removeAllText) {
        this.removeAllText = removeAllText;
    }

    public void setRemoveAllHint(String removeAllHint) {
        this.removeAllHint = removeAllHint;
    }

    public void setRemoveText(String removeText) {
        this.removeText = removeText;
    }

    public void setRemoveHint(String removeHint) {
        this.removeHint = removeHint;
    }

    public List<DropDownItem> getDropDownCollection() {
        return dropDownCollection;
    }

    public void setDropDownCollection(List<DropDownItem> dropDownCollection) {
        this.dropDownCollection = dropDownCollection;
    }

    public String getConfirmationDetails() {
        return confirmationDetails;
    }

    public void setConfirmationDetails(String confirmationDetails) {
        this.confirmationDetails = confirmationDetails;
    }

    public String getConfirmationCancelButton() {
        return confirmationCancelButton;
    }

    public void setConfirmationCancelButton(String confirmationCancelButton) {
        this.confirmationCancelButton = confirmationCancelButton;
    }

    public String getConfirmationOkButton() {
        return confirmationOkButton;
    }

    public void setConfirmationOkButton(String confirmationOkButton) {
        this.confirmationOkButton = confirmationOkButton;
    }

    public String getConfirmationCaption() {
        return confirmationCaption;
    }

    public void setConfirmationCaption(String confirmationCaption) {
        this.confirmationCaption = confirmationCaption;
    }
}