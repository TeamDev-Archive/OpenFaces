/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.services;

import org.apache.commons.digester.Digester;
import org.openfaces.component.util.IterationStatus;
import org.openfaces.util.Faces;
import org.openfaces.util.Log;
import org.openfaces.util.Resources;
import org.xml.sax.SAXException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuService implements Serializable {
    private List<MenuItem> menus;

    public MenuService() {
        loadMenu();
    }

    private void loadMenu() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("menu", ArrayList.class);
        digester.addObjectCreate("menu/menuItem", MenuItem.class);
        digester.addObjectCreate("menu/menuItem/pagePatterns", ArrayList.class);
        digester.addObjectCreate("menu/menuItem/keyFeatures", ArrayList.class);
        digester.addObjectCreate("menu/menuItem/demos", ArrayList.class);
        digester.addObjectCreate("menu/menuItem/demos/demoItem", DemoItem.class);

        digester.addSetNext("menu/menuItem", "add");
        digester.addSetNext("menu/menuItem/keyFeatures", "setFeatures");
        digester.addSetNext("menu/menuItem/demos", "setDemos");
        digester.addSetNext("menu/menuItem/demos/demoItem", "add");
        digester.addSetNext("menu/menuItem/pagePatterns/pattern", "add");

        digester.addCallMethod("*/menuItem/name", "setMenuName", 0);
        digester.addCallMethod("*/menuItem/componentName", "setComponentName", 0);
        digester.addCallMethod("*/menuItem/url", "setMenuUrl", 0);
        digester.addCallMethod("*/menuItem/image", "setMenuImage", 0);
        digester.addCallMethod("*/menuItem/selectedImage", "setSelectedMenuImage", 0);
        digester.addCallMethod("*/keyFeatures/feature", "add", 0);
        digester.addCallMethod("*/demos/demoItem/demoName", "setDemoName", 0);
        digester.addCallMethod("*/demos/demoItem/demoUrl", "setDemoUrl", 0);

        try {
            menus = (List<MenuItem>) digester.parse(exContext.getResource("/WEB-INF/menu.xml").openStream());
        } catch (IOException e) {
            Log.log(e.getMessage(), e);
        } catch (SAXException e) {
            Log.log(e.getMessage(), e);
        }
    }

    public List<MenuItem> getMenus() {
        getSelectedMenu();
        return menus;
    }

    public MenuItem getSelectedMenu() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String key = MenuService.class.getName() + ".selectedMenu";
        MenuItem selectedMenu = (MenuItem) requestMap.get(key);
        if (selectedMenu == null) {
            selectedMenu = getSelectedMenu_internal();
            requestMap.put(key, selectedMenu);
        }
        return selectedMenu;
    }

    public String getSubDemoUrl() {
        IterationStatus status = Faces.var("status", IterationStatus.class);
        int newIndex = status.getIndex();
        MenuItem selectedMenu = getSelectedMenu();
        DemoItem di = selectedMenu.getDemos().get(newIndex);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return externalContext.getRequestContextPath() + di.getDemoUrl();
    }

    private MenuItem getSelectedMenu_internal() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String requestURL = normalizeMenuUrl(request.getRequestURL().toString());
        MenuItem selectedMenu = null;
        menusIteration:
        for (MenuItem menuItem : menus) {
            menuItem.setSelected(false);
            List<DemoItem> demos = menuItem.getDemos();
            if (demos != null && demos.size() > 0) {
                for (int i = 0, count = demos.size(); i < count; i++) {
                    DemoItem di = demos.get(i);
                    String demoUrl = normalizeMenuUrl(di.getDemoUrl());
                    if (requestURL.endsWith(demoUrl) && demoUrl.length() > 0) {
                        selectedMenu = menuItem;
                        menuItem.setSelected(true);
                        menuItem.setSelectedTabIndex(i);
                        continue menusIteration;
                    }
                }
            } else {
                String menuUrl = normalizeMenuUrl(menuItem.getMenuUrl());
                if (requestURL.endsWith(menuUrl) && menuUrl.length() > 0) {
                    selectedMenu = menuItem;
                    menuItem.setSelected(true);
                }
            }
        }
        return selectedMenu;
    }

    private String normalizeMenuUrl(String menuUrl) {
        if (menuUrl.endsWith(".jsp") || menuUrl.endsWith(".jsf")) {
            menuUrl = menuUrl.substring(0, menuUrl.length() - 4);
        }
        return menuUrl;
    }

    public String getOpenFacesVersion() {
        // note: Resources.getVersionString() is an internal OpenFaces function
        // and it shouldn't be used explicitly in any application's code 
        String str = Resources.getVersionString();
        if (str == null || str.length() == 0 || !Character.isDigit(str.charAt(0)))
            return "";

        if (!str.toUpperCase().contains("EA"))
            return str;
        int buildNoSeparator = str.lastIndexOf(".");
        str = str.substring(0, buildNoSeparator);
        return str;
    }

}
