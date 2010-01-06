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

package org.openfaces.testapp.support.QKS177;

import org.openfaces.component.panel.FoldingPanel;

import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class QKS177 {
    private FoldingPanel marchesPanel;
    private FoldingPanel usersPanel;
    private List<DataTableItems> listeMarches = new ArrayList<DataTableItems>();
    private HtmlDataTable dataTableTest;

    public QKS177() {
        listeMarches.add(new DataTableItems("item1"));
        listeMarches.add(new DataTableItems("item2"));
        listeMarches.add(new DataTableItems("item3"));
        listeMarches.add(new DataTableItems("item4"));
        listeMarches.add(new DataTableItems("item5"));
    }


    public HtmlDataTable getDataTableTest() {
        return dataTableTest;
    }

    public void setDataTableTest(HtmlDataTable dataTableTest) {
        this.dataTableTest = dataTableTest;
    }

    public List<DataTableItems> getListeMarches() {
        return listeMarches;
    }

    public void setListeMarches(List<DataTableItems> listeMarches) {
        this.listeMarches = listeMarches;
    }

    public FoldingPanel getMarchesPanel() {
        return marchesPanel;
    }

    public void setMarchesPanel(FoldingPanel marchesPanel) {
        this.marchesPanel = marchesPanel;
    }


    public FoldingPanel getUsersPanel() {
        return usersPanel;
    }

    public void setUsersPanel(FoldingPanel usersPanel) {
        this.usersPanel = usersPanel;
    }

    public void marcheSelected(ActionEvent event) {
// marchesPanel is a FoldingPanel
        UIOutput anTitleMarches = (UIOutput) marchesPanel.getFacet("caption");
        UIOutput test = (UIOutput) dataTableTest.getFacet("header");
        test.setValue("another header");
// Try to update the header , title of the folding panel
        anTitleMarches.setValue("Another Folding Title");
// usersPanel is another FoldingPanel
        usersPanel.setExpanded(true);
        marchesPanel.setExpanded(true);
    }

}
