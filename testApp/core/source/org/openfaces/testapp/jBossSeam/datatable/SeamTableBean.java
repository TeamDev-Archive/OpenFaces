/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.jBossSeam.datatable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.openfaces.component.filter.ExpressionFilterCriterion;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static javax.persistence.PersistenceContextType.EXTENDED;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;

/**
 * @author Tatyana Matveyeva
 */

@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("seamTable")
public class SeamTableBean implements Serializable, SeamTable {

    @DataModel
    private List<DataTableItem> itemsList;

    @DataModelSelection
    @Out(required = false)
    private DataTableItem tableitem;

    @PersistenceContext(type = EXTENDED)
    private EntityManager em;

    @In(required = false)
    @Out(required = false)
    private SeamTestTable seamtesttable;

    private String currentItem;
    private DataTableItem singleSelectionItem;
    private List<DataTableItem> multipleSelectionItems;

    private ExpressionFilterCriterion filterValue = new ExpressionFilterCriterion("item1");

    private String col1Value;
    private String col2Value;
    private Date col3Value;
    private int col4Value;

    @Factory("itemsList")
    public void findItems() {
        itemsList = em.createQuery("select row from DataTableItem row").getResultList();
    }

    public String sortByForthColumn(ActionEvent event) {
        seamtesttable.getDataTable().setSortColumnId("col4");
        seamtesttable.getDataTable().setSortAscending(false);
        return null;
    }

    public List<String> getFilterValues() {
        List<String> values = new ArrayList<String>();
        values.add("item1");
        values.add("item2");
        values.add("item3");
        values.add("item4");
        values.add("item5");
        values.add("item6");
        values.add("item7");
        values.add("item8");
        values.add("item9");
        values.add("item10");
        values.add("item11");
        values.add("item12");
        return values;
    }

    public ExpressionFilterCriterion getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(ExpressionFilterCriterion filterValue) {
        this.filterValue = filterValue;
    }


    public String getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
    }

    public void select() {
        if (tableitem != null) currentItem = tableitem.getColumn1();
    }

    public void selectItem() {
        if (tableitem != null) {
            col1Value = tableitem.getColumn1();
            col2Value = tableitem.getColumn2();
            col3Value = tableitem.getColumn3();
            col4Value = tableitem.getColumn4();
        }
    }

    public DataTableItem getSingleSelectionItem() {
        return singleSelectionItem;
    }

    public void setSingleSelectionItem(DataTableItem singleSelectionItem) {
        this.singleSelectionItem = singleSelectionItem;
    }


    public List<DataTableItem> getMultipleSelectionItems() {
        return multipleSelectionItems;
    }

    public void setMultipleSelectionItems(List<DataTableItem> multipleSelectionItems) {
        this.multipleSelectionItems = multipleSelectionItems;
    }

    public String getCol1Value() {
        return col1Value;
    }

    public String getCol2Value() {
        return col2Value;
    }

    public Date getCol3Value() {
        return col3Value;
    }

    public int getCol4Value() {
        return col4Value;
    }

    @Remove
    @Destroy
    public void destroy() {
    }

}
