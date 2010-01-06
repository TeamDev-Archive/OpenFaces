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
package org.openfaces.testapp.datatable;

import org.openfaces.component.table.DataTable;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class LinksTableDemoBean {
    private List links;
    private List editedLinks;
    private DataTable linksDataTable;
    private List addingLinks = new ArrayList();
    private List editingLinks = new ArrayList();
    private List originalLinks = new ArrayList();

    public LinksTableDemoBean() {
        links = createLinks();
    }

    public DataTable getLinksDataTable() {
        return linksDataTable;
    }

    public void setLinksDataTable(DataTable linksDataTable) {
        this.linksDataTable = linksDataTable;
    }

    public List getLinks() {
        return editedLinks != null ? editedLinks : links;
    }

    public void setLinks(List links) {
        this.links = links;
    }

    private List<LinkData> createLinks() {
        List<LinkData> links = new ArrayList<LinkData>();
        links.add(new LinkData("http://jsfcentral.com/", "JSF Central"));
        links.add(new LinkData("http://jsfcentral.com/", "JSF Components Central"));
        links.add(new LinkData("http://jsf-faq.com/", "JSF FAQ"));
        links.add(new LinkData("http://www.jsftutorials.net/", "JSF tutorials.net"));
        links.add(new LinkData("http://java.sun.com/products/jsp/reference/techart/unifiedEL.html", "Unified Expression Language Explained"));
        return links;
    }

    public boolean isEditingLinks() {
        return editedLinks != null;
    }

    public String editLinks(ActionEvent event) {
        editedLinks = new ArrayList(links.size());
        for (Object myLink : links) {
            LinkData linkData = (LinkData) myLink;
            editedLinks.add(linkData.clone());
        }
        return "";
    }

    public String saveEditedLinks(ActionEvent event) {
        saveAllLinks();
        links = editedLinks;
        editedLinks = null;
        return "";
    }

    private void saveAllLinks() {
        Set links = new HashSet(addingLinks);
        links.addAll(editingLinks);

        for (Object link : links) {
            LinkData linkData = (LinkData) link;
            saveLink(linkData);
        }
    }

    private void cancelAllLinks() {
        Set links = new HashSet(addingLinks);
        links.addAll(editingLinks);

        for (Object link : links) {
            LinkData linkData = (LinkData) link;
            cancelLink(linkData);
        }
    }


    public String cancelEditedLinks(ActionEvent event) {
        cancelAllLinks();

        editedLinks = null;
        return "";
    }

    public String removeLink(ActionEvent event) {
        int rowIndex = linksDataTable.getRowIndex();
        editedLinks.remove(rowIndex);
        return "";
    }

    public String getLinksTableStyle() {
        if (isEditingLinks())
            return "border: 1px solid gray";
        else
            return "";
    }

    public boolean isAddingOrEditingLink() {
        LinkData currentLink = getCurrentLink();
        boolean editingLink = editingLinks.contains(currentLink);
        boolean addingLink = addingLinks.contains(currentLink);
        return editingLink || addingLink;
    }

    public boolean isEditingLink() {
        return editingLinks.contains(getCurrentLink());
    }

    public boolean isAddingLink() {
        return addingLinks.contains(getCurrentLink());
    }

    private LinkData getCurrentLink() {
        LinkData linkData = null;
        try {
            linkData = (LinkData) linksDataTable.getRowData();
        } catch (IllegalArgumentException e) {
            // ajax4jsf invokes isRendered for datatable's child components without iterationg over rows. By the spec, getRowData() should throw IllegalARgumentException if rowIndex is -1
        }
        if (linkData == null)
            return null;

        return linkData;
    }

    public String addLink(ActionEvent event) {
        if (editedLinks == null)
            throw new IllegalStateException();
        LinkData linkData = new LinkData();
        addingLinks.add(linkData);
        editedLinks.add(linkData);
        return "";
    }

    public String editLink(ActionEvent event) {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        LinkData currentLink = getCurrentLink();
        editingLinks.add(currentLink);
        originalLinks.add(currentLink.clone());
        return "";
    }

    public String saveLink(ActionEvent event) {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        LinkData currentLink = getCurrentLink();
        saveLink(currentLink);
        return "";
    }

    private void saveLink(LinkData currentLink) {
        if (addingLinks.contains(currentLink)) {
            addingLinks.remove(currentLink);
            return;
        }
        int i = editingLinks.indexOf(currentLink);
        if (i != -1) {
            editingLinks.remove(i);
            originalLinks.remove(i);
        }
    }

    public String cancelLink(ActionEvent event) {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        LinkData currentLink = getCurrentLink();
        cancelLink(currentLink);
        return "";
    }

    private void cancelLink(LinkData currentLink) {
        if (addingLinks.contains(currentLink)) {
            addingLinks.remove(currentLink);
            editedLinks.remove(currentLink);
            return;
        }
        int i = editingLinks.indexOf(currentLink);
        if (i != -1) {
            int editedLinkIndex = editedLinks.indexOf(currentLink);
            editedLinks.set(editedLinkIndex, originalLinks.get(i));

            editingLinks.remove(i);
            originalLinks.remove(i);
        }
    }
}
