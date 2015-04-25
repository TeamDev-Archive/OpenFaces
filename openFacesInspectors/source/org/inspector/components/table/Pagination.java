/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.components.table;

import org.apache.commons.lang.StringUtils;
import org.inspector.api.Input;
import org.inspector.components.input.InputText;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class Pagination extends DataTable {
    private static final String PAGE_NUMBER_ID = "--pageNo";
    private static final String FIRST_PAGE_BTN_ID = "--firstPage";
    private static final String PREVIOUS_PAGE_BTN_ID = "--prevPage";
    private static final String NEXT_PAGE_BTN_ID = "--nextPage";
    private static final String LAST_PAGE_BTN_ID = "--lastPage";

    private By pageNumberLocator;
    private By firstPageLocator;
    private By previousPageLocator;
    private By nextPageLocator;
    private By lastPageLocator;

    private String componentId;
    private Input inputField;

    public Pagination(WebDriver webDriver, String id) {
        super(webDriver, id);
        this.componentId = id;

        initInputField(webDriver);
        initButtons();
    }

    public Pagination(WebDriver webDriver, By locator) {
        super(webDriver, locator);
        this.componentId = id();

        initInputField(webDriver);
        initButtons();
    }

    private void initButtons() {
        this.pageNumberLocator = By.id(this.componentId + PAGE_NUMBER_ID);
        this.firstPageLocator = By.id(this.componentId + FIRST_PAGE_BTN_ID);
        this.previousPageLocator = By.id(this.componentId + PREVIOUS_PAGE_BTN_ID);
        this.nextPageLocator = By.id(this.componentId + NEXT_PAGE_BTN_ID);
        this.lastPageLocator = By.id(this.componentId + LAST_PAGE_BTN_ID);
    }

    private void initInputField(WebDriver webDriver) {
        inputField = new InputText(webDriver, this.componentId + PAGE_NUMBER_ID);
    }

    public void moveToFirst() {
        goToPage(firstPageLocator);
    }

    public void moveToLast() {
        goToPage(lastPageLocator);
    }

    public void moveNext() {
        goToPage(nextPageLocator);
    }

    public void movePrevious() {
        goToPage(previousPageLocator);
    }

    public void moveToPageByNumber(int page) {
        inputField.setValue(String.valueOf(page));
    }

    private void goToPage(By pageId) {
        final WebElement element = findElement(pageId);

        if (!isButtonDisabled(element)) {
            element.click();
            waitForCommandExecute();
        }
    }

    public int getIndex() {
        final String value = inputField.getValue();
        final String index = StringUtils.isNotBlank(value) ? value : inputField.getText();
        return parseInt(index, -1);
    }

    public int getMaxIndex() {
        final TableCell maxIndex = this.body().nextRow().cell(8);
        return parseInt(maxIndex.text(), -1);
    }

    public boolean isButtonDisabled(WebElement button) {
        final String id = button.getAttribute("id");
        return StringUtils.isNotBlank(id) && id.toLowerCase().contains("disabled");
    }
}
