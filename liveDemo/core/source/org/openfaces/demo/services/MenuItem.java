/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Serializable {
    private String menuName;
    private String menuUrl;
    private String menuImage;
    private String selectedMenuImage;

    private boolean selected;
    private String headerImage;
    private List<String> features;
    private String componentName;

    private List<DemoItem> demos;
    private DemoItem selectedDemo;
    private List patterns;
    private int selectedTabIndex;

    public MenuItem() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getSelectedMenuImage() {
        return selectedMenuImage;
    }

    public void setSelectedMenuImage(String selectedMenuImage) {
        this.selectedMenuImage = selectedMenuImage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public void addFeature(String feature) {
        features.add(feature);
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public List<DemoItem> getDemos() {
        return demos;
    }

    public void setDemos(List<DemoItem> demos) {
        this.demos = demos;
    }

    public DemoItem getSelectedDemo() {
        if (selectedDemo == null && demos != null && demos.size() > 0) {
            selectedDemo = demos.get(0);
        }
        return selectedDemo;
    }

    public void setSelectedDemo(DemoItem selectedDemo) {
        this.selectedDemo = selectedDemo;
    }

    public void addDemo(DemoItem demoItem) {
        if (demos == null)
            demos = new ArrayList<DemoItem>();
        demos.add(demoItem);
    }

    public List getPatterns() {
        return patterns;
    }

    public void setPatterns(List patterns) {
        this.patterns = patterns;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    public boolean isDemosAvailable() {
        return demos != null && demos.size() > 0;
    }

    public int getDemoCount() {
        return demos == null ? 0 : demos.size();
    }
}
