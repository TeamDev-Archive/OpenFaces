/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component;

/**
 * @author Dmitry Pikhulya
 */
public interface OUIComponent {
    String getStyle();
    void setStyle(String style);

    String getStyleClass();
    void setStyleClass(String styleClass);

    String getRolloverStyle();
    void setRolloverStyle(String rolloverStyle);

    String getRolloverClass();
    void setRolloverClass(String rolloverClass);

    String getOnclick();
    void setOnclick(String onclick);

    String getOndblclick();
    void setOndblclick(String ondblclick);

    String getOnmousedown();
    void setOnmousedown(String onmousedown);

    String getOnmouseover();
    void setOnmouseover(String onmouseover);

    String getOnmousemove();
    void setOnmousemove(String onmousemove);

    String getOnmouseout();
    void setOnmouseout(String onmouseout);

    String getOnmouseup();
    void setOnmouseup(String onmouseup);

    String getOnfocus();
    void setOnfocus(String onfocus);

    String getOnblur();
    void setOnblur(String onblur);

    String getOnkeydown();
    void setOnkeydown(String onkeydown);

    String getOnkeyup();
    void setOnkeyup(String onkeyup);

    String getOnkeypress();
    void setOnkeypress(String onkeypress);

    String getOncontextmenu();
    void setOncontextmenu(String oncontextmenu);
}
