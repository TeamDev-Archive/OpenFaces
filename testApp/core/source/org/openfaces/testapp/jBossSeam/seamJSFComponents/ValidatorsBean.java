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

package org.openfaces.testapp.jBossSeam.seamJSFComponents;

import org.hibernate.validator.Future;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;
import static org.jboss.seam.ScopeType.SESSION;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Tatyana Matveyeva
 */
@Stateful
@Scope(SESSION)
@Name("seamvalidators")
public class ValidatorsBean implements Validators {

    private String ddfValue;
    private Date dchValue;
    private Date calendarValue;
    private List tlsValue;
    private List<SelectItem> tlsList = new ArrayList<SelectItem>();
    private Locale locale = Locale.ENGLISH;

    public ValidatorsBean() {
        tlsList.add(new SelectItem("1", "Item1", "description1", true));
        tlsList.add(new SelectItem("2", "Item2", "description2"));
        tlsList.add(new SelectItem("3", "Item3", "description3", true));
        tlsList.add(new SelectItem("4", "Item4", "description4"));
        tlsList.add(new SelectItem("5", "Item5", "description5"));
    }

    public enum Honorific {
        MR("Mr."),
        MRS("Mrs."),
        MISS("Miss."),
        MS("Ms."),
        DOCTOR("Dr.");
        private String label;

        Honorific(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    @Enumerated(EnumType.STRING)
    private Honorific honorific;


    public Honorific getHonorific() {
        return honorific;
    }

    public void setHonorific(Honorific honorific) {
        this.honorific = honorific;
    }

    @NotNull
    @Length(min = 2, max = 10)
    public String getDdfValue() {
        return ddfValue;
    }

    public void setDdfValue(String ddfValue) {
        this.ddfValue = ddfValue;
    }

    @NotNull
    @Past
    public Date getDchValue() {
        return dchValue;
    }

    public void setDchValue(Date dchValue) {
        this.dchValue = dchValue;
    }

    @NotNull
    @Future
    public Date getCalendarValue() {
        return calendarValue;
    }

    public void setCalendarValue(Date calendarValue) {
        this.calendarValue = calendarValue;
    }

    @NotNull
    public List getTlsValue() {
        return tlsValue;
    }

    public void setTlsValue(List tlsValue) {
        this.tlsValue = tlsValue;
    }


    public List<SelectItem> getTLSList() {
        return tlsList;
    }

    public void setTLSList(List<SelectItem> TLSList) {
        tlsList = TLSList;
    }


    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Remove
    @Destroy
    public void destroy() {
    }
}
