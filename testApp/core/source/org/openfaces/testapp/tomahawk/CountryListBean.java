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

import org.apache.myfaces.component.html.ext.HtmlDataTable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DOCUMENT ME!
 *
 * @author Thomas Spiegl (latest modification by $Author: werpu $)
 * @version $Revision: 365757 $ $Date: 2006-01-03 22:33:18 +0000 (Tue, 03 Jan 2006) $
 */
public class CountryListBean {
    private List<SimpleCountry> countries = new ArrayList<SimpleCountry>();

    static {
    }

    SimpleCountry getSimpleCountry(long id) {
        for (Object country1 : countries) {
            SimpleCountry country = (SimpleCountry) country1;
            if (country.getId() == id) {
                return country;
            }
        }
        return null;
    }

    long getNewSimpleCountryId() {
        long maxId = 0;
        for (Object country1 : countries) {
            SimpleCountry country = (SimpleCountry) country1;
            if (country.getId() > maxId) {
                maxId = country.getId();
            }
        }
        return maxId + 1;
    }

    void saveSimpleCountry(SimpleCountry simpleCountry) {
        if (simpleCountry.getId() == 0) {
            simpleCountry.setId(getNewSimpleCountryId());
        }
        boolean found = false;
        for (int i = 0; i < countries.size(); i++) {
            SimpleCountry country = countries.get(i);
            if (country.getId() == simpleCountry.getId()) {
                countries.set(i, simpleCountry);
                found = true;
            }
        }
        if (!found) {
            countries.add(simpleCountry);
        }
    }

    void deleteSimpleCountry(SimpleCountry simpleCountry) {
        for (int i = 0; i < countries.size(); i++) {
            SimpleCountry country = countries.get(i);
            if (country.getId() == simpleCountry.getId()) {
                countries.remove(i);
            }
        }
    }

    public CountryListBean() {
        countries.add(new SimpleCountry(1, "AUSTRIA", "AT", new BigDecimal(123L), createCities(new String[]{"Wien", "Graz", "Linz", "Salzburg"})));
        countries.add(new SimpleCountry(2, "AZERBAIJAN", "AZ", new BigDecimal(535L), createCities(new String[]{"Baku", "Sumgait", "Qabala", "Agdam"})));
        countries.add(new SimpleCountry(3, "BAHAMAS", "BS", new BigDecimal(1345623L), createCities(new String[]{"Nassau", "Alice Town", "Church Grove", "West End"})));
        countries.add(new SimpleCountry(4, "BAHRAIN", "BH", new BigDecimal(346L), createCities(new String[]{"Bahrain"})));
        countries.add(new SimpleCountry(5, "BANGLADESH", "BD", new BigDecimal(456L), createCities(new String[]{"Chittagong", "Chandpur", "Bogra", "Feni"})));
        countries.add(new SimpleCountry(6, "BARBADOS", "BB", new BigDecimal(45645L), createCities(new String[]{"Grantley Adams"})));
    }

    private SimpleCity[] createCities(String[] names) {
        SimpleCity[] result = new SimpleCity[names.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new SimpleCity(names[i]);
        }
        return result;
    }

    public List<SimpleCountry> getCountries() {
        return countries;
    }

    public Map<String, String> getCountryMap() {
        Map<String, String> map = new HashMap<String, String>();

        List<SimpleCountry> li = getCountries();

        for (SimpleCountry simpleCountry : li) {
            map.put(simpleCountry.getIsoCode(), simpleCountry.getName());
        }

        return map;
    }

    public void setCountries(List<SimpleCountry> countries) {
        this.countries = countries;
    }

    public String addCountry() {
        List<SimpleCountry> list = getCountries();
        list.add(new SimpleCountry(list.size() + 1, "", "", new BigDecimal(0), createCities(new String[]{})));
        return "ok";
    }

    public void deleteCountry(ActionEvent ev) {
        UIData datatable = findParentHtmlDataTable(ev.getComponent());
        getCountries().remove(datatable.getRowIndex() + datatable.getFirst());
    }

    private HtmlDataTable findParentHtmlDataTable(UIComponent component) {
        while (true) {
            if (component == null) {
                return null;
            }
            if (component instanceof HtmlDataTable) {
                return (HtmlDataTable) component;
            }
            component = component.getParent();
        }
    }
}