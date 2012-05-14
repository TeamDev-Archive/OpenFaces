/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.jBossSeam.treetable;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import static org.jboss.seam.ScopeType.SESSION;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
@Entity
@Name("treetableitem")
@Scope(SESSION)

public class TreeTableItem implements Serializable {
    private Long id;
    private String subject;
    private Date date;
    private String author;

    private TreeTableItem parent;

    private List<TreeTableItem> items = new ArrayList<TreeTableItem>();

    public TreeTableItem() {
    }

    public TreeTableItem(Long id, String subject, Date date, String author, TreeTableItem parent) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.author = author;
        this.parent = parent;
    }


    @Id
    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "parent")
    public List<TreeTableItem> getItems() {
        return items;
    }

    public void setItems(List<TreeTableItem> items) {
        this.items = items;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NotNull
    @Length(min = 5, max = 15)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "parent")
    public TreeTableItem getParent() {
        return parent;
    }

    public void setParent(TreeTableItem parent) {
        this.parent = parent;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TreeTableItem that = (TreeTableItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }
}
