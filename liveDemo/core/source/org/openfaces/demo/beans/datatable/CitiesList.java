/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datatable;

import org.openfaces.component.filter.CompositeFilterCriterion;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.table.DataTable;
import org.openfaces.demo.beans.util.City;
import org.openfaces.util.Faces;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Darya Shumilina
 */
public class CitiesList implements Serializable {

    private CitiesDB citiesDB = new CitiesDB();
    private List<String> countries = new ArrayList<String>();
    private List<String> queries = new ArrayList<String>();
    private static final String REQUEST_STARTED_ADDED = "org.openfaces.demo.beans.datatable.CitiesList.queryAdded";

    public CitiesList() {
        citiesDB.addQueryListener(new QueryListener() {
            public void queryPerformed(QueryEvent event) {
                Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
                if (!requestMap.containsKey(REQUEST_STARTED_ADDED)) {
                    queries.add("---------------------------");
                    requestMap.put(REQUEST_STARTED_ADDED, Boolean.TRUE);
                }
                queries.add(event.getQueryInfo());
            }
        });
        List<City> citiesList = new ArrayList<City>();
        citiesList.addAll(citiesDB.getCitiesList());
        for (City currentCity : citiesList) {
            if (!countries.contains(currentCity.getCountry()))
                countries.add(currentCity.getCountry());
        }
    }

    public List<City> getCitiesList() {
        CompositeFilterCriterion filterCriteria = Faces.var("filterCriteria", CompositeFilterCriterion.class);
        boolean sortAscending = Faces.var("sortAscending", Boolean.class);
        String sortColumnId = Faces.var("sortColumnId", String.class);
        int pageStart = Faces.var("pageStart", Integer.class);
        int pageSize = Faces.var("pageSize", Integer.class);
        CitiesDB.CitySelectionCriteria filterConditions = calculateFilterConditions(filterCriteria);
        return citiesDB.getCitiesByParameters(filterConditions, sortColumnId, sortAscending, pageStart, pageSize);
    }

    private CitiesDB.CitySelectionCriteria calculateFilterConditions(CompositeFilterCriterion filterCriteria) {
        CitiesDB.CitySelectionCriteria filterConditions = new CitiesDB.CitySelectionCriteria();
        for (FilterCriterion entry : filterCriteria.getCriteria()) {
            ExpressionFilterCriterion criterion = (ExpressionFilterCriterion) entry;
            String columnId = criterion.getExpressionStr();
            String searchString = criterion.getArg1().toString();
            if (columnId.equals("name")) {
                filterConditions.setCityNameSearchString(searchString);
            } else if (columnId.equals("population")) {
                String[] result = searchString.split(" \u2013 ");

                String[] minLimit = result[0].split(",");
                StringBuffer minLimitBuffer = new StringBuffer();
                for (String aMinLimit : minLimit) {
                    minLimitBuffer.append(aMinLimit);
                }
                int min = new Integer(minLimitBuffer.toString());

                String[] maxLimit = result[1].split(",");
                StringBuffer maxLimitBuffer = new StringBuffer();
                for (String aMaxLimit : maxLimit) {
                    maxLimitBuffer.append(aMaxLimit);
                }
                int max = new Integer(maxLimitBuffer.toString());

                filterConditions.setMinPopulation(min);
                filterConditions.setMaxPopulation(max);
            } else if (columnId.equals("country")) {
                filterConditions.setCountry(searchString);
            }
        }
        return filterConditions;
    }

    public List<String> getRanges() {
        List<String> ranges = new ArrayList<String>();
        ranges.add("500,000 \u2013 1,000,000");
        ranges.add("1,000,000 \u2013 1,500,000");
        ranges.add("1,500,000 \u2013 2,000,000");
        ranges.add("2,000,000 \u2013 2,500,000");
        ranges.add("2,500,000 \u2013 3,000,000");
        ranges.add("3,000,000 \u2013 3,500,000");
        ranges.add("3,500,000 \u2013 6,500,000");
        ranges.add("6,500,000 \u2013 9,500,000");
        ranges.add("9,500,000 \u2013 12,500,000");
        ranges.add("12,500,000 \u2013 16,000,000");
        return ranges;
    }

    public int getRowCount() {
        CompositeFilterCriterion filterCriteria = Faces.var("filterCriteria", CompositeFilterCriterion.class);
        CitiesDB.CitySelectionCriteria filterConditions = calculateFilterConditions(filterCriteria);
        return citiesDB.getRecordCount(filterConditions);
    }

    public City getRowByKey() {
        Integer key = Faces.var("rowKey", Integer.class);
        return citiesDB.getCityById(key);
    }

    public List<String> getCountries() {
        if (countries.size() > 4)
            countries.remove(countries.get(0));
        return countries;
    }

    public List<String> getQueries() {
        List<String> prepared = new ArrayList<String>();
        StringBuffer queriesAsString = new StringBuffer();
        for (String query : queries) {
            queriesAsString.append(query);
        }
        String[] queriesAsStringArray = queriesAsString.toString().split("---------------------------");
        for (String currentString : queriesAsStringArray) {
            String[] currentStringArray = currentString.split("\t");
            StringBuffer currentStringBuffer = new StringBuffer();
            for (int j = 0; j < currentStringArray.length; j++) {
                currentStringBuffer.append(currentStringArray[j]);
                if ((j != 0) && (j != currentStringArray.length - 1))
                    currentStringBuffer.append("<br/>");
            }
            prepared.add(currentStringBuffer.toString());
        }
        Collections.reverse(prepared);
        return prepared;
    }
}