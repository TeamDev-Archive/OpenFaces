/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.seleniuminspector.LoadingMode;

/**
 * @author Andrii Gorbatov
 */
public class InputTextFilterInspector extends AbstractFilterInspector {

  public InputTextFilterInspector(String locator, LoadingMode loadingMode) {
    super(locator, loadingMode);
  }

    public InputTextInspector searchComponent() {
    return new InputTextInspector(getLocator());
  }

  public void makeFiltering(String filterValue) {
    InputTextInspector searchComponent = searchComponent();

    searchComponent.type(filterValue);
    sleep(1000);
    searchComponent.keyDown(13);

    getLoadingMode().waitForLoad();
  }

}
