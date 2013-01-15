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

package org.openfaces.testapp.jBossSeam.seamJSFComponents;

import javax.faces.model.SelectItem;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Tatyana Matveyeva
 */
public interface Validators {

    public ValidatorsBean.Honorific getHonorific();

    public void setHonorific(ValidatorsBean.Honorific honorific);

    public String getDdfValue();

    public void setDdfValue(String ddfValue);

    public Date getDchValue();

    public void setDchValue(Date dchValue);

    public Date getCalendarValue();

    public void setCalendarValue(Date calendarValue);

    public List getTlsValue();

    public void setTlsValue(List tlsValue);

    public List<SelectItem> getTLSList();

    public void setTLSList(List<SelectItem> TLSList);

    public Locale getLocale();

    public void setLocale(Locale locale);

    public void destroy();
}
