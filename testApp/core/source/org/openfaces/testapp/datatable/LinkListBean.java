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
package org.openfaces.testapp.datatable;

import org.openfaces.util.Faces;
import org.openfaces.component.table.DataTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinkListBean {
    private List links;
    private List editedLinks;
    private DataTable linksDataTable;
    private List<Link> addingLinks = new ArrayList<Link>();

    private List<Link> editingLinks = new ArrayList<Link>();
    private List<Object> originalLinks = new ArrayList<Object>();
    private String selectedIcon;
    private List selectedList = new ArrayList();

    public LinkListBean() {
        links = createLinks();
        editLinks();
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


    private List<Link> createLinks() {
        List<Link> links = new ArrayList<Link>();
        links.add(new Link(
                "music.gif",
                "http://www.napster.com",
                "Napster"
        ));
        links.add(new Link(
                "favorite.gif",
                "http://en.wikipedia.org/",
                "Wikipedia"
        ));
        links.add(new Link(
                "fun.gif",
                "http://www.eagames.pl/",
                "EA Games"
        ));
        links.add(new Link(
                "music.gif",
                "http://www.winamp.com",
                "Winamp"
        ));
        links.add(new Link(
                "favorite.gif",
                "http://www.microsoft.com",
                "Microsoft"
        ));
        links.add(new Link(
                "fun.gif",
                "http://anime.com/",
                "All about anime"
        ));
        links.add(new Link(
                "favorite.gif",
                "http://www.linux.org",
                "Linux"
        ));
        links.add(new Link(
                "favorite.gif",
                "http://www.cssplay.co.uk/",
                "css play"
        ));

        return links;
    }

    public boolean isEditingLinks() {
        return editedLinks != null;
    }

    public String editLinks() {
        editedLinks = new ArrayList(links.size());
        for (Object myLink : links) {
            Link linkData = (Link) myLink;
            editedLinks.add(linkData.clone());
        }
        return "";
    }

    public String saveEditedLinks() {
        saveAllLinks();
        links = editedLinks;
        editedLinks = null;
        return "";
    }

    private void saveAllLinks() {
        Set<Link> links = new HashSet<Link>(addingLinks);
        links.addAll(editingLinks);

        for (Object link : links) {
            Link linkData = (Link) link;
            saveLink(linkData);
        }
    }

    private void cancelAllLinks() {
        Set<Link> links = new HashSet<Link>(addingLinks);
        links.addAll(editingLinks);

        for (Object link : links) {
            Link linkData = (Link) link;
            cancelLink(linkData);
        }
    }


    public String cancelEditedLinks() {
        cancelAllLinks();

        editedLinks = null;
        return "";
    }

    public String removeLink() {
        editedLinks.remove(Faces.var("link"));
        return "";
    }

    public boolean isAddingOrEditingLink() {
        Link currentLink = getCurrentLink();
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

    private Link getCurrentLink() {
        Link linkData = (Link) Faces.var("link");
        if (linkData == null)
            return null;
        return linkData;
    }

    public String addLink() {
        if (editedLinks == null)
            throw new IllegalStateException();
        Link linkData = new Link();
        addingLinks.add(linkData);
        editedLinks.add(linkData);
        return "";
    }

    public String editLink() {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        Link currentLink = getCurrentLink();
        editingLinks.add(currentLink);
        originalLinks.add(currentLink.clone());
        return "";
    }

    public String saveLink() {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        Link currentLink = getCurrentLink();
        saveLink(currentLink);
        return "";
    }

    private void saveLink(Link currentLink) {
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

    public String cancelLink() {
        if (editedLinks == null)
            throw new IllegalStateException("Not in editing state");
        Link currentLink = getCurrentLink();
        cancelLink(currentLink);
        return "";
    }

    private void cancelLink(Link currentLink) {
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

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public List getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }
}