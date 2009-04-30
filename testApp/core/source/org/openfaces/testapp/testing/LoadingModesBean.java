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
package org.openfaces.testapp.testing;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.input.DropDownItem;

import javax.faces.component.html.HtmlOutputText;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class LoadingModesBean {

    private List<LoadingModeItem> modes = new ArrayList<LoadingModeItem>();
    private int selectedFoldingPanelIndex;
    private int selectedTabbedPaneIndex;
    private List<FoldingPanelLoadingModeItem> fpModes = new ArrayList<FoldingPanelLoadingModeItem>();

    private String addAllText;
    private String addAllHint;
    private String addText;
    private String addHint;

    private String removeAllText;
    private String removeAllHint;
    private String removeText;
    private String removeHint;

    private String selectedFontColor;
    private List<DropDownItem> fontColors = new ArrayList<DropDownItem>();
    private String confirmationDetails;
    private String confirmationCancelButton;

    private String confirmationOkButton;
    private String confirmationCaption;
    private String hintValue;
    private String hintTitle;

    private String popupText;

    private String firstTab;
    private String secondTab;

    private String tableHeader;
    private String tableBody;
    private String tableFooter;
    private String topNode;
    private String foldingPanelContent;
    private String foldingPanelCaption;
    private String firstTabContent;
    private String secondTabContent;

    private String subNode1;
    private String subNode2;
    private String subNode11;
    private String subNode12;

    private String subNode21;
    private String subNode22;

    public LoadingModesBean() {
        selectedTabbedPaneIndex = 0;
        selectedFoldingPanelIndex = 0;
        modes.addAll(loadingModes());
        fpModes.addAll(loadingModesFP());

        addAllText = "Добавить всё";
        addAllHint = "Добавить всё. Хинт.";
        addText = "Добавить";
        addHint = "Добавить. Хинт.";

        removeAllText = "Переместить всё";
        removeAllHint = "Переместить всё. Хинт.";
        removeText = "Переместить";
        removeHint = "Переместить. Хинт.";

        selectedFontColor = "чёрный";
        fontColors.add(createFontColorItem("аквамарин"));
        fontColors.add(createFontColorItem("тёмно-красный"));
        fontColors.add(createFontColorItem("коричневый"));
        fontColors.add(createFontColorItem("чёрный"));

        confirmationDetails = "Детали";

        confirmationCancelButton = "Отмена";
        confirmationCaption = "Заголовок";
        confirmationDetails = "Детали";
        confirmationOkButton = "Выполнить";

        hintTitle = "Тестовая всплывающая подсказка";
        hintValue = "Тестовый видимый текст";
        popupText = "Это -  текст внутри \"всплывающего слоя\"";
        firstTab = "Первый таб";

        secondTab = "Второй таб";
        foldingPanelContent = "Тестовое текстовое содержание";
        foldingPanelCaption = "Заголовок";
        firstTabContent = "Тестовое текстовое содержание первого таба";

        secondTabContent = "Тестовое текстовое содержание второго таба";
        tableHeader = "Заголовок";
        tableBody = "Тело таблицы";
        tableFooter = "Нижний колонтитул";

        topNode = "Цвета";
        subNode1 = "Тёплые";
        subNode2 = "Холодные";
        subNode11 = "Красный";

        subNode12 = "Оранжевый";
        subNode21 = "Синий";
        subNode22 = "Фиолетовый";
    }

    private DropDownItem createFontColorItem(String value) {
        DropDownItem fontColor = new DropDownItem();
        fontColor.setValue(value);
        HtmlOutputText text = new HtmlOutputText();
        text.setValue(value);
        fontColor.getChildren().add(text);
        return fontColor;
    }

    private static List<LoadingModeItem> loadingModes() {
        List<LoadingModeItem> tempLoadingModesList = new ArrayList<LoadingModeItem>();
        tempLoadingModesList.add(new LoadingModeItem(LoadingMode.AJAX, "ajax"));
        tempLoadingModesList.add(new LoadingModeItem(LoadingMode.CLIENT, "client"));
        tempLoadingModesList.add(new LoadingModeItem(LoadingMode.SERVER, "server"));
        return tempLoadingModesList;
    }

    private static List<FoldingPanelLoadingModeItem> loadingModesFP() {
        List<FoldingPanelLoadingModeItem> tempLoadingModesList = new ArrayList<FoldingPanelLoadingModeItem>();
        tempLoadingModesList.add(new FoldingPanelLoadingModeItem(LoadingMode.AJAX, "ajax"));
        tempLoadingModesList.add(new FoldingPanelLoadingModeItem(LoadingMode.CLIENT, "client"));
        tempLoadingModesList.add(new FoldingPanelLoadingModeItem(LoadingMode.SERVER, "server"));
        return tempLoadingModesList;
    }

    public List<LoadingModeItem> getModes() {
        return modes;
    }

    public void setModes(List<LoadingModeItem> modes) {
        this.modes = modes;
    }

    public int getSelectedFoldingPanelIndex() {
        return selectedFoldingPanelIndex;
    }

    public void setSelectedFoldingPanelIndex(int selectedFoldingPanelIndex) {
        this.selectedFoldingPanelIndex = selectedFoldingPanelIndex;
    }

    public int getSelectedTabbedPaneIndex() {
        return selectedTabbedPaneIndex;
    }

    public void setSelectedTabbedPaneIndex(int selectedTabbedPaneIndex) {
        this.selectedTabbedPaneIndex = selectedTabbedPaneIndex;
    }

    public List<String> getTabbedPaneLabels() {
        List<String> tempTP = new ArrayList<String>();
        for (LoadingModeItem currentItem : modes) {
            tempTP.add(currentItem.getLabel());
        }
        return tempTP;
    }

    public List<String> getFoldingPanelLabels() {
        List<String> tempFP = new ArrayList<String>();
        for (FoldingPanelLoadingModeItem curentItem : fpModes) {
            tempFP.add(curentItem.getLabel());
        }
        return tempFP;
    }

    public LoadingMode getTabbedPaneCurrentLoadingMode() {
        LoadingModeItem currentLoadingMode = modes.get(selectedTabbedPaneIndex);
        return currentLoadingMode.getValue();
    }

    public LoadingMode getFoldingPanelCurrentLoadingMode() {
        FoldingPanelLoadingModeItem currentLoadingMode = fpModes.get(selectedFoldingPanelIndex);
        return currentLoadingMode.getValue();
    }

    public List<FoldingPanelLoadingModeItem> getFPModes() {
        return fpModes;
    }

    public void setFPModes(List<FoldingPanelLoadingModeItem> FPModes) {
        fpModes = FPModes;
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

    public String getConfirmationDetails() {
        return confirmationDetails;
    }

    public void setConfirmationDetails(String confirmationDetails) {
        this.confirmationDetails = confirmationDetails;
    }

    public String getConfirmationCancelButton() {
        return confirmationCancelButton;
    }

    public String getConfirmationOkButton() {
        return confirmationOkButton;
    }

    public String getConfirmationCaption() {
        return confirmationCaption;
    }

    public void setConfirmationCancelButton(String confirmationCancelButton) {
        this.confirmationCancelButton = confirmationCancelButton;
    }

    public void setConfirmationOkButton(String confirmationOkButton) {
        this.confirmationOkButton = confirmationOkButton;
    }

    public void setConfirmationCaption(String confirmationCaption) {
        this.confirmationCaption = confirmationCaption;
    }

    public String getHintValue() {
        return hintValue;
    }

    public String getHintTitle() {
        return hintTitle;
    }

    public String getPopupText() {
        return popupText;
    }

    public void setPopupText(String popupText) {
        this.popupText = popupText;
    }

    public String getFirstTab() {
        return firstTab;
    }

    public String getSecondTab() {
        return secondTab;
    }

    public String getFoldingPanelCaption() {
        return foldingPanelCaption;
    }

    public String getFoldingPanelContent() {
        return foldingPanelContent;
    }

    public String getSecondTabContent() {
        return secondTabContent;
    }

    public String getFirstTabContent() {
        return firstTabContent;
    }

    public String getTableHeader() {
        return tableHeader;
    }

    public String getTableBody() {
        return tableBody;
    }

    public String getTableFooter() {
        return tableFooter;
    }

    public String getTopNode() {
        return topNode;
    }

    public String getSubNode1() {
        return subNode1;
    }

    public String getSubNode2() {
        return subNode2;
    }

    public String getSubNode11() {
        return subNode11;
    }

    public String getSubNode12() {
        return subNode12;
    }

    public String getSubNode21() {
        return subNode21;
    }

    public String getSubNode22() {
        return subNode22;
    }

    public String getSelectedFontColor() {
        return selectedFontColor;
    }

    public void setSelectedFontColor(String selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }

    public List<DropDownItem> getFontColors() {
        return fontColors;
    }
}