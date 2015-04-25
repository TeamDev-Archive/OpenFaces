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

package org.inspector.components.filters;

import org.inspector.api.Filter;
import org.inspector.components.input.ComboBoxImpl;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class ComboboxFilter extends ComboBoxImpl implements Filter {
    public ComboboxFilter(WebDriver webDriver, String elementId) {
        super(webDriver, elementId);
    }

    @Override
    public void doFilter(String value) {
        select(value);
    }

    @Override
    public String getFilterValue() {
        return super.getSelectedOption();
    }
}
