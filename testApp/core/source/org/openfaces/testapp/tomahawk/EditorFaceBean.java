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
package org.openfaces.testapp.tomahawk;

/**
 * @author Sylvain Vieujot (latest modification by $Author: dennisbyrne $)
 * @version $Revision: 405919 $ $Date: 2006-05-12 22:47:23 +0000 (Fri, 12 May 2006) $
 */
public class EditorFaceBean {

    private String text = "Default unformatted text.";

    // Options
    private boolean allowEditSource = true;
    private boolean showPropertiesToolBox = false;
    private boolean showLinksToolBox = false;
    private boolean showImagesToolBox = false;
    private boolean showTablesToolBox = false;
    private boolean showCleanupExpressionsToolBox = false;
    private boolean showDebugToolBox = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isAllowEditSource() {
        return allowEditSource;
    }

    public void setAllowEditSource(boolean allowEditSource) {
        this.allowEditSource = allowEditSource;
    }

    public boolean isShowImagesToolBox() {
        return showImagesToolBox;
    }

    public void setShowImagesToolBox(boolean showImagesToolBox) {
        this.showImagesToolBox = showImagesToolBox;
    }

    public boolean isShowLinksToolBox() {
        return showLinksToolBox;
    }

    public void setShowLinksToolBox(boolean showLinksToolBox) {
        this.showLinksToolBox = showLinksToolBox;
    }

    public boolean isShowPropertiesToolBox() {
        return showPropertiesToolBox;
    }

    public void setShowPropertiesToolBox(boolean showPropertiesToolBox) {
        this.showPropertiesToolBox = showPropertiesToolBox;
    }

    public boolean isShowTablesToolBox() {
        return showTablesToolBox;
    }

    public void setShowTablesToolBox(boolean showTablesToolBox) {
        this.showTablesToolBox = showTablesToolBox;
    }

    public boolean isShowCleanupExpressionsToolBox() {
        return showCleanupExpressionsToolBox;
    }

    public void setShowCleanupExpressionsToolBox(boolean showCleanupExpressionsToolBox) {
        this.showCleanupExpressionsToolBox = showCleanupExpressionsToolBox;
    }

    public boolean isShowDebugToolBox() {
        return showDebugToolBox;
    }

    public void setShowDebugToolBox(boolean showDebugToolBox) {
        this.showDebugToolBox = showDebugToolBox;
    }
}