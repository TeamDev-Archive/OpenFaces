/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test;

import org.openfaces.util.Resources;

/**
 * @author Dmitry Pikhulya
 */
public class VersionStringTest extends ComponentTestCase {
    private static final String EA_STRING = "EA";

    /**
     * This test checks the correctness of the value returned by Resources.getVersionString(). This is needed to ensure
     * that the correct string is appended at the end of internal resource file names (see JSFC-3722).
     * <p/>
     * This test may need to be corrected if another version naming scheme is to be chosen.
     */
    public void testOFVersionString() {

        String versionString = Resources.getVersionString();
        assertNotNull("OpenFaces version string shouldn't be null", versionString);
        assertTrue("OpenFaces version string shouldn't be empty", versionString.trim().length() > 0);
        assertTrue("OpenFaces version string should be trimmed", versionString.trim().equals(versionString));

        String[] versionComponents = versionString.split("[.]");
        assertTrue("There should be from two to four version number components in the openfaces version string, but was: " +
                versionComponents.length + "; version string: " + versionString,
                versionComponents.length >= 2 && versionComponents.length <= 4);
        for (int i = 0, count = versionComponents.length; i < count; i++) {
            String versionComponent = versionComponents[i];
            if (i == count - 2) {
                if (versionComponent.startsWith(EA_STRING))
                    versionComponent = versionComponent.substring(EA_STRING.length());
            }
            if (i == count - 1) {
                String ORDINARY_BUILD_NO_PREFIX = "b";
                if (versionComponent.startsWith(ORDINARY_BUILD_NO_PREFIX))
                    versionComponent = versionComponent.substring(ORDINARY_BUILD_NO_PREFIX.length());
            }

            try {
                Integer.parseInt(versionComponent);
            } catch (NumberFormatException e) {
                fail("Couldn't parse version component: " + versionComponent + "; full version string: " + versionString);
                // note, this may fail if this test is run right from IDE without using ant.
                // version string is auto-generated to a unique string in this case for development purposes.
            }
        }
    }

}
