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

package org.openfaces.tests;

import com.google.common.base.Joiner;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Max Yurin
 */
public class PageAvailabilityTest extends BaseSeleniumTest {

    @Test(groups = {"component"})
    public void testAllPages(){
        final List<String> errors = checkAllPages();

        Joiner joiner = Joiner.on("; ");
        assertThat("Pages not found: " + joiner.join(errors), true, is(errors.isEmpty()));
    }

}
