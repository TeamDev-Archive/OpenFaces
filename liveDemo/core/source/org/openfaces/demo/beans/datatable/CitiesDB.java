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

import org.openfaces.demo.beans.util.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Darya Shumilina
 */
public class CitiesDB implements Serializable {
    private List<QueryListener> queryListeners = new ArrayList<QueryListener>();
    private List<City> citiesList = new ArrayList<City>();
    private Map<Integer, City> cityById = new HashMap<Integer, City>();

    public CitiesDB() {
        try {
            InputStream resource = CitiesDB.class.getResourceAsStream("Data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            int id = 0;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                String[] cityAttributes = currentString.split("\t");
                String currentName = new String(cityAttributes[0].getBytes(), "utf-8");
                String currentCountry = new String(cityAttributes[2].getBytes(), "utf-8");
                City currentCity = new City(id, currentName, Integer.valueOf(cityAttributes[1]), currentCountry);
                cityById.put(id, currentCity);
                citiesList.add(currentCity);
                id++;
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<City> getCitiesByParameters(
            CitySelectionCriteria criteria,
            final String sortByColumnName,
            final boolean sortAscending,
            int firstRecordNo,
            int recordCount) {
        List<City> filteredData = filteredCollection(citiesList, criteria);
        if (sortByColumnName != null)
            Collections.sort(filteredData, new Comparator<City>() {
                public int compare(City c1, City c2) {
                    Comparable value1;
                    Comparable value2;
                    if ("name".equals(sortByColumnName)) {
                        value1 = c1.getName();
                        value2 = c2.getName();
                    } else if ("population".equals(sortByColumnName)) {
                        value1 = c1.getPopulation();
                        value2 = c2.getPopulation();
                    } else if ("country".equals(sortByColumnName)) {
                        value1 = c1.getCountry();
                        value2 = c2.getCountry();
                    } else
                        throw new IllegalStateException("Unknown columnID: " + sortByColumnName);
                    if (sortAscending) {
                        return value1.compareTo(value2);
                    } else {
                        return value2.compareTo(value1);
                    }
                }
            });
        List<City> filteredSortedList = new ArrayList<City>(recordCount != -1
                ? filteredData.subList(firstRecordNo, firstRecordNo + recordCount)
                : filteredData.subList(firstRecordNo, filteredData.size())
        );

        StringBuffer sortableColumn = new StringBuffer();
        if (sortByColumnName != null) {
            sortableColumn.append(" Sorting by ");
            if (!sortByColumnName.equals("name")) sortableColumn.append(sortByColumnName);
            else sortableColumn.append("city");
            sortableColumn.append(".");
        }
        StringBuffer filterValues = new StringBuffer();
        if (criteria != null) {
            String name = criteria.getCityNameSearchString();
            String country = criteria.getCountry();
            String range = criteria.getMinPopulation() + "-" + criteria.getMaxPopulation();
            boolean isFilteredByName = (name != null) && (!name.equals(""));
            boolean isFilteredByCountry = (country != null) && (!country.equals(""));
            boolean isFilteredByRange = (!range.equals("")) && (!range.equals("0-0"));
            if (isFilteredByRange || isFilteredByName || isFilteredByCountry) {
                filterValues.append(" Filtering by ");
                if (isFilteredByName) {
                    filterValues.append("City (");
                    filterValues.append(name);
                    filterValues.append(")");
                }
                if (isFilteredByCountry) {
                    if (isFilteredByName) filterValues.append(" and ");
                    filterValues.append("Country (");
                    filterValues.append(country);
                    filterValues.append(")");
                }
                if (isFilteredByRange) {
                    if (isFilteredByCountry || isFilteredByName) filterValues.append(" and ");
                    filterValues.append("Population (");
                    filterValues.append(range);
                    filterValues.append(")");
                }
                filterValues.append(".");
            }
        }

        fireQueryPerformed("\tFetching rows " + firstRecordNo + "-" + (firstRecordNo + recordCount) + "." +
                sortableColumn + filterValues);
        return filteredSortedList;
    }

    public int getRecordCount(CitySelectionCriteria criteria) {
        List<City> filteredData = filteredCollection(citiesList, criteria);
        return filteredData.size();
    }

    public City getCityById(Integer cityId) {
        return cityById.get(cityId);
    }

    public void addQueryListener(QueryListener listener) {
        queryListeners.add(listener);
    }

    public void removeQueryListener(QueryListener listener) {
        queryListeners.remove(listener);
    }

    private void fireQueryPerformed(String queryInfo) {
        QueryEvent event = new QueryEvent(this, queryInfo);
        for (QueryListener queryListener : queryListeners) {
            queryListener.queryPerformed(event);
        }
    }

    public List<City> getCitiesList() {
        return citiesList;
    }

    public City getCityByName(String value) {
        for (City city : citiesList) {
            if (city.getName().equals(value))
                return city;
        }
        return null;
    }

    public static class CitySelectionCriteria {
        private String cityNameSearchString;
        private int minPopulation;
        private int maxPopulation;
        private String country;
        private boolean searchFromTextStartOnly;

        public CitySelectionCriteria() {
        }

        public CitySelectionCriteria(String cityNameSearchString, int minPopulation, int maxPopulation, String country) {
            this.cityNameSearchString = cityNameSearchString;
            this.minPopulation = minPopulation;
            this.maxPopulation = maxPopulation;
            this.country = country;
        }


        public boolean isSearchFromTextStartOnly() {
            return searchFromTextStartOnly;
        }

        public void setSearchFromTextStartOnly(boolean searchFromTextStartOnly) {
            this.searchFromTextStartOnly = searchFromTextStartOnly;
        }

        public String getCityNameSearchString() {
            return cityNameSearchString;
        }

        public int getMinPopulation() {
            return minPopulation;
        }

        public int getMaxPopulation() {
            return maxPopulation;
        }

        public String getCountry() {
            return country;
        }


        public void setCityNameSearchString(String cityNameSearchString) {
            this.cityNameSearchString = cityNameSearchString;
        }

        public void setMinPopulation(int minPopulation) {
            this.minPopulation = minPopulation;
        }

        public void setMaxPopulation(int maxPopulation) {
            this.maxPopulation = maxPopulation;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    private static List<City> filteredCollection(List<City> unfileredCollection, CitySelectionCriteria criteria) {
        if (criteria == null)
            return unfileredCollection;
        List<City> filtered = new ArrayList<City>();

        String nameFilterCriterion = criteria.getCityNameSearchString();
        String countryFilterCriterion = criteria.getCountry();
        int minFilterCriterion = criteria.getMinPopulation();
        int maxFilterCriterion = criteria.getMaxPopulation();

        for (City city : unfileredCollection) {
            if (nameFilterCriterion != null) {
                String criterionUC = nameFilterCriterion.toUpperCase();
                String itemNameUC = city.getName().toUpperCase();
                if (criteria.isSearchFromTextStartOnly()) {
                    if (!itemNameUC.startsWith(criterionUC)) continue;
                } else {
                    if (!itemNameUC.contains(criterionUC)) continue;
                }
            }

            if (countryFilterCriterion != null) {
                String criterionUC = countryFilterCriterion.toUpperCase();
                String itemUC = city.getCountry().toUpperCase();
                if (!itemUC.contains(criterionUC)) continue;
            }

            if ((minFilterCriterion > 0) && (maxFilterCriterion > 0)) {
                if ((city.getPopulation() > maxFilterCriterion) || (city.getPopulation() < minFilterCriterion))
                    continue;
            }

            filtered.add(city);
        }
        return filtered;
    }
}